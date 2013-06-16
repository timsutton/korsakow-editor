package org.korsakow.domain.command;

import java.sql.SQLException;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.service.UniqueIdFactory;
import org.korsakow.domain.Image;
import org.korsakow.domain.ImageFactory;

public class InsertImageCommand extends AbstractCommand{


	public InsertImageCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		Image v = null;
		v = ImageFactory.createNew();
		request.set(UpdateImageCommand.ID, v.getId());
		new UpdateImageCommand(request, response).execute();
	}

}
