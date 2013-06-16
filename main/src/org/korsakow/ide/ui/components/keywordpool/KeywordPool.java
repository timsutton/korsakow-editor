package org.korsakow.ide.ui.components.keywordpool;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ListDataListener;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.mapper.input.SnuInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.components.KCollapsiblePane;
import org.korsakow.ide.ui.components.pool.AbstractHeader;
import org.korsakow.ide.ui.components.pool.AbstractPool;
import org.korsakow.ide.ui.components.pool.Content;
import org.korsakow.ide.util.UIResourceManager;

public class KeywordPool extends AbstractPool<KeywordEntry, SnuEntry> implements ListDataListener
{
	public static Icon inIcon = UIResourceManager.getIcon(UIResourceManager.ICON_SNU_IN);
	public static Icon outIcon = UIResourceManager.getIcon(UIResourceManager.ICON_SNU_OUT);
	
	private final Collection<ActionListener> inListeners = new ArrayList<ActionListener>();
	private final Collection<ActionListener> outListeners = new ArrayList<ActionListener>();
	private final Collection<ActionListener> itemListeners = new ArrayList<ActionListener>();
	
	private JLabel headerLabel;
	
	@Override
	protected void initUI()
	{
		super.initUI();
		
		JPanel heading = new JPanel();
		
		heading.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5));
		
		heading.add(new JLabel(inIcon));
		heading.add(new JLabel(outIcon));
		heading.add(Box.createHorizontalStrut(10));
		heading.add(headerLabel = new JLabel(LanguageBundle.getString("keywordpool.heading")));
		
		heading.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
		
		setHeadingPane(heading);
	}
	
	@Override
	protected AbstractHeader createHeader(KeywordEntry entry)
	{
		KeywordHeader header = new KeywordHeader(""+entry.getInCount(), ""+entry.getOutCount(), entry.getKeyword().getValue());
		header.addInActionListener(new InDispatcher(header));
		header.addOutActionListener(new OutDispatcher(header));
		header.addItemActionListener(new ItemDispatcher(header));
		return header;
	}
	@Override
	protected Content<SnuEntry> createContent(KeywordEntry entry)
	{
		Content<SnuEntry> content = new KeywordContent();
		content.setCellRenderer(new KeywordCellRenderer());
		return content;
	}
	@Override
	protected void updateEntry(KCollapsiblePane pane, AbstractHeader aheader, Content<SnuEntry> content, KeywordEntry entry)
	{
		KeywordHeader header = (KeywordHeader)aheader;
		header.setInText(""+entry.getInCount());
		header.setOutText(""+entry.getOutSnus().size());
		
		Collection<Long> snuIds = new HashSet<Long>();
		snuIds.addAll(entry.getInSnus());
		snuIds.addAll(entry.getOutSnus());

		List<SnuEntry> snuEntries = new ArrayList<SnuEntry>();
		try {
			for (Long snuId : snuIds)
			{
				ISnu snu = SnuInputMapper.map( snuId );
				SnuEntry snuEntry = new SnuEntry(snu.getId(), snu.getName(), entry.getInSnus().contains(snu.getId()), entry.getOutSnus().contains(snu.getId()));
				snuEntries.add(snuEntry);
			}
		} catch (MapperException e) {
			Application.getInstance().showUnhandledErrorDialog(e);
		}
		Collections.sort(snuEntries);
		content.setModel(snuEntries);
	}
	public void setHeaderText(String text)
	{
		String extra = text!=null&&text.length()>0?" - " + text:"";
		headerLabel.setText(LanguageBundle.getString("keywordpool.heading") + extra);
	}
	public void addInActionListener(ActionListener listener)
	{
		inListeners.add(listener);
	}
	public void addOutActionListener(ActionListener listener)
	{
		outListeners.add(listener);
	}
	public void addItemActionListener(ActionListener listener)
	{
		itemListeners.add(listener);
	}
	private class InDispatcher implements ActionListener
	{
		private final AbstractHeader header;
		public InDispatcher(AbstractHeader header)
		{
			this.header = header;
		}
		public void actionPerformed(ActionEvent event)
		{
			// due to (my) shoddy impl the listeners want to pull a JComponent clientProperty off of the event source, so we edit the event
			event = new ActionEvent(header, event.getID(), event.getActionCommand(), event.getWhen(), event.getModifiers());
			for (ActionListener listener : inListeners)
				listener.actionPerformed(event);
		}
	}
	private class OutDispatcher implements ActionListener
	{
		private final AbstractHeader header;
		public OutDispatcher(AbstractHeader header)
		{
			this.header = header;
		}
		public void actionPerformed(ActionEvent event)
		{
			// due to (my) shoddy impl the listeners want to pull a JComponent clientProperty off of the event source, so we edit the event
			event = new ActionEvent(header, event.getID(), event.getActionCommand(), event.getWhen(), event.getModifiers());
			for (ActionListener listener : outListeners)
				listener.actionPerformed(event);
		}
	}
	private class ItemDispatcher implements ActionListener
	{
		private final AbstractHeader header;
		public ItemDispatcher(AbstractHeader header)
		{
			this.header = header;
		}
		public void actionPerformed(ActionEvent event)
		{
			// due to (my) shoddy impl the listeners want to pull a JComponent clientProperty off of the event source, so we edit the event
			event = new ActionEvent(header, event.getID(), event.getActionCommand(), event.getWhen(), event.getModifiers());
			for (ActionListener listener : itemListeners)
				listener.actionPerformed(event);
		}
	}
}
