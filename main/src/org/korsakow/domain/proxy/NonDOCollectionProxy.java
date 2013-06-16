package org.korsakow.domain.proxy;

import java.util.Collection;
import java.util.Iterator;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.ProxyException;

public abstract class NonDOCollectionProxy<TYPE> implements Collection<TYPE> {
	private Collection<TYPE> innerList;
	
	protected abstract Collection<TYPE> getActualCollection() throws MapperException;
	
	private synchronized Collection<TYPE> getInnerList() {
		if(innerList==null)
			try {
				innerList = getActualCollection();
			} catch (MapperException e) {
				throw new ProxyException(e);
			}
		return innerList;
	}

	public boolean add(TYPE o) {
		return getInnerList().add(o);
	}

	public boolean addAll(Collection<? extends TYPE> c) {
		return getInnerList().addAll(c);
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

	public boolean isEmpty() {
		return getInnerList().isEmpty();
	}

	public Iterator<TYPE> iterator() {
		return getInnerList().iterator();
	}

	public boolean remove(Object o) {
		return getInnerList().remove(o);
	}

	public boolean removeAll(Collection<?> c) {
		return getInnerList().removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return getInnerList().retainAll(c);
	}

	public int size() {
		return getInnerList().size();
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
