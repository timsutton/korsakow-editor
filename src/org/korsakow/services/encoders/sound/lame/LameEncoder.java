package org.korsakow.services.encoders.sound.lame;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.korsakow.services.encoders.EncoderException;
import org.korsakow.services.encoders.UnsupportedFormatException;
import org.korsakow.services.encoders.sound.FileExternalSoundEncoder;
import org.korsakow.services.encoders.sound.SoundEncoder;
import org.korsakow.services.encoders.sound.SoundFormat;

public abstract class LameEncoder extends FileExternalSoundEncoder
{
	public abstract static class LameEncoderDescription implements SoundEncoder.SoundEncoderDescription
	{
		private static final Collection<SoundFormat> inputFormats = Collections.unmodifiableCollection(Arrays.asList(
				SoundFormat.WAV
		));
		private static final Collection<SoundFormat> outputFormats = Collections.unmodifiableCollection(Arrays.asList(
				SoundFormat.MP3
		));
		public Collection<SoundFormat> getSupportedInputFormats() {
			return inputFormats;
		}
		public Collection<SoundFormat> getSupportedOutputFormats() {
			return outputFormats;
		}
	}
//	@Override
//	public void encode(Process process, InputStream sourceStream, OutputStream destStream) throws EncoderException
//	{
//		try {
//			super.encode(process, sourceStream, destStream);
//		} catch (EncoderException e) {
//			if (e.getMessage().contains("unsupported audio format"))
//				throw new UnsupportedFormatException(e.getMessage(), e.getDetails(), e.getFile());
//			throw e;
//		}
//	}
	@Override
	protected void encode(Process process, File sourceFile, File destFile) throws EncoderException, InterruptedException
	{
		try {
			super.encode(process, sourceFile, destFile);
		} catch (EncoderException e) {
			if (e.getMessage().contains("unsupported audio format"))
				throw new UnsupportedFormatException(e.getMessage(), e.getDetails(), e.getFile());
			throw e;
		}
	}
}
