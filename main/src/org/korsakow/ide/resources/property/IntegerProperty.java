package org.korsakow.ide.resources.property;

public abstract class IntegerProperty extends AbstractProperty
{
	public IntegerProperty(String id)
	{
		super(id);
	}
	@Override
	public abstract Integer getValue();
	public abstract void setValue(Integer value);
	@Override
	public void setValue(Object value)
	{
		Integer i = null;
		if (value instanceof Integer) {
			i = (Integer)value;
		} else {
			if (value != null) {
				String str = value.toString();
				if (str.length() > 0) {
					i = Integer.parseInt(str);
				}
			}
		}
		setValue(i);
	}
}
