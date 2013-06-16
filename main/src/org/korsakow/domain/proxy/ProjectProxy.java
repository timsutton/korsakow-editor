package org.korsakow.domain.proxy;

import java.awt.Color;
import java.util.Collection;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.Project;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ISettings;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.mapper.input.ProjectInputMapper;

public class ProjectProxy extends ResourceProxy<Project> implements IProject {

	public ProjectProxy(long id)
	{
		super(id);
	}

	@Override
	public Class<Project> getInnerClass()
	{
		return Project.class;
	}
	
	@Override
	protected Project getFromMapper(Long id) throws MapperException {
		return ProjectInputMapper.map(id);
	}
	@Override
	public String getName() {
		return getInnerObject().getName();
	}

	@Override
	public void setName(String name) {
		getInnerObject().setName(name);
	}
	
	public void setMovieWidth(int movieWidth)
	{
		getInnerObject().setMovieWidth(movieWidth);
	}
	public int getMovieWidth()
	{
		return getInnerObject().getMovieWidth();
	}
	public void setMovieHeight(int movieHeight)
	{
		getInnerObject().setMovieHeight(movieHeight);
	}
	public int getMovieHeight()
	{
		return getInnerObject().getMovieHeight();
	}

	public ISound getBackgroundSound()
	{
		return getInnerObject().getBackgroundSound();
	}
	public void setBackgroundSound(ISound backgroundSound)
	{
		getInnerObject().setBackgroundSound(backgroundSound);
	}
	public boolean getBackgroundSoundLooping()
	{
		return getInnerObject().getBackgroundSoundLooping();
	}
	public void setBackgroundSoundLooping(boolean looping)
	{
		getInnerObject().setBackgroundSoundLooping(looping);
	}
	public IImage getBackgroundImage()
	{
		return getInnerObject().getBackgroundImage();
	}
	public void setBackgroundImage(IImage backgroundImage)
	{
		getInnerObject().setBackgroundImage(backgroundImage);
	}
	public Color getBackgroundColor()
	{
		return getInnerObject().getBackgroundColor();
	}
	public void setBackgroundColor(Color backgroundColor)
	{
		getInnerObject().setBackgroundColor(backgroundColor);
	}
	
	public ISound getClickSound()
	{
		return getInnerObject().getClickSound();
	}
	public void setClickSound(ISound sound)
	{
		getInnerObject().setClickSound(sound);
	}

	public float getBackgroundSoundVolume() {
		return getInnerObject().getBackgroundSoundVolume();
	}

	public float getClickSoundVolume() {
		return getInnerObject().getClickSoundVolume();
	}

	public void setBackgroundSoundVolume(float backgroundSoundVolume) {
		getInnerObject().setBackgroundSoundVolume(backgroundSoundVolume);
	}

	public void setClickSoundVolume(float clickSoundVolume) {
		getInnerObject().setClickSoundVolume(clickSoundVolume);
	}
	public void setSplashScreenMedia(IMedia splashScreenMedia)
	{
		getInnerObject().setSplashScreenMedia(splashScreenMedia);
	}
	public IMedia getSplashScreenMedia()
	{
		return getInnerObject().getSplashScreenMedia();
	}
	public void setRandomLinkMode(boolean randomLinkMode)
	{
		getInnerObject().setRandomLinkMode(randomLinkMode);
	}
	public boolean getRandomLinkMode()
	{
		return getInnerObject().getRandomLinkMode();
	}
	public void setKeepLinksOnEmptySearch(boolean keepLinks)
	{
		getInnerObject().setKeepLinksOnEmptySearch(keepLinks);
	}
	public boolean getKeepLinksOnEmptySearch()
	{
		return getInnerObject().getKeepLinksOnEmptySearch();
	}
	public void setMaxLinks(Long maxLinks)
	{
		getInnerObject().setMaxLinks(maxLinks);
	}
	public Long getMaxLinks()
	{
		return getInnerObject().getMaxLinks();
	}
	public IInterface getDefaultInterface()
	{
		return getInnerObject().getDefaultInterface();
	}
	public void setDefaultInterface(IInterface interf)
	{
		getInnerObject().setDefaultInterface(interf);
	}

	public void setRules(List<IRule> rules)
	{
		getInnerObject().setRules(rules);
	}
	public List<IRule> getRules()
	{
		return getInnerObject().getRules();
	}
	public void setSettings(ISettings settings) {
		getInnerObject().setSettings(settings);
	}
	public ISettings getSettings()
	{
		return getInnerObject().getSettings();
	}
	public void setSnus(Collection<ISnu> snus) {
		getInnerObject().setSnus(snus);
	}
	public Collection<ISnu> getSnus() {
		return getInnerObject().getSnus();
	}
	public void setInterfaces(Collection<IInterface> interfaces) {
		getInnerObject().setInterfaces(interfaces);
	}
	public Collection<IInterface> getInterfaces() {
		return getInnerObject().getInterfaces();
	}
	public void setMedia(Collection<IMedia> media) {
		getInnerObject().setMedia(media);
	}
	public Collection<IMedia> getMedia() {
		return getInnerObject().getMedia();
	}
	public void setUUID(String uuid) {
		getInnerObject().setUUID(uuid);
	}
	public String getUUID() {
		return getInnerObject().getUUID();
	}
}
