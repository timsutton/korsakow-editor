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
import org.korsakow.domain.Snu;
import org.korsakow.domain.SnuFactory;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.interf.ISnu.BackgroundSoundMode;
import org.korsakow.domain.proxy.EventListProxy;
import org.korsakow.domain.proxy.InterfaceProxy;
import org.korsakow.domain.proxy.KeywordCollectionProxy;
import org.korsakow.domain.proxy.RuleListProxy;
import org.korsakow.domain.proxy.SnuProxy;
import org.korsakow.domain.proxy.SoundProxy;
import org.korsakow.domain.proxy.UnknownMediaProxy;
import org.korsakow.services.finder.SnuFinder;

public class SnuInputMapper {
	
	public static Snu map(long id) throws MapperException {
		try {
			return IdentityMap.get(id, Snu.class);
		} catch (ObjectRemovedException e) {
			throw new DomainObjectNotFoundException(e);
		} catch (DomainObjectNotFoundException e) {
			//No problem, will just create it.
		}
		try {
			ResultSet rs = SnuFinder.find(id);
			if(!rs.next()) throw new DomainObjectNotFoundException("That object doesn't exist: " + id);
			Snu a = getSnu(rs);
			rs.close();
			return a;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}

	public static List<ISnu> findByName(String name) throws MapperException {
		List<ISnu> list = new ArrayList<ISnu>();
		try {
			ResultSet rs = null;
			rs = SnuFinder.findByName(name);
			while (rs.next()) {
				list.add(new SnuProxy(rs.getLong("id")));
			}
			rs.close();
			return list;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
	public static List<ISnu> findByInKeyword(String keyword) throws MapperException {
		List<ISnu> list = new ArrayList<ISnu>();
		try {
			ResultSet rs = null;
			rs = SnuFinder.findByKeyword(keyword);
			while (rs.next()) {
				list.add(new SnuProxy(rs.getLong("id")));
			}
			rs.close();
			return list;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
	public static List<ISnu> findByOutKeyword(String keyword) throws MapperException {
		List<ISnu> list = new ArrayList<ISnu>();
		try {
			ResultSet rs = null;
			rs = SnuFinder.findByOutKeyword(keyword);
			while (rs.next()) {
				list.add(new SnuProxy(rs.getLong("id")));
			}
			rs.close();
			return list;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
	public static List<? extends ISnu> findByMainMediaId(long id) throws MapperException {
		List<ISnu> list = new ArrayList<ISnu>();
		try {
			ResultSet rs = null;
			rs = SnuFinder.findByMainMediaId(id);
			while (rs.next()) {
				list.add(new SnuProxy(rs.getLong("id")));
			}
			rs.close();
			return list;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}

	public static List<? extends ISnu> findByInterfaceId(long id) throws MapperException {
		List<ISnu> list = new ArrayList<ISnu>();
		try {
			ResultSet rs = null;
			rs = SnuFinder.findByInterfaceId(id);
			while (rs.next()) {
				list.add(new SnuProxy(rs.getLong("id")));//getSnu(rs));
			}
			rs.close();
			return list;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
	/**
	 * 
	 * @return null if none exist
	 * @throws SQLException
	 */
	public static Snu findAnyOtherOne(long id) throws MapperException {
		try {
			ResultSet rs = SnuFinder.findAnyOtherOne(id);
			if(!rs.next()) return null;
			Snu a = getSnu(rs);
			rs.close();
			return a;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
	public static List<ISnu> findAll() throws MapperException {
		List<ISnu> list = new ArrayList<ISnu>();
		try {
			ResultSet rs = null;
			rs = SnuFinder.findAll();
			while (rs.next()) {
				list.add(new SnuProxy(rs.getLong("id")));//getSnu(rs));
			}
			rs.close();
			return list;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
	
	private static Snu getSnu(ResultSet rs) throws SQLException, MapperException {
		long id = rs.getLong("id");
		
		try {
			return IdentityMap.get(id, Snu.class);
		} catch (ObjectRemovedException e) {
			throw new MapperException(e);
		} catch (DomainObjectNotFoundException e) {
			//No problem, will just create it.
		}
		
		Snu Snu = SnuFactory.createClean(
				id, 
				rs.getLong("version"),
				rs.getString("name"),
				new KeywordCollectionProxy(id),
				rs.getObject("mainMediaId")!=null?new UnknownMediaProxy(rs.getLong("mainMediaId")):null,
				rs.getFloat("rating"),
				rs.getObject("backgroundSoundId")!=null?new SoundProxy(rs.getLong("backgroundSoundId")):null,
				BackgroundSoundMode.forId(rs.getString("backgroundSoundMode")),
				rs.getFloat("backgroundSoundVolume"),
				rs.getBoolean("backgroundSoundLooping"),
				rs.getObject("interfaceId")!=null?new InterfaceProxy(rs.getLong("interfaceId")):null,
				new RuleListProxy(id),
				rs.getObject("lives")!=null?rs.getLong("lives"):null,
				rs.getBoolean("looping"),
				rs.getObject("maxLinks")!=null?rs.getLong("maxLinks"):null,
				rs.getBoolean("starter"),
				rs.getBoolean("ender"),
				rs.getObject("previewMediaId")!=null?new UnknownMediaProxy(rs.getLong("previewMediaId")):null,
				rs.getObject("previewText")!=null?rs.getString("previewText"):null,
				rs.getObject("insertText")!=null?rs.getString("insertText"):null,
				new EventListProxy(id)
		);
		return Snu;
	}
}
