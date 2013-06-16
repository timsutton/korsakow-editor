/**
 * 
 */
package org.korsakow.services.plugin.predicate;

import org.dsrg.soenea.domain.MapperException;

public interface IArgumentInfo
{
	String getName();
	String getDisplayString();
	String getFormattedDisplayString(Object value);
	String serialize(Object value) throws MapperException;
	Object deserialize(String value) throws MapperException;
	Class<?> getType();
}