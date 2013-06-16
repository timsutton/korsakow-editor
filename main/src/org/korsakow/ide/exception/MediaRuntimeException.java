package org.korsakow.ide.exception;

public class MediaRuntimeException extends RuntimeException
{
	public MediaRuntimeException(String msg)
	{
		super(msg);
	}
	public MediaRuntimeException(Throwable cause)
	{
		super(cause);
	}
}
