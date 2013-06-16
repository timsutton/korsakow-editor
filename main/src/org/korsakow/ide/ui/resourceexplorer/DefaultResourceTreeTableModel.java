package org.korsakow.ide.ui.resourceexplorer;

import java.util.Map;

import javax.swing.tree.TreePath;

import org.korsakow.ide.ui.components.tree.DefaultKTreeTableModel;
import org.korsakow.ide.ui.components.tree.FolderNode;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.util.UIUtil;
import org.korsakow.ide.util.WeakReferenceMap;

import com.sun.swingx.treetable.TreeTableNode;

public class DefaultResourceTreeTableModel extends DefaultKTreeTableModel implements ResourceTreeTableModel
{
	private final Map<Long, ResourceNode> idMap = new WeakReferenceMap<Long, ResourceNode>();
	public DefaultResourceTreeTableModel(FolderNode root)
	{
		super(root);
		recursiveMapUpdate(root);
	}
    @Override
	public void setRoot(TreeTableNode root)
	{
		super.setRoot(root);
		recursiveMapUpdate((FolderNode)root);
	}
	@Override
	public FolderNode getRoot()
	{
		return (FolderNode)super.getRoot();
	}
	public KNode remove(Long id)
	{
		ResourceNode node = findResource(id);
		if (node != null) {
			removeNodeFromParent(node);
		}
		return node;
	}
	@Override
	public void insertNodeInto(KNode newChild, KNode parent, int index)
    {
		if (newChild instanceof ResourceNode) {
			ResourceNode resourceNode = (ResourceNode)newChild;
			idMap.put(resourceNode.getResourceId(), resourceNode);
		}
		
		while (parent instanceof FolderNode == false) {
			parent = parent.getParent();
			index = parent.getChildCount();
		}
		super.insertNodeInto(newChild, parent, index);
    }
    @Override
	public void removeNodeFromParent(KNode node) {
		if (node instanceof ResourceNode) {
			ResourceNode resourceNode = (ResourceNode)node;
			idMap.remove(resourceNode.getResourceId());
		}
		if (UIUtil.isRooted(getRoot(), node))
			super.removeNodeFromParent(node);
    }
	public ResourceNode findResource(Long id)
	{
		return idMap.get(id);
	}
	public void fireChanged()
	{
        fireTreeStructureChanged(this, new TreePath(getPathToRoot(getRoot())));
	}
	public void fireChanged(KNode changed)
	{
        fireTreeStructureChanged(this, new TreePath(getPathToRoot(changed)));
	}
	protected void recursiveMapUpdate(KNode node)
	{
		if (node instanceof ResourceNode) {
			ResourceNode resourceNode = (ResourceNode)node;
			idMap.put(resourceNode.getResourceId(), resourceNode);
		}
		for (KNode child : node.getChildren())
			recursiveMapUpdate(child);
	}
}
