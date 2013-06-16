/**
 * 
 */
package org.korsakow.ide.resources.widget;

import java.util.HashMap;
import java.util.Map;

public enum PreviewTextEffect
{
	NONE("none", "None"),
	ANIMATE("animate", "Animate");
	
	
	private static Map<String, PreviewTextEffect> byId = new HashMap<String, PreviewTextEffect>();
	public static PreviewTextEffect forId(String id)
	{
		if (byId.get(id)==null) {
			for (PreviewTextEffect type : PreviewTextEffect.values())
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
	PreviewTextEffect(String id, String display)
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