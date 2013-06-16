/**
 * 
 */
package org.korsakow.services.plugin.predicate;

import java.util.Collection;

/**
 * 
 * A predicate is a function of zero or more parameters to a boolean value.
 * Parameters are always other predicates.
 * It may also have some internal dynamically set configuration, called here arguments (confusing names, yes, I'm sorry).
 * Arguments are primitive types.
 * 
 * @author d
 *
 */
public interface IPredicateTypeInfo extends ICommonTypeInfo
{
	int getArity();
}