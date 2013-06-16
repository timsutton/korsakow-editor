package org.korsakow.domain.command;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.korsakow.domain.interf.IResource;

public class Response implements Helper
{
	public static final String MODIFIED_RESOURCES = "_MODIFIED_RESOURCES";
		
	private final HashMap<String, Object> values = new HashMap<String, Object>();

	public Set<String> getKeys() {
		return values.keySet();
	}
	
	public boolean has(String key) {
		return values.containsKey(key);
	}
	
	public Boolean getBoolean(String name) {
		return (Boolean)values.get(name);
	}

	
	public Double getDouble(String name) {
		return (Double)values.get(name);
	}

	
	public Float getFloat(String name) {
		return (Float)values.get(name);
	}

	
	public Integer getInt(String name) throws NumberFormatException {
		return (Integer)values.get(name);
	}

	
	public Long getLong(String name) throws NumberFormatException {
		return (Long)values.get(name);
	}

	
	public String getString(String name) {
		return (String)values.get(name);
	}

	public <T> Collection<T> getCollection(String name, Class<T> clazz) {
		return (Collection<T>)get(name);
	}
	
	public void set(String name, Object value) {
		values.put(name, value);
	}
	
	public Object get(String name) {
		return values.get(name);
	}
	
	@SuppressWarnings("unchecked")
	public void addModifiedResource(IResource resource) {
		if (!values.containsKey(MODIFIED_RESOURCES))
			values.put(MODIFIED_RESOURCES, new HashSet<IResource>());
		((Set<IResource>)values.get(MODIFIED_RESOURCES)).add(resource);
	}
	public Collection<IResource> getModifiedResources() {
		if (!values.containsKey(MODIFIED_RESOURCES))
			values.put(MODIFIED_RESOURCES, new HashSet<IResource>());
		return ((Set<IResource>)values.get(MODIFIED_RESOURCES));
	}
}
