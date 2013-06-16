package org.korsakow.services.plugin.predicate;

import java.util.HashMap;
import java.util.Map;

import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.PredicateType;
import org.korsakow.services.plugin.predicate.argument.KeywordArgumentInfo;
import org.korsakow.services.plugin.predicate.argument.LongArgumentInfo;
import org.korsakow.services.plugin.predicate.argument.PercentArgumentInfo;
import org.korsakow.services.plugin.predicate.argument.ResourceArgumentInfo;

public class PredicateTypeInfoFactory
{
	private static PredicateTypeInfoFactory singleton = null;
	public static PredicateTypeInfoFactory getFactory()
	{
		if (singleton == null) {
			singleton = new PredicateTypeInfoFactory();
		}
		return singleton;
	}
	static
	{
		try {
			PredicateTypeInfoFactory factory = getFactory();
			factory.registerTypeInfo(PredicateTypeInfo.create(PredicateType.True.getId(), 0, LanguageBundle.getString("predicate.true.label")));
			factory.registerTypeInfo(PredicateTypeInfo.create(PredicateType.False.getId(), 0, LanguageBundle.getString("predicate.false.label")));
			factory.registerTypeInfo(PredicateTypeInfo.create(PredicateType.Not.getId(), 1, LanguageBundle.getString("predicate.or.label")));
			factory.registerTypeInfo(PredicateTypeInfo.create(PredicateType.And.getId(), 2, LanguageBundle.getString("predicate.and.label")));
			factory.registerTypeInfo(PredicateTypeInfo.create(PredicateType.Or.getId(), 2, LanguageBundle.getString("predicate.or.label")));
			factory.registerTypeInfo(PredicateTypeInfo.create(PredicateType.KeywordInHistory.getId(), 0, LanguageBundle.getString("predicate.keywordinhistory.label"), LanguageBundle.getString("predicate.keywordinhistory.arglabel"), new KeywordArgumentInfo("keyword", LanguageBundle.getString("predicate.arg.keyword.label"))));
			factory.registerTypeInfo(PredicateTypeInfo.create(PredicateType.SnuInHistory.getId(), 0, LanguageBundle.getString("predicate.snuinhistory.label"), LanguageBundle.getString("predicate.snuinhistory.arglabel"), new ResourceArgumentInfo("snuId", LanguageBundle.getString("predicate.arg.snu.label"))));
			factory.registerTypeInfo(PredicateTypeInfo.create(PredicateType.PercentOfSnusInHistory.getId(), 0, LanguageBundle.getString("predicate.snuinhistory.label"), LanguageBundle.getString("predicate.percentofsnusinhistory.arglabel"), new PercentArgumentInfo("percent", LanguageBundle.getString("predicate.arg.percent.label"))));
			factory.registerTypeInfo(PredicateTypeInfo.create(PredicateType.NumberOfSnusInHistory.getId(), 0, LanguageBundle.getString("predicate.snuinhistory.label"), LanguageBundle.getString("predicate.numberofsnusinhistory.arglabel"), new LongArgumentInfo("number", LanguageBundle.getString("predicate.arg.number.label"))));
		} catch (Throwable t) {
			t.printStackTrace();
			Application.getInstance().showUnhandledErrorDialog("Internal Error", t);
			System.exit(1); // this is an unrecoverable internal error.
			// TODO: handle this better
		}
	}
	private Map<String, IPredicateTypeInfo> registry = new HashMap<String, IPredicateTypeInfo>();
	public void registerTypeInfo(IPredicateTypeInfo info) throws PredicateTypeInfoFactoryException
	{
		registry.put(info.getId(), info);
	}
	public IPredicateTypeInfo getTypeInfo(String id) throws PredicateTypeInfoFactoryException
	{
		if (!registry.containsKey(id))
			throw new PredicateTypeInfoFactoryException("No such predicate: " + id);
		return registry.get(id);
	}
}


