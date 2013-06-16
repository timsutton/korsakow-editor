package org.korsakow.domain.command;

import java.sql.SQLException;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.service.UniqueIdFactory;
import org.korsakow.domain.Interface;
import org.korsakow.domain.InterfaceFactory;

public class InsertInterfaceCommand extends AbstractCommand{


	public InsertInterfaceCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		Interface s = null;
		s = InterfaceFactory.createNew();
		request.set("id", s.getId());
		new UpdateInterfaceCommand(request, response).execute();
	}

}
