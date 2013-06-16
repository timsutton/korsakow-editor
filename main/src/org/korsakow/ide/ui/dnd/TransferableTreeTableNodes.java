/**
 * 
 */
package org.korsakow.ide.ui.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.util.Util;


public class TransferableTreeTableNodes implements Transferable
{
	private static final DataFlavor[] DEFAULT_FLAVORS = { DataFlavors.TreeTableNodesFlavor };
	private DataFlavor[] flavors;
	private final List<? extends KNode> nodes;
	public TransferableTreeTableNodes(List<? extends KNode> nodes)
	{
		if (nodes == null)
			throw new NullPointerException();
		if (nodes.contains(null))
			throw new NullPointerException();
		this.nodes = new ArrayList<KNode>(nodes);
		flavors = DEFAULT_FLAVORS;
		// special cases
		if (nodes.size() == 1) {
			KNode node = nodes.get(0);
			if (node instanceof ResourceNode) {
				ResourceNode mediaNode = (ResourceNode)node;
				ResourceType resourceType = mediaNode.getResourceType();
				switch (resourceType)
				{
				case VIDEO:
					flavors = Util.arrayAdd(flavors, DataFlavors.VideoFlavor);
					break;
				case SOUND:
					flavors = Util.arrayAdd(flavors, DataFlavors.SoundFlavor);
					break;
				case IMAGE:
					flavors = Util.arrayAdd(flavors, DataFlavors.ImageFlavor);
					break;
				case TEXT:
					flavors = Util.arrayAdd(flavors, DataFlavors.TextFlavor);
					break;
				case SNU:
					flavors = Util.arrayAdd(flavors, DataFlavors.SnuFlavor);
					break;
				}
			}
		}
	}
	public TransferableTreeTableNodes(KNode node)
	{
		nodes = Arrays.asList(node);
	}
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (!isDataFlavorSupported(flavor))
			throw new UnsupportedFlavorException(flavor);
		if (flavor.equals(DataFlavors.TreeTableNodesFlavor))
			return nodes;
		if (flavor.equals(DataFlavors.SoundFlavor))
			return ((ResourceNode)nodes.get(0)).getResourceId();
		if (flavor.equals(DataFlavors.VideoFlavor))
			return ((ResourceNode)nodes.get(0)).getResourceId();
		if (flavor.equals(DataFlavors.ImageFlavor))
			return ((ResourceNode)nodes.get(0)).getResourceId();
		if (flavor.equals(DataFlavors.TextFlavor))
			return ((ResourceNode)nodes.get(0)).getResourceId();
		if (flavor.equals(DataFlavors.SnuFlavor))
			return ((ResourceNode)nodes.get(0)).getResourceId();
		throw new IllegalStateException("unreachable code!");
	}

	public DataFlavor[] getTransferDataFlavors() {
		return flavors;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		for (DataFlavor f : flavors)
			if (f.equals(flavor))
				return true;
		return false;
	}
	
}