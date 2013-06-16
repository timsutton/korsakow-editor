package org.korsakow.domain.proxy;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.Image;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.mapper.input.ImageInputMapper;

public class ImageProxy extends MediaProxy<Image> implements IImage {

	public ImageProxy(long id)
	{
		super(id);
	}

	@Override
	public Class<Image> getInnerClass()
	{
		return Image.class;
	}
	
	@Override
	protected Image getFromMapper(Long id) throws MapperException {
		return ImageInputMapper.map(id);
	}
	@Override
	public String getType()
	{
		return getInnerObject().getType();
	}
	public void setDuration(Long duration)
	{
		getInnerObject().setDuration(duration);
	}
	public Long getDuration()
	{
		return getInnerObject().getDuration();
	}
}
