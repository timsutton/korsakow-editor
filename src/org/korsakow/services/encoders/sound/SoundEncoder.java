package org.korsakow.services.encoders.sound;

import java.io.File;
import java.util.Collection;

import org.korsakow.services.encoders.EncoderException;
import org.korsakow.services.encoders.IEncoder;

public interface SoundEncoder extends IEncoder
{
	static interface SoundEncoderDescription
	{
		Collection<SoundFormat> getSupportedInputFormats();
		Collection<SoundFormat> getSupportedOutputFormats();
		Class<? extends SoundEncoder> getEncoderClass();
	}
	void encode(SoundFormat destFormat, File src, File dst) throws EncoderException, InterruptedException;
}
