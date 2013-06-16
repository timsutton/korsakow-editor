package org.korsakow.ide.ui.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.korsakow.ide.resources.TriggerType;

public class EventModel
{
	private TriggerModel trigger;
	private PredicateModel predicate;
	private RuleModel rule;
	public EventModel(TriggerModel trigger, PredicateModel predicate, RuleModel rule)
	{
		this.trigger = trigger;
		this.predicate = predicate;
		this.rule = rule;
	}
	public TriggerModel getTrigger()
	{
		return trigger;
	}
	public PredicateModel getPredicate()
	{
		return predicate;
	}
	public RuleModel getRule()
	{
		return rule;
	}
}
