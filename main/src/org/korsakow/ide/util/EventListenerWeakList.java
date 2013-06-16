package org.korsakow.ide.util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class EventListenerWeakList
{
	private static class Entry
	{
		public final Class clazz;
		public final WeakReference<EventListener> listener;
		public Entry(Class clazz, EventListener listener)
		{
			this.clazz = clazz;
			this.listener = new WeakReference<EventListener>(listener);
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
			EventListener l1 = listener.get();
			EventListener l2 = other.listener.get();
			if (l1==null || l2==null)
				return false;
			return l1.equals(l2);
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
			if (entry.clazz.equals(clazz)) {
				T listener = (T)entry.listener.get();
				if (listener != null)
					listeners.add(listener);
			}
		}
		return listeners;
	}
}

