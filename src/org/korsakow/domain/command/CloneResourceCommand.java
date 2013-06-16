package org.korsakow.domain.command;

import java.sql.SQLException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.CloneFactory;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.mapper.input.ResourceInputMapper;

public class CloneResourceCommand extends AbstractCommand
{
	public static final String RESOURCE = "resource";
	public static final String NAME = "name";
	public static final String ID = "id";

	public CloneResourceCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException
	{
		Long id = request.getLong(ID);
		try {
			IResource resource = ResourceInputMapper.map(id);
			IResource clone = CloneFactory.clone(resource);
			if (request.has(NAME)) {
				clone.setName(request.getString(NAME));
				if (clone instanceof ISnu) {
					ISnu snu = (ISnu)clone;
					if (snu.getMainMedia() != null) {
						snu.getMainMedia().setName(NAME);
					}
				}
			}
			UoW.getCurrent().registerNew(clone);
			UoW.getCurrent().commit();
			UoW.newCurrent();
			response.set(RESOURCE, clone);
		} catch (MapperException e) {
			throw new CommandException(e);
		} catch (SQLException e) {
			throw new CommandException(e);
		} catch (KeyNotFoundException e) {
			throw new CommandException(e);
		} catch (CreationException e) {
			throw new CommandException(e);
		}
		
	}
}
