package org.korsakow.domain;

import java.util.Collection;
import java.util.Hashtable;

import org.korsakow.domain.interf.ISettings;

public class Settings extends KDomainObject implements ISettings
{
	public static enum AdjustFilenames
	{
		Disabled("disabled"),
		Absolute("absolute"),
		Relative("relative"),
		Smart("smart")
		;
		
		public static AdjustFilenames fromId(String id)
		{
			if (Disabled.getId().equals(id)) return Disabled;
			if (Absolute.getId().equals(id)) return Absolute;
			if (Relative.getId().equals(id)) return Relative;
			if (Smart.getId().equals(id)) return Smart;
			throw new IllegalArgumentException(id);
		}
		
		private String id;
		AdjustFilenames(String id)
		{
			this.id = id;
		}
		public String getId()
		{
			return id;
		}
		@Override
		public String toString()
		{
			return getId();
		}
	}
	public static final String EXPORT_FONTS = "exportFonts"; 
	public static final String FLEX_SDK_PATH = "flexSDKPath";
	public static final String EncodeVideoOnExport = "encodeVideoOnExport";
	public static final String VideoEncodingProfile = "videoEncodingProfile";
	public static final String AdjustFilenamesOnSave = "adjustFilenamesOnSave";
	public static final String PublishPath = "publishPath";
	public static final String DraftPath = "draftPath";
	public static final String PutSimilarResourcesAtTop = "putSimilarResourcesAtTop";
	public static final String ShowBackgroundPreview = "showBackgroundPreviewInInterface";
	public static final String ShowExperimentalWidgets = "showExperimentalWidgets";
	
	public static final String ExportVideos = "exportVideos";
	public static final String ExportImages = "exportImages";
	public static final String ExportSounds = "exportSounds";
	public static final String ExportSubtitles = "exportSubtitles";
	public static final String ExportWebFiles = "exportWebFiles";
	public static final String ExportDirectory = "exportDirectory";
	
	private final Hashtable<String, Object> abstractProperties = new Hashtable<String, Object>();
	
	Settings(long id, long version)
	{
		super(id, version);
	}
	public Collection<String> getDynamicPropertyIds()
	{
		return abstractProperties.keySet();
	}
	public Object getDynamicProperty(String id)
	{
		return abstractProperties.get(id);
	}
	public void setDynamicProperty(String id, Object value)
	{
		if (id == null)
			throw new NullPointerException();
		if (value == null)
			abstractProperties.remove(id);
		else
			abstractProperties.put(id, value);
	}
	public Class getPropertyType(String id)
	{
		return Object.class;
	}
	public void setString(String name, String value)
	{
		setDynamicProperty(name, value);
	}
	public String getString(String name)
	{
		Object value = getDynamicProperty(name);
		return value!=null?value.toString():"";
	}
	public void setBoolean(String name, boolean value)
	{
		setDynamicProperty(name, value);
	}
	public boolean getBoolean(String name)
	{
		Object value = getDynamicProperty(name);
		return value!=null?Boolean.valueOf(value.toString()):false;
	}
}
