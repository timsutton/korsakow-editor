/**
 * 
 */
package org.korsakow.ide.ui.interfacebuilder;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.korsakow.ide.resources.WidgetType;

public class WidgetCellRenderer extends JComponent implements ListCellRenderer
{
	private final DefaultListCellRenderer renderer = new DefaultListCellRenderer();
	
	public WidgetCellRenderer() {
		setLayout(new BorderLayout(0,0));
		add(renderer);
	}
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		String str;
		if (value instanceof WidgetType) {
			str = ((WidgetType)value).getDisplayName();
		} else {
			str = String.valueOf(value);
			isSelected = false;
			cellHasFocus = false;
		}
		
		renderer.getListCellRendererComponent(list, str, index, isSelected, cellHasFocus);

		if (value instanceof WidgetType) {
			setBorder(null);
		} else {
//			renderer.setBackground(renderer.getBackground().brighter());
//			setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		}
		
		return this;
	}
}