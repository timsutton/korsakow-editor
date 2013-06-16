package org.korsakow.domain;

import java.util.Collection;
import java.util.Hashtable;

import javax.xml.xpath.XPathException;

import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IPattern;
import org.korsakow.ide.resources.ResourceType;

public class Pattern extends Media implements IPattern
{
	String patternType;
	private Hashtable<String, Object> abstractProperties = new Hashtable<String, Object>();
	Pattern(long id, long version)
	{
		super(id, version);
	}
	Pattern(long id, long version, String patternType, String name, Collection<IKeyword> keywords, String filename)
	{
		super(id, version, name, keywords, filename);
		setPatternType(patternType);
		setFilename(filename);
	}
	public String getType()
	{
		throw new IllegalArgumentException("not implemented");
	}
	public Collection<String> getDynamicPropertyIds()
	{
		return abstractProperties.keySet();
	}
	public Object getDynamicProperty(String id)
	{
		return abstractProperties.get(id);
	}
	public void setDynamicProperty(String id, Object value)
	{
		if (id == null)
			throw new NullPointerException();
		if (value == null)
			abstractProperties.remove(id);
		else
			abstractProperties.put(id, value);
	}
	public Class getPropertyType(String id)
	{
		return null;
	}
	public void setPatternType(String patternType)
	{
		this.patternType = patternType;
	}
	public String getPatternType()
	{
		return patternType;
	}
}
