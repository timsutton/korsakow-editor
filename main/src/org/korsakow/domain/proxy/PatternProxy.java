package org.korsakow.domain.proxy;

import java.util.Collection;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.Pattern;
import org.korsakow.domain.interf.IPattern;
import org.korsakow.domain.mapper.input.PatternInputMapper;

public class PatternProxy extends MediaProxy<Pattern> implements IPattern {

	public PatternProxy(long id)
	{
		super(id);
	}

	@Override
	public Class<Pattern> getInnerClass()
	{
		return Pattern.class;
	}
	
	@Override
	protected Pattern getFromMapper(Long id) throws MapperException {
		return PatternInputMapper.map(id);
	}
	@Override
	public String getType()
	{
		return getInnerObject().getType();
	}
	public Object getDynamicProperty(String id) {
		return getInnerObject().getDynamicProperty(id);
	}

	public Collection<String> getDynamicPropertyIds() {
		return getInnerObject().getDynamicPropertyIds();
	}

	public void setDynamicProperty(String id, Object value) {
		getInnerObject().setDynamicProperty(id, value);
	}
	public Class getPropertyType(String id)
	{
		return getInnerObject().getPropertyType(id);
	}

	public void setPatternType(String patternType)
	{
		getInnerObject().setPatternType(patternType);
	}
	public String getPatternType()
	{
		return getInnerObject().getPatternType();
	}
}
