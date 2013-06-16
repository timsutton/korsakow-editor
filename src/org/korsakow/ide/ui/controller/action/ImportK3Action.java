package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JProgressBar;

import org.apache.log4j.Logger;
import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.k3.importer.K3ImportException;
import org.korsakow.domain.k3.importer.K3ImportReport;
import org.korsakow.domain.k3.importer.K3Importer;
import org.korsakow.domain.k3.importer.exception.K3InvalidRuleException;
import org.korsakow.domain.k3.parser.K3InterfaceParserException;
import org.korsakow.domain.k3.parser.K3ParserException;
import org.korsakow.domain.task.IWorker;
import org.korsakow.ide.Application;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.controller.ApplicationAdapter;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.controller.ImportHelper;
import org.korsakow.ide.ui.controller.ProjectLoader;
import org.korsakow.ide.ui.controller.ProjectExplorerController.ProgressBarWorkerListener;
import org.korsakow.ide.ui.controller.action.helper.ProgressDialogStatusListener;
import org.korsakow.ide.ui.dialogs.K3ImportDialog;
import org.korsakow.ide.util.Command;
import org.korsakow.ide.util.Pair;
import org.korsakow.ide.util.UIUtil;

public class ImportK3Action extends ApplicationAdapter implements ActionListener
{

