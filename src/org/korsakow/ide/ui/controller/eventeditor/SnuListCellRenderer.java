/**
 * 
 */
package org.korsakow.ide.ui.controller.eventeditor;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.korsakow.domain.interf.ISnu;

public class SnuListCellRenderer extends DefaultListCellRenderer
{
    @Override
	public Component getListCellRendererComponent(
        JList list,
        Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus)
    {
    	ISnu snu = (ISnu)value;
    	return super.getListCellRendererComponent(list, snu.getName(), index, isSelected, cellHasFocus);
    }
}