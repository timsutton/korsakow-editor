package org.korsakow.domain.command;

import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.Project;
import org.korsakow.domain.ProjectFactory;

public class InsertProjectCommand extends AbstractCommand{


	public InsertProjectCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		Project p = null;
		p = ProjectFactory.createNew();
		request.set(UpdateProjectCommand.ID, p.getId());
		
		new UpdateProjectCommand(request, response).execute();
	}

}
