package org.korsakow.domain.proxy;

import java.util.Collection;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.Trigger;
import org.korsakow.domain.interf.ITrigger;
import org.korsakow.domain.mapper.input.TriggerInputMapper;

public class TriggerProxy extends KDomainObjectProxy<Trigger> implements ITrigger {

	public TriggerProxy(long id)
	{
		super(id);
	}

	@Override
	public Class<Trigger> getInnerClass()
	{
		return Trigger.class;
	}
	
	@Override
	protected Trigger getFromMapper(Long id) throws MapperException {
		return TriggerInputMapper.map(id);
	}

	public String getTriggerType() {
		return getInnerObject().getTriggerType();
	}

	public void setTriggerType(String triggerType) {
		getInnerObject().setTriggerType(triggerType);
	}
	
	public Object getDynamicProperty(String id) {
		return getInnerObject().getDynamicProperty(id);
	}

	public Collection<String> getDynamicPropertyIds() {
		return getInnerObject().getDynamicPropertyIds();
	}

	public void setDynamicProperty(String id, Object value) {
		getInnerObject().setDynamicProperty(id, value);
	}
}
