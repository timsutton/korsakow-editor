package org.korsakow.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IRule;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.resources.ResourceType;

public class Rule extends Resource implements IRule
{
	private final Hashtable<String, Object> abstractProperties = new Hashtable<String, Object>();
	private String ruleType;
	private Long triggerTime = 0L;
	private List<IRule> rules = new ArrayList<IRule>();
	Rule()
	{
		this(DataRegistry.getMaxId(), 0);
	}
	Rule(String ruleType)
	{
		this(DataRegistry.getMaxId(), 0);
		this.ruleType = ruleType;
	}
	Rule(long id, long version, String ruleType, Collection<IKeyword> keywords, String name, long triggerTime, List<IRule> rules)
	{
		super(id, version, name, keywords);
		this.ruleType = ruleType;
		this.triggerTime = triggerTime;
		setRules(rules);
	}
	Rule(long id, long version)
	{
		super(id, version);
	}
	public String getType()
	{
		return ResourceType.RULE.getTypeId();
	}
	public Collection<String> getDynamicPropertyIds()
	{
		return abstractProperties.keySet();
	}
	public Object getDynamicProperty(String id)
	{
		return abstractProperties.get(id);
	}
	public void setDynamicProperty(String id, Object value)
	{
		if (id == null)
			throw new NullPointerException();
		if (value == null)
			abstractProperties.remove(id);
		else
			abstractProperties.put(id, value);
	}
	public Class getPropertyType(String id)
	{
		return Object.class;
	}
	public String getRuleType()
	{
		return ruleType;
	}
	public void setRuleType(String type) {
		ruleType = type;
	}
	public long getTriggerTime()
	{
		return triggerTime;
	}
	public void setTriggerTime(long triggerTime)
	{
		this.triggerTime = triggerTime;
	}
	public void setRules(List<IRule> rules)
	{
		this.rules = rules;
	}
	public List<IRule> getRules()
	{
		return rules;
	}
}
