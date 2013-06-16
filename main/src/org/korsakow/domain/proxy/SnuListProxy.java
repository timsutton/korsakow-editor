package org.korsakow.domain.proxy;

import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.ListProxy;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.mapper.input.SnuInputMapper;

/**
 * 
*/
public class SnuListProxy extends ListProxy<ISnu>  {
	private final long resource_id;
	public SnuListProxy(long resource_id)
	{
		this.resource_id = resource_id;
	}
	@Override
	protected List<ISnu> getActualList() throws MapperException
	{
		return SnuInputMapper.findAll();
	}
}
