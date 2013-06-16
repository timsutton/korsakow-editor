package org.korsakow.ide.code.k5;

public class K5Symbol
{
	public static final char DEFAULT_STATEMENT_SEPARATOR = ',';
	/**
	 * Convenience. Equivalent to ""+DEFAULT_STATEMENT_SEPARATOR
	 */
	public static final String DEFAULT_STATEMENT_SEPARATOR_STRING = ""+DEFAULT_STATEMENT_SEPARATOR;

	public static final char EXCLUSION_KEYWORD = '-';
	public static final char REQUIRED_KEYWORD = '+';
	/**
	 * Special
	 */
	public static final String CLEAR_PREVIOUS_LINKS = "xxx";
	/**
	 * Special
	 */
	public static final String KEEP_PREVIOUS_LINKS = "---";
	/**
	 * Reserved keyword.
	 */
	public static final String RANDOM_KEYWORD = "_random";
	/**
	 * Reserved keyword.
	 */
	public static final String ENDFILM_KEYWORD = "_end";
}
