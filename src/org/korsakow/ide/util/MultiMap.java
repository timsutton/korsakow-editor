/**
 * 
 */
package org.korsakow.ide.util;

import java.util.Collection;

public interface MultiMap<KeyType, ValueType, CollectionType extends Collection<ValueType>>
{
	void add(KeyType key, ValueType value);
	CollectionType get(KeyType key);
	boolean containsKey(KeyType key);
	int size();
	int size(KeyType key);
	boolean isEmpty();
	boolean isEmpty(KeyType key);
	boolean removeAll(Collection<KeyType> keys);
}