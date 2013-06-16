/**
 * 
 */
package org.korsakow.ide.resources.widget;

import java.util.HashMap;
import java.util.Map;

public enum PreviewTextMode
{
	ALWAYS("always", "Always"),
	MOUSEOVER("mouseover", "Mouse Over");
	
	
	private static Map<String, PreviewTextMode> byId = new HashMap<String, PreviewTextMode>();
	public static PreviewTextMode forId(String id)
	{
		if (byId.get(id)==null) {
			for (PreviewTextMode type : PreviewTextMode.values())
				if (type.getId().equals(id)) {
					byId.put(id, type);
					break;
				}
		}
		if (byId.get(id)==null)
			throw new IllegalArgumentException(id);
		return byId.get(id);
	}
	
	private String id;
	private String display;
	PreviewTextMode(String id, String display)
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