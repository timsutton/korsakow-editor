/**
 * 
 */
package org.korsakow.ide.ui.components.code;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.korsakow.ide.code.k5.K5Code;

public class CodeTableController implements TableModelListener
{
	private final CodeTable table;
	private boolean isUpdate = false;
	
	public CodeTableController(CodeTable table)
	{
		this.table = table;
		table.addPropertyChangeListener("model", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				modelReplaced();
			}
		});
		
		modelReplaced();
	}

	private void modelReplaced() {
    	table.getModel().addTableModelListener(new TableModelRepaintListener()); // might not be necessary (should happen in L&F)
		table.getModel().addTableModelListener(new SorterListener());
	}
	
	@Override
	public void tableChanged(TableModelEvent e)
	{
		if (isUpdate)
			return;
		isUpdate = true;
		if (e.getColumn() == CodeTableModel.CODE_COLUMN)
			ensureExactlyOneEmptyRow();
		isUpdate = false;
	}

	private void ensureExactlyOneEmptyRow()
	{
		List<Integer> toRemove = new ArrayList<Integer>();
		CodeTableModel model = table.getModel();
		for (int i = 0; i < model.getRowCount(); ++i) {
			if (model.getCodeAt(i).getRawCode().trim().isEmpty()) {
				toRemove.add(i);
			}
		}
		model.removeRows(toRemove);	
		model.addRow(null, new K5Code(""));
	}
	/**
	 * Maintains the selection when the model is sorted
	 * @author d
	 *
	 */
	private class SorterListener implements CodeTableModelListener
	{
		public void tableChanged(TableModelEvent e) {
			//
		}
	    public void tableSorted(TableModelSortEvent sortEvent) {
			// the order of rows has changed, but the selection model doesn't know about it
			// we get those rows which were previously selected by comparing the previousOrdering of rows
			// against the still out-of-date selection
			List<CodeRow> previousModelOrdering = sortEvent.getPreviousOrdering();
			int[] selectedViewRows = table.getSelectedRows();
			Collection<CodeRow> previousSelection = new HashSet<CodeRow>();
			for (int row : selectedViewRows) {
				previousSelection.add(previousModelOrdering.get(table.convertRowIndexToView(row)));
			}

			if (previousSelection.isEmpty())
				return;
			// and now update the selection model
			int viewIndex = table.getModel().indexOfRow(previousSelection.iterator().next());
			table.getSelectionModel().setSelectionInterval(viewIndex, viewIndex);
		}
	}
	private class TableModelRepaintListener implements TableModelListener
	{
		public void tableChanged(TableModelEvent e) {
			table.repaint();
		}
	}
}
