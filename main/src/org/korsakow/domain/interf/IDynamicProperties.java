package org.korsakow.domain.interf;

import java.util.Collection;

/**
 * An object that supports unnamed properties.
 * @author d
 *
 */
public interface IDynamicProperties
{
	Object getDynamicProperty(String id);
	void setDynamicProperty(String id, Object value);
	Collection<String> getDynamicPropertyIds();
}
