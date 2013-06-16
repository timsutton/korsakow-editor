package org.korsakow.ide.ui.components.keywordpool;

import org.korsakow.ide.ui.components.pool.ContentEntry;

public class SnuEntry implements ContentEntry
{
	private final long snuId;
	private final String snuName;
	private final boolean isInKeyword;
	private final boolean isOutKeyword;
	public SnuEntry(long snuId, String snuName, boolean inKeyword, boolean outKeyword)
	{
		this.snuId = snuId;
		this.snuName = snuName;
		isInKeyword = inKeyword;
		isOutKeyword = outKeyword;
	}
	public boolean isInKeyword()
	{
		return isInKeyword;
	}
	public boolean isOutKeyword()
	{
		return isOutKeyword;
	}
	public long getSnuId() {
		return snuId;
	}
	public String getSnuName() {
		return snuName;
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
	private static int getRank(SnuEntry entry)
	{
		int rank = 0;
		if (entry.isInKeyword)
			rank += 2;
		if (entry.isOutKeyword)
			rank += 1;
		return rank;
	}
	public int compareTo(ContentEntry oo) {
		SnuEntry o = (SnuEntry)oo;
		int rank = getRank(this);
		int rank2 = getRank(o);
		if (rank == rank2)
			return getSnuName().compareTo(o.getSnuName());
		return rank > rank2 ? -1 : 1;
	}
}
