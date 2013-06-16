package org.korsakow.ide.ui.controller.action.interf;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.command.ExportInterchangeInterfaceCommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.mapper.input.InterfaceInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTable;

public class ExportInterfaceAction implements ActionListener
{
	private final ResourceTreeTable resourceTreeTable;
	public ExportInterfaceAction(ResourceTreeTable resourceTreeTable)
	{
		this.resourceTreeTable = resourceTreeTable;
	}
	public void actionPerformed(ActionEvent event)
	{
		Application app = Application.getInstance();
		
		KNode selectedNode = resourceTreeTable.getSelectedNode();
		if (selectedNode == null)
			return;
		if (selectedNode instanceof ResourceNode == false)
			return;
		ResourceNode resourceNode = (ResourceNode)selectedNode;
		long id = resourceNode.getResourceId();
		IInterface interf = null;
		try {
			interf = InterfaceInputMapper.map(id);
		} catch (DomainObjectNotFoundException e) {
			return;
		} catch (MapperException e) {
			app.showUnhandledErrorDialog("Error exporting interface", e);
		}
		
		String defaultFilename = interf.getName().replaceAll("[ ./\\:*?\"'<>|!]", ""); // TODO: whitelist instead
		if (defaultFilename.trim().length() == 0)
			defaultFilename = "interface";
		defaultFilename += ".kif";
		File file = app.showFileSaveDialog(resourceTreeTable, new File(defaultFilename));
		if (file == null)
			return;
		
		Request request = new Request();
		request.set("filename", file.getAbsolutePath());
		request.set("interface_id", interf.getId());
		try {
			CommandExecutor.executeCommand(ExportInterchangeInterfaceCommand.class, request);
		} catch (CommandException e) {
			app.showUnhandledErrorDialog("Error exporting interface", e);
		}
	}
}
