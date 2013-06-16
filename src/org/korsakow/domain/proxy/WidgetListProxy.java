package org.korsakow.domain.proxy;

import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.ListProxy;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.domain.mapper.input.WidgetInputMapper;

/**
 * 
*/
public class WidgetListProxy extends ListProxy<IWidget>  {
	private final long resource_id;
	public WidgetListProxy(long resource_id)
	{
		this.resource_id = resource_id;
	}
	@Override
	protected List<IWidget> getActualList() throws MapperException
	{
		return WidgetInputMapper.findByInterface(resource_id);
	}
}
