package org.korsakow.domain.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.Snu;
import org.korsakow.domain.SnuFactory;

public class InsertSnuCommand extends AbstractCommand{

	public InsertSnuCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		try {
			Snu s = null;
			s = SnuFactory.createNew();
			request.set(UpdateSnuCommand.ID, s.getId());
			new UpdateSnuCommand(request, response).execute();
		} catch (NumberFormatException e) {
			throw new CommandException(e);
		}
	}

}
