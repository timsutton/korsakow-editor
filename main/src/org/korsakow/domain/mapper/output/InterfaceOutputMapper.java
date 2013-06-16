package org.korsakow.domain.mapper.output;

import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.korsakow.domain.Interface;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.domain.mapper.exception.LostUpdateException;
import org.korsakow.services.tdg.InterfaceTDG;
import org.korsakow.services.tdg.KeywordTDG;
import org.korsakow.services.tdg.WidgetTDG;
import org.korsakow.services.util.ColorFactory;

public class InterfaceOutputMapper implements GenericOutputMapper<Long, Interface>{

	public void delete(Interface a) throws MapperException {
		try{
			if (0 == InterfaceTDG.delete(a.getId(), a.getVersion()))
				throw new MapperException(String.format("Record not found: id=%d, version=%d", a.getId(), a.getVersion()));
			KeywordTDG.deleteByObject(a.getId());
			WidgetTDG.deleteByInterface(a.getId()); // must be before ResourceRuleTDG.deleteByResource
//			InterfaceWidgetTDG.deleteByInterface(a.getId());
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}

	public void insert(Interface a) throws MapperException {
		try {
			final Long clickSoundId = a.getClickSound()!=null?a.getClickSound().getId():null;
			final Long backgroundImageId = a.getBackgroundImage()!=null?a.getBackgroundImage().getId():null;
			final String backgroundColor = a.getBackgroundColor()!=null?ColorFactory.toString(a.getBackgroundColor()):null;
			if(a.getId()==0){
				int result = InterfaceTDG.insert(a.getVersion(), a.getName(), a.getGridWidth(), a.getGridHeight(), a.getViewWidth(), a.getViewHeight(), clickSoundId, a.getClickSoundVolume(), backgroundImageId, backgroundColor);
				if (result == 0)
					throw new MapperException("insert failed!");
			} else {
				int result = InterfaceTDG.insert(a.getId(), a.getVersion(), a.getName(), a.getGridWidth(), a.getGridHeight(), a.getViewWidth(), a.getViewHeight(), clickSoundId, a.getClickSoundVolume(), backgroundImageId, backgroundColor);
				if (result == 0)
					throw new MapperException("insert failed!");
			}
			for (IKeyword keyword : a.getKeywords())
				KeywordOutputMapper.insert(a.getId(), keyword);
			for (IWidget widget : a.getWidgets())
				WidgetOutputMapper.insert(a.getId(), widget);
		} catch (XPathException e) {
			throw new MapperException(e);
		}
	}

	public void update(Interface a) throws MapperException {
		try{
			final Long clickSoundId = a.getClickSound()!=null?a.getClickSound().getId():null;
			final Long backgroundImageId = a.getBackgroundImage()!=null?a.getBackgroundImage().getId():null;
			final String backgroundColor = a.getBackgroundColor()!=null?ColorFactory.toString(a.getBackgroundColor()):null;
			if(InterfaceTDG.update(a.getId(), a.getVersion(), a.getName(), a.getGridWidth(), a.getGridHeight(), a.getViewWidth(), a.getViewHeight(), clickSoundId, a.getClickSoundVolume(), backgroundImageId, backgroundColor) == 0) {
				throw new LostUpdateException("Your version is out of date. No records were altered.");
			}
			for (IKeyword keyword : a.getKeywords())
				keyword.getValue(); // force proxies;
			KeywordTDG.deleteByObject(a.getId());
			for (IKeyword keyword : a.getKeywords())
				KeywordOutputMapper.insert(a.getId(), keyword);
			for (IWidget widget : a.getWidgets()) {
				// icky implementation detail: widget is a weak mapping. we're about to delete and recreate them
				// so if any of the IWidget are Proxies we must first cause them to instantiate (read from datasource)
				// before wiping said data!
				widget.getVersion();
			}
			WidgetTDG.deleteByInterface(a.getId());
			for (IWidget widget : a.getWidgets())
				WidgetOutputMapper.insert(a.getId(), widget);
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
}
