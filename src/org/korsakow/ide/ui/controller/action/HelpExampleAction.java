package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

import org.dsrg.soenea.service.Registry;
import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.controller.ProjectLoader;

public class HelpExampleAction implements ActionListener {

	public void actionPerformed(ActionEvent event) 
	{
		try {
			if (!ExitAction.checkForChangesAndPrompt())
				return;
			ProjectLoader.loadProject(getExampleProject(), false);
		} catch (FileNotFoundException e) {
			Application.getInstance().showAlertDialog(LanguageBundle.getString("general.errors.filenotfound.title"), LanguageBundle.getString("general.errors.filenotfound.message", e.getMessage()));
		} catch ( Throwable e ) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.cantopenexample.title"), e);
			ProjectLoader.newProject();
		}
	}

	
	public static File getExampleProject () throws Exception, URISyntaxException {
		return new File(Registry.getProperty("exampleProject"));
	}
}
