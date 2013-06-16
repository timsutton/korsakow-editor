package org.korsakow.domain.mapper.output;

import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.korsakow.domain.Snu;
import org.korsakow.domain.interf.IEvent;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.mapper.exception.LostUpdateException;
import org.korsakow.services.tdg.EventTDG;
import org.korsakow.services.tdg.KeywordTDG;
import org.korsakow.services.tdg.RuleTDG;
import org.korsakow.services.tdg.SnuTDG;

public class SnuOutputMapper implements GenericOutputMapper<Long, Snu>{

	public void delete(Snu a) throws MapperException {
		try{
			if (0 == SnuTDG.delete(a.getId(), a.getVersion()))
				throw new MapperException(String.format("Record not found: id=%d, version=%d", a.getId(), a.getVersion()));
			KeywordTDG.deleteByObject(a.getId());
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}

	public void insert(Snu a) throws MapperException {
		try {
			Long backgroundSoundId = a.getBackgroundSound()!=null?a.getBackgroundSound().getId():null;
			Long mainMediaId = a.getMainMedia()!=null?a.getMainMedia().getId():null;
			Long interfaceId = a.getInterface()!=null?a.getInterface().getId():null;
			Long previewMediaId = a.getPreviewMedia()!=null?a.getPreviewMedia().getId():null;
			if(a.getId()==0){
				SnuTDG.insert(a.getVersion(), a.getName(), mainMediaId, a.getRating(), backgroundSoundId, a.getBackgroundSoundMode().getId(), a.getBackgroundSoundVolume(), a.getBackgroundSoundLooping(), interfaceId, a.getLives(), a.getLooping(), a.getMaxLinks(), a.getStarter(), a.getEnder(), previewMediaId, a.getPreviewText(), a.getInsertText());
			} else {
				SnuTDG.insert(a.getId(), a.getVersion(), a.getName(), mainMediaId, a.getRating(), backgroundSoundId, a.getBackgroundSoundMode().getId(), a.getBackgroundSoundVolume(), a.getBackgroundSoundLooping(), interfaceId, a.getLives(), a.getLooping(), a.getMaxLinks(), a.getStarter(), a.getEnder(), previewMediaId, a.getPreviewText(), a.getInsertText());
			}
			for (IKeyword keyword : a.getKeywords())
				KeywordOutputMapper.insert(a.getId(), keyword);
			for (IRule rule : a.getRules()) {
				RuleOutputMapper.insert(a.getId(), rule);
			}
			for (IEvent event : a.getEvents()) {
				EventOutputMapper.insert(a.getId(), event);
			}
		} catch (XPathException e) {
			throw new MapperException(e);
		}
	}

	public void update(Snu a) throws MapperException {
		try{
			Long backgroundSoundId = a.getBackgroundSound()!=null?a.getBackgroundSound().getId():null;
			Long mainMediaId = a.getMainMedia()!=null?a.getMainMedia().getId():null;
			Long interfaceId = a.getInterface()!=null?a.getInterface().getId():null;
			Long previewMediaId = a.getPreviewMedia()!=null?a.getPreviewMedia().getId():null;
			if(SnuTDG.update(a.getId(), a.getVersion(), a.getName(), mainMediaId, a.getRating(), backgroundSoundId, a.getBackgroundSoundMode().getId(), a.getBackgroundSoundVolume(), a.getBackgroundSoundLooping(), interfaceId, a.getLives(), a.getLooping(), a.getMaxLinks(), a.getStarter(), a.getEnder(), previewMediaId, a.getPreviewText(), a.getInsertText()) == 0) {
				throw new LostUpdateException("Your version is out of date. No records were altered.");
			}
			for (IKeyword keyword : a.getKeywords())
				keyword.getValue(); // force proxies;
			KeywordTDG.deleteByObject(a.getId());
			for (IKeyword keyword : a.getKeywords())
				KeywordOutputMapper.insert(a.getId(), keyword);
			for (IRule rule : a.getRules()) {
				// icky implementation detail: rule is a weak mapping. we're about to delete and recreate them
				// so if any of the IRule are Proxies we must first cause them to instantiate (read from datasource)
				// before wiping said data!
//				rule.getVersion();
				forceProxy(rule);
			}
			RuleTDG.deleteByResource(a.getId());
			for (IRule rule : a.getRules()) {
				RuleOutputMapper.insert(a.getId(), rule);
			}
			for (IEvent event : a.getEvents()) {
				// icky implementation detail: event is a weak mapping. we're about to delete and recreate them
				// so if any of the IEvent are Proxies we must first cause them to instantiate (read from datasource)
				// before wiping said data!
				event.getVersion();
			}
			EventTDG.deleteByResource(a.getId());
			for (IEvent event : a.getEvents()) {
				EventOutputMapper.insert(a.getId(), event);
			}
		} catch (XPathException e) {
			throw new MapperException(e);
		}
	}
	private void forceProxy(IRule rule) {
		rule.getKeywords().toString(); // force keywords to resolve as well
		for (IRule r : rule.getRules())
			forceProxy(r);
	}
}
