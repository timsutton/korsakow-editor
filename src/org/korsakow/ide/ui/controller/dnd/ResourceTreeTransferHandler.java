/**
 * 
 */
package org.korsakow.ide.ui.controller.dnd;

import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;

import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.command.RenameResourceCommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.ide.ui.components.tree.FolderNode;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.dnd.AggregateFileTransferHandler;
import org.korsakow.ide.ui.dnd.AggregateTransferHandler;
import org.korsakow.ide.ui.dnd.TransferableTreeTableNodes;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTable;

public class ResourceTreeTransferHandler extends AggregateTransferHandler
{
	protected ResourceTreeTable resourceTreeTable;
	public ResourceTreeTransferHandler(ResourceTreeTable comp) {
		
		// add in the order of priority/preference
		
		AggregateFileTransferHandler fileHandler = new AggregateFileTransferHandler();
		fileHandler.addHandler(new ResourceTreeKifTransferHandler());
		fileHandler.addHandler(new ResourceTreeProjectTransferHandler());
		fileHandler.addHandler(new ResourceTreeMediaFileTransferHandler(this));
		
		addHandler(fileHandler);
		addHandler(new ResourceTreeNodeTransferHandler(this));
		addHandler(new ResourceTreeMediaTransferHandler(this));
		resourceTreeTable = comp;
		
	}
	
	@Override
    public void exportAsDrag(JComponent comp, InputEvent e, int action) {
		super.exportAsDrag(comp, e, action);
	}

	@Override
	public Transferable createTransferable(JComponent comp)
	{
		List<? extends KNode> nodes = resourceTreeTable.getSelectedNodes();
		List<KNode> toRemove = new ArrayList<KNode>();
		for (KNode node : nodes) {
			if (node == resourceTreeTable.getRootNode())
				toRemove.add(node);
		}
		nodes.removeAll(toRemove);
		return new TransferableTreeTableNodes(nodes);
	}
	
	@Override
	protected TransferHandler pickTransferHandler(TransferSupport support) {
		for (TransferHandler handler : handlers)
			if (handler.canImport(support))
				return handler;
		return null;
	}

	protected KNode getDropPointNode(int[] indexref)
	{
		KNode dropPointNode = resourceTreeTable.getSelectedNode();
		if (dropPointNode == null) {
			Point point = resourceTreeTable.getMousePosition();
			if (point == null)
				return null;
			TreePath path = resourceTreeTable.getPathForLocation(point.x, point.y);
			if (path == null)
				return null;
			dropPointNode = (KNode)path.getLastPathComponent();
			if (dropPointNode == null)
				return null;
		}
		int index = 0;
		if (dropPointNode instanceof FolderNode) {
			index = dropPointNode.getChildCount();
		} else {
			index = dropPointNode.getParent().getIndex(dropPointNode);
			dropPointNode = dropPointNode.getParent();
		}
		if (indexref!=null && indexref.length>0)
			indexref[0] = index;
		return dropPointNode;
	}
	static void renameMedia(IMedia medium, String newName) throws CommandException
	{
		Request request = new Request();
		request.set(RenameResourceCommand.ID, medium.getId());
		request.set(RenameResourceCommand.NAME, newName);
		Response response = new Response();
		CommandExecutor.executeCommand(RenameResourceCommand.class, request, response);
	}
}
