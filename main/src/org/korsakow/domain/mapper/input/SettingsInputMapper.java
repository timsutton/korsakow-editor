package org.korsakow.domain.mapper.input;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.IdentityMap;
import org.dsrg.soenea.uow.ObjectRemovedException;
import org.korsakow.domain.Settings;
import org.korsakow.domain.SettingsFactory;
import org.korsakow.ide.DataRegistry;
import org.korsakow.services.finder.SettingsFinder;

public class SettingsInputMapper {
	
	public static Settings map(long id) throws MapperException {
		try {
			return IdentityMap.get(id, Settings.class);
		} catch (ObjectRemovedException e) {
			throw new DomainObjectNotFoundException(e);
		} catch (DomainObjectNotFoundException e) {
			//No problem, will just create it.
		}
		try {
			ResultSet rs = null;
			Settings a = null;
			rs = SettingsFinder.find(id);
			if(!rs.next()) throw new DomainObjectNotFoundException("That object doesn't exist: " + id);
			a = getSettings(rs);
			rs.close();
			return a;
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	public static Settings find() throws MapperException {
		ResultSet rs = null;
		try {
			rs = SettingsFinder.find();
			if (!rs.next())
				throw new DomainObjectNotFoundException("");
			if (rs.next())
				throw new MapperException("Multiple projects found!");
			Settings settings = getSettings(rs);
			rs.close();
			return settings;
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	private static Settings getSettings(ResultSet rs) throws MapperException, SQLException {
		long id = rs.getObject("id")!=null?rs.getLong("id"):DataRegistry.getMaxId(); // importing default project
		
		try {
			return IdentityMap.get(id, Settings.class);
		} catch (ObjectRemovedException e) {
			throw new MapperException(e);
		} catch (DomainObjectNotFoundException e) {
			//No problem, will just create it.
		}
		
		Settings settings = SettingsFactory.createClean(
				id, 
				rs.getLong("version"));
		
		Map<String, String> properties = PropertyInputMapper.map(settings.getId());
		for (String propId : properties.keySet())
			settings.setDynamicProperty(propId, properties.get(propId));
		return settings;
	}
}
