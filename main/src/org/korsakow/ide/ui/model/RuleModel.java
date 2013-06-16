package org.korsakow.ide.ui.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.korsakow.ide.rules.RuleType;

public class RuleModel
{
	private RuleType type;
	private Map<String, Object> properties = new HashMap<String, Object>();
	private List<RuleModel> rules = new ArrayList<RuleModel>();
	public RuleModel(RuleType type)
	{
		this.type = type;
	}
	public RuleType getType()
	{
		return type;
	}
	public void setProperty(String key, Object value)
	{
		properties.put(key, value);
	}
	public Collection<Object> getPropertyValues()
	{
		return properties.values();
	}
	public Object getProperty(String key)
	{
		return properties.get(key);
	}
	public Collection<String> getPropertyIds()
	{
		return properties.keySet();
	}
	public List<RuleModel> getRules()
	{
		return rules;
	}
	public void setRules(List<RuleModel> rules)
	{
		this.rules = rules;
	}
}
