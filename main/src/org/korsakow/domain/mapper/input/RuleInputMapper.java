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
import org.korsakow.domain.Rule;
import org.korsakow.domain.RuleFactory;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.proxy.KeywordCollectionProxy;
import org.korsakow.domain.proxy.RuleListProxy;
import org.korsakow.domain.proxy.RuleProxy;
import org.korsakow.services.finder.RuleFinder;
import org.korsakow.services.plugin.predicate.IArgumentInfo;
import org.korsakow.services.plugin.rule.IRuleTypeInfo;
import org.korsakow.services.plugin.rule.RuleTypeInfoFactory;
import org.korsakow.services.plugin.rule.RuleTypeInfoFactoryException;

public class RuleInputMapper {
	
	public static Rule map(long id) throws MapperException {
		try {
			return IdentityMap.get(id, Rule.class);
		} catch (ObjectRemovedException e) {
			throw new DomainObjectNotFoundException(e);
		} catch (DomainObjectNotFoundException e) {
			//No problem, will just create it.
		}
		try {
			ResultSet rs = RuleFinder.find(id);
			if(!rs.next())
				throw new DomainObjectNotFoundException("That object doesn't exist: " + id);
			Rule rule = getRule(rs);
			rs.close();
			return rule;
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		} catch (SQLException e) {
			throw new MapperException(e);
		}
	}
	public static List<? extends IRule> findByResource(long resource_id) throws MapperException {
		
		List<IRule> rules = new ArrayList<IRule>();
		try {
			ResultSet rs = null;
			rs = RuleFinder.findByResource(resource_id);
			while (rs.next()) {
				rules.add(new RuleProxy(rs.getLong("id")));
			}
			rs.close();
			return rules;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
	
	public static List<? extends IRule> findAll() throws MapperException 
	{
		List<IRule> rules = new ArrayList<IRule>();
		try {
			ResultSet rs = null;
			rs = RuleFinder.findAll();
			while (rs.next()) {
				rules.add(new RuleProxy(rs.getLong("id")));
			}
			return rules;
		} catch (SQLException e) {
			throw new MapperException(e);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
	
	private static Rule getRule(ResultSet rs) throws MapperException, SQLException {
		long id = rs.getLong("id");
		
		try {
			return IdentityMap.get(id, Rule.class);
		} catch (ObjectRemovedException e) {
			throw new MapperException(e);
		} catch (DomainObjectNotFoundException e) {
			//No problem, will just create it.
		}
		
		Rule rule = RuleFactory.createClean(
				id, 
				rs.getLong("version"),
				rs.getString("type"),
				new KeywordCollectionProxy(id),
				rs.getString("name"),
				rs.getLong("triggerTime"),
				new RuleListProxy(id));

		Map<String, String> properties = PropertyInputMapper.map(rule.getId());
		for (String propId : properties.keySet()) {
			Object value = properties.get(propId);
			
			IRuleTypeInfo typeInfo = null;
			try {
				typeInfo = RuleTypeInfoFactory.getFactory().getTypeInfo(rule.getRuleType());
				IArgumentInfo argInfo = typeInfo.getArgument(propId);
				value = argInfo.deserialize((String)value);
			} catch (RuleTypeInfoFactoryException e) {
				throw new MapperException(e);
			} catch (RuntimeException e) {
				throw new MapperException(e);
			}
			
			rule.setDynamicProperty(propId, value);
		}
		return rule;
	}
}
