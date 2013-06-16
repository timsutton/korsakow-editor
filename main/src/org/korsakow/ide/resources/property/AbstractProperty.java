/**
 * 
 */
package org.korsakow.ide.resources.property;

public abstract class AbstractProperty
{
	private final String id;
	public AbstractProperty(String id)
	{
		this.id = id;
	}
	public String getId()
	{
		return id;
	}
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof AbstractProperty == false)
			return false;
		AbstractProperty other = (AbstractProperty)o;
		return id.equals(other.id);
	}
	@Override
	public int hashCode()
	{
		return id.hashCode();
	}
	public abstract Object getValue();
	public abstract void setValue(Object value);
}