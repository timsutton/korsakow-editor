package org.korsakow.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.interf.IEvent;
import org.korsakow.domain.interf.IPredicate;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ITrigger;
import org.korsakow.ide.DataRegistry;

public class EventFactory {
	public static Event createNew(ITrigger trigger, IPredicate predicate, IRule rule)
	{
		Event object = new Event(DataRegistry.getMaxId(), 0L, trigger, predicate, rule);
		UoW.getCurrent().registerNew(object);
		return object;
	}
	public static Event createClean(ITrigger trigger, IPredicate predicate, IRule rule)
	{
		return createClean(DataRegistry.getMaxId(), 0, trigger, predicate, rule);
	}
	public static Event createClean(long id, long version, ITrigger trigger, IPredicate predicate, IRule rule)
	{
		Event object = new Event(id, version, trigger, predicate, rule);
		UoW.getCurrent().registerClean(object);
		return object;
	}
	public static Event createClean(long id, long version, Collection<ITrigger> triggers, Collection<IPredicate> predicates, Collection<IRule> rules) throws MapperException
	{
		if (triggers.size() > 1 || predicates.size() > 1 || rules.size() > 1)
			throw new MapperException("invalid argument");
		ITrigger trigger = triggers.isEmpty()?null:triggers.iterator().next();
		IPredicate predicate = predicates.isEmpty()?null:predicates.iterator().next();
		IRule rule = rules.isEmpty()?null:rules.iterator().next();
		return createClean(id, version, trigger, predicate, rule);
	}
	public static Event copy(IEvent src) {
		Event copy = createNew(TriggerFactory.copy(src.getTrigger()), PredicateFactory.copy(src.getPredicate()), RuleFactory.copy(src.getRule()));
		for (String propId : src.getDynamicPropertyIds())
			copy.setDynamicProperty(propId, src.getDynamicProperty(propId));
		return copy;
	}
	public static Collection<IEvent> copy(Collection<IEvent> src) {
		List<IEvent> copies = new ArrayList<IEvent>();
		for (IEvent event : src) {
			IEvent copy = copy(event);
			copies.add(copy);
		}
		return copies;
	}
}
