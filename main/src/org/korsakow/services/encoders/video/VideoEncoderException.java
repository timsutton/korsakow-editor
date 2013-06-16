package org.korsakow.services.encoders.video;

import java.io.File;

import org.korsakow.services.encoders.EncoderException;

public class VideoEncoderException extends EncoderException
{
	public VideoEncoderException(Throwable cause)
	{
		super(cause);
	}
	public VideoEncoderException(String message)
	{
		super(message);
	}
	public VideoEncoderException(Throwable cause, File file)
	{
		super(cause, file);
	}
	public VideoEncoderException(String message, File file)
	{
		super(message, file);
	}
}
