package org.korsakow.domain.command;

import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.mapper.input.ResourceInputMapper;

public class FindResourceByClickSoundIdCommand extends AbstractCommand{


	public FindResourceByClickSoundIdCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		try {
			List<IResource> list = ResourceInputMapper.findByClickSoundId(request.getLong("id"));
			response.set("resources", list);
		} catch (MapperException e) {
			throw new CommandException(e);
		}
	}

}
