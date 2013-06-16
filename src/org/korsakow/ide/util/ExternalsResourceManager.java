package org.korsakow.ide.util;

import java.io.File;
import java.io.IOException;

/**
 * Externals are anything that must be present in the filesystem in order to work. For example platform executables.
 * 
 * @author d
 *
 */
public class ExternalsResourceManager
{
	public static final String FLEX_SDK = "flex/flex_sdk.zip";
	public static final String FFMPEG = "ffmpeg/";
	public static final String FFMPEG_PRESETS = "ffmpeg/ffpresets";
	public static final String FFMPEG_OSX = "ffmpeg/osx/ffmpeg";
	public static final String FFMPEG_WIN = "ffmpeg/win32/ffmpeg.exe";
	public static final String YAMDI_OSX = "yamdi/osx/yamdi";
	public static final String YAMDI_WIN = "yamdi/win32/yamdi.exe";
	public static final String MP4BOX_OSX = "mp4box/osx/MP4Box";
	public static final String MP4BOX_WIN = "mp4box/win32/MP4Box.exe";
	public static final String MP4BOX_WIN_DLL = "mp4box/win32/js32.dll";
	public static final String LAME_OSX = "lame/osx/lame";
	public static final String LAME_WIN = "lame/win32/lame.exe";
	public static final String LAME_WIN_DLL = "lame/win32/lame_enc.dll";
	
	public static File getPlatformScript(String name) throws IOException
	{
		String path = "script/";
		switch (Platform.getOS())
		{
		case MAC:
			path = "scripts/osx/";
			break;
		case WIN:
			path = "scripts/win/";
			break;
		case NIX:
			path = "scripts/nix/";
			break;
		}
		path += name;
		File file = getExternalFile(path);
		return file;
	}
	
	public static File getExternalFile(String name)
	{
		return ResourceManager.getResourceFile(name);
	}
}
