package org.korsakow.domain.mapper.output;

import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.korsakow.domain.Widget;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.domain.mapper.exception.LostUpdateException;
import org.korsakow.services.tdg.KeywordTDG;
import org.korsakow.services.tdg.PropertyTDG;
import org.korsakow.services.tdg.WidgetTDG;

public class WidgetOutputMapper implements GenericOutputMapper<Long, Widget>{

	public void delete(Widget a) throws MapperException {
		try{
			if (0 == WidgetTDG.delete(a.getId(), a.getVersion()))
				throw new MapperException(String.format("Record not found: id=%d, version=%d", a.getId(), a.getVersion()));
			KeywordTDG.deleteByObject(a.getId());
//			InterfaceWidgetTDG.deleteByWidget(a.getId());
			//PropertyTDG.deleteByObject(a.getId());
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}

	public void insert(Widget a) throws MapperException {
	
	}
	public static void insert(long interface_id, IWidget a) throws MapperException {
		try {
			if(a.getId()==0){
				WidgetTDG.insert(interface_id, a.getVersion(), a.getName(), a.getWidgetId(), a.getPersistCondition().getId(), a.getPersistAction().getId(), a.getX(), a.getY(), a.getWidth(), a.getHeight());
			} else {
				WidgetTDG.insert(interface_id, a.getId(), a.getVersion(), a.getName(), a.getWidgetId(), a.getPersistCondition().getId(), a.getPersistAction().getId(),a.getX(), a.getY(), a.getWidth(), a.getHeight());
			}
			for (IKeyword keyword : a.getKeywords())
				KeywordOutputMapper.insert(a.getId(), keyword);
			for (String id : a.getDynamicPropertyIds())
				PropertyTDG.insert(a.getId(), id, a.getDynamicProperty(id));
		} catch (XPathException e) {
			throw new MapperException(e);
		}
	}

	public void update(Widget a) throws MapperException {
		try{
			if(WidgetTDG.update(a.getId(), a.getVersion(), a.getName(), a.getWidgetId(), a.getPersistCondition().getId(), a.getPersistAction().getId(),a.getX(), a.getY(), a.getWidth(), a.getHeight()) == 0) {
				throw new LostUpdateException("Your version is out of date. No records were altered. id="+a.getId()+",version="+a.getVersion());
			}
			for (IKeyword keyword : a.getKeywords())
				keyword.getValue(); // force proxies;
			KeywordTDG.deleteByObject(a.getId());
			for (IKeyword keyword : a.getKeywords())
				KeywordOutputMapper.insert(a.getId(), keyword);
			for (String id : a.getDynamicPropertyIds())
				PropertyTDG.insert(a.getId(), id, a.getDynamicProperty(id));
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}	
	}
}
