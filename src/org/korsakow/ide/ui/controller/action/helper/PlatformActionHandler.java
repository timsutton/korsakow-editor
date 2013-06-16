package org.korsakow.ide.ui.controller.action.helper;

import java.io.File;

import org.korsakow.eawt.ApplicationEvent;
import org.korsakow.eawt.IApplicationListener;
import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.controller.ProjectLoader;
import org.korsakow.ide.ui.controller.action.OpenProjectFileAction;

public class PlatformActionHandler implements IApplicationListener 
{
	public void handleAbout(ApplicationEvent event) {
		Application app = Application.getInstance();
		app.showAboutDialog();
		event.cancel(); // prevent os default about box
	}
	public void handleQuit(ApplicationEvent event) {
		Application app = Application.getInstance();
		app.shutdown();
		event.cancel(); // prevent os from shutting us down (we'll handle that, thank you)
	}
	public void handleOpenFile(ApplicationEvent event) {
		String filename = (String)event.getProperty("filename");
		try {
			OpenProjectFileAction.performAction(new File(filename));
		} catch (Throwable e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.cantopen.title"), e);
			ProjectLoader.newProject();
		}
	}
}
