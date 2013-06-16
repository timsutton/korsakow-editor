package org.korsakow.ide.ui.controller.action.interf;

import javax.swing.JFrame;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.Interface;
import org.korsakow.domain.command.AbstractCommand;
import org.korsakow.domain.command.InsertInterfaceCommand;
import org.korsakow.domain.command.InsertInterfaceCopyCommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.command.UpdateInterfaceCommand;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.mapper.input.InterfaceInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.ui.controller.action.AbstractAction;
import org.korsakow.ide.ui.controller.helper.InterfaceHelper;
import org.korsakow.ide.ui.interfacebuilder.InterfaceBuilderMainPanel;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTableModel;

public class SaveInterfaceAction extends AbstractAction
{
	public static SaveInterfaceAction insert(InterfaceBuilderMainPanel view) {
		return new SaveInterfaceAction(view, null, false);
	}
	public static SaveInterfaceAction update(InterfaceBuilderMainPanel view, long id) {
		return new SaveInterfaceAction(view, id, false);
	}
	public static SaveInterfaceAction copy(InterfaceBuilderMainPanel view, long id) {
		return new SaveInterfaceAction(view, id, true);
	}
	
	private final InterfaceBuilderMainPanel view;
	private Long id;
	private final boolean isNew;
	private final boolean copy;
	
	private SaveInterfaceAction(InterfaceBuilderMainPanel view, Long id, boolean copy)
	{
		this.view = view;
		this.id = id;
		isNew = id==null;
		this.copy = copy;
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
			return (isNew||copy)?String.format("Create Interface '%s'", view.getNameFieldText()):String.format("Edit Interface '%s'", InterfaceInputMapper.map(id).getName());
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

			if (isNew || copy) {
				final Interface interf = InterfaceInputMapper.map(id);
				model.remove( interf.getId() );
				Application.getInstance().notifyResourceDeleted(interf);
			}
			super.undo();
			UoW.newCurrent();
			
			if (!(isNew || copy)) {
				final Interface interf = InterfaceInputMapper.map(id);
				Application.getInstance().notifyResourceModified(interf);
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
			id = ((IInterface)response.get(UpdateInterfaceCommand.INTERFACE)).getId();
			UoW.newCurrent();
			DataRegistry.rollback();
		} catch (CommandException e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
		} catch (MapperException e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
		} finally {
			((JFrame)view.getTopLevelAncestor()).dispose();
		}
	}

	private Response save(InterfaceBuilderMainPanel view, Long id) throws CommandException, MapperException
	{
		Class<? extends AbstractCommand> command;
		if (copy) command = InsertInterfaceCopyCommand.class;
		else if (isNew) command = InsertInterfaceCommand.class;
		else command = UpdateInterfaceCommand.class;
		
		Response response = new Response();
		Request request = InterfaceHelper.createRequest(view, id);
		CommandExecutor.executeCommand(command, request, response);
		IInterface interf = (IInterface)response.get( UpdateInterfaceCommand.INTERFACE );
		ResourceTreeTableModel model = Application.getInstance().getProjectExplorer().getResourceBrowser().getResourceTreeTable().getTreeTableModel();
		if ( isNew || copy ) {
			model.appendNode( ResourceNode.create( interf ), model.getRoot() );
			Application.getInstance().notifyResourceAdded( interf );
		} else
			Application.getInstance().notifyResourceModified( interf );
		return response;
	}
}
