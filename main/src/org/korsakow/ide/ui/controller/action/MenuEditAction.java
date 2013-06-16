/**
 * 
 */
package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.ProjectExplorer;

public class MenuEditAction extends AbstractAction
{
	private final ProjectExplorer explorer;
	public MenuEditAction(ProjectExplorer explorer)
	{
		this.explorer = explorer;
	}
	public void actionPerformed(ActionEvent e) {
		boolean haveUndo = false;//DataRegistry.getDomSession().getHistory().size() > 0;
		explorer.getMenu(ProjectExplorer.Action.MenuEditUndo).setEnabled(Application.getInstance().getUndoManager().canUndo());
		explorer.getMenu(ProjectExplorer.Action.MenuEditUndo).setText(LanguageBundle.getString("projectexplorer.menu.edit.undo.label", Application.getInstance().getUndoManager().getUndoPresentationName()));
		explorer.getMenu(ProjectExplorer.Action.MenuEditRedo).setEnabled(Application.getInstance().getUndoManager().canRedo());
		explorer.getMenu(ProjectExplorer.Action.MenuEditRedo).setText(LanguageBundle.getString("projectexplorer.menu.edit.redo.label", Application.getInstance().getUndoManager().getRedoPresentationName()));
	}
}
