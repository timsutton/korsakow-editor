package org.korsakow.domain;

import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.interf.ITrigger;
import org.korsakow.ide.DataRegistry;

public class TriggerFactory {
	public static Trigger createNew(String triggerType)
	{
		Trigger object = new Trigger(DataRegistry.getMaxId(), 0, triggerType);
		UoW.getCurrent().registerNew(object);
		return object;
	}
	public static Trigger createClean(String triggerType)
	{
		Trigger object = new Trigger(DataRegistry.getMaxId(), 0, triggerType);
		UoW.getCurrent().registerClean(object);
		return object;
	}
	public static Trigger createClean(long id, long version, String triggerType)
	{
		Trigger object = new Trigger(id, version, triggerType);
		UoW.getCurrent().registerClean(object);
		return object;
	}
	public static Trigger copy(ITrigger src)
	{
		Trigger copy = new Trigger(DataRegistry.getMaxId(), 0, src.getTriggerType());
		for (String propId : src.getDynamicPropertyIds())
			copy.setDynamicProperty(propId, src.getDynamicProperty(propId));
		UoW.getCurrent().registerNew(copy);
		return copy;
	}
}
