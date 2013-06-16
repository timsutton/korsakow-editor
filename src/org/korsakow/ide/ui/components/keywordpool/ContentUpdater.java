package org.korsakow.ide.ui.components.keywordpool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.command.FindSnuByInKeywordCommand;
import org.korsakow.domain.command.FindSnuByOutKeywordCommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.ide.Application;
import org.korsakow.ide.ui.components.KCollapsiblePane;
import org.korsakow.ide.ui.components.KList;
import org.korsakow.ide.ui.components.linkpool.LinkPool;
import org.korsakow.ide.ui.components.pool.AbstractPool;
import org.korsakow.ide.ui.components.snupool.SnuPool;

public class ContentUpdater implements ActionListener, ListSelectionListener
{
	private final AbstractPool<KeywordEntry, SnuEntry> pool;
	public ContentUpdater(AbstractPool<KeywordEntry, SnuEntry> pool)
	{
		this.pool = pool;
	}
	public void actionPerformed(ActionEvent event)
	{
		KCollapsiblePane pane = (KCollapsiblePane)event.getSource();
		try {
			if (pane.isExpanded()) {
				Application.getInstance().beginBusyOperation(pane);
				String keyword = (String)pane.getClientProperty("poolid");
				KeywordEntry entry = pool.getModel().getEntry(keyword);
				boolean update = false;
				if (entry.getInSnus() == null) {
					Request request = new Request();
					request.set("keyword", keyword);
					Response response = CommandExecutor.executeCommand(FindSnuByInKeywordCommand.class, request);
					Collection<Long> inSnus = new HashSet<Long>();
					for ( ISnu snu : (Collection<ISnu>)response.get("snus") )
						inSnus.add( snu.getId() );
					
					entry.setInSnus(inSnus);
					update = true;
				}
				if (entry.getOutSnus() == null) {
					Request request = new Request();
					request.set("keyword", keyword);
					Response response = CommandExecutor.executeCommand(FindSnuByOutKeywordCommand.class, request);
					Collection<Long> outSnus = new HashSet<Long>();
					for ( ISnu snu : (Collection<ISnu>)response.get("snus") )
						outSnus.add( snu.getId() );
					entry.setOutSnus(outSnus);
					update = true;
				}
				if (update)
					pool.getModel().updateEntry(entry);
			}
		} catch (CommandException e) {
			Application.getInstance().showUnhandledErrorDialog(e);
		} finally {
			Application.getInstance().endBusyOperation(pane);
		}
	}
	public void valueChanged(ListSelectionEvent event)
	{
		Application app = Application.getInstance();
		LinkPool linkPool = app.getLinkPool();
		SnuPool snuPool = app.getSnuPool();
		if (linkPool == null && snuPool == null)
			return;
		int index = event.getFirstIndex();
		if (index == -1)
			return;
		KList list = (KList)event.getSource();
		SnuEntry entry = (SnuEntry)list.getModel().getElementAt(index);
		if (linkPool != null) {
			linkPool.getPane(entry.getSnuId()).setExpanded(true);
		}
		if (snuPool != null) {
			snuPool.getPane(entry.getSnuId()).setExpanded(true);
		}
	}
}
