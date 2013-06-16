package org.korsakow.ide.resources.widget;

import java.util.Hashtable;

public enum VerticalTextAlignment
{
	Top( "top", "Top"),
	Middle( "middle", "Middle"),
	Bottom( "bottom", "Bottom" ),
	
	;
	
	private static Hashtable<String, VerticalTextAlignment> byId = new Hashtable<String, VerticalTextAlignment>();
	public static VerticalTextAlignment forId(String id)
	{
		if (byId.get(id)==null) {
			for (VerticalTextAlignment type : VerticalTextAlignment.values())
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
	VerticalTextAlignment(String id, String display)
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
