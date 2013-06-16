package org.korsakow.domain.proxy;


import java.util.Collection;

import org.korsakow.domain.Resource;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IResource;

public abstract class ResourceProxy<DO extends Resource> extends KDomainObjectProxy<DO> implements IResource {

	public ResourceProxy(long id)
	{
		super(id);
	}
	
	public String getType() {
		return getInnerObject().getType();
	}

	public String getName() {
		return getInnerObject().getName();
	}

	public void setName(String name) {
		getInnerObject().setName(name);
	}

	public Collection<IKeyword> getKeywords()
	{
		return getInnerObject().getKeywords();
	}
	public void setKeywords(Collection<IKeyword> keywords)
	{
		getInnerObject().setKeywords(keywords);
	}
}
