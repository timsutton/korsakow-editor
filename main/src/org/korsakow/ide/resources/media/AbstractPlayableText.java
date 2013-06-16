package org.korsakow.ide.resources.media;

import java.awt.Dimension;


public abstract class AbstractPlayableText implements PlayableText
{
	private long time;
	private boolean isPlaying = false;
	public void setTime(long time)
	{
		this.time = time;
	}
	public long getTime()
	{
		return time;
	}
	public boolean isTemporal()
	{
		return false;
	}
	public boolean isPlaying()
	{
		return isPlaying;
	}
	public long getDuration()
	{
		return 0;
	}
	public void start()
	{
		isPlaying = true;
	}
	public void stop()
	{
		isPlaying = false;
	}
	public Dimension getAspectRespectingDimension(Dimension outter) {
		Dimension inner = getComponent().getPreferredSize();
		float aspectRatio = inner.width/(float)inner.height;
		if(outter.width/aspectRatio < outter.height) {
			return new Dimension(outter.width, (int)(outter.width/aspectRatio));
		} else {
			return new Dimension((int)(outter.height*aspectRatio), outter.height);
		}
	}
}
