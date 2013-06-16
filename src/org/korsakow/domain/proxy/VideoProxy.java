package org.korsakow.domain.proxy;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.Video;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.domain.mapper.input.VideoInputMapper;

public class VideoProxy extends MediaProxy<Video> implements IVideo {

	public VideoProxy(long id)
	{
		super(id);
	}

	@Override
	public Class<Video> getInnerClass()
	{
		return Video.class;
	}
	
	@Override
	protected Video getFromMapper(Long id) throws MapperException {
		return VideoInputMapper.map(id);
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
