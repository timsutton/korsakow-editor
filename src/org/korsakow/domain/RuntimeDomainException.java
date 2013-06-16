package org.korsakow.domain;

public class RuntimeDomainException extends RuntimeException
{
	public RuntimeDomainException(String message)
	{
		super(message);
	}
	public RuntimeDomainException(Throwable cause)
	{
		super(cause);
	}
}
