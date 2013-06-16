package org.korsakow.ide.rules;

import java.util.Hashtable;

import org.korsakow.ide.lang.LanguageBundle;

public enum RuleType
{
	RandomLookup("org.korsakow.rule.RandomLookup", LanguageBundle.getString("rules.randomlookup.label")),
	KeywordLookup("org.korsakow.rule.KeywordLookup", LanguageBundle.getString("rules.keywordlookup.label")),
	RequireKeywords("org.korsakow.rule.RequireKeywords", LanguageBundle.getString("rules.requirekeywords.label")),
	ExcludeKeywords("org.korsakow.rule.ExcludeKeywords", LanguageBundle.getString("rules.excludekeywords.label")),
	SetBackgroundImage("org.korsakow.rule.SetBackgroundImage", LanguageBundle.getString("rules.setbackgroundimage.label")),
	/**
	 * To be phased out. Has been replaced with property "KeepLinks" on SearchRule.
	 * We will also introduce a ClearLinks and KeepLinks? rule which will perform the functionality
	 * the user expects.
	 * @deprecated
	 */
	ClearScores("org.korsakow.rule.ClearScores", LanguageBundle.getString("rules.clearscores.label")),
	EndfilmLookup("org.korsakow.rule.EndfilmLookup", LanguageBundle.getString("rules.endfilmlookup.label")),
	RequireEndfilm("org.korsakow.rule.RequireEndfilm", LanguageBundle.getString("rules.requireendfilm.label")),
	ExcludeEndfilm("org.korsakow.rule.ExcludeEndfilm", LanguageBundle.getString("rules.excludeendfilm.label")),
	NextIsEndfilm("org.korsakow.rule.NextIsEndfilm", LanguageBundle.getString("rules.nextisendfilm.label")),
	SetEndfilm("org.korsakow.rule.SetEndfilm", LanguageBundle.getString("rules.setendfilm.label")),
	IfCondition("org.korsakow.rule.IfCondition", LanguageBundle.getString("rules.ifcondition.label")),
	OpenXMLSocket("org.korsakow.rule.OpenXMLSocket", LanguageBundle.getString("rules.openxmlsocket.label")),
	SendXMLSocket("org.korsakow.rule.SendXMLSocket", LanguageBundle.getString("rules.sendxmlsocket.label")),
	HttpRequest("org.korsakow.rule.HttpRequest", LanguageBundle.getString("rules.httprequest.label")),
	SetVariable("org.korsakow.rule.SetVariable", ("SetVariable")),
	Search("org.korsakow.rule.Search", "Search"),
	
	PlayMedia("org.korsakow.rule.PlayMedia", LanguageBundle.getString("rules.playmedia.label")),
	PrePlayMedia("org.korsakow.rule.PrePlayMedia", LanguageBundle.getString("rules.preplaymedia.label")),
	PostPlayMedia("org.korsakow.rule.PostPlayMedia", LanguageBundle.getString("rules.postplaymedia.label")),
	ReplaySnu("org.korsakow.rule.ReplaySnu", LanguageBundle.getString("rules.replaysnu.label")),
	ReplaySnuMainMedia("org.korsakow.rule.ReplaySnuMainMedia", LanguageBundle.getString("rules.replaysnumainmedia.label")),
	SetSnuLives("org.korsakow.rule.SetSnuLives", LanguageBundle.getString("rules.setsnulives.label")),
	
	RemoveFromSearchResults("org.korsakow.rule.RemoveFromSearchResults", LanguageBundle.getString("rules.removefromsearchresults.label")),
	
	;

	private static Hashtable<String, RuleType> byId = new Hashtable<String, RuleType>();
	public static RuleType forId(String id)
	{
		if (byId.get(id)==null) {
			for (RuleType type : RuleType.values())
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
	private String name;
	RuleType(String id, String name)
	{
		this.id = id;
		this.name = name;
	}
	public String getId()
	{
		return id;
	}
	public String getName()
	{
		return name;
	}
}
