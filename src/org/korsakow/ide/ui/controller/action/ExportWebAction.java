package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JProgressBar;

import org.apache.log4j.Logger;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.Settings;
import org.korsakow.domain.command.ExportFlashProjectCommand;
import org.korsakow.domain.command.Helper;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.command.UpdateSettingsCommand;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.ISettings;
import org.korsakow.domain.mapper.input.ProjectInputMapper;
import org.korsakow.domain.mapper.input.SettingsInputMapper;
import org.korsakow.domain.task.IWorker;
import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.controller.action.helper.CancelProgressDialogWindowAdapter;
import org.korsakow.ide.ui.controller.action.helper.ProgressDialogStatusListener;
import org.korsakow.ide.ui.controller.action.helper.ProgressDialogWorkerListener;
import org.korsakow.ide.ui.controller.helper.ProjectHelper;
import org.korsakow.ide.ui.dialogs.ProgressDialog;
import org.korsakow.ide.util.FileUtil;
import org.korsakow.ide.util.Platform;
import org.korsakow.ide.util.UIUtil;
import org.korsakow.services.encoders.EncoderException;
import org.korsakow.services.encoders.UnsupportedFormatException;
import org.korsakow.services.export.ExportException;
import org.korsakow.services.export.Exporter;


public class ExportWebAction implements ActionListener {

