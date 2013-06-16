/**
 * 
 */
package org.korsakow.ide.ui.controller.dnd;

import java.util.List;

import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.ide.Application;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.ui.dnd.InternalMediaTransferHandler;
import org.korsakow.ide.util.UIUtil;

public class ResourceTreeMediaTransferHandler extends InternalMediaTransferHandler
{
	private final ResourceTreeTransferHandler resourceTreeTransferHandler;
	public ResourceTreeMediaTransferHandler(ResourceTreeTransferHandler resourceTreeTransferHandler)
	{
		this.resourceTreeTransferHandler = resourceTreeTransferHandler;
	}
	@Override
	protected boolean importMedia(TransferSupport support, List<? extends IMedia> media)
	{
		int[] dropPointIndexRef = new int[1];
		KNode dropPointNode = resourceTreeTransferHandler.getDropPointNode(dropPointIndexRef);
		if (dropPointNode == null) {
			dropPointNode = resourceTreeTransferHandler.resourceTreeTable.getRootNode();
		}
		int dropPointIndex = dropPointIndexRef[0];
		if (dropPointIndex < 0)
			dropPointIndex = dropPointNode.getChildCount();
		
		try {
			for (IMedia medium : media) {
				String name = UIUtil.createUniqueName(dropPointNode, medium.getName());
				if (!name.equals(medium.getName())) {
					ResourceTreeTransferHandler.renameMedia(medium, name);
				}
			}
		} catch (CommandException e) {
			Application.getInstance().showUnhandledErrorDialog(null, e);
			return false;
		}
		boolean addedsome = false;
		for (IMedia medium : media) {
			KNode node = ResourceNode.create(medium);
			resourceTreeTransferHandler.resourceTreeTable.getTreeTableModel().insertNodeInto(node, dropPointNode, dropPointIndex);
			Application.getInstance().notifyResourceAdded( medium );
			addedsome = true;
		}
		return addedsome;
	}
}