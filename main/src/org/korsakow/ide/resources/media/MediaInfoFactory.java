package org.korsakow.ide.resources.media;

import java.io.File;

import org.korsakow.ide.exception.MediaRuntimeException;


public class MediaInfoFactory
{
	public static MediaInfo getInfo(File src)
	{
		// todo: do like MediaFactory and choose based on config file
		try {
			return QTMediaInfoFactory.getInfo(src);
		} catch (MediaRuntimeException e) {
			try {
				return FFMpegMediaInfoFactory.getInfo(src);
			} catch (Exception e2) {
				throw new MediaRuntimeException(e2);
			}
		}
	}
}
