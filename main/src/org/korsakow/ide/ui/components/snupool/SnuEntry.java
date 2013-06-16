package org.korsakow.ide.ui.components.snupool;

import java.util.Collection;

import org.korsakow.ide.ui.components.pool.HeaderEntry;

public class SnuEntry implements HeaderEntry
{
	private final long snuId;
	private final String snuName;
	private final Collection<String> inKeywords;
	private final Collection<String> outKeywords;
	public SnuEntry(long snuId, String snuName, Collection<String> inKeywords, Collection<String> outKeywords)
	{
		this.snuId = snuId;
		this.snuName = snuName;
		this.inKeywords = inKeywords;
		this.outKeywords = outKeywords;
	}
	public long getSnuId()
	{
		return snuId;
	}
	public String getSnuName()
	{
		return snuName;
	}
	public Collection<String> getInKeywords()
	{
		return inKeywords;
	}
	public Collection<String> getOutKeywords()
	{
		return outKeywords;
	}
	public int compareTo(HeaderEntry oo)
	{
		SnuEntry o = (SnuEntry)oo;
		return snuName.compareTo(o.getSnuName());
	}

}
