package org.korsakow.services.finder;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NodeListAdapter extends AbstractList<Node>
{
	private NodeList list;
	public NodeListAdapter(NodeList list)
	{
		this.list = list;
	}
	public Node get(int index) {
		return list.item(index);
	}
	public int size() {
		return list.getLength();
	}
}
