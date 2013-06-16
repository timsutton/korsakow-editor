package org.korsakow.services.encoders.image;

import java.io.File;

import org.korsakow.services.encoders.EncoderException;

public class ImageEncoderException extends EncoderException
{
	public ImageEncoderException(Throwable cause)
	{
		super(cause);
	}
	public ImageEncoderException(String message)
	{
		super(message);
	}
	public ImageEncoderException(Throwable cause, File file)
	{
		super(cause, file);
	}
	public ImageEncoderException(String message, File file)
	{
		super(message, file);
	}
}
