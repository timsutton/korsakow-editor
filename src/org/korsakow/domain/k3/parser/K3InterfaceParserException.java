/**
 * 
 */
package org.korsakow.domain.k3.parser;

public class K3InterfaceParserException extends K3ParserException
{

	public K3InterfaceParserException(String msg) {
		super(msg);
	}

	public K3InterfaceParserException(Throwable cause, int lineNumber) {
		super(cause, lineNumber);
	}

	public K3InterfaceParserException(Throwable cause) {
		super(cause);
	}

	public K3InterfaceParserException(String msg, int lineNumber) {
		super(msg, lineNumber);
	}
}