package org.korsakow.domain.proxy;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.ProxyException;

public abstract class NonDOListProxy<TYPE> implements List<TYPE> {
	private List<TYPE> innerList;
	
	protected abstract List<TYPE> getActualList() throws MapperException;
	
	private synchronized List<TYPE> getInnerList() {
		if(innerList==null)
			try {
				innerList = getActualList();
			} catch (MapperException e) {
				throw new ProxyException(e);
			}
		return innerList;
	}

	public boolean add(TYPE o) {
		return getInnerList().add(o);
	}

	public void add(int index, TYPE element) {
		getInnerList().add(index, element);
	}

	public boolean addAll(Collection<? extends TYPE> c) {
		return getInnerList().addAll(c);
	}

	public boolean addAll(int index, Collection<? extends TYPE> c) {
		return getInnerList().addAll(index, c);
	}

	public void clear() {
		getInnerList().clear();
	}

	public boolean contains(Object o) {
		return getInnerList().contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return getInnerList().containsAll(c);
	}

	public TYPE get(int index) {
		return getInnerList().get(index);
	}

	public int indexOf(Object o) {
		return getInnerList().indexOf(o);
	}

	public boolean isEmpty() {
		return getInnerList().isEmpty();
	}

	public Iterator<TYPE> iterator() {
		return getInnerList().iterator();
	}

	public int lastIndexOf(Object o) {
		return getInnerList().lastIndexOf(o);
	}

	public ListIterator<TYPE> listIterator() {
		return getInnerList().listIterator();
	}

	public ListIterator<TYPE> listIterator(int index) {
		return getInnerList().listIterator(index);
	}

	public boolean remove(Object o) {
		return getInnerList().remove(o);
	}

	public TYPE remove(int index) {
		return getInnerList().remove(index);
	}

	public boolean removeAll(Collection<?> c) {
		return getInnerList().removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return getInnerList().retainAll(c);
	}

	public TYPE set(int index, TYPE element) {
		return getInnerList().set(index, element);
	}

	public int size() {
		return getInnerList().size();
	}

	public List<TYPE> subList(int fromIndex, int toIndex) {
		return getInnerList().subList(fromIndex, toIndex);
	}

	public Object[] toArray() {
		return getInnerList().toArray();
	}

	public <T> T[] toArray(T[] a) {
		return getInnerList().toArray(a);
	}
	
	@Override
	public String toString() {
		return getInnerList().toString();
	}
}
