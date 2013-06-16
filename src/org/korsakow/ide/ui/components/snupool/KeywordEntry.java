package org.korsakow.ide.ui.components.snupool;

import org.korsakow.ide.ui.components.pool.ContentEntry;

public class KeywordEntry implements ContentEntry
{
	private final String keyword;
	private boolean isInKeyword;
	private boolean isOutKeyword;

	public KeywordEntry(String keyword)
	{
		this(keyword, false, false);
	}
	public KeywordEntry(String keyword, boolean isIn, boolean isOut)
	{
		this.keyword = keyword;
		isInKeyword = isIn;
		isOutKeyword = isOut;
	}
	
	public String getKeyword()
	{
		return keyword;
	}
	public void setInKeyword(boolean isIn)
	{
		isInKeyword = isIn;
	}
	public boolean isInKeyword()
	{
		return isInKeyword;
	}
	public void setOutKeyword(boolean isOut)
	{
		isOutKeyword = isOut;
	}
	public boolean isOutKeyword()
	{
		return isOutKeyword;
	}
	
	/**
	 * Used to sort. Ranks are:
	 * 3: isInKeyword && isOutKeyword
	 * 2: isInKeyword && !isOutKeyword
	 * 1: !isInKeyword && isOutKeyword
	 * 0: !isInKeyword && !isOutkeyword
	 * @param entry
	 * @return
	 */
	private static int getRank(KeywordEntry entry)
	{
		int rank = 0;
		if (entry.isInKeyword)
			rank += 2;
		if (entry.isOutKeyword)
			rank += 1;
		return rank;
	}
	public int compareTo(ContentEntry oo) {
		KeywordEntry o = (KeywordEntry)oo;
		int rank = getRank(this);
		int rank2 = getRank(o);
		if (rank == rank2)
			return keyword.compareTo(o.getKeyword());
		return rank > rank2 ? -1 : 1;
	}

}
