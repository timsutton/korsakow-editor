package org.korsakow.ide.ui.components.linkpool;

import org.korsakow.ide.ui.components.pool.HeaderEntry;

public class SnuHeaderEntry implements HeaderEntry
{
	private final long snuId;
	private final String snuName;
	public SnuHeaderEntry(long snuId, String snuName)
	{
		this.snuId = snuId;
		this.snuName = snuName;
	}
	public long getSnuId() {
		return snuId;
	}
	public String getSnuName() {
		return snuName;
	}
	public int compareTo(HeaderEntry oo)
	{
		SnuHeaderEntry o = (SnuHeaderEntry)oo;
		return getSnuName().compareTo(o.getSnuName());
	}

}
