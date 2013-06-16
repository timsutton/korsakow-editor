package org.korsakow.domain.command;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.Interface;
import org.korsakow.domain.InterfaceFactory;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.domain.mapper.input.InterfaceInputMapper;

public class DuplicateInterfaceCommand extends AbstractCommand{


	public DuplicateInterfaceCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		try {
			Interface original = InterfaceInputMapper.map(request.getLong("id"));
			List<IWidget> widgetCopies = new ArrayList<IWidget>();
			for (IWidget widgetOriginal : original.getWidgets()) {
				Request widgetRequest = new Request();
				widgetRequest.set("id", widgetOriginal.getId());
				new DuplicateWidgetCommand(widgetRequest, new Response()).execute();
				IWidget widgetCopy = (IWidget)widgetRequest.get("widget");
				widgetCopies.add(widgetCopy);
			}
			Interface copy = InterfaceFactory.createNew(
					"Copy of " + original.getName(), 
					original.getKeywords(), 
					original.getWidgets(), 
					original.getGridWidth(), original.getGridHeight(), 
					original.getViewWidth(), original.getViewHeight(), 
					original.getClickSound(), original.getClickSoundVolume(), 
					original.getBackgroundImage(), original.getBackgroundColor());
			UoW.getCurrent().commit();
			UoW.newCurrent();
			
			response.set("interface", copy);
			
		} catch (NumberFormatException e) {
			throw new CommandException(e);
		} catch (MapperException e) {
			throw new CommandException(e);
		} catch (SQLException e) {
			throw new CommandException(e);
		} catch (KeyNotFoundException e) {
			throw new CommandException(e);
		} catch (CreationException e) {
			throw new CommandException(e);
		}
	}

}
