package org.korsakow.domain.interf;

import org.dsrg.soenea.domain.interf.IDomainObject;

public interface IEvent extends IDomainObject<Long>, IDynamicProperties
{
	ITrigger getTrigger();
	void setTrigger(ITrigger trigger);
	
	IPredicate getPredicate();
	void setPredicate(IPredicate predicate);
	
	IRule getRule();
	void setRule(IRule rule);
}
