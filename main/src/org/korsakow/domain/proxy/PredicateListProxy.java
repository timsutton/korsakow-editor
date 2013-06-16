package org.korsakow.domain.proxy;

import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.ListProxy;
import org.korsakow.domain.interf.IPredicate;
import org.korsakow.domain.mapper.input.PredicateInputMapper;

/**
 * 
*/
public class PredicateListProxy extends ListProxy<IPredicate>  {
	private final long resource_id;
	public PredicateListProxy(long resource_id)
	{
		this.resource_id = resource_id;
	}
	@Override
	protected List<IPredicate> getActualList() throws MapperException
	{
		return (List<IPredicate>)PredicateInputMapper.findByResource(resource_id);
	}
}
