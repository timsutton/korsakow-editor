package org.korsakow.domain.proxy;

import java.util.Collection;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.Event;
import org.korsakow.domain.interf.IEvent;
import org.korsakow.domain.interf.IPredicate;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ITrigger;
import org.korsakow.domain.mapper.input.EventInputMapper;

public class EventProxy extends KDomainObjectProxy<Event> implements IEvent {

	public EventProxy(long id)
	{
		super(id);
	}

	@Override
	public Class<Event> getInnerClass()
	{
		return Event.class;
	}
	
	@Override
	protected Event getFromMapper(Long id) throws MapperException {
		return EventInputMapper.map(id);
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

	public IPredicate getPredicate() {
		return getInnerObject().getPredicate();
	}

	public IRule getRule() {
		return getInnerObject().getRule();
	}

	public ITrigger getTrigger() {
		return getInnerObject().getTrigger();
	}

	public void setPredicate(IPredicate predicate) {
		getInnerObject().setPredicate(predicate);
	}

	public void setRule(IRule rule) {
		getInnerObject().setRule(rule);
	}

	public void setTrigger(ITrigger trigger) {
		getInnerObject().setTrigger(trigger);
	}
}
