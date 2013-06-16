package org.korsakow.domain;

import java.util.Collection;
import java.util.Hashtable;

import org.korsakow.domain.interf.ITrigger;
import org.korsakow.ide.resources.ResourceType;

public class Trigger extends KDomainObject implements ITrigger
{
	private final Hashtable<String, Object> dynamicProperties = new Hashtable<String, Object>();
	private String triggerType;
	public Trigger(long id, long version, String triggerType)
	{
		super(id, version);
		this.triggerType = triggerType;
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
	public String getType()
	{
		return ResourceType.TRIGGER.getTypeId();
	}
	public String getTriggerType()
	{
		return triggerType;
	}
	public void setTriggerType(String predicateType) {
		triggerType = predicateType;
	}
}
