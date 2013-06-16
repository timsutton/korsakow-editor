package org.korsakow.services.encoders;

import java.io.File;

public class UnsupportedFormatException extends EncoderException
{
	public UnsupportedFormatException(Throwable cause)
	{
		super(cause);
	}
	public UnsupportedFormatException(String message)
	{
		super(message);
	}
	public UnsupportedFormatException(Throwable cause, File file)
	{
		super(cause, file);
	}
	public UnsupportedFormatException(String message, File file)
	{
		super(message, file);
	}
	public UnsupportedFormatException(String message, String details, File file)
	{
		super(message, details, file);
	}
}
