package org.korsakow.ide.ui.components.pool;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.korsakow.ide.ui.components.KCollapsiblePane;
import org.korsakow.ide.ui.components.KLayoutPanel;
import org.korsakow.ide.util.WeakReferenceMap;

public abstract class AbstractPool<HE extends HeaderEntry, CE extends ContentEntry> extends JPanel implements ListDataListener
{
	public static final String ACTION_PANEL_EXPAND = "Pool.ACTION_PANEL_EXPAND";
	private AbstractPoolModel<HE> model = null;
	private JScrollPane scrollPane;
	private JPanel contentPane;
	private JPanel headingPane;
	private ActionListener paneActionListener;
	private ListSelectionListener paneSelectionListener;
	/**
	 * Using a weak map so we can be lazy about cleaning up.
	 */
	private WeakReferenceMap<Object, KCollapsiblePane> paneMap = new WeakReferenceMap<Object, KCollapsiblePane>();
	
	public AbstractPool()
	{
		this(null);
	}
	public AbstractPool(AbstractPoolModel<HE> model)
	{
		initUI();
		setModel(model);
	}
	public KCollapsiblePane getPane(Object id)
	{
		return paneMap.get(id);
	}
	public void setModel(AbstractPoolModel<HE> model)
	{
		if (this.model != null) {
			this.model.removeListDataListener(this);
		}
		this.model = model;
		if (this.model != null) {
			model.addListDataListener(this);
			rebuild();
		}
		revalidate(); // maybe necessary?
		repaint(); // necessary: artifacts seen when repainting to a smaller size
	}
	public AbstractPoolModel<HE> getModel()
	{
		return this.model;
	}
	public void contentsChanged(ListDataEvent e) {
		if (e.getIndex0() == -1)
			rebuild();
		else {
			for (int i = e.getIndex0(); i <= e.getIndex1(); ++i)
			{
				HE entry = model.getEntry(i);

				KCollapsiblePane pane = (KCollapsiblePane)contentPane.getComponent(i);
				AbstractHeader header = (AbstractHeader)pane.getHeader();
				Content<CE> content = (Content<CE>)pane.getContent();
				
				updateEntry(pane, header, content, entry);
			}
		}
		revalidate();
	}
	public void setHeadingPane ( JPanel headingPane ) {
		if ( headingPane == null ) {
			// if we set the header to null, remove from contentPane
			if ( this.headingPane != null ) {
				remove(this.headingPane);
			}
			// otherwise do nothing
			this.headingPane = null;
		} else {
			this.headingPane = headingPane;
			add(this.headingPane, BorderLayout.NORTH);
		}
		
	}
	public JPanel getHeadingPane ()
	{
		return this.headingPane;
	}
	public void intervalAdded(ListDataEvent e) {
	}
	public void intervalRemoved(ListDataEvent e) {
	}
	public void addActionListener(ActionListener listener)
	{
		listenerList.add(ActionListener.class, listener);
	}
	public void addListSelectionListener(ListSelectionListener listener)
	{
		listenerList.add(ListSelectionListener.class, listener);
	}
	private void rebuild()
	{
		contentPane.removeAll();
		for (HE entry : model.getData()) {
			KCollapsiblePane pane = createPane(entry);
			pane.setExpanded(false);
			pane.addActionListener(paneActionListener);
			contentPane.add(pane);
		}
		revalidate();
	}
	protected KCollapsiblePane createPane(HE entry)
	{
		AbstractHeader header = createHeader(entry);
		Content content = createContent(entry);
		KCollapsiblePane pane = new KCollapsiblePane(header, content);
		Object id = getModel().getId(entry);
		pane.putClientProperty("poolid", id);
		header.putClientProperty("poolid", id);
		content.addListSelectionListener(paneSelectionListener);
		paneMap.put(id, pane);
		return pane;
	}
	protected abstract AbstractHeader createHeader(HE entry);
	protected abstract Content<CE> createContent(HE entry);
	protected abstract void updateEntry(KCollapsiblePane pane, AbstractHeader header, Content<CE> content, HE entry);
	
	protected void initUI()
	{
		setLayout(new BorderLayout());
		contentPane = new KLayoutPanel();
//		contentPane.setLayout(new VerticalFlowLayout());
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
//		setLayout(new BorderLayout());
		scrollPane = new JScrollPane(contentPane);
//		scrollPane.setPreferredSize(new Dimension(100,100));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(scrollPane, BorderLayout.CENTER);
		paneActionListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				fireActionEvent(event.getSource(), ACTION_PANEL_EXPAND);
			}
		};
		paneSelectionListener = new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent event)
			{
				fireListSelectionEvent(event);
			}
		};
	}
	protected void fireListSelectionEvent(ListSelectionEvent event)
	{
		for (ListSelectionListener listener : listenerList.getListeners(ListSelectionListener.class))
			listener.valueChanged(event);
	}
	protected void fireActionEvent(Object source, String command)
	{
		ActionEvent event = new ActionEvent(source, 0, command);
		for (ActionListener listener : listenerList.getListeners(ActionListener.class))
			listener.actionPerformed(event);
	}
}
