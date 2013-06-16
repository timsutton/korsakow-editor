package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.controller.ProjectLoader;

public class NewProjectAction implements ActionListener {

	public void actionPerformed(ActionEvent event) {
		try {
			if (!ExitAction.checkForChangesAndPrompt())
				return;
		} catch (MapperException e) {
			Application.getInstance().showUnhandledErrorDialog(e);
			return;
		}
		try {
			ProjectLoader.newProject();
		} catch (Exception e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.cantnew.title"), e);
		}
	}
}
