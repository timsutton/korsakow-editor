package org.korsakow.ide.ui.components.linkpool;

import org.korsakow.ide.ui.components.pool.ContentEntry;

public class SnuContentEntry implements ContentEntry
{
	private final long snuId;
	private final String snuName;
	public SnuContentEntry(long snuId, String snuName)
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
	public int compareTo(ContentEntry oo)
	{
		SnuContentEntry o = (SnuContentEntry)oo;
		return getSnuName().compareTo(o.getSnuName());
	}

}
