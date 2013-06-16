/**
 * 
 */
package org.korsakow.ide.ui.controller.eventeditor;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.korsakow.domain.interf.IKeyword;

public class KeywordListCellRenderer extends DefaultListCellRenderer
{
    public Component getListCellRendererComponent(
        JList list,
        Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus)
    {
    	IKeyword keyword = (IKeyword)value;
    	return super.getListCellRendererComponent(list, keyword.getValue(), index, isSelected, cellHasFocus);
    }
}