package org.korsakow.ide.resources.widget;

import java.util.Hashtable;

import org.korsakow.ide.resources.WidgetType;

public enum TextDecoration
{
	None("none", "None"),
	Underline("underline", "Underline")
	
//	Overline("overline", "Overline"),
//	LineThrough("lineThough", "Line-through");
	;
	
	private static Hashtable<String, TextDecoration> byId = new Hashtable<String, TextDecoration>();
	public static TextDecoration forId(String id)
	{
		if (byId.get(id)==null) {
			for (TextDecoration type : TextDecoration.values())
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
	TextDecoration(String id, String display)
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
