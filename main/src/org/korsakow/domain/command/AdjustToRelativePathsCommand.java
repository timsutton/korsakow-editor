package org.korsakow.domain.command;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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

public class AdjustToRelativePathsCommand extends AbstractCommand {


	public AdjustToRelativePathsCommand(Helper request, Helper response) {
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
			boolean success = adjustRelative(basePath, media, failureMedia);
			
			if (success) {
				UoW.getCurrent().commit();
				response.set("media", media);
			} else {
				response.set("failureMedia", failureMedia);
			}
			response.set("status", success);
			
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
	 * Attempts to convert all the file paths to be relative to basePath.
	 * No DO's are modified unless they could all be adjusted. That is IFF this function returns true.
	 * 
	 * @param basePath
	 * @param media
	 * @param failureMedia a list to be populated if the method returns false
	 * @return if all paths were converted. 
	 * @throws FileNotFoundException if any of the file paths could not be resolved to an actual file (for example if the path was already relative we can't guarantee being able to find the file)
	 */
	public static boolean adjustRelative(String basePath,
			Collection<IMedia> media, Set<IMedia> failureMedia) throws FileNotFoundException {
		HashMap<IMedia, String> pathMap = new HashMap<IMedia, String>();
		HashMap<IVideo, String> subtitleMap = new HashMap<IVideo, String>();
		for (IMedia medium : media)
		{
			String filename = medium.getAbsoluteFilename();
//			System.out.println(String.format("media path: %s", filename));
			if (filename.startsWith(basePath)) {
//				System.out.println(String.format("\tmaking relative\t%s", filename.substring(basePath.length()+1)));
				filename = filename.substring(basePath.length()+1);
				pathMap.put(medium, filename);
			} else {
//				System.out.println(String.format("\tnot relative\t", filename, basePath));
				failureMedia.add(medium);
			}
			if (ResourceType.forId(medium.getType()) == ResourceType.VIDEO) {
				IVideo video = (IVideo) medium;
				filename = video.getSubtitles();
				if (filename != null) {
//					System.out.println(String.format("\t\tsubtitles path: %s", filename));
					filename = Media.getAbsoluteFilename(filename);
//					System.out.println(String.format("\t\tsubtitles resolved: %s", filename));
					if (filename.startsWith(basePath)) {
//						System.out.println(String.format("\t\tmaking relative\t%s", filename.substring(basePath.length()+1)));
						filename = filename.substring(basePath.length()+1);
						subtitleMap.put(video, filename);
					} else {
//						System.out.println(String.format("\t\tnot relative\t", filename, basePath));
						failureMedia.add(video);
					}
				}
			}
		}
		if (!failureMedia.isEmpty())
			return false;
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
		return true;
	}
}
