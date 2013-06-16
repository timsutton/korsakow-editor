package org.korsakow.ide.ui.controller.action;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import org.korsakow.ide.Application;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.dnd.TransferableTreeTableNodes;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTable;
import org.korsakow.ide.util.ClipboardHelper;

public class CopyAction implements ActionListener
{
	private ResourceTreeTable resourceTreeTable;
	public CopyAction(ResourceTreeTable resourceBrowser)
	{
		this.resourceTreeTable = resourceBrowser;
	}
	public void actionPerformed(ActionEvent event)
	{
		Application app = Application.getInstance();
		app.beginBusyOperation();
		List<? extends KNode> selectedNodes = resourceTreeTable.getSelectedNodes();
		Transferable transferable = new TransferableTreeTableNodes(selectedNodes);
		ClipboardHelper.copy(transferable);
		app.endBusyOperation();
	}
}
