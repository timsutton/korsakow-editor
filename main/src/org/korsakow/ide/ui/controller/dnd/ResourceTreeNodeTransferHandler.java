/**
 * 
 */
package org.korsakow.ide.ui.controller.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import org.apache.log4j.Logger;
import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.command.CloneResourceCommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.interf.IResource;
import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.components.tree.FolderNode;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.ui.dnd.DataFlavors;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTableModel;
import org.korsakow.ide.util.UIUtil;

public class ResourceTreeNodeTransferHandler extends TransferHandler
{
	/**
	 * 
	 */
	private final ResourceTreeTransferHandler resourceTreeTransferHandler;
	/**
	 * @param resourceTreeTransferHandler
	 */
	ResourceTreeNodeTransferHandler(
			ResourceTreeTransferHandler resourceTreeTransferHandler)
	{
		this.resourceTreeTransferHandler = resourceTreeTransferHandler;
	}

	@Override
	public int getSourceActions(JComponent comp)
	{
		return TransferHandler.COPY_OR_MOVE;
	}
	
	protected Collection<KNode> getTransferNodes(TransferSupport support)
	{
		Collection<KNode> transferNodes = new ArrayList<KNode>();
		if (support.getTransferable() == null)
			return null;
		Collection<? extends KNode> nodes = Collections.EMPTY_LIST;
		try {
			nodes = (Collection<? extends KNode>)support.getTransferable().getTransferData(DataFlavors.TreeTableNodesFlavor);
		} catch (UnsupportedFlavorException e) {
        	Logger.getLogger(ResourceTreeTransferHandler.class).error(e);
			return null;
		} catch (IOException e) {
        	Logger.getLogger(ResourceTreeTransferHandler.class).error(e);
			return null;
		}
		for (KNode node : nodes) {
			// some wierd serialization (or something) happens in DnD. the node we get somehow doesnt match the old one.
			transferNodes.add(resourceTreeTransferHandler.resourceTreeTable.getNodeForPath(node.getNamePath()));
//				transferNodes.add(node);
		}
		return transferNodes;
	}
	
