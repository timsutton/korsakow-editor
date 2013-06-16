package org.korsakow.domain.command;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.mapper.input.ProjectInputMapper;

public class AdjustToRelativeOrAbsolutePathsCommand extends AbstractCommand {


	public AdjustToRelativeOrAbsolutePathsCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		try {
			long id = request.getLong("id");
			IProject project = ProjectInputMapper.map(id);
			
			String basePath = request.getString("basePath");
//			System.out.println(String.format("ProjectPath: %s", basePath));
			Collection<IMedia> media = project.getMedia();
			Set<IMedia> failureMedia = new HashSet<IMedia>();
			boolean allAreRelative = AdjustToRelativePathsCommand.adjustRelative(basePath, media, failureMedia);
			if (!allAreRelative)
				AdjustToAbsolutePathsCommand.adjustAbsolute(media);
			
			response.set("media", media);
			response.set("relative", allAreRelative);
			
			UoW.getCurrent().commit();
			
		} catch (FileNotFoundException e) {
			throw new CommandException(e);
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
