package org.korsakow.domain.command;

import java.util.Collection;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.mapper.input.MediaInputMapper;

public class FindSnuableMediaNotUsedAsSnuMainMediaCommand extends AbstractCommand{


	public FindSnuableMediaNotUsedAsSnuMainMediaCommand(Helper request,
			Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		try {
			Collection<IMedia> list = MediaInputMapper.findSnuableNotUsedAsSnuMainMedia();
			response.set("media", list);
		} catch (MapperException e) {
			throw new CommandException(e);
		} catch (NumberFormatException e) {
			throw new CommandException(e);
		}
	}

}
