package org.korsakow.ide.ui.controller.action;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.service.Registry;
import org.korsakow.ide.Application;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.controller.ProjectLoader;
import org.korsakow.ide.util.FileUtil;
import org.korsakow.services.conversion.ConversionException;
import org.xml.sax.SAXException;

public class OpenProjectFileAction extends AbstractAction implements
		FilenameFilter {

	private static String _defaultExtension = null;
	
	public static String getDefaultExtension()
	{
		try {
			if (_defaultExtension == null) {
				_defaultExtension = Registry.getProperty("defaultProjectFileExtension");
			}
		} catch (Exception e) {
			_defaultExtension = "xml";
		}
		return _defaultExtension;
	}
	
	public OpenProjectFileAction()
	{
	}
	@Override
	public void performAction() {
		try {
			if (!ExitAction.checkForChangesAndPrompt())
				return;
		} catch (MapperException e) {
			Application.getInstance().showUnhandledErrorDialog(e);
			return;
		}
		try {
			
			FileDialog fileDialog = new FileDialog(Application.getInstance().getProjectExplorer());
			fileDialog.setFilenameFilter(this);
			if (Application.getInstance().getSaveFile() != null)
				fileDialog.setDirectory(Application.getInstance().getSaveFile().getPath());
			fileDialog.setMode(FileDialog.LOAD);
			fileDialog.setFile(DataRegistry.getFile().getName());
			fileDialog.setVisible(true);
			
			if (fileDialog.getFile() == null)
				return;
			File file = new File(fileDialog.getDirectory(), fileDialog.getFile());
			
			performAction(file);
		} catch (Throwable e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.cantopen.title"), e);
			ProjectLoader.newProject();
		}
	}

	public boolean accept(File dir, String name) {
		String defaultExtension = getDefaultExtension();
		return FileUtil.getFileExtension(name).toLowerCase().equals(defaultExtension);
	}

	public static void performAction(File file) throws XPathExpressionException, SAXException, ParserConfigurationException, IOException, SQLException, Throwable
	{
		try {
			ProjectLoader.loadProject(file);
			
			SaveProjectAction.addRecent(DataRegistry.getFile().getAbsolutePath());
		} catch (FileNotFoundException e) {
			Application.getInstance().showAlertDialog(LanguageBundle.getString("general.errors.filenotfound.title"), LanguageBundle.getString("general.errors.filenotfound.message", e.getMessage()));
		} catch (ConversionException e) {
			Application.getInstance().showAlertDialog(LanguageBundle.getString("general.errors.cantopen.title"), LanguageBundle.getString("general.errors.conversionerror.message", e.getCause()!=null?e.getCause().getClass().getSimpleName():e.getClass().getSimpleName(), e.getMessage()));
		} catch (SAXException e) {
			Application.getInstance().showAlertDialog(LanguageBundle.getString("general.errors.cantopen.title"), LanguageBundle.getString("general.errors.invalidprojectfile.message"));
		}
	}
}
