package org.korsakow.ide.resources.media;

import java.awt.Component;
import java.io.File;


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
public class QTMediaInfoFactory
{
	public int width;
	public int height;
	public long duration;
	
	public static MediaInfo getInfo(File src)
	{
		QTVideo video = null;
		try {
			MediaInfo info = new MediaInfo();
			
			video = new QTVideo(src.getPath());
			Component comp = video.getComponent();
			info.width = comp.getWidth();
			info.height = comp.getHeight();
			info.duration = video.getDuration();
			return info;
		} finally {
			try { if (video != null) video.dispose(); } catch (Throwable t) {}
		}
	}
}
