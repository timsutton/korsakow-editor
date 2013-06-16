package org.korsakow.domain.command;

import org.dsrg.soenea.domain.command.CommandException;

public class InsertInterfaceCopyCommand extends InsertInterfaceCommand {


	public InsertInterfaceCopyCommand(Helper request, Helper response) {
		super(request, response);
	}

	@Override
	public void execute() throws CommandException {
		request.set(UpdateInterfaceCommand.NAME, "Copy of " + request.get(UpdateInterfaceCommand.NAME));
		super.execute();
	}

}
