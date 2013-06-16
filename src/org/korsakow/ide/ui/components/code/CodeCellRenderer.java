/**
 * 
 */
package org.korsakow.ide.ui.components.code;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

import org.korsakow.ide.code.k5.K5Code;

public class CodeCellRenderer extends DefaultTableCellRenderer
{
	private final Border border = new JTextField().getBorder();
	public CodeCellRenderer()
	{
	}
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col)
	{
		K5Code code = (K5Code)value;
		if (code != null)
			value = code.getRawCode();
		JLabel label = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
		label.setBorder(border);
		if (code != null)
			label.setForeground(code.isValid()?Color.black:Color.red);
		else
			label.setText("-");
		label.setBackground(Color.white);
		label.setForeground(Color.black);
		if (isSelected && table.getSelectedColumn()==col) {
	    	label.setBackground(CodeCellEditor.SELECTION_BACKGROUND);
	    	label.setForeground(Color.white);
		}
		return label;
	}
}