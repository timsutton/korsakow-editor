/**
 * 
 */
package org.korsakow.services.plugin.predicate.argument;



public class StringArgumentInfo extends AbstractArgumentInfo
{
	public StringArgumentInfo(String name, String displayString)
	{
		super(name, String.class, displayString);
	}
	public String formatDisplayValue(Object value)
	{
		return ""+value;
	}
	public Object deserialize(String value) {
		return value;
	}
	public String serialize(Object value) {
		return ""+value;
	}
}