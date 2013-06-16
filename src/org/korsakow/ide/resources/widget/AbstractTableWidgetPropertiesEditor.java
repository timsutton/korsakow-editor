package org.korsakow.ide.resources.widget;

import java.awt.Component;
import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import org.korsakow.ide.ui.components.KTable;
import org.korsakow.ide.ui.components.model.KComboboxModel;
import org.korsakow.ide.util.UIUtil;

/**
 * A an incomplete Table-oriented implementation of WidgetPropertiesEditor.
 * 
 * @author d
 *
 */
public abstract class AbstractTableWidgetPropertiesEditor extends AbstractWidgetPropertiesEditor implements CellEditorListener
{
	public enum Column
	{
		NAME,
		VALUE,
	}
	protected KTable table;
	protected JComboBox propertyEditor;
	protected ListCellRenderer defaultRenderer;
	protected TableCellEditor cellEditor;
	protected String editingName;
	protected boolean isEditing = false;
	public AbstractTableWidgetPropertiesEditor(Collection<WidgetModel> widgets)
	{
		super(widgets);
//		setLayout(new FlowLayout(FlowLayout.LEFT));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		table = new KTable() {
			@Override
			public Component prepareEditor(TableCellEditor editor, int row, int column)
			{
				Component comp = super.prepareEditor(editor, row, column);
				AbstractTableWidgetPropertiesEditor.this.prepareEditor(row, column);
				return comp;
			}
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return column > 0;
			}
		};
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane);
		table.setAutoCreateColumnsFromModel(false);
		table.setModel(new DefaultTableModel());
		UIUtil.addColumn(table, "Name", Column.NAME);
		UIUtil.addColumn(table, "Value", Column.VALUE);
		propertyEditor = new JComboBox();
		defaultRenderer = new DefaultListCellRenderer();
		cellEditor = new DefaultCellEditor(propertyEditor);
		table.getColumn(Column.VALUE).setCellEditor(cellEditor);
	}
	protected void prepareEditor(int row, int column)
	{
		isEditing = true;
		editingName = (String)table.getValueAt(row, 0);
		cellEditor.removeCellEditorListener(this);
		cellEditor.addCellEditorListener(this);
		ComboBoxModel model = new KComboboxModel();
//		propertyEditor.setEditable(true);
		propertyEditor.setRenderer(defaultRenderer); // it is perfectly acceptable for a property handler to set a custom renderer, so we reset it
		propertyEditor.setModel(model);
	}
	/**
	 * The name of the property currently being edited in the table. Complements JTable.editingRow/Column.
	 * @return null if not currently editing
	 */
	public String getEditingName()
	{
		return editingName;
	}
	/**
	 * 
	 * @param name
	 * @param value
	 * @return false on error
	 */
	protected boolean commitPropertyChanges(String name, Object value)
	{
		return false;
	}
    /** This tells the listeners the editor has ended editing */
    public void editingStopped(ChangeEvent e)
    {
    	if (!isEditing) // swing sometimes decides we need stop notification even if editing had previously ended
    		return;
    	commitPropertyChanges(editingName, cellEditor.getCellEditorValue());
    	fireEditingStopped();
    	isEditing = false;
    	stopEditing(); // if we're being notified by the PropertyHandler we have to do this so the editing session on the table will stop
    }

    /** This tells the listeners the editor has canceled editing */
    public void editingCanceled(ChangeEvent e)
    {
    	if (!isEditing) // swing sometimes decides we need stop notification even if editing had previously ended
    		return;
    	fireEditingCanceled();
    	isEditing = false;
    	cancelEditing(); // if we're being notified by the PropertyHandler we have to do this so the editing session on the table will stop
    }
    
    @Override
	public void stopEditing()
    {
    	// invoke it on the table cell editor causing our hooks will be notified as usual.
    	// note that this behavior is different than the supermethod behavior which just fireEditingStopped() immediately
		TableCellEditor editor = table.getCellEditor();
		if (editor != null)
			editor.stopCellEditing();
    }
    @Override
	public void cancelEditing()
    {
		TableCellEditor editor = table.getCellEditor();
		if (editor != null)
			editor.cancelCellEditing();
    }
}
