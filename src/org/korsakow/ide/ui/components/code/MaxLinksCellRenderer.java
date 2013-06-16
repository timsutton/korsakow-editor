/**
 * 
 */
package org.korsakow.ide.ui.components.code;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;

import org.korsakow.ide.ui.resources.SnuResourceView;

public class MaxLinksCellRenderer extends DefaultTableCellRenderer implements ListCellRenderer
{
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		if (value == null)
			value = SnuResourceView.INFINITE;
		JComponent comp = (JComponent)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		comp.setBackground(Color.white);
		comp.setForeground(Color.black);
		comp.setBorder(new JTextField().getBorder());
		if (isSelected && table.getSelectedColumn()==column) {
	    	comp.setBackground(CodeCellEditor.SELECTION_BACKGROUND);
			comp.setForeground(Color.white);
		}
    	comp.putClientProperty("ui.background2", null);
		return comp;
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean hasFocus) {
		if (value == null)
			value = SnuResourceView.INFINITE;
		JLabel comp = this;
		comp.setText(value.toString());
		if (isSelected)
			comp.setForeground(list.getSelectionForeground());
		else
			comp.setForeground(list.getForeground());
		return comp;
	}
}