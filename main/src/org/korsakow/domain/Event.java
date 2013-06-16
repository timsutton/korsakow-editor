package org.korsakow.domain;

import java.util.Collection;
import java.util.Hashtable;

import org.korsakow.domain.interf.IEvent;
import org.korsakow.domain.interf.IPredicate;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ITrigger;

public class Event extends KDomainObject implements IEvent
{
	private final Hashtable<String, Object> dynamicProperties = new Hashtable<String, Object>();
	private ITrigger trigger;
	private IPredicate predicate;
	private IRule rule;
	public Event(long id, long version, ITrigger trigger, IPredicate predicate, IRule rule)
	{
		super(id, version);
		this.trigger = trigger;
		this.predicate = predicate;
		this.rule = rule;
	}
	public Collection<String> getDynamicPropertyIds()
	{
		return dynamicProperties.keySet();
	}
	public Object getDynamicProperty(String id)
	{
		return dynamicProperties.get(id);
	}
	public void setDynamicProperty(String id, Object value)
	{
		if (id == null)
			throw new NullPointerException();
		if (value == null)
			dynamicProperties.remove(id);
		else
			dynamicProperties.put(id, value);
	}
	public ITrigger getTrigger()
	{
		return trigger;
	}
	public void setTrigger(ITrigger trigger) {
		this.trigger = trigger;
	}
	public IPredicate getPredicate()
	{
		return predicate;
	}
	public void setPredicate(IPredicate predicate) {
		this.predicate = predicate;
	}
	public IRule getRule()
	{
		return rule;
	}
	public void setRule(IRule rule) {
		this.rule = rule;
	}
}
