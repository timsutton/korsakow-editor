package org.korsakow.ide.ui.components.snupool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.korsakow.ide.Application;
import org.korsakow.ide.ui.components.KCollapsiblePane;
import org.korsakow.ide.ui.components.KList;
import org.korsakow.ide.ui.components.keywordpool.KeywordPool;
import org.korsakow.ide.ui.components.pool.AbstractPool;

public class ContentUpdater implements ActionListener, ListSelectionListener
{
	private final AbstractPool<SnuEntry, KeywordEntry> pool;
	public ContentUpdater(AbstractPool<SnuEntry, KeywordEntry> pool)
	{
		this.pool = pool;
	}
	public void actionPerformed(ActionEvent event)
	{
		KCollapsiblePane pane = (KCollapsiblePane)event.getSource();
		if (pane.isExpanded()) {
			Application.getInstance().beginBusyOperation(pane);
			Long snuId = (Long)pane.getClientProperty("poolid");
			SnuEntry entry = pool.getModel().getEntry(snuId);
			pool.getModel().updateEntry(entry);
			Application.getInstance().endBusyOperation(pane);
		}
	}
	public void valueChanged(ListSelectionEvent event)
	{
		Application app = Application.getInstance();
		KeywordPool keywordPool = app.getKeywordPool();
		if (keywordPool == null)
			return;
		int index = event.getFirstIndex();
		if (index == -1)
			return;
		KList list = (KList)event.getSource();
		KeywordEntry entry = (KeywordEntry)list.getModel().getElementAt(index);
		keywordPool.getPane(entry.getKeyword()).setExpanded(true);
	}
}
