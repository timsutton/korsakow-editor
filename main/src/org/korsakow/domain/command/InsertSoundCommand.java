package org.korsakow.domain.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.Sound;
import org.korsakow.domain.SoundFactory;

public class InsertSoundCommand extends AbstractCommand{


	public InsertSoundCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		Sound v = null;
		v = SoundFactory.createNew();
		request.set(UpdateSoundCommand.ID, v.getId());
		new UpdateSoundCommand(request, response).execute();
	}

}
