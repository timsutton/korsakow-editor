package org.korsakow.ide.resources.widget;

import java.util.Hashtable;

import org.korsakow.ide.resources.WidgetType;

public enum FontWeight
{
	Normal("normal", "Normal"),
	Bold("bold", "Bold")
	;
	
	private static Hashtable<String, FontWeight> byId = new Hashtable<String, FontWeight>();
	public static FontWeight forId(String id)
	{
		if (byId.get(id)==null) {
			for (FontWeight type : FontWeight.values())
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
	FontWeight(String id, String display)
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
