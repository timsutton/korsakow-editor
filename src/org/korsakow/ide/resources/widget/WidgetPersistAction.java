package org.korsakow.ide.resources.widget;

import java.util.Hashtable;

public enum WidgetPersistAction
{
	Add("add"),
	Replace("replace")
	;
	
	private static Hashtable<String, WidgetPersistAction> byId = new Hashtable<String, WidgetPersistAction>();
	public static WidgetPersistAction forId(String id)
	{
		if (byId.get(id)==null) {
			for (WidgetPersistAction type : WidgetPersistAction.values())
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
	WidgetPersistAction(String id)
	{
		this.id = id;
	}
	public String getId()
	{
		return id;
	}
}
