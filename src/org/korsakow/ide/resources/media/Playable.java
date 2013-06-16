package org.korsakow.ide.resources.media;

import java.awt.Component;
import java.awt.Dimension;

/**
 * Represents a media that can be controlled and displayed.
 * Media that doesnt support an operation is expected to just do nothing.
 * 
 * @author d
 *
 */
public interface Playable
{
	/**
	 * If the operation does not make sense for the playable, the return is a 0,0 dimension
	 * @return never null.
	 */
	Dimension getAspectRespectingDimension(Dimension outter);
	void setTime(long time);
	long getTime();
	long getDuration();
	void start();
	void stop();
	boolean isPlaying();
	boolean isTemporal();
	Component getComponent();
	void dispose();
}
