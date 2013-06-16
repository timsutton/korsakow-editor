package org.korsakow.domain;

import java.util.Collection;

import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.ide.DataRegistry;

public class PatternFactory {
	public static Pattern createNew(long id, long version)
	{
		Pattern object = new Pattern(id, version);
		UoW.getCurrent().registerNew(object);
		return object;
	}
	public static Pattern createNew()
	{
		return createNew(DataRegistry.getMaxId(), 0);
	}
	public static Pattern createClean(long id, long version, String patternType, String name, Collection<IKeyword> keywords, String filename)
	{
		Pattern object = new Pattern(id, version, patternType, name, keywords, filename);
		UoW.getCurrent().registerClean(object);
		return object;
	}
}
