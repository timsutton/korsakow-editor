/**
 * 
 */
package org.korsakow.ide.resources.widget;

import java.util.HashMap;
import java.util.Map;

public enum ScalingPolicy
{
	None("none", "None"),
	ExactFit("exactfit", "Exact Fit"),
	MaintainAspectRatio("maintainaspectratio", "Maintain aspect ratio"),
	ScaleDownMaintainAspectRatio("scaledownmaintainaspectratio", "Scale down, maintain aspect ratio"),
	;
	
	
	private static Map<String, ScalingPolicy> byId = new HashMap<String, ScalingPolicy>();
	public static ScalingPolicy forId(String id)
	{
		if (byId.get(id)==null) {
			for (ScalingPolicy type : ScalingPolicy.values())
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
	ScalingPolicy(String id, String display)
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