package org.korsakow.ide.ui.controller.action;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import org.korsakow.ide.Application;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.dnd.TransferableTreeTableNodes;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTable;

public class DuplicateAction implements ActionListener
{
	private CopyAction copyAction;
	private PasteAction pasteAction;
	public DuplicateAction(ResourceTreeTable resourceBrowser)
	{
		copyAction = new CopyAction(resourceBrowser);
		pasteAction = new PasteAction(resourceBrowser);
	}
	public void actionPerformed(ActionEvent event)
	{
		// technically this could fail if the clipboard were modified in the milliseconds between these two actions
		// so in a way we're introducing a subtle bug here. TODO: is this bad?
		copyAction.actionPerformed(event);
		pasteAction.actionPerformed(event);
	}
}
