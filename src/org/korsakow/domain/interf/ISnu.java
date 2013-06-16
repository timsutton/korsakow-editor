package org.korsakow.domain.interf;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.korsakow.ide.lang.LanguageBundle;


public interface ISnu extends IResource
{
	public static enum BackgroundSoundMode
	{
		KEEP("keep",  LanguageBundle.getString("backgroundsoundmode.keep.label")),
		CLEAR("clear", LanguageBundle.getString("backgroundsoundmode.clear.label")),
		SET("set", ""),
		;
		
		private static final Map<String, BackgroundSoundMode> map = new HashMap<String, BackgroundSoundMode>();
		public static BackgroundSoundMode forId(String id) {
			if (map.isEmpty()) {
				for (BackgroundSoundMode mode : values())
					map.put(mode.getId(), mode);
			}
			if (!map.containsKey(id))
				throw new IllegalArgumentException("Invalid BackgroundSoundMode: " + id);
			return map.get(id);
		}
		private final String id;
		private final String display;
		BackgroundSoundMode(String id, String display) {
			this.id = id;
			this.display = display;
		}
		public String getId() {
			return id;
		}
		@Override
		public String toString() {
			return display;
		}
	}
	
	void setStarter(boolean starter);
	boolean getStarter();
	void setEnder(boolean terminal);
	boolean getEnder();
	
	void setMaxLinks(Long maxLinks);
	Long getMaxLinks();
	
	void setLooping(boolean looping);
	boolean getLooping();
	
	void setMainMedia(IMedia media);
	IMedia getMainMedia();

	void setBackgroundSoundMode(BackgroundSoundMode backgroundSoundMode);
	BackgroundSoundMode getBackgroundSoundMode();
	void setBackgroundSound(ISound sound);
	ISound getBackgroundSound();
	void setBackgroundSoundVolume(float volume);
	float getBackgroundSoundVolume();
	void setBackgroundSoundLooping(boolean looping);
	boolean getBackgroundSoundLooping();
	
	void setInterface(IInterface interf);
	IInterface getInterface();
	
	void setRules(List<IRule> rules);
	List<IRule> getRules();

	void setEvents(Collection<IEvent> events);
	Collection<IEvent> getEvents();
	
	void setLives(Long lives);
	Long getLives();
	
	void setPreviewMedia(IMedia media);
	IMedia getPreviewMedia();
	void setPreviewText(String text);
	String getPreviewText();
	
	String getInsertText();
	void setInsertText(String text);
	
	void setRating(float rating);
	float getRating();
	
	void setThumbnail(IImage image);
	IImage getThumbnail();
}