	public void actionPerformed(ActionEvent event) 
	{
		try {
			if (!ExitAction.checkForChangesAndPrompt())
				return;
		} catch (MapperException e) {
			Application.getInstance().showUnhandledErrorDialog(e);
			return;
		}
		File defaultFile = new File(".");
		File file = Application.getInstance().showFileOpenDialog(Application.getInstance().getProjectExplorer(), defaultFile, new File("database.txt"));
		if (file == null)
			return;

		try {
			doimport(file);
		} catch (Exception e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.cantimport.title"), e);
			ProjectLoader.newProject();
		}
	}
	public static void doimport(File originalFile) throws Exception
	{
		Logger.getLogger(ImportK3Action.class).info("ImportK3Action: " + originalFile.getAbsolutePath());
		Application app = Application.getInstance();
		
		File file = originalFile;
		// we use a fairly lame heuristic to try and locate the data directory
		if (!file.isDirectory() || !file.getName().equals("data")) {
			while (file != null) {
				file = file.getParentFile();
				if (file != null && file.isDirectory() && file.getName().equals("data"))
					break;
			}
			if (file == null) {
				app.showAlertDialog(LanguageBundle.getString("import.notak3project.window.title"), LanguageBundle.getString("import.notak3project.window.message", originalFile.getAbsolutePath()));
				return;
			}
		}
		
		System.gc(); // TODO: Why?
		final JDialog progressDialog = new JDialog(Application.getInstance().getProjectExplorer());
		progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		final JProgressBar taskProgressBar = new JProgressBar(0, 100);
		taskProgressBar.setIndeterminate(true);
		
		final JProgressBar totalProgressBar = new JProgressBar(0, 100);
		totalProgressBar.setStringPainted(true);
		totalProgressBar.setValue(0);
		totalProgressBar.setString(LanguageBundle.getString("import.progress.initial"));
		
		progressDialog.setLayout(new BoxLayout(progressDialog.getContentPane(), BoxLayout.Y_AXIS));
		progressDialog.add(totalProgressBar);
		progressDialog.setTitle(LanguageBundle.getString("import.progress.window.title"));
		progressDialog.pack();
		progressDialog.setSize(640, progressDialog.getSize().height);
		UIUtil.centerOnFrame(progressDialog, Application.getInstance().getProjectExplorer());
		progressDialog.setModal(true);
		
//		K3Importer k3Importer = new K3Importer();
//		
//		List<Task> importTasks = k3Importer.createImportTasks(file);
//		
//		UIWorker importWorker = new UIWorker(importTasks);
		Pair<IWorker, K3Importer> pair = Command.importK3(file.getPath());
		IWorker importWorker = pair.getFirst();
		K3Importer k3Importer = pair.getSecond();
		
		if (!k3Importer.getDatabaseFile().exists()) {
			app.showAlertDialog(LanguageBundle.getString("import.notak3project.window.title"), LanguageBundle.getString("import.notak3project.window.message", originalFile.getAbsolutePath()));
			return;
		}
		
		ProgressBarWorkerListener progressListener = new ProgressBarWorkerListener(totalProgressBar);
		importWorker.addPropertyChangeListener("state", new ImportDoneWorkerListener(progressDialog, k3Importer));
		importWorker.addPropertyChangeListener("progress", progressListener);
		importWorker.addPropertyChangeListener("displayString", progressListener);

		importWorker.execute();
		progressDialog.setVisible(true);
		System.gc(); // purely speculative
	}
	private static void handleUnknownException(Throwable e)
	{
		Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.cantimport.title"), e);
		ProjectLoader.newProject();
	}
	private static void handleImportException(K3ImportException e)
	{
		if (e instanceof K3InvalidRuleException) {
			K3InvalidRuleException ruleException = (K3InvalidRuleException)e;
			String details = e.getMessage();
			if (e.getCause() != null)
				details = e.getCause().getMessage();
			else
				details = e.getMessage();
			K3ImportDialog dialog = new K3ImportDialog(ruleException.getDatabaseFile().getAbsolutePath(), ruleException.getLine(),
					LanguageBundle.getString("import.invalidrule.message", ruleException.getCode(), ruleException.getLine(), details));
			
			Application.getInstance().showAlertDialog(LanguageBundle.getString("import.invalidrule.title"), dialog);
		} else {
			Integer line = null;
			String file = null;
			String details = e.getCause()!=null?e.getCause().getMessage():e.getMessage();
			if (e.getCause() instanceof K3ParserException) {
				K3ParserException parseException = (K3ParserException)e.getCause();
				line = parseException.getLineNumber();
			}
			if (e.getCause() instanceof K3InterfaceParserException) {
				if (e.getInterfaceFile() != null)
					file = e.getInterfaceFile().getAbsolutePath();
			}
			if (file == null) {
				if (e.getDatabaseFile() != null)
					file = e.getDatabaseFile().getAbsolutePath();
				else
					file = "";
			}
			K3ImportDialog dialog = new K3ImportDialog(file, line,
					LanguageBundle.getString("import.notak3project.window.message", details));
			Application.getInstance().showAlertDialog(LanguageBundle.getString("import.notak3project.window.title"), dialog);
//			handleUnknownException(e);
		}
	}
	private static class ImportDoneWorkerListener extends ProgressDialogStatusListener
	{
		private final K3Importer importer;
		public ImportDoneWorkerListener(JDialog progressDialog, K3Importer importer)
		{
			super(progressDialog);
			this.importer = importer;
		}
		@Override
		protected void handleException(Throwable e)
		{
			if (e instanceof K3ImportException)
				handleImportException((K3ImportException)e);
			else
				handleUnknownException(e);
		}
		@Override
		protected void onDone() {
			K3ImportReport report = importer.getReport();
			
			try {
				Application.getInstance().setSaveFile(null, DataRegistry.getHeadVersion());
				Application.getInstance().clearRegistry();

				IProject project = importer.getProject();
				ImportHelper.createK3FolderStructure(Application.getInstance().getProjectExplorer().getResourceBrowser().getResourceTreeTable().getTreeTableModel(), importer);
				Application.getInstance().notifyProjectLoaded(project);
//					checkInterfaces(); // should be done in domain but is currently a bit tricky to do so
				
				Application.getInstance().showAlertDialog(LanguageBundle.getString("import.complete.title"), LanguageBundle.getString("import.complete.message"));
			} catch (Exception e) {
				handleUnknownException(e);
			}
			System.gc();
		}
	}
}
