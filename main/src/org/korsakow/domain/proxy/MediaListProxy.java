package org.korsakow.domain.proxy;

import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.proxy.ListProxy;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.mapper.input.MediaInputMapper;

/**
 * 
*/
public class MediaListProxy extends ListProxy<IMedia>  {
	private final long resource_id;
	public MediaListProxy(long resource_id)
	{
		this.resource_id = resource_id;
	}
	@Override
	protected List<IMedia> getActualList() throws MapperException
	{
		return MediaInputMapper.findAll();
	}
}
