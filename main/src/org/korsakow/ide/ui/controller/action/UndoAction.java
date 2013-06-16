package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.korsakow.ide.Application;
import org.korsakow.ide.controller.ApplicationAdapter;

public class UndoAction extends ApplicationAdapter implements ActionListener
{

	public void actionPerformed(ActionEvent event) 
	{
		Application app = Application.getInstance();
		if (app.getUndoManager().canUndo())
			app.getUndoManager().undo();
//		Collection<ResourceEditor> editors = app.getOpenEditors();
//		if (!editors.isEmpty()) {
//			if (!ExitAction.checkForChangesAndPrompt())
//				return;
//		}
//		for (ResourceEditor editor : editors)
//			editor.dispose();
//		
//		Collection<Resource> all = Command.listAll();
//		DataRegistry.getDomSession().rollbackHeadToPreviousVersion();
//		
//		
	}
}
