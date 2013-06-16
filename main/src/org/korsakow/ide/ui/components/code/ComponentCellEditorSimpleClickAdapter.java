package org.korsakow.ide.ui.components.code;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import org.korsakow.ide.util.UIUtil;

/**
 * Causes mouse press events to be directly forwarded to the cell editor. For example default JTable behavior is to
 * click the cell whereupon the editor is created. This class causes that initial click to happen on the editor.
 * 
 * For example, if the cell content and editor is a JButton, you can then just click the button as expected.
 * 
 * @author d
 *
 */
public final class ComponentCellEditorSimpleClickAdapter extends MouseAdapter
{
	private int componentEditorColumn;
	public ComponentCellEditorSimpleClickAdapter(int componentEditorColumn)
	{
		this.componentEditorColumn = componentEditorColumn;
	}
	public void mousePressed(final MouseEvent event)
	{
		final JTable table = (JTable)event.getComponent();
		Point p = event.getPoint();
		final int column = table.columnAtPoint(p);
		final int row = table.rowAtPoint(p);
		// only apply to selected columns and valid rows
		if (column != componentEditorColumn || row == -1)
			return;
		if (!table.editCellAt(row, column))
			return;
		// run later in case the editor needs a chance to be setup
		UIUtil.runUITaskLater(new Runnable() {
			public void run() {
				// dispatch the event to the editor
				if (table.getEditorComponent() != null) {
					MouseEvent copy = new MouseEvent(table.getEditorComponent(), event.getID(), event.getWhen(), event.getModifiers(), event.getX(), event.getY(), event.getClickCount(), UIUtil.isPopupTrigger(event));
					table.getEditorComponent().dispatchEvent(copy);
				}
			}
		});
	}
}
