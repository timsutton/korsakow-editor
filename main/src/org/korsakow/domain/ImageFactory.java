package org.korsakow.domain;

import java.util.Collection;

import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.ide.DataRegistry;

public class ImageFactory {
	public static Image createNew(long id, long version)
	{
		Image object = new Image(id, version);
		UoW.getCurrent().registerNew(object);
		return object;
	}
	public static Image createNew()
	{
		return createNew(DataRegistry.getMaxId(), 0);
	}
	public static Image createNew(String name, Collection<IKeyword> keywords, String filename, Long duration)
	{
		Image object = new Image(DataRegistry.getMaxId(), 0, name, keywords, filename, duration);
		UoW.getCurrent().registerNew(object);
		return object;
	}
	public static Image createClean(long id, long version, String name, Collection<IKeyword> keywords, String filename, Long duration)
	{
		Image object = new Image(id, version, name, keywords, filename, duration);
		UoW.getCurrent().registerClean(object);
		return object;
	}
	public static Image copy(IImage src)
	{
		return createNew(
			src.getName(),
			KeywordFactory.copy(src.getKeywords()),
			src.getFilename(),
			src.getDuration()
		);
	}
}
