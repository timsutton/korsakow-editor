package org.korsakow.ide.ui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;


public class KCollapsiblePane extends KLayoutPanel
{
	public static final String HEADER_UI_PROPERTY_OWNER_PANE = "org.korsakow.ide.ui.components.KCollapsiblePane.collapsiblePane";
	final protected AbstractButton header; // made final because i'm too lazy to take care of the details involved in replacing the header
	protected JComponent content;
	protected JPanel contentPane;
	protected boolean autoMaxSize = false;
	public KCollapsiblePane()
	{
		this("", null);
	}
	public KCollapsiblePane(String headerText)
	{
		this(headerText, null);
	}
	public KCollapsiblePane(String headerText, JComponent content)
	{
		this(new Header(), content);
		setHeaderText(headerText);
		setContent(content);
	}
	public KCollapsiblePane(AbstractButton header, JComponent content)
	{
		super.setLayout(null);

		super.add(this.header = header);
		header.putClientProperty(HEADER_UI_PROPERTY_OWNER_PANE, this);
		
		contentPane = new JPanel(new BorderLayout());//new FlowLayout(FlowLayout.RIGHT));
		contentPane.setOpaque(true);
		contentPane.setBackground(UIManager.getColor("CollapsiblePane.background"));
		super.add(contentPane);
//		contentPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
		header.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggle();
			}
		});
		
		if (content != null)
			setContent(content);
	}
	protected void fireActionEvent()
	{
		ActionEvent event = new ActionEvent(this, 0, "expand");
		for (ActionListener listener : listenerList.getListeners(ActionListener.class))
			listener.actionPerformed(event);
	}
	public void addActionListener(ActionListener listener)
	{
		listenerList.add(ActionListener.class, listener);
	}
	/**
	 * The Pane's preferred size always reflects is expanded state. This option
	 * determines whether its maximum size is also set in accordance.
	 * 
	 * Basically this was needed to make the component play nice in BoxLayouts
	 * @param b
	 */
	public void setAutoSetMaxSize(boolean b)
	{
		autoMaxSize = b;
	}
	@Override
	public void doLayout()
	{
		Insets insets = getInsets();
		Dimension size = getSize();
		getHeader().setSize(size.width - (insets.left+insets.right), getHeader().getPreferredSize().height);
		getContentPane().setSize(size.width - (insets.left+insets.right), size.height - (insets.top + insets.bottom) - getHeader().getHeight());
		getHeader().setLocation(insets.left, insets.top);
		getContentPane().setLocation(insets.left, getHeader().getSize().height + insets.top);
	}
	@Override
	public Dimension getPreferredSize()
	{
		if (isPreferredSizeSet())
			return super.getPreferredSize();
		Dimension pref = null;
		
		if (isExpanded()) {
			pref = getContentPane().getPreferredSize();
			pref.height += header.getPreferredSize().getHeight();
		} else {
			pref = getContentPane().getPreferredSize();
			pref.height = getHeader().getPreferredSize().height;
//			pref.height += header.getPreferredSize().getHeight();
		}
		Insets insets = getInsets();
		pref.width += insets.left + insets.right;
		pref.height += insets.top + insets.bottom;
		return pref;
	}
	@Override
	public Dimension getMaximumSize()
	{
		if (isMaximumSizeSet())
			return super.getMaximumSize();
		Dimension d = getPreferredSize();
		d.width = Short.MAX_VALUE;
		return d;
	}
	public void setHeaderText(String text)
	{
		header.setText(text);
	}
	public AbstractButton getHeader()
	{
		return header;
	}
	public void setContent(JComponent content)
	{
		getContentPane().removeAll();
		this.content = content;
		if (content != null)
			getContentPane().add(content);
		revalidate();
		repaint();
	}
	public JComponent getContent()
	{
		return content;
	}
	public JPanel getContentPane()
	{
		return contentPane;
	}
	public boolean isExpanded()
	{
		return contentPane.isVisible();
	}
	public void setExpanded(boolean expanded)
	{
		boolean wasExpanded = contentPane.isVisible();
		if (expanded) {
			contentPane.setVisible(true);
		} else {
			contentPane.setVisible(false);
		}
		if (autoMaxSize) {
			Dimension d = getPreferredSize();
			d.width = Integer.MAX_VALUE;
			setMaximumSize(d);
		}
		revalidate();
		if (wasExpanded != expanded)
			fireActionEvent();
	}
	public void toggle()
	{
		setExpanded(!isExpanded());
	}
	public static class Header extends JButton
	{
	    private static final String uiClassID = "CollapsiblePaneHeaderUI";
		public Header()
		{
		}
		@Override
		public String getUIClassID()
		{
			return uiClassID;
		}
	}
}
