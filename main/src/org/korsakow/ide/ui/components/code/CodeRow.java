/**
 * 
 */
package org.korsakow.ide.ui.components.code;

import org.korsakow.ide.code.k5.K5Code;

public class CodeRow implements Comparable
{
	private Long time;
	private K5Code code;
	private Long maxLinks;
	
	
	public CodeRow(Long time, K5Code code, Long maxLinks )
	{
		this.time = time;
		this.code = code;
		this.maxLinks= maxLinks;
	}

	public int compareTo(Object o) {
		CodeRow other = (CodeRow)o;
		if (time == null) {
			if (other.time == null)
				return 0;
			return 1;
		} else {
			if (other.time == null)
				return -1;
		}
		if (time == other.time) {
			if (code.getRawCode().trim().isEmpty())
				return -1;
			if (other.code.getRawCode().trim().isEmpty())
				return 1;
			return 0;
		} else
			return (int)(time - other.time);
	}
	public Long getTime()
	{
		return time;
	}
	public K5Code getCode()
	{
		return code;
	}
	public Long getMaxLinks()
	{
		return maxLinks;
	}
	Object getValueAt(int column)
	{
		switch (column)
		{
		case CodeTableModel.TIME_COLUMN: return time;
		case CodeTableModel.CODE_COLUMN: return code;
		case CodeTableModel.MAXLINKS_COLUMN: return maxLinks;
		default: throw new IllegalArgumentException("invalid column: " + column);
		}
	}
	void setValueAt(int column, Object value)
	{
		switch (column)
		{
		case CodeTableModel.TIME_COLUMN:
			time = (Long)value;
			break;
		case CodeTableModel.CODE_COLUMN:
			code = (K5Code)value;
			break;
		case CodeTableModel.MAXLINKS_COLUMN:
			maxLinks = (Long)value;
			break;
		default: throw new IllegalArgumentException("invalid column: " + column);
		}
	}
	@Override
	public String toString()
	{
		return String.format("Row; time=%d; code=%s; maxlinks=%d", time, code!=null?code.getRawCode():null, maxLinks);
	}
}