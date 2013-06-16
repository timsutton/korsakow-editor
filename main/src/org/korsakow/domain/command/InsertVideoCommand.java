package org.korsakow.domain.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.Video;
import org.korsakow.domain.VideoFactory;

public class InsertVideoCommand extends AbstractCommand{


	public InsertVideoCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		Video v = null;
		v = VideoFactory.createNew();
		request.set(UpdateVideoCommand.ID, v.getId());
		new UpdateVideoCommand(request, response).execute();
	}

}
