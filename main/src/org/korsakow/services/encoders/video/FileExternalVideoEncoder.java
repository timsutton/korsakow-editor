package org.korsakow.services.encoders.video;

import java.io.File;
import java.io.IOException;

import org.korsakow.services.encoders.EncoderException;
import org.korsakow.services.encoders.FileExternalEncoder;

public abstract class FileExternalVideoEncoder extends FileExternalEncoder implements VideoEncoder
{
	protected abstract Process createProcess(VideoCodec srcFormat, File srcFile, File dstFile) throws IOException;
	
	public void encode(VideoCodec srcFormat, File srcFile,
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
