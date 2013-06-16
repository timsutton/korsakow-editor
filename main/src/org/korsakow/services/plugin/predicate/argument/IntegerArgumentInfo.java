/**
 * 
 */
package org.korsakow.services.plugin.predicate.argument;

import org.dsrg.soenea.domain.MapperException;



public class IntegerArgumentInfo extends NumberArgumentInfo<Integer>
{
	public IntegerArgumentInfo(String name, String displayString)
	{
		super(name, Integer.class, displayString);
	}
	public Object deserialize(String value) throws MapperException {
		try {
			return value==null?null:Integer.parseInt(value);
		} catch ( NumberFormatException e ) {
			throw new MapperException( e );
		}
	}
}