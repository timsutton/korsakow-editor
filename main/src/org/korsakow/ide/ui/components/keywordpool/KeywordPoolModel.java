package org.korsakow.ide.ui.components.keywordpool;

import org.korsakow.domain.interf.IKeyword;
import org.korsakow.ide.ui.components.pool.AbstractPoolModel;

public class KeywordPoolModel extends AbstractPoolModel<KeywordEntry>
{
	public void add(IKeyword keyword, long inCount, long outCount)
	{
		KeywordEntry entry = new KeywordEntry(keyword, inCount, outCount);
		super.add(entry);
	}
	public Object getId(KeywordEntry entry)
	{
		return entry.getKeyword().getValue();
	}
}
