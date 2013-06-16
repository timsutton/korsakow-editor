package org.korsakow.ide.ui.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.korsakow.ide.resources.TriggerType;

public class TriggerModel
{
	private TriggerType type;
	private Map<String, Object> properties = new HashMap<String, Object>();
	public TriggerModel(TriggerType type)
	{
		this.type = type;
	}
	public TriggerType getType()
	{
		return type;
	}
	public void addProperty(String key, Object value)
	{
		properties.put(key, value);
	}
	public Object getProperty(String key)
	{
		return properties.get(key);
	}
	public Collection<String> getPropertyIds()
	{
		return properties.keySet();
	}
	public Collection<Object> getPropertyValues()
	{
		return properties.values();
	}
}
