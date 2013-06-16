/**
 * 
 */
package org.korsakow.ide.ui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.mapper.input.KeywordInputMapper;
import org.korsakow.domain.mapper.input.SnuInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.PredicateType;
import org.korsakow.ide.resources.TriggerType;
import org.korsakow.ide.rules.RuleType;
import org.korsakow.ide.ui.controller.eventeditor.EditorConfigurer;
import org.korsakow.ide.ui.controller.eventeditor.IntegerRangeConfig;
import org.korsakow.ide.ui.controller.eventeditor.KeywordsConfig;
import org.korsakow.ide.ui.controller.eventeditor.MediaConfig;
import org.korsakow.ide.ui.controller.eventeditor.NoArgPredicateConfigurer;
import org.korsakow.ide.ui.controller.eventeditor.NoArgRuleConfigurer;
import org.korsakow.ide.ui.controller.eventeditor.PercentConfig;
import org.korsakow.ide.ui.controller.eventeditor.PredicateConfigurer;
import org.korsakow.ide.ui.controller.eventeditor.RuleConfigurer;
import org.korsakow.ide.ui.controller.eventeditor.SnusConfig;
import org.korsakow.ide.ui.model.PredicateModel;
import org.korsakow.ide.ui.resources.EventEditor;
import org.korsakow.ide.util.ResourceManager;
import org.korsakow.ide.util.UIResourceManager;
import org.korsakow.ide.util.Util;
import org.korsakow.services.plugin.predicate.IPredicateTypeInfo;
import org.korsakow.services.plugin.predicate.PredicateTypeInfoFactory;

public class EventEditorController
{
	private static Map<TriggerType, Collection<PredicateType>> when2if = new HashMap<TriggerType, Collection<PredicateType>>();
	private static Map<TriggerType, Collection<RuleType>> when2then = new HashMap<TriggerType, Collection<RuleType>>();
	private static Map<PredicateType, EditorConfigurer> ifArgConfigs = new HashMap<PredicateType, EditorConfigurer>();
	private static Map<RuleType, EditorConfigurer> thenArgConfigs = new HashMap<RuleType, EditorConfigurer>();
	static {
		try {
			Properties eventProperties = new Properties();
			eventProperties.load(ResourceManager.getResourceStream(UIResourceManager.UIRESOURCE_BASE_PATH + "eventmappings.properties"));
			for (Object key : eventProperties.keySet()) {
				String value = eventProperties.getProperty(key.toString());
				TriggerType triggerType = TriggerType.forId(key.toString());
				
				Collection<RuleType> rules = new ArrayList<RuleType>();
				String[] values = value.split(",");
				for (String ruleId : values) {
					ruleId = ruleId.trim();
					RuleType ruleType = RuleType.forId(ruleId);
					rules.add(ruleType);
				}
				when2then.put(triggerType, rules);
			}
		} catch (IllegalArgumentException e) { // from XXXType.forId
			Application.getInstance().showUnhandledErrorDialog("Error loading event mapping", e);
			System.exit(1); // unrecoverable, should never happen...
		} catch (IOException e) {
			Application.getInstance().showUnhandledErrorDialog("Error loading event mapping", e);
			System.exit(1); // unrecoverable, should never happen...
		}
		
		Collection<PredicateType> commonWhen2If = Util.list(PredicateType.class,
				PredicateType.True,
				PredicateType.False,
				PredicateType.KeywordInHistory,
				PredicateType.SnuInHistory,
				PredicateType.PercentOfSnusInHistory,
				PredicateType.NumberOfSnusInHistory
		);
		for (TriggerType trigger : TriggerType.values())
			when2if.put(trigger, commonWhen2If);
		
		ifArgConfigs.put(PredicateType.KeywordInHistory, new PredicateConfigurer(new KeywordsConfig()));
		ifArgConfigs.put(PredicateType.SnuInHistory, new PredicateConfigurer(new SnusConfig()));
		ifArgConfigs.put(PredicateType.PercentOfSnusInHistory, new PredicateConfigurer(new PercentConfig(0, 1, 100, true, LanguageBundle.getString("predicate.arg.percent.label"))));
		ifArgConfigs.put(PredicateType.NumberOfSnusInHistory, new PredicateConfigurer(new IntegerRangeConfig(0, 100, true, LanguageBundle.getString("predicate.arg.number.label"))));
		thenArgConfigs.put(RuleType.PlayMedia, new RuleConfigurer((new MediaConfig())));
		thenArgConfigs.put(RuleType.PrePlayMedia, new RuleConfigurer((new MediaConfig())));
		thenArgConfigs.put(RuleType.PostPlayMedia, new RuleConfigurer((new MediaConfig())));
		thenArgConfigs.put(RuleType.SetSnuLives, new RuleConfigurer(new IntegerRangeConfig(0, 5, true, LanguageBundle.getString("predicate.arg.number.label"))));
			
	}
	private final EventEditor editor;
	public EventEditorController(EventEditor editor)
	{
		this.editor = editor;
		editor.addWhenChangeListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onWhenChange();
			}
		});
		editor.addIfChangeListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onIfChange();
			}
		});
		editor.addThenChangeListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onThenChange();
			}
		});
		editor.addAddIfButtonActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onAddIf();
			}
		});
		editor.addDeleteIfButtonActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onDeleteIf();
			}
		});
	}
	private Collection<PredicateType> getIfChoices(TriggerType triggerType)
	{
		Collection<PredicateType> choices = when2if.get(triggerType);
		try {
			if ( KeywordInputMapper.findAll().isEmpty() )
				choices.remove(PredicateType.KeywordInHistory);
			if ( SnuInputMapper.findAll().isEmpty() )
				choices.remove(PredicateType.SnuInHistory);
		} catch ( MapperException e ) {
			Application.getInstance().showUnhandledErrorDialog( e );
		}
		return choices;
	}
	private void onWhenChange()
	{
		editor.setIfChoices(getIfChoices(editor.getWhen()));
		if (!when2then.containsKey(editor.getWhen())) {
			return;
		}
		editor.setThenChoices(when2then.get(editor.getWhen()));
		onThenChange();
		onIfChange();
	}
	private void onIfChange()
	{
		PredicateType pred = editor.getIf();
		if (ifArgConfigs.containsKey(pred))
			ifArgConfigs.get(pred).configure(editor);
		else {
			NoArgPredicateConfigurer.Configure(editor);
		}
	}
	private void onThenChange()
	{
		RuleType type = editor.getThen();
		if (thenArgConfigs.containsKey(type))
			thenArgConfigs.get(type).configure(editor);
		else {
			NoArgRuleConfigurer.Configure(editor);
		}
	}
	private void onAddIf()
	{
		PredicateType type = editor.getIf();
		IPredicateTypeInfo typeInfo = PredicateTypeInfoFactory.getFactory().getTypeInfo(type.getId());
		PredicateModel model = new PredicateModel(type);
		if (!typeInfo.getArguments().isEmpty())
			model.addProperty(typeInfo.getArguments().iterator().next().getName(), editor.getIfArg());
		editor.getIfListModel().addElement(model);
	}
	private void onDeleteIf()
	{
		editor.removeSelectedIf();
	}
}
