package org.korsakow.domain.command;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.mapper.input.ResourceInputMapper;

public class RenameResourceCommand extends AbstractCommand{

	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String RESOURCE = "resource";
	

	public RenameResourceCommand(Helper request, Helper response) {
		super(request, response);
	}

	public void execute() throws CommandException
	{
		IResource resource;
		try {
			resource = ResourceInputMapper.map(request.getLong(ID));
		} catch (NumberFormatException e) {
			throw new CommandException(e);
		} catch (MapperException e) {
			throw new CommandException(e);
		}
		
		String name = request.getString(NAME);
		resource.setName(name);
		UoW.getCurrent().registerDirty(resource);
		if (resource instanceof ISnu) {
			ISnu snu = (ISnu)resource;
			if (snu.getMainMedia() != null) {
				snu.getMainMedia().setName(name);
				UoW.getCurrent().registerDirty(snu.getMainMedia());
			}
		}
		response.set(RESOURCE, resource);
		try {
			UoW.getCurrent().commit();
		} catch (SQLException e) {
			throw new CommandException(e);
		} catch (KeyNotFoundException e) {
			throw new CommandException(e);
		} catch (CreationException e) {
			throw new CommandException(e);
		} catch (MapperException e) {
			throw new CommandException(e);
		}
	}
}
