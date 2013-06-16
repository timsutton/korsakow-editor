package org.korsakow.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.korsakow.domain.interf.IEvent;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.interf.ISound;
import org.korsakow.ide.resources.ResourceType;

public class Snu extends Resource implements ISnu
{
	public static final BackgroundSoundMode DEFAULT_BACKGROUNDSOUNDMODE = BackgroundSoundMode.KEEP;
	
	private IMedia mainMedia;
	private ISound backgroundSound;
	private float backgroundSoundVolume = 1;
	private BackgroundSoundMode backgroundSoundMode = BackgroundSoundMode.KEEP;
	private boolean backgroundSoundLooping;
	private IInterface interf;
	private Long lives;
	private List<IRule> rules = Collections.EMPTY_LIST;
	private Collection<IEvent> events = Collections.EMPTY_LIST;
	private boolean looping;
	private Long maxLinks;
	private boolean isEnder = false;
	private boolean isStarter = false;
	private IMedia previewMedia;
	private String previewText;
	private String insertText;
	private float rating = 1f;
	private IImage thumbnail;
	
	
	Snu(long id, long version)
	{
		super(id, version);
	}
	Snu(long id, long version, String name, Collection<IKeyword> keywords, IMedia mainMedia, float rating, ISound backgroundSound, BackgroundSoundMode backgroundSoundMode, float backgroundSoundVolume, boolean backgroundSoundLooping, IInterface interf, List<IRule> rules, Long lives, boolean looping, Long maxLinks, boolean isStarter, boolean isEnder, IMedia previewMedia, String previewText, String insertText, Collection<IEvent> events)
	{
		super(id, version, name, keywords);
		setBackgroundSound(backgroundSound);
		setBackgroundSoundMode(backgroundSoundMode);
		setBackgroundSoundVolume(backgroundSoundVolume);
		setBackgroundSoundLooping(backgroundSoundLooping);
		setRating(rating);
		setMainMedia(mainMedia);
		setInterface(interf);
		setLives(lives);
		setRules(rules);
		setLooping(looping);
		setMaxLinks(maxLinks);
		setInterface(interf);
		setStarter(isStarter);
		setEnder(isEnder);
		setPreviewMedia(previewMedia);
		setPreviewText(previewText);
		setInsertText(insertText);
		setEvents(events);
	}
	public String getType()
	{
		return ResourceType.SNU.getTypeId();
	}
	public void setMainMedia(IMedia media)
	{
		mainMedia = media;
	}
	public IMedia getMainMedia()
	{
		return mainMedia;
	}
	public void setRating(float rating)
	{
		this.rating = rating;
	}
	public float getRating()
	{
		return rating;
	}
	public void setInterface(IInterface interf)
	{
		this.interf = interf;
	}
	public IInterface getInterface()
	{
		return interf;
	}
	public void setBackgroundSound(ISound sound)
	{
		backgroundSound = sound;
	}
	public ISound getBackgroundSound()
	{
		return backgroundSound;
	}
	public void setLives(Long lives)
	{
		this.lives = lives;
	}
	public Long getLives()
	{
		return lives;
	}
	public List<IRule> getRules() {
		return rules;
	}
	public void setRules(List<IRule> rules) {
		this.rules = rules;
	}
	public Collection<IEvent> getEvents() {
		return events;
	}
	public void setEvents(Collection<IEvent> events) {
		this.events = events;
	}
	public boolean getLooping()
	{
		return looping;
	}
	public void setLooping(boolean looping)
	{
		this.looping = looping;
	}
	public Long getMaxLinks()
	{
		return maxLinks;
	}
	public void setMaxLinks(Long maxLinks)
	{
		this.maxLinks = maxLinks;
	}
	public boolean getEnder()
	{
		return isEnder;
	}
	public void setEnder(boolean ender)
	{
		isEnder = ender;
	}
	public boolean getStarter()
	{
		return isStarter;
	}
	public void setStarter(boolean starter)
	{
		isStarter = starter;
	}
	public void setBackgroundSoundVolume(float volume)
	{
		backgroundSoundVolume = volume;
	}
	public float getBackgroundSoundVolume()
	{
		return backgroundSoundVolume;
	}
	public void setPreviewMedia(IMedia previewMedia)
	{
		this.previewMedia = previewMedia;
	}
	public IMedia getPreviewMedia()
	{
		return previewMedia;
	}
	public void setPreviewText(String text)
	{
		previewText = text;
	}
	public String getPreviewText()
	{
		return previewText;
	}
	public String getInsertText()
	{
		return insertText;
	}
	public void setInsertText(String text)
	{
		insertText = text;
	}
	public void setThumbnail(IImage image)
	{
		thumbnail = image;
	}
	public IImage getThumbnail()
	{
		return thumbnail;
	}
	
	public BackgroundSoundMode getBackgroundSoundMode()
	{
		return backgroundSoundMode;
	}
	public void setBackgroundSoundMode(BackgroundSoundMode backgroundSoundMode)
	{
		this.backgroundSoundMode = backgroundSoundMode;
	}
	public boolean getBackgroundSoundLooping()
	{
		return backgroundSoundLooping;
	}
	public void setBackgroundSoundLooping(boolean backgroundSoundLooping)
	{
		this.backgroundSoundLooping = backgroundSoundLooping;
	}
}
