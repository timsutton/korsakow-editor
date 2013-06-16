package org.korsakow.ide.ui.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JProgressBar;

import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.ProjectExplorer;
import org.korsakow.ide.ui.controller.action.AboutAction;
import org.korsakow.ide.ui.controller.action.CopyAction;
import org.korsakow.ide.ui.controller.action.DeleteAction;
import org.korsakow.ide.ui.controller.action.DuplicateAction;
import org.korsakow.ide.ui.controller.action.ExitAction;
import org.korsakow.ide.ui.controller.action.ExportDraftWebAction;
import org.korsakow.ide.ui.controller.action.ExportMenuAction;
import org.korsakow.ide.ui.controller.action.ExportWebAction;
import org.korsakow.ide.ui.controller.action.HelpExampleAction;
import org.korsakow.ide.ui.controller.action.ImportK3Action;
import org.korsakow.ide.ui.controller.action.ImportMenuAction;
import org.korsakow.ide.ui.controller.action.KorsakowHomePageAction;
import org.korsakow.ide.ui.controller.action.LogAction;
import org.korsakow.ide.ui.controller.action.MenuEditAction;
import org.korsakow.ide.ui.controller.action.MenuLanguageAction;
import org.korsakow.ide.ui.controller.action.MenuWindowAction;
import org.korsakow.ide.ui.controller.action.NewProjectAction;
import org.korsakow.ide.ui.controller.action.OpenProjectFileAction;
import org.korsakow.ide.ui.controller.action.PasteAction;
import org.korsakow.ide.ui.controller.action.ProjectSettingsAction;
import org.korsakow.ide.ui.controller.action.SaveProjectAction;
import org.korsakow.ide.ui.controller.action.SaveProjectAsAction;
import org.korsakow.ide.ui.controller.action.ShowKeywordPoolWindowAction;
import org.korsakow.ide.ui.controller.action.ShowLinkPoolWindowAction;
import org.korsakow.ide.ui.controller.action.ShowPossiblePoolWindowAction;
import org.korsakow.ide.ui.controller.action.ShowSnuPoolWindowAction;
import org.korsakow.ide.ui.controller.action.UndoAction;
import org.korsakow.ide.ui.controller.action.helper.PlatformActionHandler;
import org.korsakow.ide.ui.controller.action.interf.ExportInterfaceAction;
import org.korsakow.ide.ui.controller.action.interf.ImportInterfaceAction;
import org.korsakow.ide.ui.controller.action.media.ImportMediaAction;


public class ProjectExplorerController
{
	private final ProjectExplorer projectExplorer;
	private final ResourceExplorerController resourceExplorerController;
	private final AboutAction aboutAction;
	private final ExitAction exitAction;
	private WindowTitleUpdater windowTitleUpdater;
	public ProjectExplorerController(ProjectExplorer projectExplorer)
	{
		this.projectExplorer = projectExplorer;
		resourceExplorerController = new ResourceExplorerController(projectExplorer.getResourceBrowser());
		Application.getInstance().addApplicationListener(resourceExplorerController);
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuFileNew, new NewProjectAction());
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuFileOpen, new OpenProjectFileAction());
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuFileSave, new SaveProjectAction());
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuFileSaveAs, new SaveProjectAsAction());
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuFileProjectSettings, new ProjectSettingsAction());
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuFileImport, new ImportMenuAction(projectExplorer.getResourceBrowser().getResourceTreeTable()));
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuFileImportMedia, new ImportMediaAction(projectExplorer.getResourceBrowser().getResourceTreeTable()));
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuFileImportK3Project, new ImportK3Action());
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuFileImportInterface, new ImportInterfaceAction());
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuFileExport, new ExportMenuAction(projectExplorer.getResourceBrowser().getResourceTreeTable()));
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuFileExportWeb, new ExportWebAction());
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuFileExportDraftWeb, new ExportDraftWebAction());
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuFileExportInterface, new ExportInterfaceAction(projectExplorer.getResourceBrowser().getResourceTreeTable()));
		exitAction = new ExitAction();
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuFileExit, exitAction);
		Application.getInstance().getPlatformApplication().addApplicationListener(new PlatformActionHandler());
		
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuEdit, new MenuEditAction(projectExplorer));
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuEditCopy, new CopyAction(projectExplorer.getResourceBrowser().getResourceTreeTable()));
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuEditPaste, new PasteAction(projectExplorer.getResourceBrowser().getResourceTreeTable()));
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuEditDuplicate, new DuplicateAction(projectExplorer.getResourceBrowser().getResourceTreeTable()));
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuEditDelete, new DeleteAction(projectExplorer.getResourceBrowser().getResourceTreeTable()));
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuEditUndo, new UndoAction());
		
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuLanguage, new MenuLanguageAction(projectExplorer));
		
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuToolsKeywordPool, new ShowKeywordPoolWindowAction());
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuToolsLinkPool, new ShowLinkPoolWindowAction());
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuToolsPossiblePool, new ShowPossiblePoolWindowAction());
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuToolsSnuPool, new ShowSnuPoolWindowAction());
		
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuWindow, new MenuWindowAction());
		
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuHelpExample, new HelpExampleAction());
		aboutAction = new AboutAction();
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuHelpAbout, aboutAction);
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuHelpLog, new LogAction());
		projectExplorer.addMenuAction(ProjectExplorer.Action.MenuHelpKorsakowWebSite, new KorsakowHomePageAction());
		
		Application.getInstance().addApplicationListener(windowTitleUpdater = new WindowTitleUpdater());
	}
	
	public void loadDefaultProject()
	{
		try {
			List<String> recent = SaveProjectAction.getRecent();
			List<String> toRemove = new ArrayList<String>();
			boolean loaded = false;
			for (String filename : recent)
			{
				File file = new File(filename);
				try {
					ProjectLoader.loadProject(file);
					loaded = true;
					break;
				} catch (FileNotFoundException e) {
					toRemove.add(filename);
					Application.getInstance().showAlertDialog(LanguageBundle.getString("general.errors.filenotfound.title"), LanguageBundle.getString("general.errors.filenotfound.message", file.getPath()));
				} catch (Exception e) {
					toRemove.add(filename);
					Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.cantopen.title"), file.getAbsolutePath(), e);
				} catch (Throwable e) {
					// catch(Throwable) added due to an IllegalAccessError in Xerces for reasons unknown.
					toRemove.add(filename);
					Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.cantopen.title"), file.getAbsolutePath(), e);
				}
			}
			recent.removeAll(toRemove);
			SaveProjectAction.setRecent(recent);
			if (!loaded)
				ProjectLoader.newProject();
		} catch (Exception e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.cantnew.title"), e);
		}
	}

	public static class ProgressBarWorkerListener implements PropertyChangeListener
	{
		JProgressBar progressBar;
		public ProgressBarWorkerListener(JProgressBar progressBar)
		{
			this.progressBar = progressBar;
		}
		public void propertyChange(PropertyChangeEvent event) {
			if (event.getPropertyName().equals("progress")) {
				progressBar.setValue((Integer)event.getNewValue());
			} else
			if (event.getPropertyName().equals("displayString")) {
				progressBar.setString((String)event.getNewValue());
			}
		}
	}
}
