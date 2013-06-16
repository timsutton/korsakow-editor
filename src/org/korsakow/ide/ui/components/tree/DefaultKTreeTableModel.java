package org.korsakow.ide.ui.components.tree;

import java.util.WeakHashMap;

import javax.swing.tree.TreePath;

import org.korsakow.ide.util.WeakReferenceMap;

import com.sun.swingx.treetable.DefaultTreeTableModel;
import com.sun.swingx.treetable.MutableTreeTableNode;
import com.sun.swingx.treetable.TreeTableModel;
import com.sun.swingx.treetable.TreeTableNode;

public class DefaultKTreeTableModel extends DefaultTreeTableModel implements KTreeTableModel
{
	protected long idSeed = 0;
	protected WeakReferenceMap<Long, KNode> idMap = new WeakReferenceMap<Long, KNode>();
	protected WeakHashMap<KNode, Long> reverseIdMap = new WeakHashMap<KNode, Long>();
	protected boolean isBatchUpdating = false;
	public DefaultKTreeTableModel(KNode root)
	{
		super(root);
	}
    @Override
	public Class getColumnClass(int column)
    {
    	return column==0?TreeTableModel.class:Object.class;
    }
    @Override
	public boolean isCellEditable(Object node, int column) {
        return node != getRoot() && super.isCellEditable(node, column);
   }
	public void prependNode(KNode newChild, KNode parent)
	{
		insertNodeInto(newChild, parent, 0);
	}
	public int appendNode(KNode newChild, KNode parent)
	{
		final int index = parent.getChildCount();
		insertNodeInto(newChild, parent, index);
		return index;
	}
	@Override
	public void insertNodeInto(MutableTreeTableNode newChild, MutableTreeTableNode parent, int index)
	{
		insertNodeInto((KNode)newChild, (KNode)parent, index);
	}
	public void insertNodeInto(KNode newChild, KNode parent, int index)
	{
//		if (newChild.getParent() == parent) {
		if (newChild.getParent() != null) {
			TreeTableNode oldParent = newChild.getParent();
			int oldIndex = parent.getIndex(newChild);
			super.removeNodeFromParent(newChild);
			if (oldParent == parent)
				if (index > oldIndex)
					--index;
		}
		
		if (!reverseIdMap.containsKey(newChild)) {
			long id = nextId();
			idMap.put(id, newChild);
			reverseIdMap.put(newChild, id);
		}
		
		super.insertNodeInto(newChild, parent, index);
	}
    public void removeNodeFromParent(KNode node) {
    	super.removeNodeFromParent(node);
    }
    public KNode getNodeById(long id)
    {
    	return idMap.get(id);
    }
    public long getIdByNode(KNode node)
    {
    	return reverseIdMap.get(node);
    }
	public void clear()
	{
		
	}
	@Override
	public KNode getRoot()
	{
		return (KNode)super.getRoot();
	}
    public void beginBatchUpdate()
    {
    	isBatchUpdating = true;
    }
    public void endBatchUpdate()
    {
    	isBatchUpdating = false;
    	fireTreeStructureChanged(this, getRoot().getTreePath());
    }
    protected long nextId()
    {
    	return ++idSeed;
    }
    @Override
	protected void fireTreeNodesChanged(Object source, Object[] path, 
            int[] childIndices, 
            Object[] children) {
    	if (isBatchUpdating)
    		return;
    	super.fireTreeNodesChanged(source, path, childIndices, children);
    }
    @Override
	protected void fireTreeNodesInserted(Object source, Object[] path, 
            int[] childIndices, 
            Object[] children) {
    	if (isBatchUpdating)
    		return;
    	super.fireTreeNodesInserted(source, path, childIndices, children);
    }
    @Override
	protected void fireTreeNodesRemoved(Object source, Object[] path, 
            int[] childIndices, 
            Object[] children) {
    	if (isBatchUpdating)
    		return;
    	super.fireTreeNodesRemoved(source, path, childIndices, children);
    }
    @Override
	protected void fireTreeStructureChanged(Object source, Object[] path, 
            int[] childIndices, 
            Object[] children) {
    	if (isBatchUpdating)
    		return;
    	super.fireTreeStructureChanged(source, path, childIndices, children);
    }
    @Override
	protected void fireTreeStructureChanged(Object source, TreePath path) {
    	if (isBatchUpdating)
    		return;
    	super.fireTreeStructureChanged(source, path);
    }
    @Override
	public void nodeStructureChanged(TreeTableNode node) {
    	if (isBatchUpdating)
    		return;
    	super.nodeStructureChanged(node);
    }
}
