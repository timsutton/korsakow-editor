package org.korsakow.ide.resources.media;

import java.awt.Dimension;

/**
 * Represents a media that can be controlled and displayed.
 * @author d
 *
 */
public interface PlayableVideo extends Playable
{
	public void setVolume(float volume);
	public float getVolume();
	Dimension getAspectRespectingDimension(Dimension outter);
}
