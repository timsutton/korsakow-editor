package org.korsakow.services.encoders.sound;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.korsakow.services.encoders.EncoderException;
import org.korsakow.services.encoders.PipedExternalEncoder;

public abstract class PipedExternalSoundEncoder extends PipedExternalEncoder implements SoundEncoder
{
	protected abstract Process createProcess(SoundFormat destFormat) throws IOException;
	
	public void encode(InputStream sourceStream,
			SoundFormat destFormat, OutputStream destStream)
			throws EncoderException
	{
		Process process = null;
		try {
			process = createProcess(destFormat);
		} catch (IOException e) {
			throw new SoundEncoderException(e);
		}
		encode(process, sourceStream, destStream);
	}
}
