/**
 * 
 */
package org.korsakow.ide.ui.resourceexplorer;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.ui.components.tree.FolderNode;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.components.tree.ResourceNode;

/**
 * Handles rendering of the TreeTable's tree column.
 * @author d
 *
 */
public class ResourceTreeCellRenderer extends DefaultTreeCellRenderer
{
	public ResourceTreeCellRenderer()
	{
	}
	public Component getTreeCellRendererComponent(JTree tree, FolderNode value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{
		JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value.getName(), selected, expanded, false, row, hasFocus);
		
		// use platform folder icon
		//label.setIcon(UIResourceManager.getIcon(UIResourceManager.ICON_FOLDER));
		return label;
	}
	public Component getTreeCellRendererComponent(JTree tree, ResourceNode value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{
		String name = value.getName();
		if (name == null || name.isEmpty()) {
			name = value.getType().getDisplayString();
		}
		JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, name, selected, expanded, leaf, row, hasFocus);
		
		ResourceType resourceType = value.getResourceType();

		setIcon(resourceType.getIcon());
		setHorizontalTextPosition(SwingConstants.RIGHT);
		setHorizontalAlignment(SwingConstants.CENTER);
		setVerticalAlignment(SwingConstants.CENTER);
		return label;
	}
	public Component getTreeCellRendererComponent(JTree tree, KNode value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{
		return super.getTreeCellRendererComponent(tree, (value).getName(), selected, expanded, leaf, row, hasFocus);
	}
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus)
	{
		if (value instanceof FolderNode)
			getTreeCellRendererComponent(tree, (FolderNode)value, selected, expanded, leaf, row, hasFocus);
		else
		if (value instanceof ResourceNode)
			getTreeCellRendererComponent(tree, (ResourceNode)value, selected, expanded, leaf, row, hasFocus);
		else
		if (value instanceof KNode)
			getTreeCellRendererComponent(tree, (KNode)value, selected, expanded, leaf, row, hasFocus);
		else
			super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		
		// apparently this changed between JDK 5 and 6 
		this.setHorizontalAlignment(JLabel.LEFT);
		return this;
	}
}