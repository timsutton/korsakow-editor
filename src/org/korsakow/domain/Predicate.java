package org.korsakow.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import org.korsakow.domain.interf.IPredicate;
import org.korsakow.ide.resources.ResourceType;

public class Predicate extends KDomainObject implements IPredicate
{
	private final Hashtable<String, Object> dynamicProperties = new Hashtable<String, Object>();
	private String predicateType;
	private List<IPredicate> predicates = new ArrayList<IPredicate>();
	public Predicate(long id, long version, String predicateType, List<IPredicate> predicates)
	{
		super(id, version);
		this.predicateType = predicateType;
		setPredicates(predicates);
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
		return ResourceType.PREDICATE.getTypeId();
	}
	public String getPredicateType()
	{
		return predicateType;
	}
	public void setPredicateType(String predicateType) {
		this.predicateType = predicateType;
	}
	public void setPredicates(List<IPredicate> predicates)
	{
		this.predicates = predicates;
	}
	public List<IPredicate> getPredicates()
	{
		return predicates;
	}
}
