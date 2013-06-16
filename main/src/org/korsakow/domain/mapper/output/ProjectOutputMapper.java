package org.korsakow.domain.mapper.output;

import javax.xml.xpath.XPathException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.mapper.GenericOutputMapper;
import org.korsakow.domain.Project;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.mapper.exception.LostUpdateException;
import org.korsakow.services.tdg.KeywordTDG;
import org.korsakow.services.tdg.ProjectTDG;
import org.korsakow.services.tdg.RuleTDG;
import org.korsakow.services.util.ColorFactory;

public class ProjectOutputMapper implements GenericOutputMapper<Long, Project>{

	public void delete(Project a) throws MapperException {
		try{
			if (0 == ProjectTDG.delete(a.getId(), a.getVersion()))
				throw new MapperException(String.format("Record not found: id=%d, version=%d", a.getId(), a.getVersion()));
			KeywordTDG.deleteByObject(a.getId());
		} catch (XPathException e) {
			throw new MapperException(e);
		}
	}

	public void insert(Project a) throws MapperException {
		try {
			Long settingsId = a.getSettings()!=null?a.getSettings().getId():null;
			Long backgroundSoundId = a.getBackgroundSound()!=null?a.getBackgroundSound().getId():null;
			Long backgroundImageId = a.getBackgroundImage()!=null?a.getBackgroundImage().getId():null;
			Long clickSoundId = a.getClickSound()!=null?a.getClickSound().getId():null;
			Long splashScreenMediaId = a.getSplashScreenMedia()!=null?a.getSplashScreenMedia().getId():null;
			Long defaultInterfaceId = a.getDefaultInterface()!=null?a.getDefaultInterface().getId():null;
			ProjectTDG.insert(a.getId(), a.getVersion(), a.getName(), settingsId, a.getMovieWidth(), a.getMovieHeight(),
					backgroundSoundId, a.getBackgroundSoundVolume(), a.getBackgroundSoundLooping(),
					clickSoundId, a.getClickSoundVolume(),
					backgroundImageId, a.getBackgroundColor()!=null?ColorFactory.toString(a.getBackgroundColor()):null,
					splashScreenMediaId,
					a.getRandomLinkMode(), a.getKeepLinksOnEmptySearch(),
					a.getMaxLinks(),
					defaultInterfaceId,
					a.getUUID());
			KeywordTDG.deleteByObject(a.getId());
			for (IKeyword keyword : a.getKeywords())
				KeywordOutputMapper.insert(a.getId(), keyword);
			for (IRule rule : a.getRules()) {
				RuleOutputMapper.insert(a.getId(), rule);
			}
		} catch (XPathException e) {
			throw new MapperException(e);
		}
	}

	public void update(Project a) throws MapperException {
		try{
			Long settingsId = a.getSettings()!=null?a.getSettings().getId():null;
			Long backgroundSoundId = a.getBackgroundSound()!=null?a.getBackgroundSound().getId():null;
			Long backgroundImageId = a.getBackgroundImage()!=null?a.getBackgroundImage().getId():null;
			Long clickSoundId = a.getClickSound()!=null?a.getClickSound().getId():null;
			Long splashScreenMediaId = a.getSplashScreenMedia()!=null?a.getSplashScreenMedia().getId():null;
			Long defaultInterfaceId = a.getDefaultInterface()!=null?a.getDefaultInterface().getId():null;
			if(ProjectTDG.update(a.getId(), a.getVersion(), a.getName(), settingsId, a.getMovieWidth(), a.getMovieHeight(),
					backgroundSoundId, a.getBackgroundSoundVolume(), a.getBackgroundSoundLooping(),
					clickSoundId, a.getClickSoundVolume(),
					backgroundImageId, a.getBackgroundColor()!=null?ColorFactory.toString(a.getBackgroundColor()):null,
							splashScreenMediaId,
					a.getRandomLinkMode(), a.getKeepLinksOnEmptySearch(),
					a.getMaxLinks(),
					defaultInterfaceId,
					a.getUUID()) == 0) {
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
				rule.getVersion();
			}
			RuleTDG.deleteByResource(a.getId());
			for (IRule rule : a.getRules()) {
				RuleOutputMapper.insert(a.getId(), rule);
			}
		} catch (XPathException e) {
			throw new MapperException(e);
		}	
	}
}
