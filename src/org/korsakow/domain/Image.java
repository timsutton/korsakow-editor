package org.korsakow.domain;

import java.util.Collection;

import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.ide.resources.ResourceType;


public class Image extends Media implements IImage
{
	protected Long duration;
	public Image(long id, long version)
	{
		super(id, version);
	}
	public Image(long id, long version, String name, Collection<IKeyword> keywords, String filename, Long duration)
	{
		super(id, version, name, keywords, filename);
		setDuration(duration);
	}
	
	public String getType()
	{
		return ResourceType.IMAGE.getTypeId();
	}
	
	public void setDuration(Long duration)
	{
		this.duration = duration;
	}
	public Long getDuration()
	{
		return duration;
	}
}
