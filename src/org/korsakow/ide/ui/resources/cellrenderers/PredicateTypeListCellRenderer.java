/**
 * 
 */
package org.korsakow.ide.ui.resources.cellrenderers;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.korsakow.ide.resources.PredicateType;

public class PredicateTypeListCellRenderer extends DefaultListCellRenderer implements ListCellRenderer
{
	public Component getListCellRendererComponent(
		       JList list,              // the list
		       Object value,            // value to display
		       int index,               // cell index
		       boolean isSelected,      // is the cell selected
		       boolean cellHasFocus)    // does the cell have focus
	{
		PredicateType type = (PredicateType)value; 
		return super.getListCellRendererComponent(list, type.getDisplayName(), index, isSelected, cellHasFocus);
//		setHorizontalTextPosition(SwingConstants.CENTER);
//		setVerticalTextPosition(SwingConstants.LEFT);
//		setIcon(res.getType().getIcon());
//		setText(res.getName());
//		return this;
	}
	
}