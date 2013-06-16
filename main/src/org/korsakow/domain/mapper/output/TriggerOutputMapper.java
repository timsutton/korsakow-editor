package org.korsakow.domain.mapper.output;

import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.korsakow.domain.Trigger;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IPredicate;
import org.korsakow.domain.interf.ITrigger;
import org.korsakow.domain.mapper.exception.LostUpdateException;
import org.korsakow.services.tdg.KeywordTDG;
import org.korsakow.services.tdg.PredicateTDG;
import org.korsakow.services.tdg.PropertyTDG;
import org.korsakow.services.tdg.TriggerTDG;

public class TriggerOutputMapper implements GenericOutputMapper<Long, Trigger>{

	public void delete(Trigger a) throws MapperException {
		try{
			TriggerTDG.delete(a.getId(), a.getVersion());
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}

	public void insert(Trigger a) throws MapperException {
	}
	
	public static void insert(long object_id, ITrigger a) throws MapperException {
		try {
			if(a.getId()==0){
				TriggerTDG.insert(object_id, a.getVersion(), a.getTriggerType());
			} else {
				TriggerTDG.insert(object_id, a.getId(), a.getVersion(), a.getTriggerType());
			}
			for (String id : a.getDynamicPropertyIds())
				PropertyTDG.insert(a.getId(), id, a.getDynamicProperty(id));
		} catch (XPathException e) {
			throw new MapperException(e);
		}
	}

	public void update(Trigger a) throws MapperException {
		try{
			if(TriggerTDG.update(a.getId(), a.getVersion(), a.getTriggerType()) == 0) {
				throw new LostUpdateException("Your version is out of date. No records were altered.");
			}
			for (String id : a.getDynamicPropertyIds())
				PropertyTDG.insert(a.getId(), id, a.getDynamicProperty(id));
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
}
