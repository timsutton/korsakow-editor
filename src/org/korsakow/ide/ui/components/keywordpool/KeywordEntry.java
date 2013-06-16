package org.korsakow.ide.ui.components.keywordpool;

import java.util.Collection;

import org.korsakow.domain.interf.IKeyword;
import org.korsakow.ide.ui.components.pool.HeaderEntry;

public class KeywordEntry implements HeaderEntry
{
	private IKeyword keyword = null;
	private Collection<Long> inSnus = null;
	private Collection<Long> outSnus = null;
	private final long inCount;
	private final long outCount;
	public KeywordEntry(IKeyword keyword, long inCount, long outCount)
	{
		this.keyword = keyword;
		this.inCount = inCount;
		this.outCount = outCount;
	}
	public IKeyword getKeyword()
	{
		return keyword;
	}
	public void setInSnus(Collection<Long> snus)
	{
		inSnus = snus;
	}
	public Collection<Long> getInSnus()
	{
		return inSnus;
	}
	public long getInCount()
	{
		return inSnus!=null?inSnus.size():inCount;
	}
	public void setOutSnus(Collection<Long> snus)
	{
		outSnus = snus;
	}
	public Collection<Long> getOutSnus()
	{
		return outSnus;
	}
	public long getOutCount()
	{
		return outSnus!=null?outSnus.size():outCount;
	}
	public int compareTo(HeaderEntry oo) {
		KeywordEntry o = (KeywordEntry)oo;
		return keyword.getValue().compareTo(o.getKeyword().getValue());
	}
}
