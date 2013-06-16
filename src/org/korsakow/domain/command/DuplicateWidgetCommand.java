package org.korsakow.domain.command;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.Widget;
import org.korsakow.domain.WidgetFactory;
import org.korsakow.domain.mapper.input.WidgetInputMapper;

public class DuplicateWidgetCommand extends AbstractCommand{


	public DuplicateWidgetCommand(Helper request, Helper response) {
		super(request, response);
	}

	public void execute()
			throws CommandException {
		try {
			Widget original = WidgetInputMapper.map(request.getLong("id"));
			Widget copy = WidgetFactory.createNew();
			copy.setName(original.getName());
			copy.setWidgetId(original.getWidgetId());
			copy.setPersistCondition(original.getPersistCondition());
			copy.setPersistAction(original.getPersistAction());
			copy.setX(original.getX());
			copy.setY(original.getY());
			copy.setWidth(original.getWidth());
			copy.setHeight(original.getHeight());
			copy.setName("Copy of " + original.getName());
			UoW.getCurrent().commit();
			UoW.newCurrent();
			
			response.set("widget", copy);
			
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
