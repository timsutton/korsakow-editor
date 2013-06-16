/**
 * 
 */
package org.korsakow.ide.ui.components.code;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

import org.korsakow.ide.ui.components.NewMediaPanel;

public class TimeCellRenderer extends DefaultTableCellRenderer
{
	public static final Font font = new Font(Font.MONOSPACED, Font.PLAIN, 10);
	private final Border border = new JTextField().getBorder();
	public TimeCellRenderer()
	{
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		String s;
		if (value == null)
			s = "-";
		else {
			s = NewMediaPanel.formatTime((Long)value);
		}
		JLabel comp = (JLabel)super.getTableCellRendererComponent(table, s, isSelected, hasFocus, row, column);
		comp.setBorder(border);
		comp.setFont(font);
		comp.setForeground(Color.black);
		
		comp.setBackground(Color.white);
		if (isSelected && table.getSelectedColumn()==column) {
	    	comp.setBackground(CodeCellEditor.SELECTION_BACKGROUND);
	    	comp.setForeground(Color.white);
		}
		return comp;
	}
}