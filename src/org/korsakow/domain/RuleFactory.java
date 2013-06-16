package org.korsakow.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IRule;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.rules.RuleType;

public class RuleFactory {
	public static void registerNewRecursive(IRule rule)
	{
		for (IRule subRule : rule.getRules()) {
			registerNewRecursive(subRule);
		}
		UoW.getCurrent().registerNew(rule);
	}
	public static void registerCleanRecursive(IRule rule)
	{
		for (IRule subRule : rule.getRules()) {
			registerCleanRecursive(subRule);
		}
		UoW.getCurrent().registerNew(rule);
	}
	public static Rule createNew(long id, long version)
	{
		Rule object = new Rule(id, version);
		UoW.getCurrent().registerNew(object);
		return object;
	}
	public static Rule createNew(String ruletype)
	{
		Rule object = new Rule(DataRegistry.getMaxId(), 0);
		object.setRuleType(ruletype);
		UoW.getCurrent().registerNew(object);
		return object;
	}
	public static Rule createNew()
	{
		return createNew(DataRegistry.getMaxId(), 0);
	}
	public static Rule createNew(String type, Collection<IKeyword> keywords, String name, long triggerTime, List<IRule> rules)
	{
		Rule object = new Rule(DataRegistry.getMaxId(), 0, type, keywords, name, triggerTime, rules);
		UoW.getCurrent().registerNew(object);
		return object;
	}
	public static Rule createClean(String ruletype)
	{
		return createClean(DataRegistry.getMaxId(), 0, ruletype, new HashSet<IKeyword>(), ruletype, 0, new ArrayList<IRule>());
	}
	public static Rule createClean(long id, long version, String type, Collection<IKeyword> keywords, String name, long triggerTime, List<IRule> rules)
	{
		Rule object = new Rule(id, version, type, keywords, name, triggerTime, rules);
		UoW.getCurrent().registerClean(object);
		return object;
	}
	public static Rule copy(IRule src) {
		final Rule copy = createNew(src.getRuleType(), KeywordFactory.copy(src.getKeywords()), src.getName(), src.getTriggerTime(), copy(src.getRules()));
		for (String propId : src.getDynamicPropertyIds())
			copy.setDynamicProperty(propId, src.getDynamicProperty(propId));
		return copy;
	}
	public static List<IRule> copy(List<IRule> src) {
		List<IRule> copies = new ArrayList<IRule>();
		for (IRule rule : src) {
			copies.add(copy(rule));
		}
		return copies;
	}
	public static IRule createSearchRule(List<IRule> rules)
	{
		IRule rule = RuleFactory.createClean(RuleType.Search.getId());
		rule.setRules(rules);
		return rule;
	}
	public static IRule createClearScoresRule()
	{
		IRule rule = RuleFactory.createClean(RuleType.ClearScores.getId());
		return rule;
	}
	public static IRule createSetEndFilmRule()
	{
		IRule rule = RuleFactory.createClean(RuleType.SetEndfilm.getId());
		return rule;
	}
	public static IRule createRandomLookupRule()
	{
		IRule rule = RuleFactory.createClean(RuleType.RandomLookup.getId());
		return rule;
	}
	public static IRule createEndfilmLookupRule()
	{
		IRule rule = RuleFactory.createClean(RuleType.EndfilmLookup.getId());
		return rule;
	}
	public static IRule createKeywordLookupRule()
	{
		IRule rule = RuleFactory.createClean(RuleType.KeywordLookup.getId());
		return rule;
	}
	public static IRule createRequireEndfilmRule()
	{
		IRule rule = RuleFactory.createClean(RuleType.RequireEndfilm.getId());
		return rule;
	}
	public static IRule createExcludeEndfilmRule()
	{
		IRule rule = RuleFactory.createClean(RuleType.ExcludeEndfilm.getId());
		return rule;
	}
	public static IRule createRequireKeywordsRuleFromStrings(Collection<String> keywords)
	{
		return createRequireKeywordsRule(Keyword.fromStrings(keywords));
	}
	public static IRule createRequireKeywordsRule(Collection<IKeyword> keywords)
	{
		IRule rule = RuleFactory.createClean(RuleType.RequireKeywords.getId());
		rule.setKeywords(keywords);
		return rule;
	}
	public static IRule createExcludeKeywordsRuleFromStrings(Collection<String> keywords)
	{
		return createExcludeKeywordsRule(Keyword.fromStrings(keywords));
	}
	public static IRule createExcludeKeywordsRule(Collection<IKeyword> keywords)
	{
		IRule rule = RuleFactory.createClean(RuleType.ExcludeKeywords.getId());
		rule.setKeywords(keywords);
		return rule;
	}
}
