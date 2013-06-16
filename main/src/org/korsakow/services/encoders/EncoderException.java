package org.korsakow.services.encoders;

import java.io.File;

public class EncoderException extends Exception
{
	private File file;
	private String details;
	public EncoderException(Throwable cause)
	{
		super(cause);
	}
	public EncoderException(String message)
	{
		super(message);
	}
	public EncoderException(Throwable cause, File file)
	{
		super(cause);
		this.file = file;
	}
	public EncoderException(String message, File file)
	{
		super(message);
		this.file = file;
	}
	public EncoderException(String message, String details, File file)
	{
		super(message);
		this.details = details;
		this.file = file;
	}
	public File getFile()
	{
		return file;
	}
	public String getDetails()
	{
		return details;
	}
}
