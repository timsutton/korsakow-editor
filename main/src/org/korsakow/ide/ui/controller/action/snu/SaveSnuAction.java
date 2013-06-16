package org.korsakow.ide.ui.controller.action.snu;

import javax.swing.JFrame;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.Snu;
import org.korsakow.domain.command.AbstractCommand;
import org.korsakow.domain.command.InsertSnuCommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.command.UpdateSnuCommand;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.mapper.input.MediaInputMapper;
import org.korsakow.domain.mapper.input.SnuInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.ui.controller.action.AbstractAction;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTableModel;
import org.korsakow.ide.ui.resources.SnuResourceView;

public class SaveSnuAction extends AbstractAction
{
	public static SaveSnuAction insert(SnuResourceView view) {
		return new SaveSnuAction(view, null);
	}
	public static SaveSnuAction update(SnuResourceView view, long id) {
		return new SaveSnuAction(view, id);
	}
	private final SnuResourceView view;
	private Long id;
	private final boolean isNew;
	
	private SaveSnuAction(SnuResourceView view, Long id) {
		this.view = view;
		this.id = id;
		isNew = id==null;
	}
	
	@Override
	public boolean isUndoable()
	{
		return true;
	}
	@Override // UndoableEdit
	public String getUndoPresentationName()
	{
		try {
			return (isNew)?String.format("Create SNU '%s'", view.getNameFieldText()):String.format("Edit SNU '%s'", SnuInputMapper.map(id).getName());
		} catch (MapperException e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
			return "";
		}
	}
	@Override
	public void undo()
	{
		try {
			ResourceTreeTableModel model = Application.getInstance().getProjectExplorer().getResourceBrowser().getResourceTreeTable().getTreeTableModel();
			Snu snu = SnuInputMapper.map(id);
			IMedia media = snu.getMainMedia();
			media.getVersion(); // force proxy
			
			KNode node = model.findResource( snu.getId() );
			KNode insertParent = node.getParent();
			int insertIndex = model.getIndexOfChild( insertParent, node );
			
			if (isNew) {
				Application.getInstance().notifyResourceDeleted(snu);
				model.remove( snu.getId() );
			}
			
			super.undo();
			UoW.newCurrent();
			
			media = MediaInputMapper.map( media.getId() );
			
			if (isNew) {
				Application.getInstance().notifyResourceAdded( media );
			} else {
				snu = SnuInputMapper.map(id);
				Application.getInstance().notifyResourceModified(snu);
			}
		} catch (MapperException e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
		}
	}
	
	@Override
	public void performAction()
	{
		try {
			Response response = save(view, id);
			id = ((ISnu)response.get(UpdateSnuCommand.SNU)).getId();
			UoW.newCurrent();
			DataRegistry.rollback();
		} catch (CommandException e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
		} catch (InterruptedException e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
		} finally {
			((JFrame)view.getTopLevelAncestor()).dispose();
		}
	}

	private Response save(SnuResourceView view, Long id) throws CommandException, InterruptedException
	{
		Class<? extends AbstractCommand> command;
		if (isNew) command = InsertSnuCommand.class;
		else command = UpdateSnuCommand.class;
		
		Response response = new Response();
		Request request = SnuHelper.createRequest(view, id);
		CommandExecutor.executeCommand(command, request, response);
		ISnu snu = (ISnu)response.get( UpdateSnuCommand.SNU );
		ResourceTreeTableModel model = Application.getInstance().getProjectExplorer().getResourceBrowser().getResourceTreeTable().getTreeTableModel();
		
		if ( isNew ) {
			KNode node = model.findResource( snu.getMainMedia().getId() );
			if ( node != null ) {
				KNode insertParent = node.getParent();
				int insertIndex = model.getIndexOfChild( insertParent, node );
				model.removeNodeFromParent( node );
				model.insertNodeInto( ResourceNode.create( snu ), insertParent, insertIndex );
			} else {
				model.appendNode( ResourceNode.create( snu ), model.getRoot() );
			}
			Application.getInstance().notifyResourceAdded( snu );
		} else
		Application.getInstance().notifyResourceModified( snu );
		return response;
	}
}
