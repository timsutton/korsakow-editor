package org.korsakow.ide.resources.widget;

import java.util.Hashtable;

import org.korsakow.ide.resources.WidgetType;

public enum FontStyle
{
	Normal("normal", "Normal"),
	Italic("italic", "Italic")
//	Oblique("oblique", "Oblique")
	;
	
	private static Hashtable<String, FontStyle> byId = new Hashtable<String, FontStyle>();
	public static FontStyle forId(String id)
	{
		if (byId.get(id)==null) {
			for (FontStyle type : FontStyle.values())
				if (type.getId().equals(id)) {
					byId.put(id, type);
					break;
				}
		}
		if (byId.get(id)==null)
			throw new IllegalArgumentException();
		return byId.get(id);
	}
	
	private String id;
	private String display;
	FontStyle(String id, String display)
	{
		this.id = id;
		this.display = display;
	}
	public String getId()
	{
		return id;
	}
	public String getDisplay()
	{
		return display;
	}
}
