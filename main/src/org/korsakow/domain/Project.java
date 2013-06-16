package org.korsakow.domain;


import java.awt.Color;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ISettings;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.interf.ISound;
import org.korsakow.ide.resources.ResourceType;

public class Project extends Resource implements IProject
{
	private String uuid;
	private String name;
	private int movieWidth;
	private int movieHeight;
	private ISound backgroundSound;
	private float backgroundSoundVolume = 1.0f;
	private boolean backgroundSoundLooping;
	private ISound clickSound;
	private float clickSoundVolume = 1.0f;
	private IImage backgroundImage;
	private Color backgroundColor;
	private IMedia splashScreenMedia;
	private boolean randomLinkMode;
	private boolean keepLinksOnEmptySearch;
	private Long maxLinks;
	private IInterface defaultInterface;
	private List<IRule> rules = Collections.emptyList();
	private Collection<ISnu> snus = Collections.emptySet();
	private Collection<IInterface> interfaces = Collections.emptySet();
	private Collection<IMedia> media = Collections.emptySet();
	private ISettings settings;
	
	Project(long id, long version, ISettings settings)
	{
		super(id, version);
		this.settings = settings;
	}
	Project(long id, long version, String name, Collection<IKeyword> keywords, int movieWidth, int movieHeight, ISound backgroundSound, float backgroundSoundVolume, boolean backgroundSoundLooping, ISound clickSound, float clickSoundVolume, IImage backgroundImage, Color backgroundColor, IMedia splashScreenMedia, boolean randomLinkMode, boolean keepLinksOnEmptySearch, Long maxLinks, IInterface defaultInterface, List<IRule> rules, Collection<ISnu> snus, Collection<IInterface> interfaces, Collection<IMedia> media, ISettings settings, String uuid)
	{
		super(id, version, name, keywords);
		this.movieWidth = movieWidth;
		this.movieHeight = movieHeight;
		this.backgroundSound = backgroundSound;
		this.backgroundSoundVolume = backgroundSoundVolume;
		this.backgroundSoundLooping = backgroundSoundLooping;
		this.clickSound = clickSound;
		this.clickSoundVolume = clickSoundVolume;
		this.backgroundImage = backgroundImage;
		this.backgroundColor = backgroundColor;
		this.splashScreenMedia = splashScreenMedia;
		this.randomLinkMode = randomLinkMode;
		this.keepLinksOnEmptySearch = keepLinksOnEmptySearch;
		this.maxLinks = maxLinks;
		this.defaultInterface = defaultInterface;
		this.rules = rules;
		this.snus = snus;
		this.interfaces = interfaces;
		this.media = media;
		this.settings = settings;
		this.uuid = uuid;
	}
	public String getType()
	{
		return ResourceType.PROJECT.getTypeId();
	}
	@Override
	public void setName(String name)
	{
		this.name = name;
	}
	@Override
	public String getName()
	{
		return name;
	}
	public void setMovieWidth(int width)
	{
		movieWidth = width;
	}
	public int getMovieWidth()
	{
		return movieWidth;
	}
	public void setMovieHeight(int height)
	{
		movieHeight = height;
	}
	public int getMovieHeight()
	{
		return movieHeight;
	}
	public void setBackgroundSound(ISound sound)
	{
		backgroundSound = sound;
	}
	public ISound getBackgroundSound()
	{
		return backgroundSound;
	}
	public void setClickSound(ISound sound)
	{
		clickSound = sound;
	}
	public ISound getClickSound()
	{
		return clickSound;
	}
	public float getBackgroundSoundVolume() {
		return backgroundSoundVolume;
	}
	public float getClickSoundVolume() {
		return clickSoundVolume;
	}
	public void setBackgroundSoundVolume(float backgroundSoundVolume) {
		this.backgroundSoundVolume = backgroundSoundVolume;
	}
	public void setClickSoundVolume(float clickSoundVolume) {
		this.clickSoundVolume = clickSoundVolume;
	}
	public void setBackgroundImage(IImage backgroundImage) {
		this.backgroundImage = backgroundImage;
	}
	public Color getBackgroundColor()
	{
		return backgroundColor;
	}
	public void setBackgroundColor(Color backgroundColor)
	{
		this.backgroundColor = backgroundColor;
	}
	public IImage getBackgroundImage() {
		return backgroundImage;
	}
	public void setSplashScreenMedia(IMedia splashScreenMedia)
	{
		this.splashScreenMedia = splashScreenMedia;
	}
	public IMedia getSplashScreenMedia()
	{
		return splashScreenMedia;
	}
	public void setRandomLinkMode(boolean randomLinkMode)
	{
		this.randomLinkMode = randomLinkMode;
	}
	public boolean getRandomLinkMode()
	{
		return randomLinkMode;
	}
	public void setKeepLinksOnEmptySearch(boolean keepLinks)
	{
		keepLinksOnEmptySearch = keepLinks;
	}
	public boolean getKeepLinksOnEmptySearch()
	{
		return keepLinksOnEmptySearch;
	}
	public void setMaxLinks(Long maxLinks)
	{
		this.maxLinks = maxLinks;
	}
	public Long getMaxLinks()
	{
		return maxLinks;
	}
	public List<IRule> getRules() {
		return rules;
	}
	public void setRules(List<IRule> rules) {
		this.rules = rules;
	}
	public void setSnus(Collection<ISnu> snus) {
		this.snus = snus;
	}
	public Collection<ISnu> getSnus() {
		return snus;
	}
	public void setInterfaces(Collection<IInterface> interfaces) {
		this.interfaces = interfaces;
	}
	public Collection<IInterface> getInterfaces() {
		return interfaces;
	}
	public void setMedia(Collection<IMedia> media) {
		this.media = media;
	}
	public Collection<IMedia> getMedia() {
		return media;
	}
	public void setSettings(ISettings settings)
	{
		this.settings = settings;
	}
	public ISettings getSettings()
	{
		return settings;
	}
	public String getUUID()
	{
		return uuid;
	}
	public void setUUID(String uuid)
	{
		this.uuid = uuid;
	}
	public boolean getBackgroundSoundLooping()
	{
		return backgroundSoundLooping;
	}
	public void setBackgroundSoundLooping(boolean backgroundSoundLooping)
	{
		this.backgroundSoundLooping = backgroundSoundLooping;
	}
	public IInterface getDefaultInterface()
	{
		return defaultInterface;
	}
	public void setDefaultInterface(IInterface defaultInterface)
	{
		this.defaultInterface = defaultInterface;
	}
}
