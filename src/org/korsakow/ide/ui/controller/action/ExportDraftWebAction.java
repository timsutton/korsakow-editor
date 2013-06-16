package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JDialog;
import javax.swing.JProgressBar;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.Settings;
import org.korsakow.domain.command.ExportDraftFlashProjectCommand;
import org.korsakow.domain.command.Helper;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.mapper.input.ProjectInputMapper;
import org.korsakow.domain.mapper.input.SettingsInputMapper;
import org.korsakow.domain.task.IWorker;
import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.controller.action.ExportWebAction.ExportDoneWorkerListener;
import org.korsakow.ide.ui.controller.action.helper.CancelProgressDialogWindowAdapter;
import org.korsakow.ide.ui.controller.action.helper.ProgressDialogWorkerListener;
import org.korsakow.ide.ui.dialogs.ProgressDialog;
import org.korsakow.ide.util.ShellExec;
import org.korsakow.ide.util.UIUtil;
import org.korsakow.ide.util.ShellExec.ShellException;
import org.korsakow.services.export.Exporter;

public class ExportDraftWebAction implements ActionListener {

	public void actionPerformed(ActionEvent event) {
		try {
			boolean isAltDown = (event.getModifiers()&KeyEvent.SHIFT_MASK)==KeyEvent.SHIFT_MASK;
			export(isAltDown, SettingsInputMapper.find().getBoolean(Settings.EncodeVideoOnExport));
			
		} catch (IOException e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.cantexport.title"), e);
		} catch (Exception e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.cantexport.title"), e);
		}
	}
	public static void export(boolean forceSkipOverwrite, boolean encodeVideo) throws IOException, CommandException, MapperException
	{
		final Application app = Application.getInstance();
		
		final JDialog progressDialog = new JDialog(app.getProjectExplorer());
		final JProgressBar taskProgressBar = new JProgressBar(0, 100);
		taskProgressBar.setIndeterminate(true);
		
		final ProgressDialog dialogMain = new ProgressDialog();
		progressDialog.add(dialogMain);
		
		progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		progressDialog.setTitle(LanguageBundle.getString("export.progress.window.title"));
		progressDialog.pack();
		progressDialog.setSize(640, progressDialog.getSize().height);
		UIUtil.centerOnFrame(progressDialog, app.getProjectExplorer());
		progressDialog.setModal(true);
	
		IProject project = ProjectInputMapper.find();
		Helper request = new Request();
		Helper response = new Response();
		request.set(ExportDraftFlashProjectCommand.PROJECT_ID, project.getId());
		CommandExecutor.executeCommand(ExportDraftFlashProjectCommand.class, request, response);
		final IWorker exportWorker = (IWorker)response.get(ExportDraftFlashProjectCommand.WORKER);
		Exporter exporter = (Exporter)response.get(ExportDraftFlashProjectCommand.EXPORTER);
		
		if (forceSkipOverwrite)
			exporter.getExportOptions().overwriteExisting = false;
		exporter.getExportOptions().encodeVideo = encodeVideo;
		
		File exportFile = (File)response.get(ExportDraftFlashProjectCommand.EXPORT_FILE);
		
		ProgressDialogWorkerListener progressListener = new ProgressDialogWorkerListener(dialogMain);
		exportWorker.addPropertyChangeListener(IWorker.PROPERTY_STATE, new DraftExportDoneWorkerListener(progressDialog, exportFile));
		exportWorker.addPropertyChangeListener(IWorker.PROPERTY_PROGRESS, progressListener);
		exportWorker.addPropertyChangeListener(IWorker.PROPERTY_DISPLAY_STRING, progressListener);
		exportWorker.addPropertyChangeListener(IWorker.PROPERTY_SUB_PROGRESS, progressListener);
		exportWorker.addPropertyChangeListener(IWorker.PROPERTY_SUB_DISPLAY_STRING, progressListener);
		exportWorker.execute();
		
		progressDialog.addWindowListener(new CancelProgressDialogWindowAdapter(progressDialog, exportWorker));
		progressDialog.setVisible(true);
		System.gc(); // purely speculative
	}
	public static class DraftExportDoneWorkerListener extends ExportDoneWorkerListener
	{
		public DraftExportDoneWorkerListener(JDialog progressDialog, File exportFile)
		{
			super(progressDialog, exportFile);
		}
		@Override
		protected void onDone()
		{
			try {
				ShellExec.openUrl(exportFile.toURL());
			} catch (ShellException e) {
				Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
			} catch (MalformedURLException e) {
				Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
			}
		}
	}
	private static String urlEncode(String path)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < path.length(); ++i) {
			char c = path.charAt(i);
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9'))
				sb.append(c);
			else
				sb.append('%').append(Integer.toHexString(c));
		}
		return sb.toString();
	}
	private static String formatExportUrl(String path)
	{
		String[] bits = path.split(File.separator);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bits.length; ++i) {
			sb.append(urlEncode(bits[i]));
			if (i != bits.length -1)
				sb.append(File.separator);
		}
		return sb.toString();
	}
}
