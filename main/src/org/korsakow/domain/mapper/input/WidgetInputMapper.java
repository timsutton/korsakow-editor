package org.korsakow.domain.mapper.input;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.dsrg.soenea.domain.mapper.IdentityMap;
import org.dsrg.soenea.uow.ObjectRemovedException;
import org.korsakow.domain.Widget;
import org.korsakow.domain.WidgetFactory;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.domain.proxy.KeywordCollectionProxy;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.resources.widget.WidgetPersistAction;
import org.korsakow.ide.resources.widget.WidgetPersistCondition;
import org.korsakow.services.finder.WidgetFinder;

public class WidgetInputMapper {
	
	public static Widget map(long id) throws MapperException {
		try {
			return IdentityMap.get(id, Widget.class);
		} catch (ObjectRemovedException e) {
			throw new DomainObjectNotFoundException(e);
		} catch (DomainObjectNotFoundException e) {
			// ok, we'll create it
		}
		try {
			ResultSet rs = null;
			Widget a = null;
			rs = WidgetFinder.find(id);
			if(!rs.next()) throw new DomainObjectNotFoundException("That object doesn't exist: " + id);
			a = getWidget(rs);
			rs.close();
			return a;
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}

	public static List<IWidget> findAll() throws MapperException {
		List<IWidget> list = new ArrayList<IWidget>();
		try {
			ResultSet rs = null;
			rs = WidgetFinder.findAll();
			while (rs.next()) {
				list.add(getWidget(rs));
			}
			rs.close();
			return list;
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	public static List<IWidget> findByInterface(long interface_id) throws MapperException {
		List<IWidget> list = new ArrayList<IWidget>();
		try {
			ResultSet rs = null;
			rs = WidgetFinder.findByInterfaceId(interface_id);
			while (rs.next()) {
				list.add(getWidget(rs));
			}
			return list;
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	private static Widget getWidget(ResultSet rs) throws MapperException, SQLException {
		long id = rs.getObject("id")!=null?rs.getLong("id"):DataRegistry.getMaxId(); // importing default project
		
		try {
			return IdentityMap.get(id, Widget.class);
		} catch (ObjectRemovedException e) {
			throw new MapperException(e);
		} catch (DomainObjectNotFoundException e) {
			//
		}
		
		Widget widget = WidgetFactory.createClean(
				id, 
				rs.getLong("version"),
				rs.getString("name"),
				new KeywordCollectionProxy(id),
				rs.getString("widgetType"),
				WidgetPersistCondition.forId(rs.getString("persistCondition")),
				WidgetPersistAction.forId(rs.getString("persistAction")),
				rs.getInt("x"),
				rs.getInt("y"),		
				rs.getInt("width"),
				rs.getInt("height"));
		
		Map<String, String> properties = PropertyInputMapper.map(widget.getId());
		for (String propId : properties.keySet())
			widget.setDynamicProperty(propId, properties.get(propId));
		return widget;
	}
}
