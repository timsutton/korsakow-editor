package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTable;

public class ImportMenuAction implements ActionListener
{
	private final ResourceTreeTable resourceTreeTable;
	public ImportMenuAction(ResourceTreeTable resourceTreeTable)
	{
		this.resourceTreeTable = resourceTreeTable;
	}
	public void actionPerformed(ActionEvent event)
	{
	}
}
