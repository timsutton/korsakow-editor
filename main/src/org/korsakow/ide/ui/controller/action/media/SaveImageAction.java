package org.korsakow.ide.ui.controller.action.media;

import javax.swing.JFrame;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.Image;
import org.korsakow.domain.command.AbstractCommand;
import org.korsakow.domain.command.InsertImageCommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.command.UpdateImageCommand;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.mapper.input.ImageInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.ui.controller.action.AbstractAction;
import org.korsakow.ide.ui.controller.helper.MediaHelper;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTableModel;
import org.korsakow.ide.ui.resources.ImageResourceView;

public class SaveImageAction extends AbstractAction
{
	public static SaveImageAction insert(ImageResourceView view) {
		return new SaveImageAction(view, null);
	}
	public static SaveImageAction update(ImageResourceView view, long id) {
		return new SaveImageAction(view, id);
	}
	
	private final ImageResourceView view;
	private Long id;
	private final boolean isNew;
	
	private SaveImageAction(ImageResourceView view, Long id)
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
			return (isNew)?String.format("Create Image '%s'", view.getNameFieldText()):String.format("Edit Image '%s'", ImageInputMapper.map(id).getName());
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
				final Image image = ImageInputMapper.map(id);
				model.remove( image.getId() );
				Application.getInstance().notifyResourceDeleted(image);
			}
			super.undo();
			UoW.newCurrent();
			
			if (!(isNew)) {
				final Image image = ImageInputMapper.map(id);
				Application.getInstance().notifyResourceModified(image);
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
			id = ((IImage)response.get(UpdateImageCommand.IMAGE)).getId();
			UoW.newCurrent();
			DataRegistry.rollback();
		} catch (CommandException e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
		} finally {
			((JFrame)view.getTopLevelAncestor()).dispose();
		}
	}

	private Response save(ImageResourceView view, Long id) throws CommandException
	{
		Class<? extends AbstractCommand> command;
		if (isNew) command = InsertImageCommand.class;
		else command = UpdateImageCommand.class;
		
		Response response = new Response();
		Request request = MediaHelper.createRequest(view, id);
		CommandExecutor.executeCommand(command, request, response);
		IImage image = (IImage)response.get( UpdateImageCommand.IMAGE );
		ResourceTreeTableModel model = Application.getInstance().getProjectExplorer().getResourceBrowser().getResourceTreeTable().getTreeTableModel();
		if ( isNew ) {
			model.appendNode( ResourceNode.create( image ), model.getRoot() );
			Application.getInstance().notifyResourceAdded( image );
		} else 
			Application.getInstance().notifyResourceModified( image );
		return response;
	}
}
