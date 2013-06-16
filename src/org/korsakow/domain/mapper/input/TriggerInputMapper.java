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
import org.korsakow.domain.Trigger;
import org.korsakow.domain.TriggerFactory;
import org.korsakow.domain.interf.ITrigger;
import org.korsakow.domain.proxy.TriggerProxy;
import org.korsakow.services.finder.TriggerFinder;

public class TriggerInputMapper {
	
	public static Trigger map(long id) throws MapperException {
		try {
			return IdentityMap.get(id, Trigger.class);
		} catch (ObjectRemovedException e) {
			throw new DomainObjectNotFoundException(e);
		} catch (DomainObjectNotFoundException e) {
			//No problem, will just create it.
		}
		try {
			ResultSet rs = TriggerFinder.find(id);
			if(!rs.next())
				throw new DomainObjectNotFoundException("That object doesn't exist: " + id);
			Trigger trigger = getTrigger(rs);
			rs.close();
			return trigger;
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	public static List<? extends ITrigger> findByResource(long resource_id) throws MapperException {
		
		List<ITrigger> triggers = new ArrayList<ITrigger>();
		try {
			ResultSet rs = null;
			rs = TriggerFinder.findByResource(resource_id);
			while (rs.next()) {
				triggers.add(new TriggerProxy(rs.getLong("id")));
			}
			rs.close();
			return triggers;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
	
	public static List<? extends ITrigger> findAll() throws MapperException
	{
		List<ITrigger> triggers = new ArrayList<ITrigger>();
		try {
			ResultSet rs = null;
			rs = TriggerFinder.findAll();
			while (rs.next()) {
				triggers.add(new TriggerProxy(rs.getLong("id")));
			}
			return triggers;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
	
	private static Trigger getTrigger(ResultSet rs) throws MapperException, SQLException {
		long id = rs.getLong("id");
		
		try {
			return IdentityMap.get(id, Trigger.class);
		} catch (ObjectRemovedException e) {
			throw new MapperException(e);
		} catch (DomainObjectNotFoundException e) {
			//No problem, will just create it.
		}
		
		Trigger trigger = TriggerFactory.createClean(
				id, 
				rs.getLong("version"),
				rs.getString("type")
				);

		Map<String, String> properties = PropertyInputMapper.map(trigger.getId());
		for (String propId : properties.keySet())
			trigger.setDynamicProperty(propId, properties.get(propId));
		return trigger;
	}
}
