/**
 * 
 */
package org.korsakow.ide.ui.resourceexplorer;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import org.korsakow.ide.resources.MediaProperty;
import org.korsakow.ide.resources.ResourceProperty;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.resources.SnuProperty;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.ui.resources.MediaResourceListColumns;
import org.korsakow.ide.util.UIResourceManager;

/**
 * Renders columns other than the tree column in a TreeTable
 */
public class ResourceTreeTableCellRenderer extends AggregateCellRenderer
{
	public ResourceTreeTableCellRenderer()
	{
		addRenderer(ResourceBrowser.Column.class, new ResourceBrowserColumnRenderer());
		addRenderer(ResourceProperty.class, new ResourcePropertyRenderer());
		addRenderer(MediaResourceListColumns.Column.class, new MediaColumnRenderer());
		addRenderer(MediaProperty.class, new MediaPropertyRenderer());
		addRenderer(SnuProperty.class, new SnuPropertyRenderer());
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
			super.getTableCellRendererComponent(table, "", isSelected, cellHasFocus, row, column);
			setIcon(null);
			if (value instanceof ResourceNode == false)
				return this;
			ResourceNode resourceNode = (ResourceNode)value;
			ResourceType type = resourceNode.getType();

			ResourceBrowser.Column property = (ResourceBrowser.Column)AggregateCellRenderer.getColumnIdentifier(table, column);
			switch (property)
			{
			case ISSNU:
				setText("");
				if (resourceNode.isSnufied())
					setIcon(UIResourceManager.getIcon(UIResourceManager.ICON_CHECK));
				else
					setIcon(null);
				break;
			case PREVIEW:
				setText("");
				setIcon(null);
				if (resourceNode.getPreview())
					setIcon(UIResourceManager.getIcon(UIResourceManager.ICON_CHECK));
				else
					setIcon(null);
				setHorizontalAlignment(SwingConstants.CENTER);
				break;
			case BACKGROUNDSOUND:
				setText("");
				setIcon(null);
				if (resourceNode.getBgSound())
					setIcon(UIResourceManager.getIcon(UIResourceManager.ICON_CHECK));
				else
					setIcon(null);
				setHorizontalAlignment(SwingConstants.CENTER);
				break;
			case CLICKSOUND:
				setText("");
				setIcon(null);
				if (resourceNode.getClickSound())
					setIcon(UIResourceManager.getIcon(UIResourceManager.ICON_CHECK));
				else
					setIcon(null);
				setHorizontalAlignment(SwingConstants.CENTER);
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
			super.getTableCellRendererComponent(table, "", isSelected, cellHasFocus, row, column);
			setIcon(null);
			if (value instanceof ResourceNode == false)
				return this;
			ResourceNode resourceNode = (ResourceNode)value;
			ResourceType type = resourceNode.getResourceType();

			ResourceProperty property = (ResourceProperty)AggregateCellRenderer.getColumnIdentifier(table, column);
			switch (property)
			{
			case TYPE:{
				// do not be mislead: this doesn't actually handle the tree column of the treetable (see ResourceTreeCellRenderer)
				// but it would handle this info if it appeared in any other column.
				if (resourceNode.getMediaType() != null)
					setIcon(resourceNode.getMediaType().getIcon());
				else
					setIcon(resourceNode.getType().getIcon());
				setHorizontalTextPosition(SwingConstants.LEFT);
				setHorizontalAlignment(SwingConstants.CENTER);
				setVerticalAlignment(SwingConstants.CENTER);
				}break;
			case NAME:{
				// do not be mislead: this doesn't actually handle the tree column of the treetable
				// but it would handle this info if it appeared in any other column.
				// see ResourceTreeCellRenderer
				setText(resourceNode.getName());
				}break;
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
			super.getTableCellRendererComponent(table, "", isSelected, cellHasFocus, row, column);
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
			super.getTableCellRendererComponent(table, "", isSelected, cellHasFocus, row, column);
			setIcon(null);
			if (value instanceof ResourceNode == false)
				return this;
			ResourceNode resourceNode = (ResourceNode)value;
			ResourceType type = resourceNode.getResourceType();
			if (!type.isMedia()) {
				if (type != ResourceType.SNU)
					return this;
			}

			MediaProperty property = (MediaProperty)AggregateCellRenderer.getColumnIdentifier(table, column);
			switch (property)
			{
			case FILENAME:
				if (resourceNode.getFilename() != null)
					setText(resourceNode.getFilename());
				else
					setText("N/A");
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
			super.getTableCellRendererComponent(table, "", isSelected, cellHasFocus, row, column);
			setIcon(null);
			if (value instanceof ResourceNode == false)
				return this;
			ResourceNode resourceNode = (ResourceNode)value;
			ResourceType type = resourceNode.getResourceType();
			if (!type.isMedia()) {
				if (type!=ResourceType.SNU)
					return this;
//				resource = ((Snu)resource).getMainMedia();
			}
			
			MediaResourceListColumns.Column property = (MediaResourceListColumns.Column)AggregateCellRenderer.getColumnIdentifier(table, column);
			switch (property)
			{
			case PREVIEW:
				setText("");
				setIcon(null);
				if (resourceNode.getPreview())
					setIcon(UIResourceManager.getIcon(UIResourceManager.ICON_CHECK));
				else
					setIcon(null);
				setHorizontalAlignment(SwingConstants.CENTER);
				break;
			case BACKGROUND:
				setText("");
				setIcon(null);
				if (resourceNode.getBgSound())
					setIcon(UIResourceManager.getIcon(UIResourceManager.ICON_CHECK));
				else
					setIcon(null);
				setHorizontalAlignment(SwingConstants.CENTER);
				break;
			case CLICKSOUND:
				setText("");
				setIcon(null);
				if (resourceNode.getClickSound())
					setIcon(UIResourceManager.getIcon(UIResourceManager.ICON_CHECK));
				else
					setIcon(null);
				setHorizontalAlignment(SwingConstants.CENTER);
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
			super.getTableCellRendererComponent(table, "", isSelected, cellHasFocus, row, column);
			setIcon(null);
			if (value instanceof ResourceNode == false)
				return this;
			ResourceNode resourceNode = (ResourceNode)value;
			ResourceType type = resourceNode.getResourceType();
			if (type!=ResourceType.SNU)
				return this;

			SnuProperty property = (SnuProperty)AggregateCellRenderer.getColumnIdentifier(table, column);
			
			switch (property)
			{
			case ENDER:
				setText("");
				if (resourceNode.isEndSnu())
					setIcon(UIResourceManager.getIcon(UIResourceManager.ICON_CHECK));
				else
					setIcon(null);
				setHorizontalAlignment(SwingConstants.CENTER);
				break;
			case STARTER:
				setText("");
				if (resourceNode.isStartSnu())
					setIcon(UIResourceManager.getIcon(UIResourceManager.ICON_CHECK));
				else
					setIcon(null);
				setHorizontalAlignment(SwingConstants.CENTER);
				break;
			case LIVES:
				Object lives = resourceNode.getLives();
				if (lives == null)
					lives = UIResourceManager.getSymbol(UIResourceManager.SYMBOL_INFINITE);
				setText(lives.toString());
				setHorizontalAlignment(SwingConstants.CENTER);
				break;
			case INTERFACE:
				if (resourceNode.getInterfaceName() != null)
					setText(resourceNode.getInterfaceName());
				else
					setText("");
				break;
			case BACKGROUNDSOUND:
				setText("");
				if (resourceNode.getBgSound())
					setIcon(UIResourceManager.getIcon(UIResourceManager.ICON_CHECK));
				else
					setIcon(null);
				setHorizontalAlignment(SwingConstants.CENTER);
				break;
			case PREVIEWMEDIA:
				setText("");
				if (resourceNode.getPreview())
					setIcon(UIResourceManager.getIcon(UIResourceManager.ICON_CHECK));
				else
					setIcon(null);
				setHorizontalAlignment(SwingConstants.CENTER);
				break;
			}
			return this;
		}
	}
}