package org.korsakow.ide.ui.components;

import java.awt.event.MouseEvent;

import javax.swing.JTable;

import org.korsakow.ide.util.Platform;

public class KTable extends JTable {
	private boolean selectionEnabled = true;
	private Boolean isEditable;
	
	public void setSelectionEnabled(boolean enabled)
	{
		selectionEnabled = enabled;
	}
	public boolean isSelectionEneabled()
	{
		return selectionEnabled;
	}
	/**
	 * Set to non-null to override isCellEditable.
	 * @param editable
	 */
	public void setEditable(Boolean editable)
	{
		isEditable = editable;
	}
	public Boolean isEditable()
	{
		return isEditable;
	}
	public boolean isCellEditable(int row, int col)
	{
		if (isEditable == null)
			return super.isCellEditable(row, col);
		return isEditable();
	}
	public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend)
	{
		if (!selectionEnabled)
			return;
		super.changeSelection(rowIndex, columnIndex, toggle, extend);
	}
	protected void processMouseEvent(MouseEvent event)
	{
		// L&F fix for MAC which uses CTL-left click as a popup trigger
		if (Platform.isMacOS() && event.isControlDown() && event.getButton() == MouseEvent.BUTTON1) {
			event = new MouseEvent(event.getComponent(), event.getID(), event.getWhen(), event.getModifiers(), event.getX(), event.getY(), event.getClickCount(), true);
		}
		super.processMouseEvent(event);
	}
}
