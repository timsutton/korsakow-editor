/**
 * 
 */
package org.korsakow.ide.ui.components.linkpool;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

import org.korsakow.ide.ui.components.KList;

public class ContentCellRenderer extends JLabel implements ListCellRenderer
{
	public ContentCellRenderer()
	{
		setOpaque(true);
	}
	public Component getListCellRendererComponent(
		       JList list,              // the list
		       Object value,            // value to display
		       int index,               // cell index
		       boolean isSelected,      // is the cell selected
		       boolean cellHasFocus)    // does the cell have focus
	{
		SnuContentEntry entry = (SnuContentEntry)value;
		setText(entry.getSnuName());
		int mouseIndex = ((KList)list).getRolloverIndex();
		if (mouseIndex==index) {
			setBackground(UIManager.getColor("CollapsiblePaneHeader.background"));
		} else {
			setBackground(UIManager.getColor("CollapsiblePaneHeader.background2"));
		}
		return this;
	}
	
}