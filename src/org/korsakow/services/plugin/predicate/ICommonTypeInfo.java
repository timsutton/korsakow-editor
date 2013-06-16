/**
 * 
 */
package org.korsakow.services.plugin.predicate;

import java.util.Collection;

/**
 */
public interface ICommonTypeInfo extends ITypeInfo
{
	String getId();
	String getDisplayString();
	String getFormattedDisplayString(Object... args);
	Collection<IArgumentInfo> getArguments();
	IArgumentInfo getArgument(String name);
}