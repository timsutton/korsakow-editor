/**
 * 
 */
package org.korsakow.services.plugin.predicate.argument;

import java.text.MessageFormat;

import org.korsakow.services.plugin.predicate.IArgumentInfo;


public abstract class AbstractArgumentInfo implements IArgumentInfo
{
	private final String name;
	private final String displayString;
	private final Class<?> type;
	public AbstractArgumentInfo(String name, Class<?> type, String displayString)
	{
		this.name = name;
		this.type = type;
		this.displayString = displayString;
	}
	protected abstract String formatDisplayValue(Object value);
	public String getName()
	{
		return name;
	}
	public Class<?> getType()
	{
		return type;
	}
	public String getDisplayString()
	{
		return displayString;
	}
	public String getFormattedDisplayString(Object value)
	{
		return formatDisplayValue(value);
	}
}