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
import org.korsakow.domain.Interface;
import org.korsakow.domain.InterfaceFactory;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.proxy.ImageProxy;
import org.korsakow.domain.proxy.InterfaceProxy;
import org.korsakow.domain.proxy.KeywordCollectionProxy;
import org.korsakow.domain.proxy.SoundProxy;
import org.korsakow.domain.proxy.WidgetListProxy;
import org.korsakow.services.finder.InterfaceFinder;
import org.korsakow.services.util.ColorFactory;


public class InterfaceInputMapper {
	
	public static Interface map(long id) throws MapperException {
		try {
			return IdentityMap.get(id, Interface.class);
		} catch (ObjectRemovedException e) {
			throw new DomainObjectNotFoundException(e);
		} catch (DomainObjectNotFoundException e) {
			//No problem, will just create it.
		}
		try {
			ResultSet rs = null;
			Interface a = null;
			rs = InterfaceFinder.find(id);
			if(!rs.next()) throw new DomainObjectNotFoundException("That object doesn't exist: " + id);
			a = getInterface(rs);
			rs.close();
			return a;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}

	public static List<? extends IInterface> findByWidget(long widget_id) throws MapperException {
		List<IInterface> list = new ArrayList<IInterface>();
		try {
			ResultSet rs = null;
			rs = InterfaceFinder.findByWidget(widget_id);
			while (rs.next()) {
				list.add(new InterfaceProxy(rs.getLong("id")));
			}
			rs.close();
			return list;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
	public static List<? extends IInterface> findBySound(long sound_id) throws MapperException {
		List<IInterface> list = new ArrayList<IInterface>();
		try {
			ResultSet rs = null;
			rs = InterfaceFinder.findBySound(sound_id);
			while (rs.next()) {
				list.add(new InterfaceProxy(rs.getLong("id")));
			}
			rs.close();
			return list;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
	public static List<IInterface> findAll() throws MapperException {
		List<IInterface> list = new ArrayList<IInterface>();
		try {
			ResultSet rs = null;
			rs = InterfaceFinder.findAll();
			while (rs.next()) {
				list.add(new InterfaceProxy(rs.getLong("id")));
			}
			rs.close();
			return list;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
	public static List<Interface> findAllConcrete() throws MapperException {
		List<Interface> list = new ArrayList<Interface>();
		try {
			ResultSet rs = null;
			rs = InterfaceFinder.findAll();
			while (rs.next()) {
				list.add(getInterface(rs));
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
	 * first checks against the identity map
	 * @param rs
	 * @return
	 * @throws SQLException
	 * @throws MapperException 
	 */
	public static Interface getInterface(ResultSet rs) throws SQLException, MapperException {
		long id = rs.getLong("id");
		long version = rs.getLong("version");
		return getInterface(rs, id, version);
	}
	public static Interface getInterface(ResultSet rs, long id, long version) throws SQLException, MapperException {

		try {
			return IdentityMap.get(id, Interface.class);
		} catch (ObjectRemovedException e) {
			throw new MapperException(e);
		} catch (DomainObjectNotFoundException e) {
			//No problem, will just create it.
		}
		
		Interface iface = InterfaceFactory.createClean(
				id, 
				version,
				rs.getString("name"),
				new KeywordCollectionProxy(id),
				new WidgetListProxy(id),
				rs.getInt("gridWidth"),
				rs.getInt("gridHeight"),
				rs.getObject("viewWidth")!=null?rs.getInt("viewWidth"):null,
				rs.getObject("viewHeight")!=null?rs.getInt("viewHeight"):null,
				rs.getObject("clickSoundId")!=null?new SoundProxy(rs.getLong("clickSoundId")):null,
				rs.getFloat("clickSoundVolume"),
				rs.getObject("backgroundImageId")!=null?new ImageProxy(rs.getLong("backgroundImageId")):null,
				rs.getObject("backgroundColor")!=null?ColorFactory.createRGB(rs.getString("backgroundColor")):null
		);		
		return iface;
	}
}
