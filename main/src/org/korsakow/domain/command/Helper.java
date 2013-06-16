package org.korsakow.domain.command;

import java.util.Set;


public interface Helper {

	abstract Set<String> getKeys();
	
	abstract String getString(String pName);
	
	abstract Integer getInt(String pName) throws NumberFormatException;

	abstract Long getLong(String pName) throws NumberFormatException;

	abstract Boolean getBoolean(String pName);
	
	abstract Float getFloat(String pName) throws NumberFormatException;
	
	abstract Double getDouble(String pName) throws NumberFormatException;

	abstract boolean has(String pName);
	
	abstract Object get(String pName);
	abstract void set(String pName, Object value);
}
