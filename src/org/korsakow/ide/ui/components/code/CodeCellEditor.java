/**
 * 
 */
package org.korsakow.ide.ui.components.code;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextField;

import org.korsakow.ide.code.RuleParserException;
import org.korsakow.ide.code.k5.K5Code;
import org.korsakow.ide.code.k5.K5RuleParser2;
import org.korsakow.ide.ui.components.cell.DefaultAdvancedCellEditor;
import org.korsakow.ide.util.UIUtil;

public class CodeCellEditor extends DefaultAdvancedCellEditor
{
	private final K5RuleParser2 parser = new K5RuleParser2();
	private boolean valid = false;
	public static Color SELECTION_BACKGROUND = new Color(0x214ca1);
	public CodeCellEditor()
	{
		setClickCountToStart(1);
		// we used to have a focus listener here to stop cell editing. replaced by terminateEditOnFocusLost (see CodeTable ctor)
	}
	public void requestFocus()
	{
    	editorComponent.requestFocus();
    	// set cursor to end, no selection
    	UIUtil.runUITaskLater(new Runnable() {
    		public void run() {
    			((JTextField)editorComponent).setSelectionStart(((JTextField)editorComponent).getText().length());
    			((JTextField)editorComponent).setSelectionEnd(((JTextField)editorComponent).getText().length());
    		}
    	});
	}
    @Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected,
			int row, int column)
    {
    	K5Code code = (K5Code)value;
    	delegate.setValue(code.getRawCode());
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
		editorComponent.setBackground(SELECTION_BACKGROUND);
    	editorComponent.setForeground(Color.white);
		((JTextField)editorComponent).setCaretColor(Color.white);
    	return editorComponent;
    }
    @Override
	public boolean doStopCellEditing()
    {
    	try {
    		parser.parse(super.getCellEditorValue().toString());
    		valid = true;
    	} catch (RuleParserException e) {
    		valid = false;
    	}
		return super.doStopCellEditing();
    }
    @Override
	public Object getCellEditorValue()
    {
    	String value = super.getCellEditorValue().toString().trim();
    	K5Code code = new K5Code(value);
    	code.setValid(valid);
    	return code;
    }
}