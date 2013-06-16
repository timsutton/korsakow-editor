package org.korsakow.ide.ui.components.pool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public abstract class AbstractPoolModel<HE extends HeaderEntry>
{
	protected EventListenerList listeners = new EventListenerList();
	protected List<HE> data = new ArrayList<HE>();
	protected HashMap<Object, HE> entrymap = new HashMap<Object, HE>();
	protected boolean isBatchUpdate = false;
	public void beginBatchUpdate()
	{
		isBatchUpdate = true;
	}
	public void endBatchUpdate()
	{
		isBatchUpdate = false;
		sort();
		fireContentsChanged(-1, -1);
	}
	public void add(HE entry)
	{
		data.add(entry);
		entrymap.put(getId(entry), entry);
		if (!isBatchUpdate) {
			sort();
			fireIntervalAdded(data.size()-1, data.size()-1);
		}
	}
	public Collection<HE> getData()
	{
		return data;
	}
	public HE getEntry(int index)
	{
		return data.get(index);
	}
	public HE getEntry(Object id)
	{
		return entrymap.get(id);
	}
	public void updateEntry(HeaderEntry entry)
	{
		sort();
		fireContentsChanged(data.indexOf(entry), data.indexOf(entry));
	}
	public void addListDataListener(ListDataListener listener)
	{
		listeners.add(ListDataListener.class, listener);
	}
	public void removeListDataListener(ListDataListener listener)
	{
		listeners.remove(ListDataListener.class, listener);
	}
	public void fireIntervalAdded(int index0, int index1)
	{
		ListDataEvent event = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index0, index1);
		for (ListDataListener listener : listeners.getListeners(ListDataListener.class)) {
			listener.intervalAdded(event);
		}
	}
	public void fireContentsChanged(int index0, int index1)
	{
		ListDataEvent event = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, index0, index1);
		for (ListDataListener listener : listeners.getListeners(ListDataListener.class)) {
			listener.contentsChanged(event);
		}
	}
	protected abstract Object getId(HE entry);
	protected void sort()
	{
		Collections.sort(data);
	}
}
