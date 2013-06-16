package org.korsakow.domain.command;

import java.sql.SQLException;
import java.util.Collection;

import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.Snu;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.mapper.input.MapperHelper;
import org.korsakow.domain.mapper.input.ResourceInputMapper;
import org.korsakow.domain.mapper.input.SnuInputMapper;

public class DeleteSnuCommand extends AbstractCommand{


	public DeleteSnuCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		try {
			Snu s = null;
			s = SnuInputMapper.map(request.getLong(UpdateSnuCommand.ID));

			Collection<IResource> references = MapperHelper.findResourcesReferencing(s.getId());
			
			IMedia mainMedia = s.getMainMedia();
			if (mainMedia != null) {
				Collection<IResource> referencesToMedia = MapperHelper.findResourcesReferencing(mainMedia.getId());
				referencesToMedia.remove(s);
				references.addAll(referencesToMedia);
			}
			
			if (references.isEmpty()) {
				UoW.getCurrent().registerRemoved(s);
				if (mainMedia != null) {
					mainMedia = (IMedia)ResourceInputMapper.map(mainMedia.getId());
					UoW.getCurrent().registerRemoved(mainMedia);
				}
				response.set("deleted", s);
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
