package org.korsakow.domain.command;

import java.util.Collection;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.mapper.input.SnuInputMapper;

public class CountSnuByMainMediaIdCommand extends AbstractCommand{


	public CountSnuByMainMediaIdCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		try {
			Collection<ISnu> list = (Collection<ISnu>)SnuInputMapper.findByMainMediaId(request.getLong("id"));
			response.set("count", list.size());
		} catch (MapperException e) {
			throw new CommandException(e);
		} catch (NumberFormatException e) {
			throw new CommandException(e);
		}
	}

}
