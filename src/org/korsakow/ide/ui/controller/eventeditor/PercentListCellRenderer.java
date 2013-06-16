/**
 * 
 */
package org.korsakow.ide.ui.controller.eventeditor;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.korsakow.domain.interf.IKeyword;

public class PercentListCellRenderer extends DefaultListCellRenderer
{
    public Component getListCellRendererComponent(
        JList list,
        Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus)
    {
    	Number num = (Number)value;
    	String display = String.format("%d", (int)(num.floatValue()*100));
    	return super.getListCellRendererComponent(list, display, index, isSelected, cellHasFocus);
    }
}