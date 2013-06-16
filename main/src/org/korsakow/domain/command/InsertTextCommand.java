package org.korsakow.domain.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.Text;
import org.korsakow.domain.TextFactory;

public class InsertTextCommand extends AbstractCommand{


	public InsertTextCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		Text v = null;
		v = TextFactory.createNew();
		request.set(UpdateTextCommand.ID, v.getId());
		new UpdateTextCommand(request, response).execute();
	}

}
