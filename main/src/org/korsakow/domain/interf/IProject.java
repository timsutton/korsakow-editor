package org.korsakow.domain.interf;

import java.awt.Color;
import java.util.Collection;
import java.util.List;


public interface IProject extends IResource
{
	void setUUID(String uuid);
	String getUUID();
	void setMovieWidth(int width);
	int getMovieWidth();
	void setMovieHeight(int height);
	int getMovieHeight();
	void setBackgroundSound(ISound sound);
	ISound getBackgroundSound();
	float getBackgroundSoundVolume();
	void setBackgroundSoundVolume(float backgroundSoundVolume);
	void setBackgroundSoundLooping(boolean looping);
	boolean getBackgroundSoundLooping();
	void setBackgroundImage(IImage image);
	IImage getBackgroundImage();
	public Color getBackgroundColor();
	
	void setSplashScreenMedia(IMedia media);
	IMedia getSplashScreenMedia();

	void setRandomLinkMode(boolean randomLinkMode);
	boolean getRandomLinkMode();
	
	void setKeepLinksOnEmptySearch(boolean keepLinks);
	boolean getKeepLinksOnEmptySearch();
	
	void setMaxLinks(Long maxLinks);
	Long getMaxLinks();
	
	void setClickSound(ISound sound);
	ISound getClickSound();
	float getClickSoundVolume();
	void setClickSoundVolume(float clickSoundVolume);
	
	IInterface getDefaultInterface();
	void setDefaultInterface(IInterface interf);
	
	void setRules(List<IRule> rules);
	List<IRule> getRules();
	
	Collection<ISnu> getSnus();
	void setSnus(Collection<ISnu> snus);
	Collection<IMedia> getMedia();
	void setMedia(Collection<IMedia> media);
	Collection<IInterface> getInterfaces();
	void setInterfaces(Collection<IInterface> interfaces);
}
