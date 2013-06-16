/**
 * 
 */
package org.korsakow.ide.ui.components.pool;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionListener;

import org.korsakow.ide.ui.components.KList;

public class Content<CE extends ContentEntry> extends JPanel
{
	private JScrollPane scroll;
	protected KList list;
	public Content()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		list = new KList();
		list.setVisibleRowCount(0);
		add(scroll = new JScrollPane(list));
		list.setRolloverEnabled(true);
	}
	public void addListSelectionListener(ListSelectionListener listener)
	{
		list.addListSelectionListener(listener);
	}
	public void setCellRenderer(ListCellRenderer cellRenderer)
	{
		list.setCellRenderer(cellRenderer);
	}
	public void setModel(Collection<CE> entries)
	{
		DefaultListModel model = new DefaultListModel();
		for (CE entry : entries)
			model.addElement(entry);
		list.setModel(model);
		list.setVisibleRowCount(entries.size());
	}
}
