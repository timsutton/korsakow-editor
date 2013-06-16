package org.korsakow.domain.k3.code;

/**
 * K3 rules are made of lexemes, each of which consists of a symbol/token pair.
 * 
 * @author d
 *
 */
public class K3Lexeme
{
	private K3OpType opType;
	private Character symbol;
	private String token;
	public K3Lexeme(K3OpType opType, String token)
	{
		this(opType, null, token);
	}
	public K3Lexeme(K3OpType opType, Character symbol, String token)
	{
		this.opType = opType;
		this.symbol = symbol;
		this.token = token;
	}
	public K3OpType getOpType()
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
