package org.korsakow.ide.code.k5;

/**
 * K5 rules are made of lexemes, each of which consists of a symbol/token pair.
 * 
 * @author d
 *
 */
public class K5Lexeme
{
	private K5OpType opType;
	private Character symbol;
	private String token;
	public K5Lexeme(K5OpType opType, String token)
	{
		this(opType, null, token);
	}
	public K5Lexeme(K5OpType opType, Character symbol, String token)
	{
		this.opType = opType;
		this.symbol = symbol;
		this.token = token;
	}
	public K5OpType getOpType()
	{
		return opType;
	}
	public Character getSymbol()
	{
		return symbol;
	}
	public String getToken()
	{
		return token;
	}
}
