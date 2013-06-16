package org.korsakow.ide.ui.controller.action.interf;

import java.io.CharConversionException;
import java.io.File;
import java.io.FilenameFilter;

import org.apache.log4j.Logger;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.service.Registry;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.command.ImportInterchangeInterfaceCommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.ui.controller.action.AbstractAction;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTableModel;
import org.korsakow.ide.util.FileUtil;
import org.korsakow.ide.util.Util;

public class ImportInterfaceAction extends AbstractAction
{
	private static String _defaultExtension = null;
	public static String getDefaultExtension()
	{
		try {
			if (_defaultExtension == null) {
				_defaultExtension = Registry.getProperty("defaultInterchangeInterfaceExtension");
			}
		} catch (Exception e) {
			_defaultExtension = "kif";
		}
		return _defaultExtension;
	}
	
	public ImportInterfaceAction()
	{
	}
	@Override
	public void performAction()
	{
		Application app = Application.getInstance();
		File file = app.showFileOpenDialog(app.getProjectExplorer(), new File("interface.kif"), null, new InterchangeFilenameFilter());
		if (file == null)
			return;
		try {
			doImport(file);
		
		} catch (CommandException e) {
			Throwable cause = Util.getRootCause(e);
			if (cause instanceof CharConversionException)
				app.showHandledErrorDialog(LanguageBundle.getString("general.errors.invalidinterfacefile.title"), LanguageBundle.getString("general.errors.invalidinterfacefile.message"));
			else
				app.showUnhandledErrorDialog("Error importing interface", e);
		}
	}

	public void doImport(File file) throws CommandException
	{
		Application app = Application.getInstance();
		Logger.getLogger(ImportInterfaceAction.class).info("ImportInterfaceAction: " + file.getAbsolutePath());
		
		Request request = new Request();
		request.set("filename", file.getAbsolutePath());
		Response response = new Response();
		CommandExecutor.executeCommand(ImportInterchangeInterfaceCommand.class, request, response);
		
		IInterface domain = (IInterface)response.get("interface");
		ResourceTreeTableModel model = Application.getInstance().getProjectExplorer().getResourceBrowser().getResourceTreeTable().getTreeTableModel();
		model.appendNode( ResourceNode.create( domain ), model.getRoot() );
		
		Application.getInstance().notifyResourceAdded( domain );
	}
	private final class InterchangeFilenameFilter implements FilenameFilter
	{
		public InterchangeFilenameFilter()
		{
		}
		public boolean accept(File dir, String name)
		{
			return getDefaultExtension().equals(FileUtil.getFileExtension(name));
		}
	}

}
