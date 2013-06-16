package org.korsakow.domain.interf;

import org.dsrg.soenea.domain.interf.IDomainObject;


public interface ISettings extends IDomainObject<Long>, IDynamicProperties
{
	void setString(String name, String value);
	String getString(String name);
	void setBoolean(String name, boolean value);
	boolean getBoolean(String name);
}
