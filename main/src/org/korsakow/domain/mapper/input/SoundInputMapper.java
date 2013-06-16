package org.korsakow.domain.mapper.input;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.IdentityMap;
import org.dsrg.soenea.uow.ObjectRemovedException;
import org.korsakow.domain.Sound;
import org.korsakow.domain.SoundFactory;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.proxy.KeywordCollectionProxy;
import org.korsakow.domain.proxy.SoundProxy;
import org.korsakow.services.finder.SoundFinder;

public class SoundInputMapper {
	
	public static Sound map(long id) throws MapperException {
		try {
			return IdentityMap.get(id, Sound.class);
		} catch (ObjectRemovedException e) {
			throw new DomainObjectNotFoundException(e);
		} catch (DomainObjectNotFoundException e) {
			//No problem, will just create it.
		}
		try {
			ResultSet rs = null;
			Sound a = null;
			rs = SoundFinder.find(id);
			if(!rs.next()) throw new DomainObjectNotFoundException("That object doesn't exist: " + id);
			a = getSound(rs);
			rs.close();
			return a;
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	public static List<ISound> findAll() throws MapperException {
		List<ISound> list = new ArrayList<ISound>();
		try {
			ResultSet rs = null;
			rs = SoundFinder.findAll();
			while (rs.next()) {
				list.add(new SoundProxy(rs.getLong("id")));
			}
			rs.close();
			return list;
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	private static Sound getSound(ResultSet rs) throws SQLException, MapperException {
		long id = rs.getLong("id");
		
		try {
			return IdentityMap.get(id, Sound.class);
		} catch (ObjectRemovedException e) {
			throw new MapperException(e);
		} catch (DomainObjectNotFoundException e) {
			//No problem, will just create it.
		}
		
		Sound sound = SoundFactory.createClean(
				id, 
				rs.getLong("version"),
				rs.getString("name"),
				new KeywordCollectionProxy(id),
				rs.getString("filename"),
				rs.getString("subtitles"));		
		return sound;
	}
}
