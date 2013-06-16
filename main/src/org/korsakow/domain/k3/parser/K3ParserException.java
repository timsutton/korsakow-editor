/**
 * 
 */
package org.korsakow.domain.k3.parser;

public class K3ParserException extends Exception
{
	private int lineNumber;
	public K3ParserException(Throwable cause)
	{
		this(cause, -1);
	}
	public K3ParserException(Throwable cause, int lineNumber)
	{
		super(cause);
		this.lineNumber = lineNumber;
	}
	public K3ParserException(String msg)
	{
		this(msg, -1);
	}
	public K3ParserException(String msg, int lineNumber)
	{
		super(msg);
		this.lineNumber = lineNumber;
	}
	public int getLineNumber()
	{
		return lineNumber;
	}
}