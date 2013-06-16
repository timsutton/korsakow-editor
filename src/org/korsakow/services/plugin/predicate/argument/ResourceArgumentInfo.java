/**
 * 
 */
package org.korsakow.services.plugin.predicate.argument;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.mapper.input.ResourceInputMapper;

public class ResourceArgumentInfo extends AbstractArgumentInfo
{
	public ResourceArgumentInfo(String name, String displayString)
	{
		super(name, IResource.class, displayString);
	}
	@Override
	public String formatDisplayValue(Object value)
	{
		return ((IResource)value).getName();
	}
	public Object deserialize(String value) throws MapperException {
		try {
			return ResourceInputMapper.map( Long.parseLong( value ) );
		} catch ( NumberFormatException e ) {
			throw new MapperException( e );
		}
	}
	public String serialize(Object value) {
		return ""+((IResource)value).getId();
	}
}