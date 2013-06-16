/**
 * 
 */
package org.korsakow.ide.ui.components.code;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.korsakow.ide.ui.components.NewMediaPanel;
import org.korsakow.ide.util.UIUtil;

public class TimeCellEditor extends DefaultCellEditor
{
	public TimeCellEditor()
	{
		super(new JTextField());
	}
    @Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected,
			int row, int column)
    {
    	if (value == null)
    		value = "";
    	else {
    		value = NewMediaPanel.formatTime((Long)value);
    	}
    	UIUtil.runUITaskLater(new Runnable() {
    		public void run() {
		    	editorComponent.requestFocus();
		    	// set cursor to end, no selection
		    	UIUtil.runUITaskLater(new Runnable() {
		    		public void run() {
		    			((JTextField)editorComponent).setSelectionStart(((JTextField)editorComponent).getText().length());
		    			((JTextField)editorComponent).setSelectionEnd(((JTextField)editorComponent).getText().length());
		    		}
		    	});
    		}
    	});
    	final Component editor = super.getTableCellEditorComponent(table, value, isSelected, row, column);
		((JTextField)editor).setCaretColor(Color.black);
		if (isSelected && table.getSelectedColumn()==column) {
			editor.setBackground(CodeCellEditor.SELECTION_BACKGROUND);
	    	editor.setForeground(Color.white);
			((JTextField)editor).setCaretColor(Color.white);
		}
    	editor.setFont(TimeCellRenderer.font);
		return editor;
    }
    @Override
	public Object getCellEditorValue()
    {
    	return NewMediaPanel.parseTime(super.getCellEditorValue().toString());
    }
}