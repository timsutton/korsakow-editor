package org.korsakow.domain.mapper.output;

import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.korsakow.domain.Predicate;
import org.korsakow.domain.interf.IPredicate;
import org.korsakow.domain.mapper.exception.LostUpdateException;
import org.korsakow.services.plugin.predicate.IArgumentInfo;
import org.korsakow.services.plugin.predicate.IPredicateTypeInfo;
import org.korsakow.services.plugin.predicate.PredicateTypeInfoFactory;
import org.korsakow.services.plugin.predicate.PredicateTypeInfoFactoryException;
import org.korsakow.services.tdg.PredicateTDG;
import org.korsakow.services.tdg.PropertyTDG;

public class PredicateOutputMapper implements GenericOutputMapper<Long, Predicate>{

	public void delete(Predicate a) throws MapperException {
		try{
			PredicateTDG.delete(a.getId(), a.getVersion());
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}

	public void insert(Predicate a) throws MapperException {
	}
	
	public static void insert(long object_id, IPredicate a) throws MapperException {
		try {
			if(a.getId()==0){
				PredicateTDG.insert(object_id, a.getVersion(), a.getPredicateType());
			} else {
				PredicateTDG.insert(object_id, a.getId(), a.getVersion(), a.getPredicateType());
			}
			outputProperties(a);
			for (IPredicate predicate : a.getPredicates()) {
				// icky implementation detail: rule is a weak mapping. we're about to delete and recreate them
				// so if any of the IPredicate are Proxies we must first cause them to instantiate (read from datasource)
				// before wiping said data!
				predicate.getVersion();
			}
			PredicateTDG.deleteByResource(a.getId());
			for (IPredicate predicate : a.getPredicates()) {
				PredicateOutputMapper.insert(a.getId(), predicate);
			}
		} catch (XPathException e) {
			throw new MapperException(e);
		}
	}

	public void update(Predicate a) throws MapperException {
		try{
			if(PredicateTDG.update(a.getId(), a.getVersion(), a.getPredicateType()) == 0) {
				throw new LostUpdateException("Your version is out of date. No records were altered.");
			}
			
			outputProperties(a);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
	
	private static void outputProperties(IPredicate a) throws MapperException
	{
		for (String id : a.getDynamicPropertyIds()) {
			Object value = a.getDynamicProperty(id);
			
			IPredicateTypeInfo typeInfo = null;
			try {
				typeInfo = PredicateTypeInfoFactory.getFactory().getTypeInfo(a.getPredicateType());
				IArgumentInfo argInfo = typeInfo.getArgument(id);
				value = argInfo.serialize(value);
			} catch (PredicateTypeInfoFactoryException e) {
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
}
