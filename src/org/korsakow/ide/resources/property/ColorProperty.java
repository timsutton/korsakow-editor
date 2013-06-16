package org.korsakow.ide.resources.property;

import java.awt.Color;

import org.korsakow.services.util.ColorFactory;

public abstract class ColorProperty extends AbstractProperty
{
	public ColorProperty(String id)
	{
		super(id);
	}
	@Override
	public abstract Object getValue();
	public abstract void setValue(Color value);
	@Override
	public void setValue(Object value)
	{
		if (value instanceof Color == false)
			value = value!=null?ColorFactory.createRGB(value.toString()):null;
		setValue((Color)value);
	}
}
