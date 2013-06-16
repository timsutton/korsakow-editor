package org.korsakow.domain.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.domain.mapper.input.ProjectInputMapper;
import org.korsakow.domain.mapper.input.VideoInputMapper;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.util.FileUtil;

public class FindSubtitlesCommand extends AbstractCommand{

	public FindSubtitlesCommand(Helper request, Helper response) {
		super(request, response);
		
	}
	public void execute()
			throws CommandException {
		Collection<IVideo> updated = new HashSet<IVideo>();
		
		Collection<IMedia> media = null;
		
		if (request.has("projectId")) {
			long projectId = request.getLong("projectId");
			
			IProject project;
			try {
				project = ProjectInputMapper.map(projectId);
			} catch (MapperException e) {
				throw new CommandException(e);
			}
			
			media = project.getMedia();
		} else {
			long videoId = request.getLong("videoId");
			IVideo video;
			try {
				video = VideoInputMapper.map(videoId);
			} catch (MapperException e) {
				throw new CommandException(e);
			}
			
			media = new HashSet<IMedia>();
			media.add(video);
		}
		
		
		
		for (IMedia medium : media)
		{
			if (ResourceType.forId(medium.getType()) != ResourceType.VIDEO)
				continue;
			IVideo video = (IVideo)medium;
			String subtitles = video.getSubtitles();
			if (subtitles != null && subtitles.trim().length()>0)
				continue;
			String filename = null;
			// don't get the absolute filename. keep it in whatever format was already being used
			filename = video.getFilename();
			filename = FileUtil.getFilenameWithoutExtension(filename);
			
			File file;
			subtitles = filename + ".srt";
			file = new File(subtitles);
			if (file.exists()) {
				video.setSubtitles(subtitles);
				UoW.getCurrent().registerDirty(video);
				updated.add(video);
				continue;
			}
			
			subtitles = filename + ".txt";
			file = new File(subtitles);
			if (file.exists() && isK3SubtitleFile(file)) {
				video.setSubtitles(subtitles);
				UoW.getCurrent().registerDirty(video);
				updated.add(video);
				continue;
			}
		}
		
		if (!updated.isEmpty()) {
			try {
				response.set("updated", updated);
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
	private boolean isK3SubtitleFile(File file)
	{
		final String MAGIC = "[subtitle.tool]";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			if (line == null)
				return false;
			if (!MAGIC.equals(line))
				return false;
			return true;
		} catch (IOException e) {
			return false;
		} finally {
			if (br != null) try { br.close(); } catch (IOException e) {}
		}
	}
}
