package org.korsakow.ide.ui.components.cell;

import javax.swing.event.CellEditorListener;

import org.korsakow.ide.ui.components.event.VetoableChangeEvent;

public interface AdvancedCellEditorListener extends CellEditorListener {
	public void editingWillStop(VetoableChangeEvent event);
}
