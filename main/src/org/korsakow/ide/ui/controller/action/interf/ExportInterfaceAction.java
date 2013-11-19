package org.korsakow.ide.ui.controller.action.interf;

import org.apache.log4j.Logger;
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

        Logger.getLogger(ExportInterfaceAction.class).debug("SelectedNodes(size) = " + resourceTreeTable.getSelectedNodes().size());
        List<IInterface> interfaces = getInterfacesFromNodes(resourceTreeTable.getSelectedNodes());
        if (interfaces == null)
            return;

        File basePath = app.showDirOpenDialog(resourceTreeTable, null);
        if (basePath == null)
            return;

        for (IInterface interf : interfaces) {
            String defaultFilename = interf.getName().replaceAll("[ ./\\:*?\"'<>|!]", ""); // TODO: whitelist instead
            if (defaultFilename.trim().length() == 0)       // TODO this needs to be handled differently when saving multiple files to dir
                defaultFilename = "interface";
            defaultFilename += ".kif";

            Request request = new Request();
            request.set("filename", basePath + File.separator + defaultFilename);
            request.set("interface_id", interf.getId());
            try {
                CommandExecutor.executeCommand(ExportInterchangeInterfaceCommand.class, request);
            } catch (CommandException e) {
                app.showUnhandledErrorDialog("Error exporting interface", e);
            }
        }
	}

    /* This is a seperate method so that we can catch mapper errors before starting any saving */
    private List<IInterface> getInterfacesFromNodes(List<? extends KNode> knodeList)
    {
        List<IInterface> result = new ArrayList<IInterface>();
        for (KNode node : knodeList)
        {
            try {
                if (!isValidResourceNode(node))
//                TODO probably should do something nicer here
                    return null;

                ResourceNode resourceNode = (ResourceNode)node;
                result.add(InterfaceInputMapper.map(resourceNode.getResourceId()));

            } catch (DomainObjectNotFoundException e) {
                return null;
            } catch (MapperException e) {
                Application.getInstance().showUnhandledErrorDialog("Error exporting interface", e);
                return null;
            }
        }
        Logger.getLogger(ExportInterfaceAction.class).debug("We have " + result.size() + " results");
        return result;

    }

    private Boolean isValidResourceNode(KNode node) {
        return node instanceof ResourceNode;
    }
}
