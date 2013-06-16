package org.korsakow.ide.util;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.MissingResourceException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.xml.parsers.ParserConfigurationException;

import org.korsakow.ide.lang.LanguageBundle;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class UIResourceManager
{
	public static final String UIRESOURCE_BASE_PATH = "ui/";
	public static final String IMAGES_BASE_PATH = "images/";
	public static final String SOUNDS_BASE_PATH = "sounds/";
	
	public static final String ICON_ERROR = "error.png";
	public static final String ICON_ADD = "add.png";
	public static final String ICON_EDIT = "pencil.png";
	public static final String ICON_DELETE = "delete.png";
	public static final String ICON_MISSING = "link_break.png";
	public static final String ICON_SNU = "SNU_icon1.png";
	public static final String ICON_SNU_ADD = "SNU_icon_add.png";
	public static final String ICON_SNU_STARTING = "brick_go.png";
	public static final String ICON_VIDEO = "film.png";
	public static final String ICON_SOUND = "sound.png";
	public static final String ICON_SFX = "bell.png";
	public static final String ICON_MUSIC = "music.png";
	public static final String ICON_PREVIEW = "camera.png";
	public static final String ICON_IMAGE = "image.png";
	public static final String ICON_TEXT = "page_white_text.png";
	public static final String ICON_INTERFACE = "application_form.png";
	public static final String ICON_INTERFACE_ADD = "application_form_add.png";
	public static final String ICON_PROJECT = "box.png";
	public static final String ICON_WIDGET = "plugin.png";
	public static final String ICON_RULE = "cog.png";
	public static final String ICON_STARTFILM = "traffic_green.png";
	public static final String ICON_ENDFILM = "traffic_red.png";
	public static final String ICON_FOLDER = "folder.png";
	public static final String ICON_FOLDER_ADD = "folder_add.png";
	public static final String ICON_FOLDER_EDIT = "folder_edit.png";
	public static final String ICON_CONTROL_PLAY = "control_play_silver.png";
	public static final String ICON_CONTROL_PLAY_WIDGET = "control_play_blue.png";
	public static final String ICON_CONTROL_PAUSE_WIDGET = "control_pause_blue.png";
	public static final String ICON_CONTROL_STOP = "control_stop_silver.png";
	public static final String ICON_CONTROL_PAUSE = "control_pause_blug.png";
	public static final String ICON_CONTROL_SEEK_START = "control_start_blue.png";
	public static final String ICON_CONTROL_SEEK_END = "control_end_blue.png";
	public static final String ICON_CONTROL_FULLSCREEN = "fullscreenbw.png";
	public static final String ICON_CHECK = "check.png";

	public static final String ICON_SNU_IN = "SNU_in.png";
	public static final String ICON_SNU_OUT = "SNU_out1.png";
	public static final String ICON_BLANK_SM = "blank_16x16.png";	
	
	public static final String ICON_WINDOW_ICON = "korsakow-64.png";
	
	public static final String DRAG_DROP_KEY = "images.draganddrop";
	public static final String ABOUT_KEY = "images.about";
	
	public static final String SOUND_SAVE = "StarsNew2.aif";
	
//	public static final String LAYOUT_SNU_RESOURCE_VIEW = "snuresourceview";
	
	public static final String SYMBOL_INFINITE = "\u221E";
	
	private static final Hashtable<String, Icon> iconCache = new Hashtable<String, Icon>();
	private static final Hashtable<String, Image> imageCache = new Hashtable<String, Image>();
	
	/**
	 * Language-specific images might not exist due to delays in translation. This method
	 * returns a generic image in such cases.
	 */
	public static Icon getLanguageIcon(String name)
	{
		try {
			return getIcon(LanguageBundle.getString(name));
		} catch (MissingResourceException e) {
			return getIcon(LanguageBundle.getStringFromDefaultBundle(name));
		}
	}
	
	public static Image getImage(String name)
	{
		Image icon;
		if ((icon = imageCache.get(name)) == null) {
			icon = Toolkit.getDefaultToolkit().createImage(ResourceManager.getResourceFile(UIRESOURCE_BASE_PATH + IMAGES_BASE_PATH + name).getAbsolutePath());
			imageCache.put(name, icon);
		}
		return icon;
	}
	public static Icon getIcon(String name)
	{
		Icon icon;
		if ((icon = iconCache.get(name)) == null) {
			icon = new ImageIcon(ResourceManager.getResourceFile(UIRESOURCE_BASE_PATH + IMAGES_BASE_PATH + name).getAbsolutePath());
			iconCache.put(name, icon);
		}
		return icon;
	}
	public static Icon getIconImmediately(String name) throws IOException
	{
		Icon icon;
		if ((icon = iconCache.get(name)) == null) {
			icon = new ImageIcon(FileUtil.readBytesFully(ResourceManager.getResourceStream(UIRESOURCE_BASE_PATH + IMAGES_BASE_PATH + name)));
			iconCache.put(name, icon);
		}
		return icon;
	}
	public static String getSymbol(String name)
	{
		return name;
	}
	public static Document getLayout(String name) throws SAXException, ParserConfigurationException, IOException
	{
		String path = UIRESOURCE_BASE_PATH + "layouts/" + name + ".xml";
		InputStream stream = ResourceManager.getResourceStream(path);
		Document document = DomUtil.parseXML(stream);
		return document;
	}
	public static InputStream getSoundResourceStream(String name)
	{
		return ResourceManager.getResourceStream(UIRESOURCE_BASE_PATH + SOUNDS_BASE_PATH + name); 
	}
}
