package org.korsakow.ide.ui.components.linkpool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.korsakow.ide.Application;
import org.korsakow.ide.ui.components.KCollapsiblePane;
import org.korsakow.ide.ui.components.KList;
import org.korsakow.ide.ui.components.pool.AbstractPool;
import org.korsakow.ide.ui.components.snupool.SnuPool;

public class ContentUpdater implements ActionListener, ListSelectionListener
{
	private final AbstractPool<SnuHeaderEntry, SnuContentEntry> pool;
	public ContentUpdater(AbstractPool<SnuHeaderEntry, SnuContentEntry> pool)
	{
		this.pool = pool;
	}
	public void actionPerformed(ActionEvent event)
	{
		KCollapsiblePane pane = (KCollapsiblePane)event.getSource();
		if (pane.isExpanded()) {
			Application.getInstance().beginBusyOperation(pane);
			Long snuId = (Long)pane.getClientProperty("poolid");
			SnuHeaderEntry snuEntry = pool.getModel().getEntry(snuId);
			pool.getModel().updateEntry(snuEntry);
			Application.getInstance().endBusyOperation(pane);
		}
	}
	public void valueChanged(ListSelectionEvent event)
	{
		Application app = Application.getInstance();
		SnuPool snuPool = app.getSnuPool();
		if (snuPool == null)
			return;
		int index = event.getFirstIndex();
		if (index == -1)
			return;
		KList list = (KList)event.getSource();
		SnuContentEntry entry = (SnuContentEntry)list.getModel().getElementAt(index);
		snuPool.getPane(entry.getSnuId()).setExpanded(true);
	}
}
