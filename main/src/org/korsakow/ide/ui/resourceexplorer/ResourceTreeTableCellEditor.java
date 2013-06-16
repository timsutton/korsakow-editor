package org.korsakow.ide.ui.resourceexplorer;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextField;

import org.korsakow.ide.ui.components.KTreeTable.KTreeTableCellEditor;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.util.UIUtil;

public class ResourceTreeTableCellEditor extends KTreeTableCellEditor
{
	private final ResourceTreeTable treeTable;
	public ResourceTreeTableCellEditor(ResourceTreeTable treeTable)
	{
		super(treeTable);
		this.treeTable = treeTable;
	}
	private KNode editingNode;
	@Override
	public ResourceTreeTable getTreeTable() {
		return (ResourceTreeTable)super.getTreeTable();
	}
	public KNode getEditingNode()
	{
		return editingNode;
	}
	@Override
	public Component getTableCellEditorComponent(JTable table,
		     Object value,
		     boolean isSelected,
		     int r, int c)
    {
		editingNode = treeTable.getNodeAt(r);
		if (editingNode != null)
			value = editingNode.getName();
		if (value == null)
			value = "";
		int TreeTableCellEditorFixedRow = getTreeTable().getTree().isRootVisible()?r:r+1; // TreeTableCellEditor tries to compensate if root isn't visible. however its trick seems to simply not work
		final JTextField textField = (JTextField)super.getTableCellEditorComponent(table, value, isSelected, TreeTableCellEditorFixedRow, c); 
		textField.setBorder(null);
		UIUtil.runUITaskLater(new Runnable() {
			public void run() {
				textField.setSelectionStart(0);
				textField.setSelectionEnd(textField.getText().length());
				textField.requestFocus();
			}
		});
		return textField;
    }	
}
