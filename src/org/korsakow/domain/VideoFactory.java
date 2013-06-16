package org.korsakow.domain;

import java.util.Collection;

import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.ide.DataRegistry;

public class VideoFactory {
	public static Video createNew(long id, long version)
	{
		Video object = new Video(id, version);
		UoW.getCurrent().registerNew(object);
		return object;
	}
	public static Video createNew()
	{
		return createNew(DataRegistry.getMaxId(), 0);
	}
	public static Video createNew(String name, Collection<IKeyword> keywords, String filename, String subtitles)
	{
		Video object = new Video(DataRegistry.getMaxId(), 0, name, keywords, filename, subtitles);
		UoW.getCurrent().registerNew(object);
		return object;
	}
	public static Video createClean(long id, long version, String name, Collection<IKeyword> keywords, String filename, String subtitles)
	{
		Video object = new Video(id, version, name, keywords, filename, subtitles);
		UoW.getCurrent().registerClean(object);
		return object;
	}
	public static Video copy(IVideo src)
	{
		return createNew(
			src.getName(),
			KeywordFactory.copy(src.getKeywords()),
			src.getFilename(),
			src.getSubtitles()
		);
	}
}
