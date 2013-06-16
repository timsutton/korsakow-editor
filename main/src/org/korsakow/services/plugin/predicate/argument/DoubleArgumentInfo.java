/**
 * 
 */
package org.korsakow.services.plugin.predicate.argument;

import org.dsrg.soenea.domain.MapperException;



public class DoubleArgumentInfo extends NumberArgumentInfo<Double>
{
	public DoubleArgumentInfo(String name, String displayString)
	{
		super(name, Double.class, displayString);
	}
	public Object deserialize(String value) throws MapperException {
		try {
			return value==null?null:Double.parseDouble(value);
		} catch ( NumberFormatException e ) {
			throw new MapperException( e );
		}
	}
}