	@Override
	public boolean canImport(JComponent comp, DataFlavor[] flavours) {
		KNode dropPointNode = resourceTreeTransferHandler.getDropPointNode(null);
		if (dropPointNode == null) {
			dropPointNode = resourceTreeTransferHandler.resourceTreeTable.getRootNode();
			resourceTreeTransferHandler.resourceTreeTable.getSelectionModel().setSelectionInterval(0, 0);
//			resourceTreeTable.repaint();
		}
		if (Arrays.asList(flavours).contains(DataFlavors.TreeTableNodesFlavor))
			return true;
		return false;
	}
	@Override
	public boolean importData(TransferSupport support) {
		if (!canImport(support))
			return false;
		int[] _dropPointIndex = new int[1];
		KNode dropPointNode = resourceTreeTransferHandler.getDropPointNode(_dropPointIndex);
		int dropPointIndex = _dropPointIndex[0];
		if (dropPointNode == null)
			return false;
		
		int dropAction;
		try {
			// Swing really sucks. TransferHandler throws IllegalStateException if we try to call getDropAction when the support was constructed
			// using anything other than its private constructor. Also all its innards are private.
			dropAction = support.getDropAction();
		} catch (IllegalStateException e) {
			dropAction = TransferHandler.COPY;;
		}
		
		Collection<KNode> transferNodes = getTransferNodes(support);
		// error checking pass
		for (KNode transferNode : transferNodes) {
		
			if (transferNode.isNodeDescendant(dropPointNode))
				return false;
			
			if (dropPointNode!=transferNode.getParent() && dropPointNode.getChild(transferNode.getName()) != null) {
				
				switch (dropAction)
				{
				case TransferHandler.COPY:
				case TransferHandler.COPY_OR_MOVE:
					break;
				default:
					Application.getInstance().showAlertDialog(resourceTreeTransferHandler.resourceTreeTable, LanguageBundle.getString("resourcebrowser.errors.duplicateiteminfolder.title"), LanguageBundle.getString("resourcebrowser.errors.duplicateiteminfolder.message"));
					return false;
				}
			}
		}
		
		// insertion pass
		for (KNode transferNode : transferNodes) {
			KNode loopLocalDropPointNode = dropPointNode;
			int loopLocalDropPointIndex = dropPointIndex;
			
			int offset = 0;
//					resourceTreeTable.getModel().removeNodeFromParent(transferNode); // do this before the getDropPointNode so the indices work out when transferring to the name parent node
			// compensate for re-inserting into same parent
			if (loopLocalDropPointNode == transferNode.getParent()) {
				// This is not only a NO-OP if MOVING a node. But in some cases such as copy/paste this is not the case and in fact prevents such functionality!
				// In any case there is no harm and this was a pointless optimization.
				// Leaving the old code with this comment as an advisory, though you could remove the code since the comment is here.
					//if (dropPointNode.getIndex(transferNode) == dropPointIndex) // no op
					//	continue;
				if (loopLocalDropPointNode.getIndex(transferNode) < loopLocalDropPointIndex) // compensate for index-shift from remove
					offset = -1;
			}
			if (transferNode == loopLocalDropPointNode) {
				if (transferNode instanceof FolderNode) {
					loopLocalDropPointIndex = transferNode.getParent().getIndex(transferNode);
					loopLocalDropPointNode = transferNode.getParent();
				} else
					continue; // can this happen if not a folder?
			}
			
			try {
				switch (dropAction)
				{
				case TransferHandler.COPY:
//				case TransferHandler.COPY_OR_MOVE:
					transferNode = cloneNode(resourceTreeTransferHandler.resourceTreeTable.getTreeTableModel(), transferNode, loopLocalDropPointNode, loopLocalDropPointIndex + offset);
					break;
				default:
					break;
				}
			} catch (CommandException e) {
				Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
				return false;
			} catch (InterruptedException e) {
				Logger.getLogger(getClass()).error("", e);
				return false;
			}
			
			resourceTreeTransferHandler.resourceTreeTable.getTreeTableModel().removeNodeFromParent(transferNode);
			resourceTreeTransferHandler.resourceTreeTable.getTreeTableModel().insertNodeInto(transferNode, loopLocalDropPointNode, loopLocalDropPointIndex + offset);
//				resourceTreeTable.getTreeTableModel().add(transferNode, dropPointNode, dropPointIndex + offset);
		}
		
		return true;
	}
	private KNode cloneNode(ResourceTreeTableModel model, KNode node, KNode parentNode, int childIndex) throws CommandException, InterruptedException
	{
		if (node instanceof ResourceNode)
		{
			ResourceNode resourceNode = (ResourceNode)node;
			final String resourceName = resourceNode.getName(); // TODO: get this from the resource
			String name = UIUtil.createUniqueName(parentNode, resourceName, "copy of ");
			IResource clone;
			{
				Request request = new Request();
				request.set(CloneResourceCommand.ID, resourceNode.getResourceId());
				request.set(CloneResourceCommand.NAME, name);
				Response response = new Response();
				CommandExecutor.executeCommand(CloneResourceCommand.class, request, response);
				clone = (IResource)response.get(CloneResourceCommand.RESOURCE);
			}
			Application.getInstance().notifyResourceAdded( clone );
			KNode cloneNode = ResourceNode.create( clone );
			model.insertNodeInto( cloneNode, parentNode, childIndex );
			return cloneNode;
		}
		else
		if (node instanceof FolderNode)
		{
			FolderNode clone = new FolderNode(node.getName());
			String name = UIUtil.createUniqueName(parentNode, node.getName(), "copy of ");
			clone.setName(name);
			model.insertNodeInto(clone, parentNode, parentNode.getChildCount());
			for (KNode folderChild : node.getChildren())
			{
				clone.add(cloneNode(model, folderChild, clone, node.getIndex(folderChild)));
			}
			return clone;
		}
		throw new IllegalArgumentException("unknown node type");
	}
}