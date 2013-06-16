package org.korsakow.domain.command;

import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.mapper.input.SnuInputMapper;

public class FindSnuByOutKeywordCommand extends AbstractCommand{


	public FindSnuByOutKeywordCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		try {
			List<ISnu> list = SnuInputMapper.findByOutKeyword(request.getString("keyword"));
			response.set("snus", list);
		} catch (MapperException e) {
			throw new CommandException(e);
		}
	}

}
