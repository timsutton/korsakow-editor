package org.korsakow.domain.interf;


import java.util.Collection;

import org.dsrg.soenea.domain.interf.IDomainObject;

public interface IResource extends IDomainObject<Long>, Cloneable
{
	void setName(String name);
	String getName();
	
	String getType();
	
	Collection<IKeyword> getKeywords();
	void setKeywords(Collection<IKeyword> keywords);
}
