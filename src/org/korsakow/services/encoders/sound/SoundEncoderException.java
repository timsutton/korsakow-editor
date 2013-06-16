package org.korsakow.services.encoders.sound;

import org.korsakow.services.encoders.EncoderException;

public class SoundEncoderException extends EncoderException
{
	public SoundEncoderException(Throwable cause)
	{
		super(cause);
	}
	public SoundEncoderException(String message)
	{
		super(message);
	}
}
