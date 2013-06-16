/**
 * 
 */
package org.korsakow.ide.ui.controller.eventeditor;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.korsakow.domain.interf.IMedia;

public class MediaListCellRenderer extends DefaultListCellRenderer
{
    @Override
	public Component getListCellRendererComponent(
        JList list,
        Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus)
    {
    	IMedia medium = (IMedia)value;
    	return super.getListCellRendererComponent(list, medium.getName(), index, isSelected, cellHasFocus);
    }
}