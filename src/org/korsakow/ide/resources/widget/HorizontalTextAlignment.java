package org.korsakow.ide.resources.widget;

import java.util.Hashtable;

public enum HorizontalTextAlignment
{
	Left( "left", "Left"),
	Center( "center", "Center"),
	Right( "right", "Right" ),
	
	;
	
	private static Hashtable<String, HorizontalTextAlignment> byId = new Hashtable<String, HorizontalTextAlignment>();
	public static HorizontalTextAlignment forId(String id)
	{
		if (byId.get(id)==null) {
			for (HorizontalTextAlignment type : HorizontalTextAlignment.values())
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
	HorizontalTextAlignment(String id, String display)
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
