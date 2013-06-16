package org.korsakow.domain.interf;

import org.dsrg.soenea.domain.interf.IDomainObject;

public interface ITrigger extends IDomainObject<Long>, IDynamicProperties
{
	String getTriggerType();
	void setTriggerType(String type);
}
