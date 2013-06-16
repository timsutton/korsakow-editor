package org.korsakow.domain.proxy;

import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.mapper.input.KeywordInputMapper;

/**
 * 
*/
public class KeywordListProxy extends NonDOListProxy<IKeyword>  {
	private final long object_id;
	public KeywordListProxy(long object_id)
	{
		this.object_id = object_id;
	}
	@Override
	protected List<IKeyword> getActualList() throws MapperException
	{
		return (List<IKeyword>)KeywordInputMapper.findByObject(object_id);
	}
}
