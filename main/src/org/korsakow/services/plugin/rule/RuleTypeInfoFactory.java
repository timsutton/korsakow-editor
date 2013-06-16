package org.korsakow.services.plugin.rule;

import java.util.HashMap;
import java.util.Map;

import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.rules.RuleType;
import org.korsakow.services.plugin.predicate.argument.LongArgumentInfo;
import org.korsakow.services.plugin.predicate.argument.ResourceArgumentInfo;
import org.korsakow.services.plugin.predicate.argument.StringArgumentInfo;

public class RuleTypeInfoFactory
{
	private static RuleTypeInfoFactory singleton = null;
	public static RuleTypeInfoFactory getFactory()
	{
		if (singleton == null) {
			singleton = new RuleTypeInfoFactory();
		}
		return singleton;
	}
	static
	{
		try {
			RuleTypeInfoFactory factory = getFactory();
			factory.registerTypeInfo(RuleTypeInfo.create(RuleType.RandomLookup.getId(), LanguageBundle.getString("rules.randomlookup.label")));
			factory.registerTypeInfo(RuleTypeInfo.create(RuleType.KeywordLookup.getId(), LanguageBundle.getString("rules.keywordlookup.label")));
			factory.registerTypeInfo(RuleTypeInfo.create(RuleType.RequireKeywords.getId(), LanguageBundle.getString("rules.requirekeywords.label")));
			factory.registerTypeInfo(RuleTypeInfo.create(RuleType.ExcludeKeywords.getId(), LanguageBundle.getString("rules.excludekeywords.label")));
			factory.registerTypeInfo(RuleTypeInfo.create(RuleType.SetBackgroundImage.getId(), LanguageBundle.getString("rules.setbackgroundimage.label"), LanguageBundle.getString("rules.setbackgroundimage.label"), new LongArgumentInfo("imageId", "Image")));
			factory.registerTypeInfo(RuleTypeInfo.create(RuleType.ClearScores.getId(), LanguageBundle.getString("rules.clearscores.label")));
			factory.registerTypeInfo(RuleTypeInfo.create(RuleType.EndfilmLookup.getId(), LanguageBundle.getString("rules.endfilmlookup.label")));
			factory.registerTypeInfo(RuleTypeInfo.create(RuleType.RequireEndfilm.getId(), LanguageBundle.getString("rules.requireendfilm.label")));
			factory.registerTypeInfo(RuleTypeInfo.create(RuleType.ExcludeEndfilm.getId(), LanguageBundle.getString("rules.excludeendfilm.label")));
			factory.registerTypeInfo(RuleTypeInfo.create(RuleType.NextIsEndfilm.getId(), LanguageBundle.getString("rules.nextisendfilm.label")));
			factory.registerTypeInfo(RuleTypeInfo.create(RuleType.SetEndfilm.getId(), LanguageBundle.getString("rules.setendfilm.label")));
			factory.registerTypeInfo(RuleTypeInfo.create(RuleType.IfCondition.getId(), LanguageBundle.getString("rules.ifcondition.label")));
			factory.registerTypeInfo(RuleTypeInfo.create(RuleType.OpenXMLSocket.getId(), LanguageBundle.getString("rules.openxmlsocket.label")));
			factory.registerTypeInfo(RuleTypeInfo.create(RuleType.SendXMLSocket.getId(), LanguageBundle.getString("rules.sendxmlsocket.label")));
			factory.registerTypeInfo(RuleTypeInfo.create(RuleType.HttpRequest.getId(), LanguageBundle.getString("rules.httprequest.label"), LanguageBundle.getString("rules.httprequest.label"), new StringArgumentInfo("hostname", "Hostname"), new StringArgumentInfo("requestVariableName", "RequestVariableName"), new StringArgumentInfo("requestVariableValue", "RequestVariableValue")));
			factory.registerTypeInfo(RuleTypeInfo.create(RuleType.SetVariable.getId(), "SetVariable", "SetVariable", new StringArgumentInfo("variableName", "Name"), new StringArgumentInfo("variableValue", "Value")));
			
			
			factory.registerTypeInfo(RuleTypeInfo.create(RuleType.Search.getId(), RuleType.Search.getName(), RuleType.Search.getName(), new LongArgumentInfo("maxLinks", "MaxLinks")));
			factory.registerTypeInfo(RuleTypeInfo.create(RuleType.ExcludeKeywords.getId(), RuleType.ExcludeKeywords.getName(), RuleType.Search.getName(), new LongArgumentInfo("maxLinks", "MaxLinks")));
			
			factory.registerTypeInfo(RuleTypeInfo.create(RuleType.PrePlayMedia.getId(), RuleType.PrePlayMedia.getName(), LanguageBundle.getString("rules.preplaymedia.arglabel"), new ResourceArgumentInfo("mediaId", LanguageBundle.getString("rules.arg.media.label"))));
			factory.registerTypeInfo(RuleTypeInfo.create(RuleType.PostPlayMedia.getId(), RuleType.PostPlayMedia.getName(), LanguageBundle.getString("rules.postplaymedia.arglabel"), new ResourceArgumentInfo("mediaId", LanguageBundle.getString("rules.arg.media.label"))));
			factory.registerTypeInfo(RuleTypeInfo.create(RuleType.PlayMedia.getId(), RuleType.PlayMedia.getName(), LanguageBundle.getString("rules.playmedia.arglabel"), new ResourceArgumentInfo("mediaId", LanguageBundle.getString("rules.arg.media.label"))));
			factory.registerTypeInfo(RuleTypeInfo.create(RuleType.ReplaySnu.getId(), RuleType.ReplaySnu.getName()));
			factory.registerTypeInfo(RuleTypeInfo.create(RuleType.ReplaySnuMainMedia.getId(), RuleType.ReplaySnuMainMedia.getName()));
			factory.registerTypeInfo(RuleTypeInfo.create(RuleType.SetSnuLives.getId(), LanguageBundle.getString("rules.setsnulives.label"), LanguageBundle.getString("rules.setsnulives.arglabel"), new LongArgumentInfo("lives", LanguageBundle.getString("rules.arg.lives.label"))));

			factory.registerTypeInfo(RuleTypeInfo.create(RuleType.RemoveFromSearchResults.getId(), RuleType.ClearScores.getName()));
		} catch (Throwable t) {
			t.printStackTrace();
			Application.getInstance().showUnhandledErrorDialog("Internal Error", t);
			System.exit(1); // this is an unrecoverable internal error.
			// TODO: handle this better
		}
	}
	private final Map<String, IRuleTypeInfo> registry = new HashMap<String, IRuleTypeInfo>();
	public void registerTypeInfo(IRuleTypeInfo info) throws RuleTypeInfoFactoryException
	{
		registry.put(info.getId(), info);
	}
	public IRuleTypeInfo getTypeInfo(String id) throws RuleTypeInfoFactoryException
	{
		if (!registry.containsKey(id))
			throw new RuleTypeInfoFactoryException("No such rule: " + id);
		return registry.get(id);
	}
}

