package org.korsakow.ide.util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Obviously, most of the methods in this object are not reliable to call several times.
 * For example: <code>assert myMap.size()==myMap.size()</code> may well fail.
 * 
 * This class uses teh policy of clean-on-write, meaning stale entries are removed whenever the map is modified.
 * 
 * This class actually just wraps a Map of weak references and provides a constructor allowing the user to specify their own backend.
 * 
 * @author d
 *
 * @param <K>
 * @param <V>
 */
public class WeakReferenceMap<K, V> implements Map<K, V>
{
	private Map<K, WeakReference<V>> map;
	private static class WeakEntry<K, V> implements Entry<K, V>
	{
		private Entry<K, WeakReference<V>> entry;
		public WeakEntry(Entry<K, WeakReference<V>> entry)
		{
			this.entry = entry;
		}
		public K getKey() {
			return entry.getKey();
		}
		public V getValue() {
			return entry.getValue().get();
		}
		public V setValue(V value) {
			entry.setValue(new WeakReference<V>(value));
			return value;
		}
	}
	
	/**
	 * Constructs a WeakReferenceMap backed by a HashMap
	 */
	public WeakReferenceMap()
	{
		this(new HashMap<K, WeakReference<V>>());
	}
	public WeakReferenceMap(Map<K, WeakReference<V>> innerMap)
	{
		this.map = innerMap;
	}
	
	public void clear() {
		map.clear();
	}
	

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map.containsValue(new WeakReference<V>((V)value));
	}

	public Set<Entry<K, V>> entrySet() {
		Set<Entry<K,V>> entries = new HashSet<Entry<K,V>>();
		for (Entry<K,WeakReference<V>> entry : map.entrySet())
			entries.add(new WeakEntry(entry));
		return entries;
	}

	public V get(Object key) {
		WeakReference<V> ref = map.get(key);
		return ref!=null?ref.get():null;
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set<K> keySet() {
		return map.keySet();
	}

	public V put(K key, V value) {
		map.put(key, new WeakReference<V>(value));
		cleanup();
		return value;
	}

	public void putAll(Map<? extends K, ? extends V> t) {
		for (K key : t.keySet())
			put(key, t.get(key));
		cleanup();
	}

	public V remove(Object key) {
		WeakReference<V> ref = map.remove(key);
		cleanup();
		return ref!=null?ref.get():null;
	}

	public int size() {
		return map.size();
	}

	public Collection<V> values() {
		Collection<V> values = new ArrayList<V>();
		for (WeakReference<V> ref : map.values())
		{
			V v = ref.get();
			if (v != null)
				values.add(v);
		}
		return values;
	}
	
	/**
	 * Removes stale entries.
	 */
	private void cleanup()
	{
		Set<K> keys = new HashSet<K>(map.keySet());
		for (K key : keys)
		{
			WeakReference<V> ref = map.get(key);
			if (ref.get() == null)
				map.remove(key);
		}
	}
}
