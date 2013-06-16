/**
 * 
 */
package org.korsakow.ide.resources.widget;

import java.util.HashMap;
import java.util.Map;

public enum PlayMode
{
	Always("always", "Always"),
	MouseOver("mouseOver", "Mouse Over"),
	Click("click", "Click"),
	;
	
	
	private static Map<String, PlayMode> byId = new HashMap<String, PlayMode>();
	public static PlayMode forId(String id)
	{
		if (byId.get(id)==null) {
			for (PlayMode type : PlayMode.values())
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
	PlayMode(String id, String display)
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