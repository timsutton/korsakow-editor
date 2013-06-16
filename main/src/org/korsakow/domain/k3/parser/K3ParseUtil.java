package org.korsakow.domain.k3.parser;


public class K3ParseUtil
{
	private LineContext context;
	public K3ParseUtil(LineContext context)
	{
		this.context = context;
	}
	/**
	 * Checkes the value against a special value which represents null.
	 * Returns null if value.equals(specialNull), value otherwise
	 * @param <Type>
	 * @param value
	 * @param specialNull
	 * @return value or null
	 */
	public  <Type> Type specialNullValue(Type value, Type specialNull)
	{
		return value.equals(specialNull)?null:value;
	}
	public  String parseString(String str)
	{
		return str.trim();
	}
	public  String parseStringPrefix(String str, String prefix) throws K3ParserException
	{
		return parseString(stripPrefix(str, prefix));
	}
	public  int parseInt(String str) throws K3ParserException
	{
		try {
			return Integer.parseInt(parseString(str));
		} catch (NumberFormatException e) {
			throw new K3ParserException(e, context.currentLine+1);
		}
	}
	public  int parseInt(String str, String prefix) throws K3ParserException
	{
		return parseInt(stripPrefix(str, prefix));
	}
	public  Long parseLong(String str) throws K3ParserException
	{
		try {
			return Long.parseLong(parseString(str));
		} catch (NumberFormatException e) {
			throw new K3ParserException(e, context.currentLine+1);
		}
	}
	/**
	 * returns true iff parseString(str).equals(truthValue)
	 * @param str
	 * @param truthValue
	 * @return
	 */
	public  boolean parseBooleanValue(String str, String truthValue) throws K3ParserException
	{
		return parseString(str).equals(truthValue);
	}
	public  boolean parseBooleanPrefix(String str, String prefix) throws K3ParserException
	{
		return parseBoolean(stripPrefix(str, prefix));
	}
	public  boolean parseBoolean(String str) throws K3ParserException
	{
		return parseInt(str)==0?false:true;
	}
	public  float parseFloat(String str) throws K3ParserException
	{
		try {
			return Float.parseFloat(parseString(str));
		} catch (NumberFormatException e) {
			throw new K3ParserException(e, context.currentLine+1);
		}
	}
	public  float parseFloat(String str, String prefix) throws K3ParserException
	{
		return parseFloat(stripPrefix(str, prefix));
	}
	public  String stripPrefix(String str, String prefix) throws K3ParserException
	{
		str = str.trim();
		if (!str.startsWith(prefix))
			throw new K3ParserException("expecting: '" + prefix + "', found: '" + str + "'", context.currentLine+1);
		return str.substring(prefix.length());
	}
}
