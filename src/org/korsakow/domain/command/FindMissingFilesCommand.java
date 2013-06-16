package org.korsakow.domain.command;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.Video;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.mapper.input.ProjectInputMapper;
import org.korsakow.domain.mapper.input.VideoInputMapper;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.util.MultiMap;

public class FindMissingFilesCommand extends AbstractCommand{


	public FindMissingFilesCommand(Helper request, Helper response) {
		super(request, response);
		
	}
	public void execute()
			throws CommandException {
		long projectId = request.getLong("id");
		File basePath = new File(request.getString("basePath"));
		boolean updateUniqueMatches = request.has("updateUniqueMatches")?request.getBoolean("updateUniqueMatches"):false;
		IProject project;
		try {
			project = ProjectInputMapper.map(projectId);
		} catch (MapperException e) {
			throw new CommandException(e);
		}
		Collection<IMedia> media = project.getMedia();
		Collection<IMedia> mediaMissingFiles = new HashSet<IMedia>();
		MultiMap<String, File, Set<File>> filesToPossibleMatches = new MultiHashMapHashSet<String, File>();
		
		// find media with missing files
		for (IMedia medium : media)
		{
			File file = null;
			try {
				file = new File(medium.getAbsoluteFilename());
			} catch (FileNotFoundException e) {
				mediaMissingFiles.add(medium);
				continue;
			}
		}
		
		boolean modified = false;
		Collection<String> uniqueMatches = new HashSet<String>();
		Collection<IMedia> uniqueMatchedMedia = new HashSet<IMedia>();
		// find possible matches to the missing files
		for (IMedia medium : mediaMissingFiles)
		{
			File target = new File(medium.getFilename());
			findFileRecursive(target, basePath, filesToPossibleMatches);
			
			if (updateUniqueMatches) {
				String name = target.getName();
				if (filesToPossibleMatches.containsKey(name) && filesToPossibleMatches.size(name) == 1) {
					File match = filesToPossibleMatches.get(name).iterator().next();
					medium.setFilename(match.getAbsolutePath());
					
					if ( ResourceType.VIDEO.isInstance( medium ) ) {
						// map it because UnknownResourceProxy is a broken solution & we can't cast to IVideo
						Video video;
						try {
							video = VideoInputMapper.map( medium.getId() );
						} catch (MapperException e) {
							throw new CommandException(e);
						}
						
						if ( video.getSubtitles() != null ) {
							if ( match.getParent() != null ) {
								File testFile = new File( match.getParent(), new File( video.getSubtitles() ).getName() );
								if ( testFile.exists() && testFile.isFile() )
									video.setSubtitles( testFile.getAbsolutePath() );
							}
						}
					}
					
					UoW.getCurrent().registerDirty(medium);
					modified = true;
					uniqueMatches.add(name);
					uniqueMatchedMedia.add(medium);
				}
			}
		}
		
		filesToPossibleMatches.removeAll(uniqueMatches);
		
		if (modified) {
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
		
		if (updateUniqueMatches)
			response.set("updatedMedia", uniqueMatchedMedia);
		response.set("possibleMatches", filesToPossibleMatches);
	}
	public static void findFileRecursive(File target, File currentFile, MultiMap<String, File, Set<File>> filesToPossibleMatches)
	{
		if (currentFile.isDirectory()) {
			File[] files = currentFile.listFiles();
			if (files != null)
				for (File child : files) {
					findFileRecursive(target, child, filesToPossibleMatches);
				}
		} else {
			if (currentFile.getName().equals(target.getName()))
				filesToPossibleMatches.add(target.getName(), currentFile);
		}
	}
	private static class MultiHashMapHashSet<KeyType, ValueType> 
		implements MultiMap<KeyType, ValueType, Set<ValueType>>
	{
		private final Map<KeyType, Set<ValueType>> map;
		public MultiHashMapHashSet()
		{
			this.map = new HashMap<KeyType, Set<ValueType>>();
		}
		private Set<ValueType> ensure(KeyType key)
		{
			Set<ValueType> values = map.get(key);
			if (values == null) {
				values = new HashSet<ValueType>();
				map.put(key, values);
			}
			return values;
		}
		public void add(KeyType key, ValueType value)
		{
			ensure(key).add(value);
		}
		public Set<ValueType> get(KeyType key) {
			return map.get(key);
		}
		public boolean containsKey(KeyType key) {
			return map.containsKey(key);
		}
		public int size()
		{
			return map.size();
		}
		public int size(KeyType key)
		{
			return ensure(key).size();
		}
		public boolean isEmpty()
		{
			return map.isEmpty();
		}
		public boolean isEmpty(KeyType key)
		{
			Set<ValueType> values = map.get(key);
			if (values == null)
				return true;
			return values.isEmpty();
		}
		public boolean removeAll(Collection<KeyType> keys)
		{
			boolean modified = false;
			for (KeyType key : keys) {
				modified = modified || map.containsKey(key);
				map.remove(key);
			}
			return modified;
		}
	}
}
