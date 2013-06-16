package org.korsakow.services.encoders;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.korsakow.ide.util.FileUtil;

public abstract class FileExternalEncoder implements IEncoder
{
	protected void encode(Process process, File sourceFile, File destFile)
			throws EncoderException, InterruptedException
	{
		OutputStream errBuff = new ByteArrayOutputStream();
		OutputStream outBuff = new ByteArrayOutputStream();
		int exitCode;

		exitCode = FileUtil.executeProcess(process, outBuff, errBuff);

		Logger.getLogger(getClass()).info(outBuff.toString());
		Logger.getLogger(getClass()).info(errBuff.toString());
		if (0 != exitCode) {
			throw new EncoderException(String.format("unknown error: code=%d, output=%s", exitCode, errBuff.toString()), errBuff.toString(), sourceFile);
		}
	}
}
