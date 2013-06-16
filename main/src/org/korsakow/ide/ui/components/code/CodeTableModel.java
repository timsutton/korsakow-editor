/**
 * 
 */
package org.korsakow.ide.ui.components.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import org.korsakow.ide.code.k5.K5Code;

public class CodeTableModel extends AbstractTableModel
{
	public static final int EVENT_SORT = 100;
	public static final int TIME_COLUMN = 0;
	public static final int CODE_COLUMN = 1;
	public static final int MAXLINKS_COLUMN = 2;
	
	private final Hashtable<Integer, String> identifiers = new Hashtable<Integer, String>();
	List<CodeRow> rows = new ArrayList<CodeRow>();

	public CodeTableModel()
	{
		addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				if (e.getColumn() == TIME_COLUMN)
					sort();
			}
		});
		rows.add(new CodeRow(null, new K5Code(""), null));
	}
	private boolean isLastRowEmpty()
	{
		return getRowCount()!=0 && getTimeAt(getRowCount()-1)==null && getCodeAt(getRowCount()-1).getRawCode().trim().length()==0;
	}
	public void clear()
	{
		List<CodeRow> oldRows = rows;
		rows = new ArrayList<CodeRow>();
		fireTableRowsDeleted(0, oldRows.size());
	}
	public int addRow(Long time, K5Code code)
	{
		int index = addRowInternal(time, code);
		fireTableRowsInserted(index, index);
		return index;
	}
	/**
	 * does not notify
	 */
	private int addRowInternal(Long time, K5Code code)
	{
		int index = isLastRowEmpty()?getRowCount()-1:getRowCount();
		CodeRow row = new CodeRow(time, code, null);
		rows.add(index, row);
		return rows.indexOf(row);
	}
	public void removeRows(List<Integer> toRemove)
	{
		toRemove = new ArrayList<Integer>(toRemove);
		Collections.sort(toRemove);
		// reverse iterate so that the input indices don't change with each removal
		for (int i = toRemove.size() - 1; i >= 0; --i) {
			int row = toRemove.get(i);
			rows.remove(row);
			fireTableRowsDeleted(row, row);
		}
	}
	public void removeRow(int row)
	{
		rows.remove(row);
		fireTableRowsDeleted(row, row);
	}
	/**
	 * Made public for Unit tests.
	 */
	public CodeRow getRow(int row)
	{
		return rows.get(row);
	}
	public int indexOfRow(CodeRow row)
	{
		return rows.indexOf(row);
	}
	public int getEmptyRow()
	{
		for (CodeRow row : rows)
			if (row.getCode().getRawCode().isEmpty())
				return rows.indexOf(row);
		// should not reach here...
		return -1;
	}
	public int getColumnCount() {
		return identifiers.size();
	}
	public int getRowCount() {
		return rows.size();
	}
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return getRow(rowIndex).getValueAt(columnIndex);
	}
    @Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
    	getRow(rowIndex).setValueAt(columnIndex, aValue);
    	fireTableCellUpdated(rowIndex, columnIndex);
    }
    public Long getTimeAt(int rowIndex)
    {
    	return getRow(rowIndex).getTime();
    }
    public void setTimeAt(Long time, int rowIndex)
    {
    	setValueAt(time, rowIndex, TIME_COLUMN);
    }
    public void setCodeAt(K5Code code, int rowIndex)
    {
    	setValueAt(code, rowIndex, CODE_COLUMN);
    }
    public void setMaxLinks(Long maxlinks, int rowIndex)
    {
    	setValueAt(maxlinks, rowIndex, MAXLINKS_COLUMN);
    }
    public K5Code getCodeAt(int rowIndex)
    {
    	return getRow(rowIndex).getCode();
    }
    public Long getMaxLinksAt(int rowIndex)
    {
    	return getRow(rowIndex).getMaxLinks();
    }
    @Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
    {
    	return true;
    }
    public void setColumnName(int column, String identifier)
    {
    	identifiers.put(column, identifier);
    }
    @Override
	public String getColumnName(int column)
    {
    	return identifiers.get(column);
    }
	@SuppressWarnings("unchecked")
	public void sort()
	{
		List<CodeRow> previousOrdering = new ArrayList<CodeRow>(rows);
		Collections.sort(rows);
		fireTableSorted(new TableModelSortEvent(this, previousOrdering));
	}
    public void fireTableSorted(TableModelSortEvent e)
    {
    	// Guaranteed to return a non-null array
    	Object[] listeners = listenerList.getListenerList();
    	// Process the listeners last to first, notifying
    	// those that are interested in this event
    	for (int i = listeners.length-2; i>=0; i-=2) {
    	    if (listeners[i+1] instanceof CodeTableModelListener) {
    		((CodeTableModelListener)listeners[i+1]).tableSorted(e);
    	    }
    	}
    }
}
