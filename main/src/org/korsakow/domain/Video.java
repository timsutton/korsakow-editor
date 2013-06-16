package org.korsakow.domain;

import java.util.Collection;

import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.ide.resources.ResourceType;

public class Video extends Media implements IVideo
{
	private String subtitles;
	Video(long id, long version)
	{
		super(id, version);
	}
	Video(long id, long version, String name, Collection<IKeyword> keywords, String filename, String subtitles)
	{
		super(id, version, name, keywords, filename);
		this.subtitles = subtitles;
	}
	public String getSubtitles()
	{
		return subtitles;
	}
	public void setSubtitles(String subtitles)
	{
		this.subtitles = subtitles;
	}
	public String getType()
	{
		return ResourceType.VIDEO.getTypeId();
	}
}
