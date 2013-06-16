package org.korsakow.ide.ui.components.cell;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.korsakow.domain.interf.IResource;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.ui.components.model.ResourceComboBoxModel;

public class ResourceDOComboBoxRenderer extends DefaultListCellRenderer
{
    @Override
	public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus)
    {
    	super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    	if (value instanceof IResource == false) {
    		if (value == null || value == ResourceComboBoxModel.NULL_SELECTION_ITEM) {
        		setIcon(null);
        		setText("---");
    		} else {
        		setIcon(null);
        		setText(""+value);
    		}
    	} else {
	    	IResource resource = (IResource)value;
	    	try {
	    		setIcon(ResourceType.forId(resource.getType()).getIcon());
	    	} catch (IllegalArgumentException e) {
	    		// some unknown resource type... so no icon; i guess we could have an "unknown" icon.
	    	}
	    	setText(resource.getName());
    	}
    	return this;
    }
}
