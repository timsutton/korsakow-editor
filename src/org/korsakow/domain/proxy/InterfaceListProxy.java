package org.korsakow.domain.proxy;

import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.ListProxy;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.mapper.input.InterfaceInputMapper;

/**
 * 
*/
public class InterfaceListProxy extends ListProxy<IInterface>  {
	private final long resource_id;
	public InterfaceListProxy(long resource_id)
	{
		this.resource_id = resource_id;
	}
	@Override
	protected List<IInterface> getActualList() throws MapperException
	{
		return InterfaceInputMapper.findAll();
	}
}
