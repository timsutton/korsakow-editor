package org.korsakow.ide.ui.controller.action.media;

import javax.swing.JFrame;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.Sound;
import org.korsakow.domain.command.AbstractCommand;
import org.korsakow.domain.command.InsertSoundCommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.command.UpdateSoundCommand;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.mapper.input.SoundInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.ui.controller.action.AbstractAction;
import org.korsakow.ide.ui.controller.helper.MediaHelper;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTableModel;
import org.korsakow.ide.ui.resources.SoundResourceView;

public class SaveSoundAction extends AbstractAction
{
	public static SaveSoundAction insert(SoundResourceView view) {
		return new SaveSoundAction(view, null);
	}
	public static SaveSoundAction update(SoundResourceView view, long id) {
		return new SaveSoundAction(view, id);
	}
	
	private final SoundResourceView view;
	private Long id;
	private final boolean isNew;
	
	private SaveSoundAction(SoundResourceView view, Long id)
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
			return (isNew)?String.format("Create Sound '%s'", view.getNameFieldText()):String.format("Edit Sound '%s'", SoundInputMapper.map(id).getName());
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
				final Sound sound = SoundInputMapper.map(id);
				model.remove( sound.getId() );
				Application.getInstance().notifyResourceDeleted(sound);
			}
			super.undo();
			UoW.newCurrent();
			
			if (!(isNew)) {
				final Sound sound = SoundInputMapper.map(id);
				Application.getInstance().notifyResourceModified((sound));
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
			id = ((ISound)response.get(UpdateSoundCommand.SOUND)).getId();
			UoW.newCurrent();
			DataRegistry.rollback();
		} catch (CommandException e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
		} finally {
			((JFrame)view.getTopLevelAncestor()).dispose();
		}
	}

	private Response save(SoundResourceView view, Long id) throws CommandException
	{
		Class<? extends AbstractCommand> command;
		if (isNew) command = InsertSoundCommand.class;
		else command = UpdateSoundCommand.class;
		
		Response response = new Response();
		Request request = MediaHelper.createRequest(view, id);
		CommandExecutor.executeCommand(command, request, response);
		ISound sound = (ISound)response.get( UpdateSoundCommand.SOUND );
		ResourceTreeTableModel model = Application.getInstance().getProjectExplorer().getResourceBrowser().getResourceTreeTable().getTreeTableModel();
		if ( isNew ) {
			model.appendNode( ResourceNode.create( sound), model.getRoot() );
			Application.getInstance().notifyResourceAdded( sound );
		} else 
			Application.getInstance().notifyResourceModified( sound );
		return response;
	}
}
