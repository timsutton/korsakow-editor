/**
 * 
 */
package org.korsakow.ide.ui.resourceexplorer;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

import org.korsakow.ide.resources.MediaProperty;
import org.korsakow.ide.resources.ResourceProperty;
import org.korsakow.ide.resources.SnuProperty;
import org.korsakow.ide.ui.resources.MediaResourceListColumns;

public class ResourceTreeTableHeaderCellRenderer extends AggregateCellRenderer
{
	public ResourceTreeTableHeaderCellRenderer()
	{
		addRenderer(ResourceBrowser.Column.class, new ResourceBrowserColumnRenderer());
		addRenderer(ResourceProperty.class, new ResourcePropertyRenderer());
		addRenderer(MediaResourceListColumns.Column.class, new MediaColumnRenderer());
		addRenderer(MediaProperty.class, new MediaPropertyRenderer());
		addRenderer(SnuProperty.class, new SnuPropertyRenderer());
	}
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{
		Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (comp instanceof JLabel) {
			JLabel renderer = (JLabel)comp;
			renderer.setHorizontalTextPosition(SwingConstants.LEFT);
			renderer.setHorizontalAlignment(SwingConstants.CENTER);
			renderer.setVerticalAlignment(SwingConstants.CENTER);

			// not sure why we have to do this every time, but the JTableHeader source does it this way, and it doesn't work otherwise
			renderer.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
		}
		return comp;
	}
	private static class ResourceBrowserColumnRenderer extends DefaultTableCellRenderer
	{
		@Override
		public Component getTableCellRendererComponent(
			       JTable table,              // the list
			       Object value,            // value to display
			       boolean isSelected,      // is the cell selected
			       boolean cellHasFocus,      // is the cell selected
			       int row, // column index
			       int column)    // column index
		{
			super.getTableCellRendererComponent(table, value, isSelected, cellHasFocus, row, column);
			setIcon(null);

			ResourceBrowser.Column property = (ResourceBrowser.Column)AggregateCellRenderer.getColumnIdentifier(table, column);
			switch (property)
			{
			default:
				setToolTipText(null);
				setText(value.toString());
				break;
			case ISSNU:
				setToolTipText(null);
				setText(value.toString());
				setIcon(null);
				break;
			case PREVIEW:
				setToolTipText(null);
				setText(value.toString());
				setIcon(null);
				break;
			case BACKGROUNDSOUND:
				setToolTipText(null);
				setText(value.toString());
				setIcon(null);
				break;
			case CLICKSOUND:
				setToolTipText(null);
				setText(value.toString());
				setIcon(null);
				break;
			}
			return this;
		}
	}
	private static class ResourcePropertyRenderer extends DefaultTableCellRenderer
	{
		@Override
		public Component getTableCellRendererComponent(
			       JTable table,              // the list
			       Object value,            // value to display
			       boolean isSelected,      // is the cell selected
			       boolean cellHasFocus,      // is the cell selected
			       int row, // column index
			       int column)    // column index
		{
			super.getTableCellRendererComponent(table, value, isSelected, cellHasFocus, row, column);
			setIcon(null);

			ResourceProperty property = (ResourceProperty)AggregateCellRenderer.getColumnIdentifier(table, column);
			switch (property)
			{
			case TYPE:
				// Text should not be set manually, this overrides the properties file
//				setText("type");
				break;
			default:
				setToolTipText(null);
				setText(value.toString());
				break;
			}
			return this;
		}
	}
	private static class ResourceColumnRenderer extends DefaultTableCellRenderer
	{
		@Override
		public Component getTableCellRendererComponent(
			       JTable table,              // the list
			       Object value,            // value to display
			       boolean isSelected,      // is the cell selected
			       boolean cellHasFocus,      // is the cell selected
			       int row, // column index
			       int column)    // column index
		{
			super.getTableCellRendererComponent(table, value, isSelected, cellHasFocus, row, column);
			setIcon(null);
			return this;
		}
	}
	private static class MediaPropertyRenderer extends DefaultTableCellRenderer
	{
		@Override
		public Component getTableCellRendererComponent(
			       JTable table,              // the list
			       Object value,            // value to display
			       boolean isSelected,      // is the cell selected
			       boolean cellHasFocus,      // is the cell selected
			       int row, // column index
			       int column)    // column index
		{
			super.getTableCellRendererComponent(table, value, isSelected, cellHasFocus, row, column);
			setIcon(null);

			MediaProperty property = (MediaProperty)AggregateCellRenderer.getColumnIdentifier(table, column);
			switch (property)
			{
			default:
				break;
			}
			return this;
		}
	}
	private static class MediaColumnRenderer extends DefaultTableCellRenderer
	{
		@Override
		public Component getTableCellRendererComponent(
			       JTable table,              // the list
			       Object value,            // value to display
			       boolean isSelected,      // is the cell selected
			       boolean cellHasFocus,      // is the cell selected
			       int row, // column index
			       int column)    // column index
		{
			super.getTableCellRendererComponent(table, value, isSelected, cellHasFocus, row, column);
			setIcon(null);

			MediaResourceListColumns.Column property = (MediaResourceListColumns.Column)AggregateCellRenderer.getColumnIdentifier(table, column);
			switch (property)
			{
			default:
				setToolTipText(null);
				setText(value.toString());
				setIcon(null);
				break;
			case PREVIEW:
				setToolTipText(null);
				setText(value.toString());
				setIcon(null);
				break;
			case BACKGROUND:
				setToolTipText(null);
				setText(value.toString());
				setIcon(null);
				break;
			case CLICKSOUND:
				setToolTipText(null);
				setText(value.toString());
				setIcon(null);
				break;
			}
			return this;
		}
	}
	private static class SnuPropertyRenderer extends DefaultTableCellRenderer
	{
		@Override
		public Component getTableCellRendererComponent(
			       JTable table,              // the list
			       Object value,            // value to display
			       boolean isSelected,      // is the cell selected
			       boolean cellHasFocus,      // is the cell selected
			       int row, // column index
			       int column)    // column index
		{
			super.getTableCellRendererComponent(table, value, isSelected, cellHasFocus, row, column);
			setIcon(null);

			SnuProperty property = (SnuProperty)AggregateCellRenderer.getColumnIdentifier(table, column);
			
			switch (property)
			{
			default:
				setToolTipText(null);
				setText(value.toString());
				break;
			case ENDER:
				setToolTipText(null);
				setText(value.toString());
				setIcon(null);
				break;
			case STARTER:
				setToolTipText(null);
				setText(value.toString());
				setIcon(null);
				break;
			case BACKGROUNDSOUND:
				setToolTipText(null);
				setText(value.toString());
				setIcon(null);
				break;
			case PREVIEWMEDIA:
				setToolTipText(null);
				setText(value.toString());
				setIcon(null);
				break;
			}
			return this;
		}
	}
}