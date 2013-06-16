package org.korsakow.domain;


import java.util.Collection;
import java.util.Collections;

import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IResource;

/**
 * Abstract because so far there is no such thing as a "Resource", this is just a helper class.
 * @author dave
 */
public abstract class Resource extends KDomainObject implements IResource
{
	private String name;
	private Collection<IKeyword> keywords = Collections.emptySet();
	public Resource(long id, long version)
	{
		super(id, version);
	}
	public Resource(long id, long version, String name, Collection<IKeyword> keywords)
	{
		this(id, version);
		setName(name);
		setKeywords(keywords);
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getName()
	{
		return name;
	}
	public Collection<IKeyword> getKeywords()
	{
		return keywords;
	}
	public void setKeywords(Collection<IKeyword> keywords)
	{
		this.keywords = keywords;
	}
}
