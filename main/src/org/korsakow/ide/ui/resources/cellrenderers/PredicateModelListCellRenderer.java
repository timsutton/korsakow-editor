/**
 * 
 */
package org.korsakow.ide.ui.resources.cellrenderers;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.korsakow.ide.ui.model.PredicateModel;
import org.korsakow.services.plugin.predicate.PredicateTypeInfoFactory;

public class PredicateModelListCellRenderer extends DefaultListCellRenderer implements ListCellRenderer
{
	public Component getListCellRendererComponent(
		       JList list,              // the list
		       Object value,            // value to display
		       int index,               // cell index
		       boolean isSelected,      // is the cell selected
		       boolean cellHasFocus)    // does the cell have focus
	{
		PredicateModel model = (PredicateModel)value; 
		String display = PredicateTypeInfoFactory.getFactory().getTypeInfo(model.getType().getId()).getFormattedDisplayString(model.getPropertyValues().toArray());
		return super.getListCellRendererComponent(list, display, index, isSelected, cellHasFocus);
	}
	
}