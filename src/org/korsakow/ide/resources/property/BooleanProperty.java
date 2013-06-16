package org.korsakow.ide.resources.property;

public abstract class BooleanProperty extends AbstractProperty
{
	/**
	 * TODO: fix
	 * Used when set value is null
	 * The fact that we have this is due to poor handling of the file format from
	 * one version to the next (basically there is no explicit handling)
	 */
	private final Boolean defaultValue;
	
	public BooleanProperty(String id)
	{
		this(id, null);
	}
	public BooleanProperty(String id, Boolean defaultValue)
	{
		super(id);
		this.defaultValue = defaultValue;
	}
	@Override
	public abstract Boolean getValue();
	public abstract void setValue(Boolean value);
	@Override
	public void setValue(Object value)
	{
		if (value instanceof Boolean == false)
			value = value!=null?Boolean.parseBoolean(value.toString()):null;
		if (value == null)
			value = defaultValue;
		setValue((Boolean)value);
	}
}
