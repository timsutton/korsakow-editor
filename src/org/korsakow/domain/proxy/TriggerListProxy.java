package org.korsakow.domain.proxy;

import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.ListProxy;
import org.korsakow.domain.interf.ITrigger;
import org.korsakow.domain.mapper.input.TriggerInputMapper;

/**
 * 
*/
public class TriggerListProxy extends ListProxy<ITrigger>  {
	private final long resource_id;
	public TriggerListProxy(long resource_id)
	{
		this.resource_id = resource_id;
	}
	@Override
	protected List<ITrigger> getActualList() throws MapperException
	{
		return (List<ITrigger>)TriggerInputMapper.findByResource(resource_id);
	}
}
