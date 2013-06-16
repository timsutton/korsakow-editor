package org.korsakow.ide.ui.components.snupool;

import java.util.Collection;

import org.korsakow.ide.ui.components.pool.AbstractPoolModel;

public class SnuPoolModel extends AbstractPoolModel<SnuEntry>
{
	public void add(long snuId, String snuName, Collection<String> inKeywords, Collection<String> outKeywords)
	{
		SnuEntry entry = new SnuEntry(snuId, snuName, inKeywords, outKeywords);
		super.add(entry);
		entrymap.put(snuId, entry);
	}
	@Override
	public Object getId(SnuEntry entry)
	{
		return entry.getSnuId();
	}
}
