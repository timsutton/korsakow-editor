package org.korsakow.domain;

import java.awt.Color;
import java.util.Collection;
import java.util.List;

import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ISettings;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.interf.ISound;
import org.korsakow.ide.DataRegistry;

public class ProjectFactory {
	public static Project createNew(long id, long version)
	{
		Project object = new Project(id, version, SettingsFactory.createNew());
		UoW.getCurrent().registerNew(object);
		return object;
	}
	public static Project createNew()
	{
		return createNew(DataRegistry.getMaxId(), 0);
	}
	public static Project createNew(
			String name, Collection<IKeyword> keywords, 
			int movieWidth, int movieHeight, 
			ISound backgroundSound, float backgroundSoundVolume, boolean backgroundSoundLooping,
			ISound clickSound, float clickSoundVolume, 
			IImage backgroundImage, Color backgroundColor, 
			IMedia splashScreenMedia, 
			boolean randomLinkMode, boolean keepLinksOnEmptySearch, 
			Long maxLinks, 
			IInterface defaultInterface,
			List<IRule> rules, 
			Collection<ISnu> snus, 
			Collection<IInterface> interfaces, 
			Collection<IMedia> media, 
			ISettings settings, 
			String uuid)
	{
		Project object = new Project(
				DataRegistry.getMaxId(), 1L,
				name, keywords, 
				movieWidth, movieHeight, 
				backgroundSound, backgroundSoundVolume, backgroundSoundLooping,
				clickSound, clickSoundVolume, 
				backgroundImage, backgroundColor, 
				splashScreenMedia, 
				randomLinkMode, 
				keepLinksOnEmptySearch, maxLinks, 
				defaultInterface,
				rules, 
				snus, 
				interfaces, 
				media, 
				settings, 
				uuid);
		UoW.getCurrent().registerNew(object);
		return object;
	}
	public static Project createClean(
			long id, long version,
			String name, Collection<IKeyword> keywords, 
			int movieWidth, int movieHeight, 
			ISound backgroundSound, float backgroundSoundVolume, boolean backgroundSoundLooping,
			ISound clickSound, float clickSoundVolume, 
			IImage backgroundImage, Color backgroundColor, 
			IMedia splashScreenMedia, 
			boolean randomLinkMode, boolean keepLinksOnEmptySearch, 
			Long maxLinks, 
			IInterface defaultInterface,
			List<IRule> rules, 
			Collection<ISnu> snus, 
			Collection<IInterface> interfaces, 
			Collection<IMedia> media, 
			ISettings settings, 
			String uuid)
	{
		Project object = new Project(
				id, version,
				name, keywords, 
				movieWidth, movieHeight, 
				backgroundSound, backgroundSoundVolume, backgroundSoundLooping,
				clickSound, clickSoundVolume, 
				backgroundImage, backgroundColor, 
				splashScreenMedia, 
				randomLinkMode, 
				keepLinksOnEmptySearch, maxLinks, 
				defaultInterface,
				rules, 
				snus, 
				interfaces, 
				media, 
				settings, 
				uuid);
		UoW.getCurrent().registerClean(object);
		return object;
	}
}
