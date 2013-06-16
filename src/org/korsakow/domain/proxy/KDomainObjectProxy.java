package org.korsakow.domain.proxy;


import org.dsrg.soenea.domain.DomainObject;
import org.dsrg.soenea.domain.interf.IDomainObject;
import org.dsrg.soenea.domain.proxy.DomainObjectProxy;

public abstract class KDomainObjectProxy<DO extends DomainObject<Long>> extends DomainObjectProxy<Long, DO>
{

	protected KDomainObjectProxy(long id)
	{
		super(id);
		
	}
	
	public abstract Class<DO> getInnerClass();

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object d) {
		if (d instanceof IDomainObject<?> == false) {
			if (d == null)
				return false;
			else
				return d.equals(this); // due to the way current version of SoenEA works
		}
		return getId().equals(((IDomainObject<Long>)d).getId());
	}

	@Override
	public int hashCode()
	{
		return getId().hashCode();
	}
}
