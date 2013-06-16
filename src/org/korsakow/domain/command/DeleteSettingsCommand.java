package org.korsakow.domain.command;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.Settings;
import org.korsakow.domain.mapper.input.SettingsInputMapper;

public class DeleteSettingsCommand extends AbstractCommand{


	public DeleteSettingsCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		try {
			Settings p = null;
			p = SettingsInputMapper.map(request.getLong(UpdateSettingsCommand.ID));
			response.set("deleted", p);
			UoW.getCurrent().registerRemoved(p);
			UoW.getCurrent().commit();
			UoW.newCurrent();
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
