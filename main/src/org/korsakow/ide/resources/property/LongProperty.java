package org.korsakow.ide.resources.property;

public abstract class LongProperty extends AbstractProperty
{
	public LongProperty(String id)
	{
		super(id);
	}
	@Override
	public abstract Long getValue();
	public abstract void setValue(Long value);
	@Override
	public void setValue(Object value)
	{
		Long l = null;
		if (value instanceof Long) {
			l = (Long)value;
		} else {
			if (value != null) {
				String str = value.toString();
				if (str.length() > 0) {
					l = Long.parseLong(str);
				}
			}
		}
		setValue(l);
	}
}
