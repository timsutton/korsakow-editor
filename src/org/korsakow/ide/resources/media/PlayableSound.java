package org.korsakow.ide.resources.media;


/**
 * Represents a media that can be controlled and displayed.
 * @author d
 *
 */
public interface PlayableSound extends Playable
{
	public void setVolume(float volume);
	public float getVolume();
}
