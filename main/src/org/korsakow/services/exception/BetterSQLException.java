package org.korsakow.services.exception;

import java.sql.SQLException;

public class BetterSQLException extends SQLException {
	private Throwable cause;
	public BetterSQLException(String message, Throwable cause)
	{
		super(message);
		this.cause = cause;
	}
	public BetterSQLException(Throwable cause)
	{
		this((cause==null ? null : cause.toString()), cause);
	}
	public Throwable getCause()
	{
		return this.cause;
	}
}
