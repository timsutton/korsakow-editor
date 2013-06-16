package org.korsakow.ide.resources.media;



/**
 * Depends on QTSession.open being called.
 * We -could- open/close it here, but for now I choose not to.
 * 
 * Originally (see SVN history) this was implemented via FFMPEG but a build that fixed
 * other issues subsequently failed to accurately report duration!!!
 * 
 * @author d
 *
 */
public class MediaInfo
{
	public int width;
	public int height;
	public long duration;
	public String codec;
}
