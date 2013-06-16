package org.korsakow.ide.resources.media;

import java.awt.Dimension;

/**
 * Represents a media that can be controlled and displayed.
 * @author d
 *
 */
public interface PlayableImage extends Playable
{
	Dimension getAspectRespectingDimension(Dimension outter);
	void setDuration(long duration);
}
