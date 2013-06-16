package org.korsakow.domain.k3.code;

public enum  K3OpType
{
	/**
	 * xxx
	 */
	CLEAR_PREVIOUS_LINKS,
	/**
	 * ---
	 */
	KEEP_PREVIOUS_LINKS,
	/**
	 * >
	 */
	KEYWORD_LOOKUP,
	/**
	 * <
	 */
	INBOUND_KEYWORD,
	/**
	 * -
	 */
	KEYWORD_EXCLUSION,
	/**
	 * +
	 */
	KEYWORD_REQUIRED,
	RANDOM_LOOKUP,
	INBOUND_AND_LOOKUP_KEYWORD,
}