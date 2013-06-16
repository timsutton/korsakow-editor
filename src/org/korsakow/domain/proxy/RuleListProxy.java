package org.korsakow.domain.proxy;

import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.ListProxy;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.mapper.input.RuleInputMapper;

/**
 * 
*/
public class RuleListProxy extends ListProxy<IRule>  {
	private final long resource_id;
	public RuleListProxy(long resource_id)
	{
		this.resource_id = resource_id;
	}
	@Override
	protected List<IRule> getActualList() throws MapperException
	{
		return (List<IRule>)RuleInputMapper.findByResource(resource_id);
	}
}
