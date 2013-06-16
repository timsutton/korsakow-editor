package org.korsakow.ide.ui.components.tree;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FolderNode extends KNode
{
	private TreeCellRenderer renderer = new DefaultTreeCellRenderer();
	
	public FolderNode(String name)
	{
		super(name);
	}
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{
		assert value == this;
		assert false : "checking assertions are enabled";
		Component comp = renderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		return comp;
	}
	public Element toDomElement(Document doc)
	{
		Element e = doc.createElement("Folder");
		e.setAttribute("name", getName());
		for (KNode child : this) {
			e.appendChild(child.toDomElement(doc));
		}
		return e;
	}
}
