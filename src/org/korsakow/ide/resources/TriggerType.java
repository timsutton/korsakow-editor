package org.korsakow.ide.resources;

import java.util.Hashtable;

import org.korsakow.ide.lang.LanguageBundle;

public enum TriggerType
{
	Initialized("org.korsakow.trigger.Initialized", "Initialized"),
	
	SnuTime("org.korsakow.trigger.SnuTime", LanguageBundle.getString("trigger.snutime.label")),
	BeforeSnuRules("org.korsakow.trigger.BeforeSnuRules", LanguageBundle.getString("trigger.beforesnurules.label")),
	AfterSnuRules("org.korsakow.trigger.AfterSnuRules", LanguageBundle.getString("trigger.aftersnurules.label")),
	BeforeSnuMainMedia("org.korsakow.trigger.BeforeSnuMainMedia", LanguageBundle.getString("trigger.beforesnumainmedia.label")),
	AfterSnuMainMedia("org.korsakow.trigger.AfterSnuMainMedia", LanguageBundle.getString("trigger.aftersnumainmedia.label")),
	BeforeFoundInSearch("org.korsakow.trigger.BeforeFoundInSearch", LanguageBundle.getString("trigger.beforefoundinsearch.label")),
	;

	private static Hashtable<String, TriggerType> byId = new Hashtable<String, TriggerType>();
	public static TriggerType forId(String id)
	{
		if (byId.get(id)==null) {
			for (TriggerType type : TriggerType.values())
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
	private String displayName;
	TriggerType(String id, String displayName)
	{
		this.id = id;
		this.displayName = displayName;
	}
	public String getId()
	{
		return id;
	}
	public String getDisplayName()
	{
		return displayName;
	}
}
