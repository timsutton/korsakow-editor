package org.korsakow.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;

import org.korsakow.domain.interf.IKeyword;

public class Keyword implements IKeyword
{
	public static Collection<IKeyword> fromStrings(Collection<String> values)
	{
		HashSet<IKeyword> keywords = new LinkedHashSet<IKeyword>();
		for (String value : values)
			keywords.add(new Keyword(value));
		return keywords;
	}
	public static Collection<String> toStrings(Collection<IKeyword> keywords)
	{
		HashSet<String> values = new LinkedHashSet<String>();
		for (IKeyword keyword : keywords)
			values.add(keyword.getValue());
		return values;
	}
	private String value;
	private float weight;
	
	Keyword(String value)
	{
		this(value, 1);
	}
	Keyword(String value, float weight)
	{
		setValue(value);
		setWeight(weight);
	}
	
	public void setValue(String value)
	{
		this.value = value;
	}
	public String getValue()
	{
		return value;
	}
	
	public void setWeight(float weight)
	{
		this.weight = weight;
	}
	public float getWeight()
	{
		return weight;
	}
	
	/**
	 * Based solely on value, not weight.
	 */
	@Override
	public int hashCode()
	{
		return (value.hashCode());
	}
	/**
	 * Based solely on value, not weight.
	 */
	@Override
	public boolean equals(Object other)
	{
		if (other instanceof IKeyword == false)
			return false;
		IKeyword k = (IKeyword)other;
		return value.equals(k.getValue());
	}
	/**
	 * By value, then weight.
	 */
	public int compareTo(IKeyword o) {
		int c = value.compareTo(o.getValue());
		if (c == 0)
			c = (int)Math.signum(weight-o.getWeight());
		return c;
	}
	
	@Override
	public String toString() {
		return String.format("[Keyword; %s(%s)]", getValue(), getWeight());
	}
}