	public void actionPerformed(ActionEvent event) {
		try {
			ISettings settings = SettingsInputMapper.find();
			String defaultPath = settings.getString(Settings.ExportDirectory);
			File file = null;
			if ( defaultPath == null || defaultPath.trim().isEmpty() ) {
				Application.getInstance().showAlertDialog("About to export", "You are about to export your film. You will need to select where to save your files but next time that location will be used automatically. You can change the location any time in the settings.");
				file = Application.getInstance().showFileSaveDialog(Application.getInstance().getProjectExplorer(), new File("index.html"));
				if ( file == null )
					return;
				settings.setString(Settings.ExportDirectory, file.getPath());
				CommandExecutor.executeCommand(UpdateSettingsCommand.class, ProjectHelper.createRequest(settings));
			} else {
				file = new File(defaultPath);
			}
	
			if (Platform.isMacOS() && FileUtil.getFileExtension(file.getName()).isEmpty())
				file = new File(file.getAbsolutePath() + ".html");
				
			if (file.exists() && file.isDirectory()) {
				Application.getInstance().showAlertDialog(LanguageBundle.getString("general.errors.cantexport.title"), "File is a directory");
				return;
			}
			if (file.getCanonicalPath().matches(".*[+?%#&].*")) {
				Application.getInstance().showAlertDialog("Invalid Path", LanguageBundle.getString("general.errors.pathcontainsinvalidcharacters"));
			}
			
			boolean isAltDown = (event.getModifiers()&KeyEvent.SHIFT_MASK)==KeyEvent.SHIFT_MASK;
			export(file, isAltDown, settings.getBoolean(Settings.EncodeVideoOnExport));
			
		} catch (IOException e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.cantexport.title"), e);
		} catch (Exception e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.cantexport.title"), e);
		}
	}
	public static void export(File mainFile, boolean forceSkipOverwrite, boolean encodeVideo) throws IOException, CommandException, MapperException
	{
		Logger.getLogger(ExportWebAction.class).info("ExportWebAction: " + mainFile.getAbsolutePath());
		System.gc();
		final Application app = Application.getInstance();
		
		File parentFile = mainFile.getParentFile();
		FileUtil.mkdirs(parentFile);
			
		final JDialog progressDialog = new JDialog(app.getProjectExplorer());
		progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		final JProgressBar taskProgressBar = new JProgressBar(0, 100);
		taskProgressBar.setIndeterminate(true);
		
		final ProgressDialog dialogMain = new ProgressDialog();
		progressDialog.add(dialogMain);
		
		progressDialog.setTitle(LanguageBundle.getString("export.progress.window.title"));
		progressDialog.pack();
		progressDialog.setSize(640, progressDialog.getSize().height);
		UIUtil.centerOnFrame(progressDialog, app.getProjectExplorer());
		progressDialog.setModal(true);
	
		IProject project = ProjectInputMapper.find();
		Helper request = new Request();
		Helper response = new Response();
		request.set(ExportFlashProjectCommand.PROJECT_ID, project.getId());
		request.set(ExportFlashProjectCommand.EXPORT_DIR, parentFile.getPath());
		request.set(ExportFlashProjectCommand.INDEX_FILENAME, mainFile.getName());
		CommandExecutor.executeCommand(ExportFlashProjectCommand.class, request, response);
		IWorker exportWorker = (IWorker)response.get(ExportFlashProjectCommand.WORKER);
		Exporter exporter = (Exporter)response.get(ExportFlashProjectCommand.EXPORTER);
		if (forceSkipOverwrite)
			exporter.getExportOptions().overwriteExisting = false;
		exporter.getExportOptions().encodeVideo = encodeVideo;
		
		ProgressDialogWorkerListener progressListener = new ProgressDialogWorkerListener(dialogMain);
		exportWorker.addPropertyChangeListener(IWorker.PROPERTY_STATE, new ExportDoneWorkerListener(progressDialog, mainFile));
		exportWorker.addPropertyChangeListener(IWorker.PROPERTY_PROGRESS, progressListener);
		exportWorker.addPropertyChangeListener(IWorker.PROPERTY_DISPLAY_STRING, progressListener);
		exportWorker.addPropertyChangeListener(IWorker.PROPERTY_SUB_PROGRESS, progressListener);
		exportWorker.addPropertyChangeListener(IWorker.PROPERTY_SUB_DISPLAY_STRING, progressListener);
		exportWorker.execute();
		progressDialog.addWindowListener(new CancelProgressDialogWindowAdapter(progressDialog, exportWorker));
		progressDialog.setVisible(true);
		System.gc(); // purely speculative
	}
	public static class ExportDoneWorkerListener extends ProgressDialogStatusListener
	{
		protected final File exportFile;
		public ExportDoneWorkerListener(JDialog progressDialog, File exportFile)
		{
			super(progressDialog);
			this.exportFile = exportFile;
		}
		private boolean handleUnsupportedFormatException(UnsupportedFormatException e)
		{
			Application.getInstance().showHandledErrorDialog(LanguageBundle.getString("export.errors.encodingerror.title"),
					LanguageBundle.getString("export.errors.encodingerror.message", e.getFile()!=null?e.getFile().getAbsolutePath():"[no file?]"),
					e.getMessage());
			return true;
		}
		private boolean handleEncodingException(EncoderException e)
		{
			Logger.getLogger(getClass()).info("", e);
			
			if (e instanceof UnsupportedFormatException)
				return handleUnsupportedFormatException((UnsupportedFormatException) e);
			
			return false;
		}
		private boolean handleFileNotFoundException(FileNotFoundException e)
		{
			Application.getInstance().showHandledErrorDialog(LanguageBundle.getString("general.errors.filenotfound.title"),
					LanguageBundle.getString("general.errors.filenotfound.message", e.getMessage()));
			return true;
		}
		boolean myHandleException(Exception e)
		{
			if (e instanceof ExportException)
				e = (Exception)e.getCause();
			
			if (e instanceof EncoderException) {
				return handleEncodingException((EncoderException)e);
			} else
			if (e instanceof FileNotFoundException) {
				return handleFileNotFoundException((FileNotFoundException)e);
			}
			return false;
		}
		@Override
		protected void handleException(Throwable e)
		{
			if (e instanceof Exception) {
				if (myHandleException((Exception)e))
					return;
			}
			super.handleException(e);
		}
		
		@Override
		protected void onDone()
		{
			Application.getInstance().showAlertDialog(LanguageBundle.getString("export.complete.title"), LanguageBundle.getString("export.complete.message"));
		}
	}
}
