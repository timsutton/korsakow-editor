package org.korsakow.domain.command;

import java.sql.SQLException;
import java.util.Collection;

import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.Interface;
import org.korsakow.domain.Project;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.mapper.input.InterfaceInputMapper;
import org.korsakow.domain.mapper.input.MapperHelper;
import org.korsakow.domain.mapper.input.ProjectInputMapper;

public class DeleteInterfaceCommand extends AbstractCommand{


	public DeleteInterfaceCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		try {
			Interface v = null;
			v = InterfaceInputMapper.map(request.getLong("id"));
			
			Collection<IResource> references = MapperHelper.findResourcesReferencing(v.getId());
			final Project project = ProjectInputMapper.find();
			if ( v.equals( project.getDefaultInterface() ) )
				references.add( project );
			if (!references.isEmpty()) {
				response.set("resourceInUse", true);
				response.set("references", references);
				return;
			} else {
				response.set("deleted", v);
			}
			UoW.getCurrent().registerRemoved(v);
			UoW.getCurrent().commit();
			UoW.newCurrent();
		} catch (MapperException e) {
			throw new CommandException(e);
		} catch (SQLException e) {
			throw new CommandException(e);
		} catch (KeyNotFoundException e) {
			throw new CommandException(e);
		} catch (CreationException e) {
			throw new CommandException(e);
		} catch (XPathExpressionException e) {
			throw new CommandException(e);
		}
		
	}
}
