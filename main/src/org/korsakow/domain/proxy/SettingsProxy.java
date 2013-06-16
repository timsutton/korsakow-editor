package org.korsakow.domain.proxy;

import java.util.Collection;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.Settings;
import org.korsakow.domain.interf.ISettings;
import org.korsakow.domain.mapper.input.SettingsInputMapper;

public class SettingsProxy extends KDomainObjectProxy<Settings> implements ISettings {

	public SettingsProxy(long id)
	{
		super(id);
	}

	@Override
	public Class<Settings> getInnerClass()
	{
		return Settings.class;
	}
	
	@Override
	protected Settings getFromMapper(Long id) throws MapperException {
		return SettingsInputMapper.map(id);
	}

	public Object getDynamicProperty(String id) {
		return getInnerObject().getDynamicProperty(id);
	}

	public Collection<String> getDynamicPropertyIds() {
		return getInnerObject().getDynamicPropertyIds();
	}

	public void setDynamicProperty(String id, Object value) {
		getInnerObject().setDynamicProperty(id, value);
	}
	public Class getPropertyType(String id) {
		return getInnerObject().getPropertyType(id);
	}

	public boolean getBoolean(String name) {
		return getInnerObject().getBoolean(name);
	}

	public String getString(String name) {
		return getInnerObject().getString(name);
	}

	public void setBoolean(String name, boolean value) {
		getInnerObject().setBoolean(name, value);
	}

	public void setString(String name, String value) {
		getInnerObject().setString(name, value);
	}

}
