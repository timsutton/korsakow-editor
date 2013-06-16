package org.korsakow.domain;

import java.util.Collection;
import java.util.List;

import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.interf.IEvent;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.interf.ISnu.BackgroundSoundMode;
import org.korsakow.ide.DataRegistry;

public class SnuFactory {
	public static Snu createNew(long id, long version)
	{
		Snu object = new Snu(id, version);
		UoW.getCurrent().registerNew(object);
		return object;
	}
	public static Snu createNew()
	{
		return createNew(DataRegistry.getMaxId(), 0);
	}

	public static Snu createNew(String name, Collection<IKeyword> keywords, IMedia mainMedia, float rating, ISound backgroundSound, BackgroundSoundMode backgroundSoundMode, float backgroundSoundVolume, boolean backgroundSoundLooping, IInterface interf, List<IRule> rules, Long lives, boolean looping, Long maxLinks, boolean isStarter, boolean isEnder, IMedia previewMedia, String previewText, String insertText, Collection<IEvent> events)
	{
		Snu object = new Snu(DataRegistry.getMaxId(), 0, name, keywords, mainMedia, rating, backgroundSound, backgroundSoundMode, backgroundSoundVolume, backgroundSoundLooping, interf, rules, lives, looping, maxLinks, isStarter, isEnder, previewMedia, previewText, insertText, events);
		UoW.getCurrent().registerNew(object);
		return object;
	}
	
	public static Snu createClean(long id, long version, String name, Collection<IKeyword> keywords, IMedia mainMedia, float rating, ISound backgroundSound, BackgroundSoundMode backgroundSoundMode, float backgroundSoundVolume, boolean backgroundSoundLooping, IInterface interf, List<IRule> rules, Long lives, boolean looping, Long maxLinks, boolean isStarter, boolean isEnder, IMedia previewMedia, String previewText, String insertText, Collection<IEvent> events)
	{
		Snu object = new Snu(id, version, name, keywords, mainMedia, rating, backgroundSound, backgroundSoundMode, backgroundSoundVolume, backgroundSoundLooping, interf, rules, lives, looping, maxLinks, isStarter, isEnder, previewMedia, previewText, insertText, events);
		UoW.getCurrent().registerClean(object);
		return object;
	}
	
	public static Snu copy(ISnu src)
	{
		IMedia originalMainMedia = src.getMainMedia();
		IMedia copyMainMedia = originalMainMedia!=null?CloneFactory.clone(originalMainMedia):null;
		Snu copy = createNew(
				src.getName(),
				KeywordFactory.copy(src.getKeywords()),
				copyMainMedia,
				src.getRating(),
				getSwap(originalMainMedia, copyMainMedia, src.getBackgroundSound()),
				src.getBackgroundSoundMode(),
				src.getBackgroundSoundVolume(),
				src.getBackgroundSoundLooping(),
				src.getInterface(),
				RuleFactory.copy(src.getRules()),
				src.getLives(),
				src.getLooping(),
				src.getMaxLinks(),
				src.getStarter(),
				src.getEnder(),
				getSwap(originalMainMedia, copyMainMedia, src.getPreviewMedia()),
				src.getPreviewText(),
				src.getInsertText(),
				EventFactory.copy(src.getEvents())
		);
		return copy;
	}
	/**
	 * Eg... when cloning we also clone the MainMedia due to how we represent the Snu and MainMedia as one object.
	 * That means that we have to check if any of the Snu's other media is the same media as the MainMedia and if so
	 * pass the clone the one clonedMainMedia, otherwise pass it the shared Media that we started with.
	 */
	private static <T extends IMedia> T getSwap(IMedia originalMainMedia, IMedia cloneMainMedia, T media)
	{
		if (media == null)
			return null;
		if (media.equals(originalMainMedia))
			return (T)cloneMainMedia;
		return media;
	}
}
