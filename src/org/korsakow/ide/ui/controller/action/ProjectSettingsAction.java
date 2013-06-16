package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.korsakow.domain.mapper.input.ProjectInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.controller.ProjectEditAction;
import org.korsakow.ide.lang.LanguageBundle;

public class ProjectSettingsAction implements ActionListener
{
	public void actionPerformed(ActionEvent event)
	{
		try {
			new ProjectEditAction().run(ProjectInputMapper.find());
		} catch (Exception e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
		}
	}
}
