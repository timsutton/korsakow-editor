package org.korsakow.ide.ui.components.linkpool;

import org.korsakow.ide.ui.components.pool.AbstractPoolModel;

public class LinkPoolModel extends AbstractPoolModel<SnuHeaderEntry>
{
	@Override
	public void add(SnuHeaderEntry entry)
	{
		super.add(entry);
		entrymap.put(entry.getSnuId(), entry);
	}
	@Override
	public Object getId(SnuHeaderEntry entry)
	{
		return entry.getSnuId();
	}
}
