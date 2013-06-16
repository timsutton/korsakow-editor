/**
 * 
 */
package org.korsakow.ide.ui.components.code;

import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

public class TableModelSortEvent extends TableModelEvent
{
	private final List<CodeRow> previousOrdering;
	public TableModelSortEvent(TableModel source, List<CodeRow> previousOrdering) {
		super(source);
		type = CodeTableModel.EVENT_SORT;
		this.previousOrdering = previousOrdering;
	}
	public List<CodeRow> getPreviousOrdering()
	{
		return previousOrdering;
	}
}