package org.korsakow.ide.resources.widget;

import java.util.Hashtable;

import org.korsakow.ide.resources.WidgetType;

public enum WidgetPersistCondition
{
	Never("never"),
	Always("always"),
	MatchId("id"),
	MatchType("type");
	
	private static Hashtable<String, WidgetPersistCondition> byId = new Hashtable<String, WidgetPersistCondition>();
	public static WidgetPersistCondition forId(String id)
	{
		if (byId.get(id)==null) {
			for (WidgetPersistCondition type : WidgetPersistCondition.values())
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
	WidgetPersistCondition(String id)
	{
		this.id = id;
	}
	public String getId()
	{
		return id;
	}
}
