package org.korsakow.ide.resources;

import java.util.Hashtable;

import org.korsakow.ide.lang.LanguageBundle;

public enum PredicateType
{
	True("org.korsakow.predicate.True", LanguageBundle.getString("predicate.true.label")),
	False("org.korsakow.predicate.False", LanguageBundle.getString("predicate.false.label")),
	And("org.korsakow.predicate.And", LanguageBundle.getString("predicate.and.label")),
	Or("org.korsakow.predicate.Or", LanguageBundle.getString("predicate.or.label")),
	Not("org.korsakow.predicate.Not", LanguageBundle.getString("predicate.not.label")),
	KeywordInHistory("org.korsakow.predicate.KeywordInHistory", LanguageBundle.getString("predicate.keywordinhistory.label")),
	SnuInHistory("org.korsakow.predicate.SnuInHistory", LanguageBundle.getString("predicate.snuinhistory.label")),
	PercentOfSnusInHistory("org.korsakow.predicate.PercentOfSnusInHistory", LanguageBundle.getString("predicate.percentofsnusinhistory.label")),
	NumberOfSnusInHistory("org.korsakow.predicate.NumberOfSnusInHistory", LanguageBundle.getString("predicate.numberofsnusinhistory.label"))
	;

	private static Hashtable<String, PredicateType> byId = new Hashtable<String, PredicateType>();
	public static PredicateType forId(String id)
	{
		if (byId.get(id)==null) {
			for (PredicateType type : PredicateType.values())
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
	PredicateType(String id, String displayName)
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
