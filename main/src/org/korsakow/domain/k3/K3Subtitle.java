package org.korsakow.domain.k3;

import org.korsakow.services.encoders.subtitle.ISubtitle;

public class K3Subtitle implements ISubtitle
{
	public long startTime;
	public long endTime;
	public String text;
	
	public long getStartTime()
	{
		return startTime;
	}
	public long getEndTime()
	{
		return endTime;
	}
	public String getText()
	{
		return text;
	}
	public int compareTo(ISubtitle o) {
		return (int)(startTime - o.getStartTime());
	}
}
