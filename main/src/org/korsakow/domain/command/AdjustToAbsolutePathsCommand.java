package org.korsakow.domain.command;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.Media;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.domain.mapper.input.ProjectInputMapper;
import org.korsakow.ide.resources.ResourceType;

public class AdjustToAbsolutePathsCommand extends AbstractCommand {


	public AdjustToAbsolutePathsCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		try {
			long id = request.getLong("id");
			IProject project = ProjectInputMapper.map(id);
			
			Collection<IMedia> media = project.getMedia();
			adjustAbsolute(media);
			
			response.set("media", media);
			
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
	
	/**
	 * Attempts to convert all the file paths to be absolute.
	 * No DO's are modified in the case of an exception.
	 * 
	 * @param media
	 * @throws FileNotFoundException if any of the file paths could not be resolved to an actual file (for example if the path was already relative we can't guarantee being able to find the file)
	 */
	public static void adjustAbsolute(Collection<IMedia> media) throws FileNotFoundException {
		HashMap<IMedia, String> pathMap = new HashMap<IMedia, String>();
		HashMap<IVideo, String> subtitleMap = new HashMap<IVideo, String>();
		
		for (IMedia medium : media)
		{
//			System.out.println(String.format("making absolute: %s -> %s", medium.getFilename(), medium.getAbsoluteFilename()));
			String filename = medium.getAbsoluteFilename();
			pathMap.put(medium, filename);

			if (ResourceType.forId(medium.getType()) == ResourceType.VIDEO) {
				IVideo video = (IVideo) medium;
				filename = video.getSubtitles();
				if (filename != null) {
//					System.out.println(String.format("\tmaking subtitles absolute: %s -> %s", medium.getFilename(), Media.getResolvedFilename(filename)));
					filename = Media.getAbsoluteFilename(filename);
					subtitleMap.put(video, filename);
				}
			}
		}
		for (IMedia medium : pathMap.keySet()) {
			String path = pathMap.get(medium);
			medium.setFilename(path);
			UoW.getCurrent().registerDirty(medium);
		}
		for (IVideo video : subtitleMap.keySet()) {
			String path = subtitleMap.get(video);
			video.setSubtitles(path);
			UoW.getCurrent().registerDirty(video);
		}
	}

}
