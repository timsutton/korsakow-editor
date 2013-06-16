package org.korsakow.ide.ui.controller.action;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.command.RenameResourceCommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.mapper.input.ResourceInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTable;

public class RenameCommitAction extends AbstractAction
{
	private final ResourceTreeTable treeTable;
	private final long id;
	private final String newName;
	public RenameCommitAction(ResourceTreeTable treeTable, long id, String newName)
	{
		this.treeTable = treeTable;
		this.id = id;
		this.newName = newName;
	}

	@Override
	public boolean isUndoable()
	{
		return true;
	}
	@Override // UndoableEdit
	public String getUndoPresentationName()
	{
		return String.format("Rename %s", newName);
	}
	@Override
	public void undo()
	{
		try {
			super.undo();
			UoW.newCurrent();
			Application.getInstance().notifyResourceModified(ResourceInputMapper.map(id));
		} catch (MapperException e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
		}
	}
	
	@Override
	public void performAction()
	{
		try {
			renameMedia(ResourceInputMapper.map(id), newName);
		} catch (CommandException e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
		} catch (MapperException e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
		} catch (InterruptedException e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
		} catch (Exception e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
		}
		treeTable.repaint();
	}
	
//	@Override
//	public void editingCanceled(ChangeEvent e)
//	{
//	}
//	@Override
//	public void editingStopped(ChangeEvent event)
//	{
//		String name = treeTable.getDefaultEditor(TreeTableModel.class).getCellEditorValue().toString();
//		// TreeTable.getEditingNode, the obvious choice, returns null at this point because swing is terribly designed
//		KNode editingNode = ((ResourceTreeTableCellEditor)treeTable.getDefaultEditor(TreeTableModel.class)).getEditingNode();
//
//		if (editingNode instanceof ResourceNode) {
//			try {
//				renameMedia(ResourceInputMapper.map(((ResourceNode)editingNode).getResourceId()), name);
//			} catch (CommandException e) {
//				Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
//			} catch (MapperException e) {
//				Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
//			} catch (InterruptedException e) {
//				Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
//			} catch (Exception e) {
//				Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
//			}
//    		treeTable.repaint();
//		}
//	}
	private static void renameMedia(IResource resource, String newName) throws CommandException, InterruptedException
	{
		Request request = new Request();
		request.set(RenameResourceCommand.ID, resource.getId());
		request.set(RenameResourceCommand.NAME, newName);
		Response response = new Response();
		CommandExecutor.executeCommand(RenameResourceCommand.class, request, response);
		Application.getInstance().notifyResourceModified(((IResource)response.get(RenameResourceCommand.RESOURCE)));
	}
}
