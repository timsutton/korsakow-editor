package org.korsakow.domain.proxy;

import java.util.Collection;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.mapper.input.KeywordInputMapper;

/**
 * 
*/
public class KeywordCollectionProxy extends NonDOCollectionProxy<IKeyword>  {
	private final long object_id;
	public KeywordCollectionProxy(long object_id)
	{
		this.object_id = object_id;
	}
	@Override
	protected Collection<IKeyword> getActualCollection() throws MapperException
	{
		return KeywordInputMapper.findByObject(object_id);
	}
}
