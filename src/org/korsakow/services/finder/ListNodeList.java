package org.korsakow.services.finder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ListNodeList implements NodeList, List<Node>
{
	private List<Node> list;
	public ListNodeList(Node node)
	{
		if (node != null)
			this.list = Arrays.asList(node);
		else
			this.list = new ArrayList<Node>();
	}
	public ListNodeList(List<Node> list)
	{
		this.list = list;
	}
	public ListNodeList()
	{
		this.list = new ArrayList<Node>();
	}
	/**
	 * implements NodeList
	 */
	public int getLength() {
		return list.size();
	}

	/**
	 * implements NodeList
	 */
	public Node item(int index) {
		return list.get(index);
	}
	
	public boolean add(Node node)
	{
		if (node == null)
			throw new NullPointerException();
		return list.add(node);
	}
	public boolean addAll(Collection<? extends Node> c)
	{
		return list.addAll(c);
	}
	public void addAll(NodeList l)
	{
		int length = l.getLength();
		for (int i = 0; i < length; ++i)
			list.add(l.item(i));
	}
	public void add(int index, Node element) {
		list.add(index, element);
	}
	public boolean addAll(int index, Collection<? extends Node> c) {
		return list.addAll(index, c);
	}
	public void clear() {
		list.clear();
	}
	public boolean contains(Object o) {
		return list.contains(o);
	}
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}
	public Node get(int index) {
		return list.get(index);
	}
	public int indexOf(Object o) {
		return list.indexOf(o);
	}
	public boolean isEmpty() {
		return list.isEmpty();
	}
	public Iterator<Node> iterator() {
		return list.iterator();
	}
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}
	public ListIterator<Node> listIterator() {
		return list.listIterator();
	}
	public ListIterator<Node> listIterator(int index) {
		return list.listIterator(index);
	}
	public boolean remove(Object o) {
		return list.remove(o);
	}
	public Node remove(int index) {
		return list.remove(index);
	}
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}
	public Node set(int index, Node element) {
		return list.set(index, element);
	}
	public int size() {
		return list.size();
	}
	public List<Node> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}
	public Object[] toArray() {
		return list.toArray();
	}
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

}
