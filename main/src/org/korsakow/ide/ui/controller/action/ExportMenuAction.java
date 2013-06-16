package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.korsakow.ide.Application;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.ui.ProjectExplorer;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTable;

public class ExportMenuAction implements ActionListener
{
	private final ResourceTreeTable resourceTreeTable;
	public ExportMenuAction(ResourceTreeTable resourceTreeTable)
	{
		this.resourceTreeTable = resourceTreeTable;
	}
	public void actionPerformed(ActionEvent event)
	{
		boolean enabled = false;
		KNode selectedNode = resourceTreeTable.getSelectedNode();
		if (selectedNode != null) {
			
			if (selectedNode instanceof ResourceNode) {
				ResourceNode resourceNode = (ResourceNode)selectedNode;
		
				if (resourceNode.getResourceType() == ResourceType.INTERFACE) {
					enabled = true;
				}
			}
		}
		Application.getInstance().getProjectExplorer().getMenu(ProjectExplorer.Action.MenuFileExportInterface).setEnabled(enabled);
	}
}
