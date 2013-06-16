package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.TransferHandler.TransferSupport;

import org.korsakow.ide.Application;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTable;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTableModel;
import org.korsakow.ide.util.ClipboardHelper;
import org.korsakow.ide.util.ClipboardHelper.ClipboardResult;

public class PasteAction implements ActionListener
{
	private final ResourceTreeTable resourceTreeTable;
	public PasteAction(ResourceTreeTable resourceBrowser)
	{
		resourceTreeTable = resourceBrowser;
	}
	public void actionPerformed(ActionEvent event)
	{
		Application app = Application.getInstance();
		app.beginBusyOperation();
		KNode selectedNode = resourceTreeTable.getSelectedNode();
		if (selectedNode == null)
			selectedNode = resourceTreeTable.getRootNode();
		
		ResourceTreeTableModel model = resourceTreeTable.getTreeTableModel();
		ClipboardResult result = ClipboardHelper.paste();
		TransferSupport support = new TransferSupport(resourceTreeTable, result.getTransferable());
		resourceTreeTable.getTransferHandler().importData(support);
		app.endBusyOperation();
	}
}
