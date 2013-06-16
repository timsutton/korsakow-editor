package org.korsakow.domain.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.Settings;
import org.korsakow.domain.SettingsFactory;

public class InsertSettingsCommand extends AbstractCommand{


	public InsertSettingsCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		Settings p = null;
		p = SettingsFactory.createNew();
		request.set(UpdateSettingsCommand.ID, p.getId());
		
		new UpdateSettingsCommand(request, response).execute();
	}

}
