package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.log4j.Logger;
import org.korsakow.ide.Application;
import org.korsakow.ide.controller.ApplicationAdapter;
import org.korsakow.ide.util.ShellExec;
import org.korsakow.ide.util.ShellExec.ShellException;

public class LogAction extends ApplicationAdapter implements ActionListener
{

	public void actionPerformed(ActionEvent event) 
	{
		try {
			ShellExec.revealInPlatformFilesystemBrowser(Application.getLogfilename());
		} catch (ShellException e) {
			// TODO: should we show an error dialog here?
			Logger.getLogger(LogAction.class).error("how ironic, logging an error while attempting to show the logfile", e);
		}
	}
}
