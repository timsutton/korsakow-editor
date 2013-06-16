package org.korsakow.domain.mapper.output;

import java.sql.SQLException;

import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.korsakow.domain.Settings;
import org.korsakow.domain.mapper.exception.LostUpdateException;
import org.korsakow.services.finder.SettingsFinder;
import org.korsakow.services.tdg.PropertyTDG;
import org.korsakow.services.tdg.SettingsTDG;

public class SettingsOutputMapper implements GenericOutputMapper<Long, Settings>{

	public void delete(Settings a) throws MapperException {
		try{
			if (0 == SettingsTDG.delete(a.getId(), a.getVersion()))
				throw new MapperException(String.format("Record not found: id=%d, version=%d", a.getId(), a.getVersion()));
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}

	
	public void insert(Settings a) throws MapperException {
		try {
			if(a.getId()==0){
				SettingsTDG.insert(a.getVersion());
			} else {
				SettingsTDG.insert(a.getId(), a.getVersion());
			}
			for (String id : a.getDynamicPropertyIds())
				PropertyTDG.insert(a.getId(), id, a.getDynamicProperty(id));
		} catch (XPathException e) {
			throw new MapperException(e);
		}
	}

	public void update(Settings a) throws MapperException {
		try{
			if(SettingsTDG.update(a.getId(), a.getVersion()) == 0) {
				throw new LostUpdateException("Your version is out of date. No records were altered. id="+a.getId()+",version="+a.getVersion());
			}

			for (String id : a.getDynamicPropertyIds())
				PropertyTDG.insert(a.getId(), id, a.getDynamicProperty(id));
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}	
	}
}
