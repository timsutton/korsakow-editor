package org.korsakow.ide.ui.controller.action.media;

import javax.swing.JFrame;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.Text;
import org.korsakow.domain.command.AbstractCommand;
import org.korsakow.domain.command.InsertTextCommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.command.UpdateTextCommand;
import org.korsakow.domain.interf.IText;
import org.korsakow.domain.mapper.input.TextInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.ui.controller.action.AbstractAction;
import org.korsakow.ide.ui.controller.helper.MediaHelper;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTableModel;
import org.korsakow.ide.ui.resources.TextResourceView;

public class SaveTextAction extends AbstractAction
{
	public static SaveTextAction insert(TextResourceView view) {
		return new SaveTextAction(view, null);
	}
	public static SaveTextAction update(TextResourceView view, long id) {
		return new SaveTextAction(view, id);
	}
	
	private final TextResourceView view;
	private Long id;
	private final boolean isNew;
	
	private SaveTextAction(TextResourceView view, Long id)
	{
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
			return (isNew)?String.format("Create Text '%s'", view.getNameFieldText()):String.format("Edit Text '%s'", TextInputMapper.map(id).getName());
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

			if (isNew) {
				final Text text = TextInputMapper.map(id);
				model.remove( text.getId() );
				Application.getInstance().notifyResourceDeleted(text);
			}
			super.undo();
			UoW.newCurrent();
			
			if (!(isNew)) {
				final Text text = TextInputMapper.map(id);
				Application.getInstance().notifyResourceModified((text));
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
			id = ((IText)response.get(UpdateTextCommand.TEXT)).getId();
			UoW.newCurrent();
			DataRegistry.rollback();
		} catch (CommandException e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
		} finally {
			((JFrame)view.getTopLevelAncestor()).dispose();
		}
	}

	private Response save(TextResourceView view, Long id) throws CommandException
	{
		Class<? extends AbstractCommand> command;
		if (isNew) command = InsertTextCommand.class;
		else command = UpdateTextCommand.class;
		
		Response response = new Response();
		Request request = MediaHelper.createRequest(view, id);
		CommandExecutor.executeCommand(command, request, response);
		IText text = (IText)response.get( UpdateTextCommand.TEXT );
		ResourceTreeTableModel model = Application.getInstance().getProjectExplorer().getResourceBrowser().getResourceTreeTable().getTreeTableModel();
		if ( isNew ) {
			model.appendNode( ResourceNode.create( text), model.getRoot() );
			Application.getInstance().notifyResourceAdded( text );
		} else
			Application.getInstance().notifyResourceModified( text );
		return response;
	}
}
