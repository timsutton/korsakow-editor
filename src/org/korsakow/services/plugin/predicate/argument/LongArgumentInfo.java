/**
 * 
 */
package org.korsakow.services.plugin.predicate.argument;

import org.dsrg.soenea.domain.MapperException;



public class LongArgumentInfo extends NumberArgumentInfo<Long>
{
	public LongArgumentInfo(String name, String displayString)
	{
		super(name, Long.class, displayString);
	}
	public Object deserialize(String value) throws MapperException {
		try {
			return value==null?null:Long.parseLong(value);
		} catch ( NumberFormatException e ){
			throw new MapperException( e );
		}
	}
}