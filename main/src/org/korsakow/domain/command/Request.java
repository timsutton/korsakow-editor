package org.korsakow.domain.command;

import java.util.HashMap;
import java.util.Set;

public class Request implements Helper
{
	public static Request single(String key, Object value) {
		Request req = new Request();
		req.set(key, value);
		return req;
	}
	
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

	
	public void set(String name, Object value) {
		values.put(name, value);
	}
	
	public Object get(String name) {
		return values.get(name);
	}
}
