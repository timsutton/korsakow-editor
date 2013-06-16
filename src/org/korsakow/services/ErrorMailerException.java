package org.korsakow.services;

public class ErrorMailerException extends Exception
{

	public ErrorMailerException()
	{
		super();
		
	}

	public ErrorMailerException(String message, Throwable cause)
	{
		super(message, cause);
		
	}

	public ErrorMailerException(String message)
	{
		super(message);
		
	}

	public ErrorMailerException(Throwable cause)
	{
		super(cause);
		
	}

}
