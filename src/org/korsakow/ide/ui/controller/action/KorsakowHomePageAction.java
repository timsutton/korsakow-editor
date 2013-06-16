package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.log4j.Logger;
import org.dsrg.soenea.service.Registry;
import org.korsakow.ide.controller.ApplicationAdapter;
import org.korsakow.ide.util.ShellExec;
import org.korsakow.ide.util.ShellExec.ShellException;

public class KorsakowHomePageAction extends ApplicationAdapter implements ActionListener
{

	public void actionPerformed(ActionEvent event) 
	{
		try {
			ShellExec.openUrl(Registry.getProperty("homepage"));
		} catch (ShellException e) {
			// TODO: should we show an error dialog here?
			Logger.getLogger(LogAction.class).error("", e);
		} catch (Exception e) {
			// TODO: should we show an error dialog here?
			Logger.getLogger(LogAction.class).error("", e);
		}
	}
}
