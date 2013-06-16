package org.korsakow.domain.k3.code;

public class K3Symbol
{
	public static final char DEFAULT_WHITESPACE = ' ';
	/**
	 * Convenience. Equivalent to ""+DEFAULT_WHITESPACE
	 */
	public static final String DEFAULT_WHITESPACE_STRING = ""+DEFAULT_WHITESPACE;
	public static final char LOOKUP_KEYWORD = '>';
	public static final char INBOUND_KEYWORD = '<';
	public static final char EXCLUSION_KEYWORD = '-';
	public static final char REQUIRED_KEYWORD = '+';
	/**
	 */
	public static final String CLEAR_PREVIOUS_LINKS = "xxx";
	/**
	 */
	public static final String KEEP_PREVIOUS_LINKS = "---";
	/**
	 * Reserved keyword.
	 */
	public static final String RANDOM_KEYWORD = "random";
	/**
	 * Reserved keyword.
	 */
	public static final String ENDFILM_KEYWORD = "end";
}
