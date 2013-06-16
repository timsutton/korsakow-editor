package org.korsakow.domain.proxy;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.Sound;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.mapper.input.SoundInputMapper;

public class SoundProxy extends MediaProxy<Sound> implements ISound {

	public SoundProxy(long id)
	{
		super(id);
	}

	@Override
	public Class<Sound> getInnerClass()
	{
		return Sound.class;
	}
	
	@Override
	protected Sound getFromMapper(Long id) throws MapperException {
		return SoundInputMapper.map(id);
	}
	public String getSubtitles()
	{
		return getInnerObject().getSubtitles();
	}
	public void setSubtitles(String subtitles)
	{
		getInnerObject().setSubtitles(subtitles);
	}
	@Override
	public String getType()
	{
		return getInnerObject().getType();
	}
}
