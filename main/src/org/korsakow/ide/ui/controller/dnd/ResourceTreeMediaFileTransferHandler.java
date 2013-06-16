/**
 * 
 */
package org.korsakow.ide.ui.controller.dnd;

import java.util.List;

import org.korsakow.domain.interf.IMedia;
import org.korsakow.ide.Application;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.components.tree.ResourceNode;

public class ResourceTreeMediaFileTransferHandler extends AbstractMediaFileTransferHandler
{
	private final ResourceTreeTransferHandler resourceTreeTransferHandler;
	public ResourceTreeMediaFileTransferHandler(ResourceTreeTransferHandler resourceTreeTransferHandler)
	{
		this.resourceTreeTransferHandler = resourceTreeTransferHandler;
	}
	@Override
	protected boolean importMedia(List<? extends IMedia> media)
	{
		int[] dropPointIndexRef = new int[1];
		KNode dropPointNode = resourceTreeTransferHandler.getDropPointNode(dropPointIndexRef);
		if (dropPointNode == null) {
			dropPointNode = resourceTreeTransferHandler.resourceTreeTable.getRootNode();
		}
		int dropPointIndex = dropPointIndexRef[0];
		if (dropPointIndex < 0)
			dropPointIndex = dropPointNode.getChildCount();
		
		boolean addedsome = false;
		for (IMedia medium : media ) {
			KNode node = ResourceNode.create(medium);
			resourceTreeTransferHandler.resourceTreeTable.getTreeTableModel().insertNodeInto(node, dropPointNode, dropPointIndex);
			Application.getInstance().notifyResourceAdded(medium);
			addedsome = true;
		}
		return addedsome;
	}
}