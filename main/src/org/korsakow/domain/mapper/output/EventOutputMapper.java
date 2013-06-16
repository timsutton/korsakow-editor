package org.korsakow.domain.mapper.output;

import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.korsakow.domain.Event;
import org.korsakow.domain.interf.IEvent;
import org.korsakow.domain.mapper.exception.LostUpdateException;
import org.korsakow.services.tdg.EventTDG;
import org.korsakow.services.tdg.PredicateTDG;
import org.korsakow.services.tdg.PropertyTDG;
import org.korsakow.services.tdg.RuleTDG;
import org.korsakow.services.tdg.TriggerTDG;

public class EventOutputMapper implements GenericOutputMapper<Long, Event>{

	public void delete(Event a) throws MapperException {
		try{
			EventTDG.delete(a.getId(), a.getVersion());
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}

	public void insert(Event a) throws MapperException {
	}
	
	public static void insert(long object_id, IEvent a) throws MapperException {
		try {
			if(a.getId()==0){
				EventTDG.insert(object_id, a.getVersion());
			} else {
				EventTDG.insert(object_id, a.getId(), a.getVersion());
			}
			for (String id : a.getDynamicPropertyIds())
				PropertyTDG.insert(a.getId(), id, a.getDynamicProperty(id));
			
			TriggerTDG.deleteByResource(a.getId());
			PredicateTDG.deleteByResource(a.getId());
			RuleTDG.deleteByResource(a.getId());
			
			TriggerOutputMapper.insert(a.getId(), a.getTrigger());
			PredicateOutputMapper.insert(a.getId(), a.getPredicate());
			RuleOutputMapper.insert(a.getId(), a.getRule());
			
		} catch (XPathException e) {
			throw new MapperException(e);
		}
	}

	public void update(Event a) throws MapperException {
		try{
			if(EventTDG.update(a.getId(), a.getVersion()) == 0) {
				throw new LostUpdateException("Your version is out of date. No records were altered.");
			}
			for (String id : a.getDynamicPropertyIds())
				PropertyTDG.insert(a.getId(), id, a.getDynamicProperty(id));
			
			TriggerTDG.deleteByResource(a.getId());
			PredicateTDG.deleteByResource(a.getId());
			RuleTDG.deleteByResource(a.getId());
			
			TriggerOutputMapper.insert(a.getId(), a.getTrigger());
			PredicateOutputMapper.insert(a.getId(), a.getPredicate());
			RuleOutputMapper.insert(a.getId(), a.getRule());
			
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
}
