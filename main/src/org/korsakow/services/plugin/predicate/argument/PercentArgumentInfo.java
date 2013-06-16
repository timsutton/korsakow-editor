/**
 * 
 */
package org.korsakow.services.plugin.predicate.argument;



public class PercentArgumentInfo extends DoubleArgumentInfo
{
	public PercentArgumentInfo(String name, String displayString)
	{
		super(name, displayString);
	}
	public String formatDisplayValue(Object value)
	{
		return String.format("%d", (int)(((Number)value).floatValue()*100));
	}
}