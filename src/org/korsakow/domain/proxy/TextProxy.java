package org.korsakow.domain.proxy;

import java.awt.Font;
import java.util.Collection;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.Text;
import org.korsakow.domain.interf.IText;
import org.korsakow.domain.mapper.input.TextInputMapper;

public class TextProxy extends MediaProxy<Text> implements IText {

	public TextProxy(long id)
	{
		super(id);
	}

	@Override
	public Class<Text> getInnerClass()
	{
		return Text.class;
	}
	
	@Override
	protected Text getFromMapper(Long id) throws MapperException {
		return TextInputMapper.map(id);
	}
	
	public String getText()
	{
		return getInnerObject().getText();
	}
	public void setText(String text)
	{
		getInnerObject().setText(text);
	}
	public String getValue()
	{
		return getInnerObject().getValue();
	}
	public Collection<Font> getFonts() throws Exception
	{
		return getInnerObject().getFonts();
	}
	@Override
	public String getType()
	{
		return getInnerObject().getType();
	}
}
