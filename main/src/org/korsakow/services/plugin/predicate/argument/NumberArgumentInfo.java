/**
 * 
 */
package org.korsakow.services.plugin.predicate.argument;



public abstract class NumberArgumentInfo<N extends Number> extends AbstractArgumentInfo
{
	public NumberArgumentInfo(String name, Class<N> clazz, String displayString)
	{
		super(name, clazz, displayString);
	}
	public String formatDisplayValue(Object value)
	{
		return ""+(N)value;
	}
	public String serialize(Object value) {
		return ""+value;
	}
}