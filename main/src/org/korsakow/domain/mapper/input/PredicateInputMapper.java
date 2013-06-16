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
import org.korsakow.domain.Predicate;
import org.korsakow.domain.PredicateFactory;
import org.korsakow.domain.interf.IPredicate;
import org.korsakow.domain.proxy.PredicateListProxy;
import org.korsakow.domain.proxy.PredicateProxy;
import org.korsakow.services.finder.PredicateFinder;
import org.korsakow.services.plugin.predicate.IArgumentInfo;
import org.korsakow.services.plugin.predicate.IPredicateTypeInfo;
import org.korsakow.services.plugin.predicate.PredicateTypeInfoFactory;
import org.korsakow.services.plugin.predicate.PredicateTypeInfoFactoryException;

public class PredicateInputMapper {
	
	public static Predicate map(long id) throws MapperException {
		try {
			return IdentityMap.get(id, Predicate.class);
		} catch (ObjectRemovedException e) {
			throw new DomainObjectNotFoundException(e);
		} catch (DomainObjectNotFoundException e) {
			//No problem, will just create it.
		}
		try {
			ResultSet rs = PredicateFinder.find(id);
			if(!rs.next())
				throw new DomainObjectNotFoundException("That object doesn't exist: " + id);
			Predicate predicate = getPredicate(rs);
			rs.close();
			return predicate;
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	public static List<? extends IPredicate> findByResource(long resource_id) throws MapperException {
		
		List<IPredicate> predicates = new ArrayList<IPredicate>();
		try {
			ResultSet rs = null;
			rs = PredicateFinder.findByResource(resource_id);
			while (rs.next()) {
				predicates.add(new PredicateProxy(rs.getLong("id")));
			}
			rs.close();
			return predicates;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
	
	public static List<? extends IPredicate> findAll() throws MapperException 
	{
		List<IPredicate> predicates = new ArrayList<IPredicate>();
		try {
			ResultSet rs = null;
			rs = PredicateFinder.findAll();
			while (rs.next()) {
				predicates.add(new PredicateProxy(rs.getLong("id")));
			}
			return predicates;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
	
	private static Predicate getPredicate(ResultSet rs) throws MapperException, SQLException {
		long id = rs.getLong("id");
		
		try {
			return IdentityMap.get(id, Predicate.class);
		} catch (ObjectRemovedException e) {
			throw new MapperException(e);
		} catch (DomainObjectNotFoundException e) {
			//No problem, will just create it.
		}
		
		Predicate predicate = PredicateFactory.createClean(
				id, 
				rs.getLong("version"),
				rs.getString("type"),
				new PredicateListProxy(id));

		Map<String, String> properties = PropertyInputMapper.map(predicate.getId());
		for (String propId : properties.keySet()) {
			Object value = properties.get(propId);
			
			IPredicateTypeInfo typeInfo = null;
			try {
				typeInfo = PredicateTypeInfoFactory.getFactory().getTypeInfo(predicate.getPredicateType());
				IArgumentInfo argInfo = typeInfo.getArgument(propId);
				value = argInfo.deserialize((String)value);
			} catch (PredicateTypeInfoFactoryException e) {
				throw new MapperException(e);
			} catch (RuntimeException e) {
				throw new MapperException(e);
			}
			
			predicate.setDynamicProperty(propId, value);
		}
		return predicate;
	}
}
