package org.korsakow.domain;


import org.dsrg.soenea.domain.DomainObject;
import org.dsrg.soenea.domain.interf.IDomainObject;

public class KDomainObject extends DomainObject<Long>
{

	public KDomainObject(long id) {
		super(id);
	}
	public KDomainObject(long id, long version) {
		super(id, version);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object d) {
		if (d instanceof IDomainObject<?> == false) {
			if (d == null)
				return false;
			else
				return d.equals(this);
		}
		return getId().equals(((IDomainObject<Long>)d).getId());
	}

	@Override
	public int hashCode()
	{
		return getId().hashCode();
	}
}
