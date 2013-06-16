package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.korsakow.ide.Application;

public class AboutAction implements ActionListener
{
	public void actionPerformed(ActionEvent event) 
	{
		Application app = Application.getInstance();
		app.showAboutDialog();
	}
}
