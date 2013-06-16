package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JMenuItem;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.ide.Application;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.controller.ProjectLoader;
import org.korsakow.services.conversion.ConversionException;
import org.xml.sax.SAXException;

public class LoadRecentProjectAction extends NewProjectAction implements
		ActionListener {

	@Override
	public void actionPerformed(ActionEvent event) {
		try {
			if (!ExitAction.checkForChangesAndPrompt())
				return;
		} catch (MapperException e) {
			Application.getInstance().showUnhandledErrorDialog(e);
			return;
		}
		JMenuItem item = (JMenuItem)event.getSource();
		try {
			ProjectLoader.loadProject(new File(item.getText()));
			
			SaveProjectAction.addRecent(DataRegistry.getFile().getAbsolutePath());
		} catch (FileNotFoundException e) {
			Application.getInstance().showAlertDialog(LanguageBundle.getString("general.errors.filenotfound.title"), LanguageBundle.getString("general.errors.filenotfound.message", e.getMessage()));
		} catch (ConversionException e) {
			Application.getInstance().showAlertDialog(LanguageBundle.getString("general.errors.cantopen.title"), LanguageBundle.getString("general.errors.conversionerror.message", e.getCause()!=null?e.getCause().getClass().getSimpleName():e.getClass().getSimpleName(), e.getMessage()));
		} catch (SAXException e) {
			Application.getInstance().showAlertDialog(LanguageBundle.getString("general.errors.cantopen.title"), LanguageBundle.getString("general.errors.invalidprojectfile.message"));
		} catch (Throwable e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.cantopen.title"), e);
			ProjectLoader.newProject();
		}
	}

}
