/**
 * 
 */
package org.korsakow.ide.ui.components.code;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.korsakow.ide.ui.resources.SnuResourceView;

public class MaxLinksCellEditor extends DefaultCellEditor
{
	private final JComboBox editor;
	public MaxLinksCellEditor()
	{
		super(new JComboBox());
		editor = (JComboBox)editorComponent;
		editor.setEditable(true);
		editor.setRenderer(new MaxLinksCellRenderer());
	}
    @Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected,
			int row, int column)
    {
    	if (value == null)
    		value = SnuResourceView.INFINITE;
    	super.getTableCellEditorComponent(table, value, isSelected, row, column);
//    	editor.setModel(new KComboboxModel(SnuResourceView.MAXLINKS_CHOICES, value));
    	editor.setModel(new DefaultComboBoxModel(SnuResourceView.MAXLINKS_CHOICES));
    	editor.setSelectedItem(value);
    	editor.setForeground(Color.white);
    	editor.setBackground(CodeCellEditor.SELECTION_BACKGROUND);
    	editor.putClientProperty("ui.background2", null);
		((JTextField)editor.getEditor().getEditorComponent()).setCaretColor(Color.white);
    	return editor;
    }
    @Override
	public Object getCellEditorValue()
    {
    	try {
    		Object value = super.getCellEditorValue();
    		if (value != null && value.toString().length() > 0 && !SnuResourceView.INFINITE.equals(value.toString()))
    			return (long)(Long.parseLong(super.getCellEditorValue().toString()));
    		else
    			return null;
    	} catch (NumberFormatException e) {
    		return null;
    	}
    }
}