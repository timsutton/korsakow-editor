package org.korsakow.services.conversion;

public class ConversionException extends Exception
{

	public ConversionException()
	{
	}

	public ConversionException(String message)
	{
		super(message);

	}

	public ConversionException(Throwable cause)
	{
		super(cause);

	}

	public ConversionException(String message, Throwable cause)
	{
		super(message, cause);

	}

}
