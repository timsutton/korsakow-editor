package org.korsakow.domain.proxy;

import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.ListProxy;
import org.korsakow.domain.interf.IEvent;
import org.korsakow.domain.mapper.input.EventInputMapper;

/**
 * 
*/
public class EventListProxy extends ListProxy<IEvent>  {
	private final long resource_id;
	public EventListProxy(long resource_id)
	{
		this.resource_id = resource_id;
	}
	@Override
	protected List<IEvent> getActualList() throws MapperException
	{
		return (List<IEvent>)EventInputMapper.findByResource(resource_id);
	}
}
