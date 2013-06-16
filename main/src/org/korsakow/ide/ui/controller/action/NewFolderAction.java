package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.components.tree.FolderNode;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTable;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTableModel;

public class NewFolderAction implements ActionListener
{
	private ResourceTreeTable resourceTreeTable;
	public NewFolderAction(ResourceTreeTable resourceBrowser)
	{
		this.resourceTreeTable = resourceBrowser;
	}
	public void actionPerformed(ActionEvent event)
	{
		Application app = Application.getInstance();
		ResourceTreeTableModel model = resourceTreeTable.getTreeTableModel();
		
		KNode selectedNode = resourceTreeTable.getSelectedNode();
		if (selectedNode == null)
			selectedNode = model.getRoot();

		int index = -1;
		KNode parentNode = selectedNode.getParent();
		
		// null checks for rootNode, and for folders we want to create inside
		if (parentNode == null || selectedNode instanceof FolderNode) {
			parentNode = selectedNode;
			index = 0;
		} else
			index = model.getIndexOfChild(selectedNode.getParent(), selectedNode);
		
		FolderNode folderNode = new FolderNode(LanguageBundle.getString("general.newfoldername"));
		
		model.insertNodeInto(folderNode, parentNode, index);
	}
}
