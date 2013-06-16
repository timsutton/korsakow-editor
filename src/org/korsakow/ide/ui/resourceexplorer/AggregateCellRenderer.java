package org.korsakow.ide.ui.resourceexplorer;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class AggregateCellRenderer implements TableCellRenderer
{
	protected Map<Class, TableCellRenderer> rendererMap = new HashMap<Class, TableCellRenderer>();
	protected TableCellRenderer defaultRenderer = new DefaultTableCellRenderer();
	
	public static Object getColumnIdentifier(JTable table, int column)
	{
		return table.getColumnModel().getColumn(column).getIdentifier();
	}
	
	protected TableCellRenderer getRenderer(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{
		TableCellRenderer renderer = null;
		Object identifier = getColumnIdentifier(table, column);
		if (identifier != null)
			renderer = rendererMap.get(identifier.getClass());
		if (renderer == null)
			renderer = defaultRenderer;
		return renderer;
	}
	public void addRenderer(Class clazz, TableCellRenderer renderer)
	{
		rendererMap.put(clazz, renderer);
	}
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		TableCellRenderer renderer = getRenderer(table, value, isSelected, hasFocus, row, column);
		return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}

}
