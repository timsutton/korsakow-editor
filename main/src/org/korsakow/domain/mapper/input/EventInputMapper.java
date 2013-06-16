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
import org.korsakow.domain.Event;
import org.korsakow.domain.EventFactory;
import org.korsakow.domain.interf.IEvent;
import org.korsakow.domain.proxy.EventProxy;
import org.korsakow.domain.proxy.PredicateListProxy;
import org.korsakow.domain.proxy.RuleListProxy;
import org.korsakow.domain.proxy.TriggerListProxy;
import org.korsakow.services.finder.EventFinder;

public class EventInputMapper {
	
	public static Event map(long id) throws MapperException {
		try {
			return IdentityMap.get(id, Event.class);
		} catch (ObjectRemovedException e) {
			throw new DomainObjectNotFoundException(e);
		} catch (DomainObjectNotFoundException e) {
			//No problem, will just create it.
		}
		try {
			ResultSet rs = EventFinder.find(id);
			if(!rs.next())
				throw new DomainObjectNotFoundException("That object doesn't exist: " + id);
			Event event = getEvent(rs);
			rs.close();
			return event;
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	public static List<? extends IEvent> findByResource(long resource_id) throws MapperException {
		
		List<IEvent> events = new ArrayList<IEvent>();
		try {
			ResultSet rs = null;
			rs = EventFinder.findByResource(resource_id);
			while (rs.next()) {
				events.add(new EventProxy(rs.getLong("id")));
			}
			rs.close();
			return events;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
	
	public static List<? extends IEvent> findAll() throws MapperException 
	{
		List<IEvent> events = new ArrayList<IEvent>();
		try {
			ResultSet rs = null;
			rs = EventFinder.findAll();
			while (rs.next()) {
				events.add(new EventProxy(rs.getLong("id")));
			}
			return events;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
	
	private static Event getEvent(ResultSet rs) throws MapperException, SQLException {
		long id = rs.getLong("id");
		
		try {
			return IdentityMap.get(id, Event.class);
		} catch (ObjectRemovedException e) {
			throw new MapperException(e);
		} catch (DomainObjectNotFoundException e) {
			//No problem, will just create it.
		}
		
		Event event = EventFactory.createClean(
				id, 
				rs.getLong("version"),
				new TriggerListProxy(id),
				new PredicateListProxy(id),
				new RuleListProxy(id)
				);

		Map<String, String> properties = PropertyInputMapper.map(event.getId());
		for (String propId : properties.keySet())
			event.setDynamicProperty(propId, properties.get(propId));
		return event;
	}
}
