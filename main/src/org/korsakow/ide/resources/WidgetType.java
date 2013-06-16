package org.korsakow.ide.resources;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.media.AudioInfo;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.ui.interfacebuilder.widget.Comments;
import org.korsakow.ide.ui.interfacebuilder.widget.FullscreenButton;
import org.korsakow.ide.ui.interfacebuilder.widget.GetHistory;
import org.korsakow.ide.ui.interfacebuilder.widget.History;
import org.korsakow.ide.ui.interfacebuilder.widget.InsertText;
import org.korsakow.ide.ui.interfacebuilder.widget.MainMedia;
import org.korsakow.ide.ui.interfacebuilder.widget.MasterVolume;
import org.korsakow.ide.ui.interfacebuilder.widget.MediaArea;
import org.korsakow.ide.ui.interfacebuilder.widget.MediaControls;
import org.korsakow.ide.ui.interfacebuilder.widget.PlayButton;
import org.korsakow.ide.ui.interfacebuilder.widget.PlayTime;
import org.korsakow.ide.ui.interfacebuilder.widget.Scrubber;
import org.korsakow.ide.ui.interfacebuilder.widget.SnuAutoLink;
import org.korsakow.ide.ui.interfacebuilder.widget.SnuAutoMultiLink;
import org.korsakow.ide.ui.interfacebuilder.widget.SnuFixedLink;
import org.korsakow.ide.ui.interfacebuilder.widget.Subtitles;
import org.korsakow.ide.ui.interfacebuilder.widget.TotalTime;

public enum WidgetType
{
	MainMedia("org.korsakow.widget.MainMedia", LanguageBundle.getString("widget.mainmedia.label"), MainMedia.class),
	SnuAutoLink("org.korsakow.widget.SnuAutoLink", LanguageBundle.getString("widget.snuautolink.label"), SnuAutoLink.class),
	SnuAutoMultiLink("org.korsakow.widget.SnuAutoMultiLink", LanguageBundle.getString("widget.snuautomultilink.label"), SnuAutoMultiLink.class),
	SnuFixedLink("org.korsakow.widget.SnuFixedLink", LanguageBundle.getString("widget.snufixedlink.label"), SnuFixedLink.class),
	MasterVolume("org.korsakow.widget.MasterVolume", LanguageBundle.getString("widget.mastervolume.label"), MasterVolume.class),
	MediaArea("org.korsakow.widget.MediaArea", LanguageBundle.getString("widget.mediaarea.label"), MediaArea.class),
	History("org.korsakow.widget.History", LanguageBundle.getString("widget.history.label"), History.class),
	GetHistory("org.korsakow.widget.GetHistory", LanguageBundle.getString("widget.gethistory.label"), GetHistory.class),
	InsertText("org.korsakow.widget.InsertText", LanguageBundle.getString("widget.inserttext.label"), InsertText.class),
//	PreviewText("org.korsakow.widget.PreviewText", LanguageBundle.getString("widget.previewtext.label"), PreviewText.class),
	Comments("org.korsakow.widget.Comments", LanguageBundle.getString("widget.comments.label"), Comments.class),
	Subtitles("org.korsakow.widget.Subtitles", LanguageBundle.getString("widget.subtitles.label"), Subtitles.class),
	Scrubber("org.korsakow.widget.Scrubber", LanguageBundle.getString("widget.scrubber.label"), Scrubber.class),
	MediaControls("org.korsakow.widget.MediaControls", LanguageBundle.getString("widget.mediacontrols.label"), MediaControls.class),
	PlayButton("org.korsakow.widget.PlayButton", LanguageBundle.getString("widget.playbutton.label"), PlayButton.class),
	FullscreenButton("org.korsakow.widget.FullscreenButton", LanguageBundle.getString("widget.fullscreenbutton.label"), FullscreenButton.class),
	TotalTime("org.korsakow.widget.TotalTime", LanguageBundle.getString("widget.totaltime.label"), TotalTime.class),
	PlayTime("org.korsakow.widget.PlayTime", LanguageBundle.getString("widget.playtime.label"), PlayTime.class)
	;

	private static Map<String, WidgetType> byId = new HashMap<String, WidgetType>();
	public static WidgetType forId(String id)
	{
		if (id == null)
			throw new NullPointerException();
		if (byId.isEmpty()) {
			for (WidgetType type : WidgetType.values())
				byId.put(type.getId(), type);
		}
		if (byId.get(id)==null)
			throw new IllegalArgumentException("Invalid widget type: " + id);
		return byId.get(id);
	}
	
	private String id;
	private Class<? extends WidgetModel> clazz;
	private String displayName;
	WidgetType(String id, String displayName, Class<? extends WidgetModel> clazz)
	{
		this.id = id;
		this.displayName = displayName;
		this.clazz = clazz;
	}
	public String getId()
	{
		return id;
	}
	public String getDisplayName()
	{
		return displayName;
	}
	public WidgetModel newInstance()
	{
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			// should never happen
        	Logger.getLogger(AudioInfo.class).fatal(e);
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			// should never happen
        	Logger.getLogger(AudioInfo.class).fatal(e);
			throw new RuntimeException(e);
		}
	}
}
