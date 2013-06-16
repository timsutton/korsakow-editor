package org.korsakow.domain;

import java.util.Collection;

import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.ISound;
import org.korsakow.ide.DataRegistry;

public class SoundFactory {
	public static Sound createNew(long id, long version)
	{
		Sound object = new Sound(id, version);
		UoW.getCurrent().registerNew(object);
		return object;
	}
	public static Sound createNew()
	{
		return createNew(DataRegistry.getMaxId(), 0);
	}
	public static Sound createNew(String name, Collection<IKeyword> keywords, String filename, String subtitles)
	{
		Sound object = new Sound(DataRegistry.getMaxId(), 0, name, keywords, filename, subtitles);
		UoW.getCurrent().registerNew(object);
		return object;
	}
	public static Sound createClean(long id, long version, String name, Collection<IKeyword> keywords, String filename, String subtitles)
	{
		Sound object = new Sound(id, version, name, keywords, filename, subtitles);
		UoW.getCurrent().registerClean(object);
		return object;
	}
	public static Sound copy(ISound src)
	{
		return createNew(
			src.getName(),
			KeywordFactory.copy(src.getKeywords()),
			src.getFilename(),
			src.getSubtitles()
		);
	}
}
