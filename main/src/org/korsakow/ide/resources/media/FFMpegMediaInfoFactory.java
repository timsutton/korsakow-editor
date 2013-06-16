package org.korsakow.ide.resources.media;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.korsakow.ide.io.AsyncStreamPipe;
import org.korsakow.ide.util.ExternalsResourceManager;
import org.korsakow.ide.util.Platform;


/**
 * According to FFMPEG:
		"Note, the names of encoders and decoders dont always match, so there are
		several cases where the above table shows encoder only or decoder only entries
		even though both encoding and decoding are supported for example, the h263
		decoder corresponds to the h263 and h263p encoders, for file formats its even
		worse"
 */
public class FFMpegMediaInfoFactory
{
	private static final Pattern durationPattern = Pattern.compile("Duration:\\s*([0-9]{2}):([0-9]{2}):([0-9]{2})[.]([0-9]+)");
	private static final Pattern videoPattern = Pattern.compile("Video:\\s([^,]+), (?:[^,]+, )?([0-9]{1,5})x([0-9]{1,5})");
	

	public static MediaInfo getInfo(File src) throws IOException, InterruptedException
	{
		Process process = Runtime.getRuntime().exec(new String[] {
			getFFMpegCommand().getAbsolutePath(), "-i", src.getAbsolutePath(),
		});
		AsyncStreamPipe<InputStream, ByteArrayOutputStream> errPiper = new AsyncStreamPipe<InputStream, ByteArrayOutputStream>(process.getErrorStream(), new ByteArrayOutputStream(), "LameEncoderOSX.errpipe");
		errPiper.start();
		// we avoid thread issues by basically not doing anything until all pipers have joined.
		errPiper.join();
		String output = errPiper.getOutputStream().toString();
		process.destroy();
		
		MediaInfo info = new MediaInfo();
		
		parseDuration(info, output);
		parseVideo(info, output);
		return info;
	}
	private static boolean parseVideo(MediaInfo info, String line)
	{
		Matcher matcher;
		matcher = videoPattern.matcher(line);
		if (matcher.find()) {
			info.codec = matcher.group(1);
			info.width = Integer.parseInt(matcher.group(2));
			info.height = Integer.parseInt(matcher.group(3));
			return true;
		}
		return false;
	}
	private static boolean parseDuration(MediaInfo info, String line)
	{
		Matcher matcher;
		matcher = durationPattern.matcher(line);
		if (matcher.find()) {
			int hours = Integer.parseInt(matcher.group(1));
			int minutes = Integer.parseInt(matcher.group(2));
			int seconds = Integer.parseInt(matcher.group(3));
			int millis = Integer.parseInt(matcher.group(4));
			long duration = 0L;
			duration += hours*60*60*1000;
			duration += minutes*60*1000;
			duration += seconds*1000;
			duration += millis;
			info.duration = duration;
			return true;
		}
		return false;
	}
	private static File getFFMpegCommand() throws IOException
	{
		String path = null;
		switch (Platform.getOS())
		{
		case MAC:
			path = ExternalsResourceManager.FFMPEG_OSX;
			break;
		case WIN:
			path = ExternalsResourceManager.FFMPEG_WIN;
			break;
		case NIX:
		default:
			throw new RuntimeException();
		}
		return ExternalsResourceManager.getExternalFile(path);
	}
}
