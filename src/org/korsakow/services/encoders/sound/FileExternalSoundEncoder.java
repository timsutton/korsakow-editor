package org.korsakow.services.encoders.sound;

import java.io.File;
import java.io.IOException;

import org.korsakow.services.encoders.EncoderException;
import org.korsakow.services.encoders.FileExternalEncoder;
import org.korsakow.services.encoders.video.VideoEncoderException;

public abstract class FileExternalSoundEncoder extends FileExternalEncoder implements SoundEncoder
{
	protected abstract Process createProcess(SoundFormat srcFormat, File srcFile, File dstFile) throws IOException;
	
	public void encode(SoundFormat srcFormat, File srcFile,
			File destFile)
			throws EncoderException, InterruptedException
	{
		Process process = null;
		try {
			process = createProcess(srcFormat, srcFile, destFile);
		} catch (IOException e) {
			throw new VideoEncoderException(e, srcFile);
		}
		encode(process, srcFile, destFile);
	}
}
