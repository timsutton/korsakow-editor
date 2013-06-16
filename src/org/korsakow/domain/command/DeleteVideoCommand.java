package org.korsakow.domain.command;

import java.sql.SQLException;
import java.util.Collection;

import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.Video;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.mapper.input.MapperHelper;
import org.korsakow.domain.mapper.input.VideoInputMapper;

public class DeleteVideoCommand extends AbstractCommand{


	public DeleteVideoCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		try {
			Video v = null;
			v = VideoInputMapper.map(request.getLong(UpdateVideoCommand.ID));
			
			Collection<IResource> references = MapperHelper.findResourcesReferencing(v.getId());
			if (references.isEmpty()) {
				UoW.getCurrent().registerRemoved(v);
				response.set("deleted", v);
			} else {
				response.set("resourceInUse", true);
				response.set("references", references);
			}
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
