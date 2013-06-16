package org.korsakow.ide.ui.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.korsakow.ide.resources.PredicateType;

public class PredicateModel
{
	private PredicateType type;
	private Map<String, Object> properties = new HashMap<String, Object>();
	private List<PredicateModel> predicates = new ArrayList<PredicateModel>();
	public PredicateModel(PredicateType type)
	{
		this.type = type;
	}
	public PredicateType getType()
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
	public void addPredicate(PredicateModel model)
	{
		predicates.add(model);
	}
	public void setPredicates(List<PredicateModel> predicates)
	{
		this.predicates = predicates;
	}
	public List<PredicateModel> getPredicates()
	{
		return predicates;
	}
}
