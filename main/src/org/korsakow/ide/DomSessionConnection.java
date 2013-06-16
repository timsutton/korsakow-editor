/**
 * 
 */
package org.korsakow.ide;

import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.NClob;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Struct;
import java.util.Properties;

import org.dsrg.soenea.service.SoenEAConnection;

public class DomSessionConnection extends SoenEAConnection implements Connection
{
	
	public static class UnsupportedFeatureException extends SQLException
	{
		public UnsupportedFeatureException()
		{
			super("Unsupported Feature");
		}
	}
	public DomSessionConnection()
	{
		super(new DummyConnection());
	}
	@Override
	public void close() throws SQLException {
		// sadly SOENEA has some threadlcal thing that closes the connection when the thread dies
		// which fucks our shit up since we have these swingworker threads coming and going
		// so we dont actually clean up ehre
		// and depend on the GC
//			System.out.println("closing: " + this);
//			dom = null;
	}
	@Override
	public void commit() throws SQLException {
		DataRegistry.commit();
	}
	@Override
	public void rollback() throws SQLException {
		DataRegistry.rollback();
	}
	@Override
	public void lockTable(String tableName) throws SQLException {
	}
	@Override
	public void unlockTable(String tableName) throws SQLException {
	}
	@Override
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException
	{
		throw new UnsupportedFeatureException();
	}
	@Override
	public Blob createBlob() throws SQLException
	{
		throw new UnsupportedFeatureException();
	}
	@Override
	public Clob createClob() throws SQLException
	{
		throw new UnsupportedFeatureException();
	}
	@Override
	public NClob createNClob() throws SQLException
	{
		throw new UnsupportedFeatureException();
	}
	@Override
	public SQLXML createSQLXML() throws SQLException
	{
		throw new UnsupportedFeatureException();
	}
	@Override
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException
	{
		throw new UnsupportedFeatureException();
	}
	@Override
	public Properties getClientInfo() throws SQLException
	{
		throw new UnsupportedFeatureException();
	}
	@Override
	public String getClientInfo(String name) throws SQLException
	{
		throw new UnsupportedFeatureException();
	}
	@Override
	public boolean isValid(int timeout) throws SQLException
	{
		return false;
	}
	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException
	{
	}
	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException
	{
	}
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException
	{
		return false;
	}
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException
	{
		return null;
	}
}
