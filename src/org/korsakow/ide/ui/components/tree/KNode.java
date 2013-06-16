package org.korsakow.ide.ui.components.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.tree.TreePath;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.swingx.treetable.DefaultMutableTreeTableNode;
import com.sun.swingx.treetable.MutableTreeTableNode;

public class KNode extends DefaultMutableTreeTableNode implements Iterable<KNode>
{
	public static void visitDepthFirstLeafFirst(KNode node, NodeVisitor visitor) {
		// copy the list to allow modifications by the visitor
		Collection<KNode> children = new ArrayList<KNode>(node.getChildren());
		for (KNode child : children) {
			visitDepthFirstLeafFirst(child, visitor);
		}
		visitor.visit(node);
	}
	private String name;
	public KNode(String name)
	{
		setName(name);
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getName()
	{
		return name;
	}
	public void add(KNode child)
	{
		// resolve ambiguous call
		super.add((MutableTreeTableNode)child);
	}
	public void remove(KNode child)
	{
		// resolve ambiguous call
		super.remove((MutableTreeTableNode)child);
	}
	public List<String> getNamePath()
	{
		List<String> names = new ArrayList<String>();
		KNode node = this;
		while (node != null) {
			names.add(0, node.getName());
			node = node.getParent();
		}
		return names;
	}
	@Override
	public KNode getParent()
	{
		return (KNode)super.getParent();
	}
//	public TreePath getTreePath()
//	{
//		return new TreePath(getPath());
//	}
	public void setChildIndex(int index, KNode child)
	{
		if (child.getParent() != this)
			throw new IllegalArgumentException("not of parent of child");
		int oldIndex = getIndex(child);
		if (index > oldIndex) {
			--index;
		}
		children.remove(oldIndex);
		children.add(index, child);
	}
	public Collection<KNode> getChildren()
	{
		return new NodeChildListAdapter<KNode>(this);
	}
	@Override
	public KNode getChildAt(int index)
	{
		return (KNode)super.getChildAt(index);
	}
	public KNode getChild(String name)
	{
		if (children == null)
			return null;
		for (Object child : children) {
			KNode node = (KNode)child;
			if (node.getName().equals(name))
				return node;
		}
		return null;
	}
	public boolean isNodeDescendant(KNode node)
	{
		// compromise of sorts between breadth and depth first
		for (Object child : children) {
			if (child == node)
				return true;
		}
		for (Object child : children) {
			if (((KNode)child).isNodeDescendant(node))
				return true;
		}
		return false;
	}
	public TreePath getTreePath()
	{
		List<KNode> elms = new ArrayList<KNode>();
		KNode node = this;
		while (node != null) {
			elms.add(node);
			node = node.getParent();
		}
		Collections.reverse(elms);
		TreePath path = new TreePath(elms.toArray());
		return path;
	}
    @Override
	public int getColumnCount() {
        return Integer.MAX_VALUE;
    }
    @Override
	public Object getValueAt(int column)
    {
    	return this;
    }
    @Override
	public void setValueAt(Object value, int column)
    {
    	setName(value!=null?value.toString():null);
    }
	public Iterator<KNode> iterator()
	{
		return new ChildIterator();
	}
	private class ChildIterator implements Iterator<KNode>
	{
		private int index = 0;
		public boolean hasNext() {
			return index < getChildCount();
		}
		public KNode next() {
			return getChildAt(index++);
		}
		public void remove() {
			throw new IllegalArgumentException();
		}
	}
	public Element toDomElement(Document doc)
	{
		throw new IllegalArgumentException("KNode has no default XML representation");
	}
}
