package org.korsakow.ide.util;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class OrderedEventListenerList
{
	private static class Entry
	{
		public final Class clazz;
		public final EventListener listener;
		public Entry(Class clazz, EventListener listener)
		{
			this.clazz = clazz;
			this.listener = listener;
		}
		public boolean equals(Object object)
		{
			if (object instanceof Entry == false)
				return false;
			Entry other = (Entry)object;
			if (!clazz.equals(other.clazz))
				return false;
			if (!listener.equals(other.listener))
				return false;
			return true;
		}
	}
	private List<Entry> listeners = new ArrayList<Entry>();
	public void add(Class clazz, EventListener listener)
	{
		Entry entry = new Entry(clazz, listener);
		listeners.add(entry);
	}
	public <T extends EventListener> List<T> getListeners(Class<T> clazz)
	{
		List<T> listeners = new ArrayList<T>();
		for (Entry entry : this.listeners) {
			if (entry.clazz.equals(clazz))
				listeners.add((T)entry.listener);
		}
		return listeners;
	}
}

