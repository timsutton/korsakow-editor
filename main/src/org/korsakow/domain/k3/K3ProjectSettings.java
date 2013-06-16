package org.korsakow.domain.k3;


public class K3ProjectSettings
{
	public String startFilmFilename;
	public String startFilmFoldername;
	public String logWindow;
	public float movieRatingFactor;
	/**
	 * @deprecated
	 */
	public boolean automaticClick;
	/**
	 * @deprecated
	 */
	public String unknown16_6;
	public boolean keepOldLinksIfNoNewLinks = false;
	public boolean randomLinkMode = true;
	public String endFilmFilename;
	public String endFilmFoldername;
	/**
	 * @deprecated
	 */
	public String randomLinkIcon;
	public String unknown16_12;
	public String unknown16_13;
	/**
	 * @deprecated
	 */
	public String pseudoRandomLink;
	/**
	 * Presentation-Mode (Timeout after x minutes, Exit-lock)
	 */
	public boolean presentationMode;
	/**
	 * @deprecated
	 */
	public boolean positiveLinking;
	/**
	 * special setting for kairo exhibition
	 * @deprecated
	 */
	public boolean kairoSpecialSetting;
	public int videoWidth;
	public int videoHeight;
	public String subtitles;
	public int stageWidth = 1024;
	public int stageHeight = 768;
	public int backgroundColor = 0x000000;
	public int foregroundColor = 0xFFFFFF;
	/**
	 * "999" in the database file means use foreground. here we use null instead.
	 */
	public Integer insertTextColor = null;
	/**
	 * "999" in the database file means use default, here we use null instead.
	 */
	public String insertTextFontFamily = "Arial";
	/**
	 * "999" in the database file means use foreground. here we use null instead.
	 */
	public Integer previewTextColor = null;
	/**
	 * "999" in the database file means use default, here we use null instead.
	 */
	public String previewTextFontFamily= "Arial";
	/**
	 * "999" in the database file means use default, here we use null instead.
	 */
	public Integer subtitleTextColor = null;
	/**
	 * "999" in the database file means use default, here we use null instead.
	 */
	public String subtitleTextFontFamily = "Arial";
	
	
	public int insertTextSize = 14;
	public int previewTextSize = 30;
	public int subtitleTextSize = 24;
	/**
	 * @deprecated
	 */
	public boolean link3Lines;
	/**
	 *  defines which type of interface. 
	 */
	public boolean use3LinkInterface;
	public int delayValue;
	public boolean loopSnus = false;
	public boolean loopPreviews = true;
	/**
	 * satelite (for installation, when using multiple screen projections)
	 */
	public boolean satelliteMode;
	public boolean backgroundSound = false;
	/**
	 * database file has [0,255] we normalize to [0,1]
	 */
	public float backgroundSoundVolume;
	/**
	 * @deprecated
	 */
	public boolean chair;
	public String unknown17_13;
	/**
	 *  for online-version if set to 1 K saves a text-file containing the history so that K remembers previously seen film when revisiting the site.
	 */
	public boolean saveHistory;
	/**
	 *  for the history of the "database.txt" K3 stores old versions of the database.
	 */
	public String databaseHistory;
	/*
	 * width of the preview-icon when using the many-link-interface
	 */
	public int manyLinkPreviewWidth;
	/**
	 * height of the preview-icon when using the many-link-interface
	 */
	public int manyLinkPreviewHeight;
	public String unknown17_18;
	/**
	 * number of satelite (for installation)
	 */
	public String satelliteId;
	public String unknown17_20;
	/**
	 * @deprecated
	 */
	public String unknown17_21;
	/**
	 * @deprecated
	 */
	public String unknown17_22;
	public boolean useSnuAsPreview = false;
	public int manyLinkMaxLinks;
	public boolean useNewInterface;
	/**
	 * autolink K picks one of the offered links automatically (selection-process is visualized)
	 */
	public boolean autoLinkMode;
	/**
	 *  K starts to automatically select a link after 600 seconds in case the SNU loops. If it does not loop, it selects a link after the SNU is finished. (selection-process is visualized)
	 */
	public int autoLinkTimeout;
	
}
