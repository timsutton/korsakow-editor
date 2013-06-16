package org.korsakow.ide.resources.media;

import java.awt.Dimension;

public abstract class AbstractPlayableImage implements PlayableImage
{
	private long time;
	private boolean isPlaying = false;
	private long duration = 5000;
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
		return true;
	}
	public boolean isPlaying()
	{
		return isPlaying;
	}
	public void setDuration(long duration)
	{
		this.duration = duration;
	}
	public long getDuration()
	{
		return duration;
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
	public static Dimension getAspectRespectingDimension(int outterWidth, int outterHeight, float aspectRatio) {
		if(outterWidth/aspectRatio < outterHeight) {
			return new Dimension(outterWidth, (int)(outterWidth/aspectRatio));
		} else {
			return new Dimension((int)(outterHeight*aspectRatio), outterHeight);
		}
	}
}
