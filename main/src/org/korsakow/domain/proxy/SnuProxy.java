package org.korsakow.domain.proxy;

import java.util.Collection;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.Snu;
import org.korsakow.domain.interf.IEvent;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.mapper.input.SnuInputMapper;

public class SnuProxy extends ResourceProxy<Snu> implements ISnu {

	public SnuProxy(long id)
	{
		super(id);
	}

	@Override
	public Class<Snu> getInnerClass()
	{
		return Snu.class;
	}
	
	@Override
	protected Snu getFromMapper(Long id) throws MapperException {
		return SnuInputMapper.map(id);
	}

	public ISound getBackgroundSound() {
		return getInnerObject().getBackgroundSound();
	}
	public void setBackgroundSound(ISound sound) {
		getInnerObject().setBackgroundSound(sound);
		
	}
	public void setBackgroundSoundVolume(float volume)
	{
		getInnerObject().setBackgroundSoundVolume(volume);
	}
	public float getBackgroundSoundVolume()
	{
		return getInnerObject().getBackgroundSoundVolume();
	}
	public IMedia getMainMedia() {
		return getInnerObject().getMainMedia();
	}
	public void setMainMedia(IMedia media) {
		getInnerObject().setMainMedia(media);
		
	}
	public float getRating() {
		return getInnerObject().getRating();
	}
	public void setRating(float rating)
	{
		getInnerObject().setRating(rating);
	}
	public IInterface getInterface()
	{
		return getInnerObject().getInterface();
	}
	public void setInterface(IInterface interf)
	{
		getInnerObject().setInterface(interf);
	}
	public void setRules(List<IRule> rules)
	{
		getInnerObject().setRules(rules);
	}
	public List<IRule> getRules()
	{
		return getInnerObject().getRules();
	}
	public void setEvents(Collection<IEvent> events)
	{
		getInnerObject().setEvents(events);
	}
	public Collection<IEvent> getEvents()
	{
		return getInnerObject().getEvents();
	}
	public void setLives(Long lives) {
		getInnerObject().setLives(lives);
	}
	public Long getLives() {
		return getInnerObject().getLives();
	}
	public void setLooping(boolean looping)
	{
		getInnerObject().setLooping(looping);
	}
	public boolean getLooping()
	{
		return getInnerObject().getLooping();
	}
	public void setMaxLinks(Long maxLinks)
	{
		getInnerObject().setMaxLinks(maxLinks);
	}
	public Long getMaxLinks()
	{
		return getInnerObject().getMaxLinks();
	}
	public void setEnder(boolean ender)
	{
		getInnerObject().setEnder(ender);
	}
	public boolean getEnder()
	{
		return getInnerObject().getEnder();
	}
	public void setStarter(boolean starter)
	{
		getInnerObject().setStarter(starter);
	}
	public boolean getStarter()
	{
		return getInnerObject().getStarter();
	}
	public void setPreviewMedia(IMedia previewMedia)
	{
		getInnerObject().setPreviewMedia(previewMedia);
	}
	public IMedia getPreviewMedia()
	{
		return getInnerObject().getPreviewMedia();
	}
	public void setPreviewText(String text)
	{
		getInnerObject().setPreviewText(text);
	}
	public String getPreviewText()
	{
		return getInnerObject().getPreviewText();
	}
	public String getInsertText()
	{
		return getInnerObject().getInsertText();
	}
	public void setInsertText(String text)
	{
		getInnerObject().setInsertText(text);
	}
	public IImage getThumbnail()
	{
		return getInnerObject().getThumbnail();
	}
	public void setThumbnail(IImage thumbnail)
	{
		getInnerObject().setThumbnail(thumbnail);
	}

	public BackgroundSoundMode getBackgroundSoundMode()
	{
		return getInnerObject().getBackgroundSoundMode();
	}

	public void setBackgroundSoundMode(BackgroundSoundMode backgroundSoundMode)
	{
		getInnerObject().setBackgroundSoundMode(backgroundSoundMode);
	}

	@Override
	public boolean getBackgroundSoundLooping()
	{
		return getInnerObject().getBackgroundSoundLooping();
	}

	@Override
	public void setBackgroundSoundLooping(boolean looping)
	{
		getInnerObject().setBackgroundSoundLooping(looping);
	}
}
