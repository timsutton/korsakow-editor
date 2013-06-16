package org.korsakow.domain.mapper.output;

import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.korsakow.domain.Rule;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.mapper.exception.LostUpdateException;
import org.korsakow.services.plugin.predicate.IArgumentInfo;
import org.korsakow.services.plugin.rule.IRuleTypeInfo;
import org.korsakow.services.plugin.rule.RuleTypeInfoFactory;
import org.korsakow.services.plugin.rule.RuleTypeInfoFactoryException;
import org.korsakow.services.tdg.KeywordTDG;
import org.korsakow.services.tdg.PropertyTDG;
import org.korsakow.services.tdg.RuleTDG;

public class RuleOutputMapper implements GenericOutputMapper<Long, Rule>{

	public void delete(Rule a) throws MapperException {
		try{
			if (0 == RuleTDG.delete(a.getId(), a.getVersion()))
				throw new MapperException(String.format("Record not found: id=%d, version=%d", a.getId(), a.getVersion()));
			KeywordTDG.deleteByObject(a.getId());
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}

	public void insert(Rule a) throws MapperException {
	}
	
	public static void insert(long object_id, IRule a) throws MapperException {
		try {
			if(a.getId()==0){
				RuleTDG.insert(object_id, a.getVersion(), a.getRuleType(), a.getName(), a.getTriggerTime());
			} else {
				RuleTDG.insert(object_id, a.getId(), a.getVersion(), a.getRuleType(), a.getName(), a.getTriggerTime());
			}
			for (IKeyword keyword : a.getKeywords())
				KeywordOutputMapper.insert(a.getId(), keyword);
			outputProperties(a);
			for (IRule rule : a.getRules()) {
				// icky implementation detail: rule is a weak mapping. we're about to delete and recreate them
				// so if any of the IRule are Proxies we must first cause them to instantiate (read from datasource)
				// before wiping said data!
				forceProxy(rule);
			}
			RuleTDG.deleteByResource(a.getId());
			for (IRule rule : a.getRules()) {
				RuleOutputMapper.insert(a.getId(), rule);
			}
		} catch (XPathException e) {
			throw new MapperException(e);
		}
	}

	public void update(Rule a) throws MapperException {
		try{
			if(RuleTDG.update(a.getId(), a.getVersion(), a.getRuleType(), a.getName(), a.getTriggerTime()) == 0) {
				throw new LostUpdateException("Your version is out of date. No records were altered.");
			}
			for (IKeyword keyword : a.getKeywords())
				keyword.getValue(); // force proxies;
			KeywordTDG.deleteByObject(a.getId());
			for (IKeyword keyword : a.getKeywords())
				KeywordOutputMapper.insert(a.getId(), keyword);
			for (String id : a.getDynamicPropertyIds()) {
				Object value = a.getDynamicProperty(id);
				
				IRuleTypeInfo typeInfo = null;
				try {
					typeInfo = RuleTypeInfoFactory.getFactory().getTypeInfo(a.getRuleType());
					IArgumentInfo argInfo = typeInfo.getArgument(id);
					value = argInfo.serialize(value);
				} catch (RuleTypeInfoFactoryException e) {
					throw new MapperException(e);
				} catch (RuntimeException e) {
					throw new MapperException(e);
				}
				
				outputProperties(a);
			}
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
	
	private static void outputProperties(IRule a) throws MapperException
	{
		for (String id : a.getDynamicPropertyIds()) {
			Object value = a.getDynamicProperty(id);
			
			IRuleTypeInfo typeInfo = null;
			try {
				typeInfo = RuleTypeInfoFactory.getFactory().getTypeInfo(a.getRuleType());
				IArgumentInfo argInfo = typeInfo.getArgument(id);
				value = argInfo.serialize(value);
			} catch (RuleTypeInfoFactoryException e) {
				throw new MapperException(e);
			} catch (RuntimeException e) {
				throw new MapperException(e);
			}
			
			try {
				PropertyTDG.insert(a.getId(), id, value);
			} catch (XPathExpressionException e) {
				throw new MapperException(e);
			}
		}
	}
	private static void forceProxy(IRule rule) {
		rule.getKeywords().toString(); // force keywords to resolve as well
		for (IRule r : rule.getRules())
			forceProxy(r);
	}
}
