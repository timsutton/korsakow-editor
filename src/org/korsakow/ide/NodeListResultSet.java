/**
 * 
 */
package org.korsakow.ide;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

import org.korsakow.ide.util.DomUtil;
import org.korsakow.services.exception.BetterSQLException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Null is represented as the absence of an expected node.
 * @author d
 *
 */
public class NodeListResultSet extends UnsupportedResultSet
{
	private NodeList list;
	private int index = -1;
	public NodeListResultSet(NodeList list)
	{
		if (list == null)
			throw new NullPointerException();
		this.list = list;
	}
	private Element getCurrent() throws SQLException
	{
		if (list == null)
			throw new SQLException("ResultSet already closed");
		if (index < 0 || index > list.getLength()-1)
			throw new SQLException("index out of bounds: " + index);
		if (list.item(index) == null)
			throw new NullPointerException();
		return (Element)list.item(index);
	}
	public void close() throws SQLException {
		list = null;
	}
	public boolean getBoolean(String columnName) throws SQLException {
		try {
			return Boolean.parseBoolean(getString(columnName));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new BetterSQLException(e);
		}
	}
	public byte getByte(String columnName) throws SQLException {
		try {
			return Byte.parseByte(getString(columnName));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new BetterSQLException(e);
		}
	}
	public Date getDate(String columnName) throws SQLException {
		return Date.valueOf(getString(columnName));
	}
	public double getDouble(String columnName) throws SQLException {
		try {
			return Double.parseDouble(getString(columnName));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new BetterSQLException(e);
		}
	}
	public float getFloat(String columnName) throws SQLException {
		try {
			return Float.parseFloat(getString(columnName));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new BetterSQLException(e);
		}
	}
	public int getInt(String columnName) throws SQLException {
		try {
			return Integer.parseInt(getString(columnName));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new BetterSQLException(e);
		}
	}
	public long getLong(String columnName) throws SQLException {
		try {
			return Long.parseLong(getString(columnName));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new BetterSQLException(e);
		}
	}
	public Object getObject(String columnName) throws SQLException {
		return getString(columnName);
	}
	public int getRow() throws SQLException {
		return index;
	}
	public short getShort(String columnName) throws SQLException {
		try {
			return Short.parseShort(getString(columnName));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new BetterSQLException(e);
		}
	}
	public String getString(String columnName) throws SQLException {
		return DomUtil.getString(getCurrent(), columnName);
	}
	public Time getTime(String columnName) throws SQLException {
		return new Time(getLong(columnName));
	}
	public Timestamp getTimestamp(String columnName) throws SQLException {
		return new Timestamp(getLong(columnName));
	}
	public URL getURL(String columnName) throws SQLException {
		try {
			return new URL(getString(columnName));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new BetterSQLException(e);
		}
	}
	public boolean next() throws SQLException {
		if (list == null)
			throw new SQLException("ResultSet already closed");
		if (list.getLength() == 0)
			return false;
		if (index > list.getLength()-2)
			return false;
		++index;
		return true;
	}

	public boolean previous() throws SQLException {
		if (list == null)
			throw new SQLException("ResultSet already closed");
		if (index < 1)
			return false;
		if (list.getLength() == 0)
			return false;
		--index;
		return true;
	}
}