package org.korsakow.ide.ui.controller.action.media;

import javax.swing.JFrame;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.Video;
import org.korsakow.domain.command.AbstractCommand;
import org.korsakow.domain.command.InsertVideoCommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.command.UpdateVideoCommand;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.domain.mapper.input.VideoInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.ui.controller.action.AbstractAction;
import org.korsakow.ide.ui.controller.helper.MediaHelper;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTableModel;
import org.korsakow.ide.ui.resources.VideoResourceView;

public class SaveVideoAction extends AbstractAction
{
	public static  SaveVideoAction insert(VideoResourceView view) {
		return new SaveVideoAction(view, null, true);
	}
	public static  SaveVideoAction update(VideoResourceView view, long id) {
		return new SaveVideoAction(view, id, false);
	}
	
	private final VideoResourceView view;
	private Long id;
	private final boolean isNew;
	
	private SaveVideoAction(VideoResourceView view, Long id, boolean copy)
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
			return (isNew)?String.format("Create Video '%s'", view.getNameFieldText()):String.format("Edit Video '%s'", VideoInputMapper.map(id).getName());
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
				final Video video = VideoInputMapper.map(id);
				model.remove( video.getId() );
				Application.getInstance().notifyResourceDeleted(video);
			}
			super.undo();
			UoW.newCurrent();
			
			if (!(isNew)) {
				final Video video = VideoInputMapper.map(id);
				Application.getInstance().notifyResourceModified((video));
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
			id = ((IVideo)response.get(UpdateVideoCommand.VIDEO)).getId();
			UoW.newCurrent();
			DataRegistry.rollback();
		} catch (CommandException e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
		} finally {
			((JFrame)view.getTopLevelAncestor()).dispose();
		}
	}

	private Response save(VideoResourceView view, Long id) throws CommandException
	{
		Class<? extends AbstractCommand> command;
		if (isNew) command = InsertVideoCommand.class;
		else command = UpdateVideoCommand.class;
		
		Response response = new Response();
		Request request = MediaHelper.createRequest(view, id);
		CommandExecutor.executeCommand(command, request, response);
		IVideo video = (IVideo)response.get( UpdateVideoCommand.VIDEO );
		ResourceTreeTableModel model = Application.getInstance().getProjectExplorer().getResourceBrowser().getResourceTreeTable().getTreeTableModel();
		if ( isNew ) {
			model.appendNode( ResourceNode.create( video ), model.getRoot() );
			Application.getInstance().notifyResourceAdded( video );
		} else
			Application.getInstance().notifyResourceModified( video );
		for (IResource modified : response.getModifiedResources())
			Application.getInstance().notifyResourceModified( modified );
		return response;
	}
}
