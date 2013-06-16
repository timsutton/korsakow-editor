package org.korsakow.domain;

import java.util.Collection;
import java.util.HashSet;

import org.korsakow.domain.interf.IKeyword;

public class KeywordFactory {
	public static Keyword createNew(String value, float weight)
	{
		Keyword object = new Keyword(value, weight);
		return object;
	}
	public static Keyword createNew(String value)
	{
		return createNew(value, 1);
	}
	public static Keyword createClean(String value, float weight)
	{
		Keyword object = new Keyword(value, weight);
		return object;
	}
	public static Keyword createClean(String value)
	{
		return createClean(value, 1);
	}
	
	public static Collection<IKeyword> copy(Collection<IKeyword> src)
	{
		Collection<IKeyword> copies = new HashSet<IKeyword>();
		for (IKeyword keyword : src) {
			copies.add(createNew(keyword.getValue(), keyword.getWeight()));
		}
		return copies;
	}
}
