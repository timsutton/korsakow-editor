package org.korsakow.ide.resources.media;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.korsakow.domain.MediaSource;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.ide.exception.MediaException;
import org.korsakow.ide.exception.MediaRuntimeException;
import org.korsakow.ide.util.FileUtil;
import org.korsakow.ide.util.Platform;

public class MediaFactory
{
	private static String extension_pattern = null;
	
	public static Playable getMediaNoThrow(IMedia media) {
		try {
			return getMediaNoThrow(media.getAbsoluteFilename());
		} catch (FileNotFoundException e) {
			return new UnsupportedMedia(media.getFilename());
		}
	}
	
	/**
	 * Swallows exceptions, instead returning a generic dummy media instance.
	 */
	public static Playable getMediaNoThrow(String filename) {
		try {
			return getMedia(filename);
		} catch (MediaRuntimeException e) {
			Logger.getLogger(MediaFactory.class).error("", e);
			return new UnsupportedMedia(filename);
		} catch (MediaException e) {
			Logger.getLogger(MediaFactory.class).error("", e);
			return new UnsupportedMedia(filename);
		}
	}
	public static Playable getMedia(String media_url) throws MediaException{
		Logger.getLogger(MediaFactory.class).trace("Loading " + media_url);
		String extension = FileUtil.getFileExtension(media_url).toLowerCase();
		
		try {
			// mostly a debugging thing, might want to remove so that actual urls could be used?
			// just to have a more obvious error than the media impl might give
			File test = new File(media_url);
			if (!test.canRead())
				throw new FileNotFoundException("Cannot read from file: " + media_url);
			
			String className = ResourceBundle.getBundle("MyResources").getString(Platform.getOS().getCanonicalName().toLowerCase()+"."+extension);
			Logger.getLogger(MediaFactory.class).trace("\tas: " + className);
			return (Playable) Class.forName(className).getConstructor(String.class).newInstance(media_url);
		} catch (FileNotFoundException e) {
			throw new MediaException(e);
		} catch (IllegalArgumentException e) {
			throw new MediaException(e);
		} catch (SecurityException e) {
			throw new MediaException(e);
		} catch (InstantiationException e) {
			throw new MediaException(e);
		} catch (IllegalAccessException e) {
			throw new MediaException(e);
		} catch (InvocationTargetException e) {
			throw new MediaException(e);
		} catch (NoSuchMethodException e) {
			throw new MediaException(e);
		} catch (ClassNotFoundException e) {
			throw new MediaException(e);
		}

	}
	public static Playable getText(MediaSource source, String value) throws MediaException {
		switch (source)
		{
		case INLINE:
			return new DefaultText(value);
		case FILE:
			try {
				return new DefaultText(new File(value));
			} catch (IOException e) {
				throw new MediaException(e);
			}
		default:
			throw new MediaException("");
		}
	}
	
	public static String getAcceptedExpressionPattern() {
		if(extension_pattern == null) {
			extension_pattern = ResourceBundle.getBundle("MyResources").getString("extension_"+Platform.getOS().getCanonicalName());
		}
		return extension_pattern;
	}
	
}
