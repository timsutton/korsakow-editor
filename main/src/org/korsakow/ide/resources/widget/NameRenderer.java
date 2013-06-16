/**
 * 
 */
package org.korsakow.ide.resources.widget;

import java.awt.Component;
import java.util.MissingResourceException;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.korsakow.ide.lang.LanguageBundle;

class NameRenderer extends DefaultTableCellRenderer
{
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column)
    {
    	try {
    		value = LanguageBundle.getString("widget.property."+value+".label");
    	} catch (MissingResourceException e) {
    		// 
    	}
    	return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}