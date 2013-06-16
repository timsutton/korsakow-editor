package org.korsakow.domain;

import java.util.Collection;

import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IText;
import org.korsakow.ide.DataRegistry;

public class TextFactory {
	public static Text createNew(long id, long version)
	{
		Text object = new Text(id, version);
		UoW.getCurrent().registerNew(object);
		return object;
	}
	public static Text createNew()
	{
		return createNew(DataRegistry.getMaxId(), 0);
	}
	public static Text createNew(String name, Collection<IKeyword> keywords, MediaSource source, String value)
	{
		Text object = new Text(DataRegistry.getMaxId(), 0, name, keywords, source, value);
		UoW.getCurrent().registerNew(object);
		return object;
	}
	public static Text createClean(long id, long version, String name, Collection<IKeyword> keywords, MediaSource source, String value)
	{
		Text object = new Text(id, version, name, keywords, source, value);
		UoW.getCurrent().registerClean(object);
		return object;
	}
	public static Text copy(IText src)
	{
		return createNew(
			src.getName(),
			KeywordFactory.copy(src.getKeywords()),
			src.getSource(),
			src.getValue()
		);
	}
}
