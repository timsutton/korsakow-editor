package org.korsakow.ide.ui.components.tree;

import java.util.AbstractList;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * A java.lang.List wrapper around a TreeNode's children
 * 
 * @author d
 *
 * @param <N>
 */
public class NodeChildListAdapter<N extends TreeNode> extends AbstractList<N>
{
	private TreeNode node;
	public NodeChildListAdapter(N node)
	{
		this.node = node;
	}
	public N get(int index) {
		return (N)node.getChildAt(index);
	}

	public int size() {
		return node.getChildCount();
	}

}
