package org.korsakow.ide.resources.property;

public abstract class StringProperty extends AbstractProperty
{
	public StringProperty(String id)
	{
		super(id);
	}
	public abstract String getValue();
	public abstract void setValue(String value);
	public void setValue(Object value)
	{
		if (value instanceof String == false)
			value = value!=null?value.toString():null;
		setValue((String)value);
	}
}
