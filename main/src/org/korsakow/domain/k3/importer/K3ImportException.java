package org.korsakow.domain.k3.importer;

import java.io.File;

public class K3ImportException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5444675961922632849L;
	File databaseFile;
	File interfaceFile;
	
	public File getDatabaseFile() {
		return databaseFile;
	}
	public File getInterfaceFile() {
		return interfaceFile;
	}
	public K3ImportException(String msg, File databaseFile, File interfaceFile)
	{
		super(msg);
		this.databaseFile = databaseFile;
		this.interfaceFile = interfaceFile;
	}
	public K3ImportException(String message, Throwable cause, File databaseFile, File interfaceFile)
	{
		super(message, cause);
		this.databaseFile = databaseFile;
		this.interfaceFile = interfaceFile;
	}
	
	public K3ImportException(Throwable cause, File databaseFile, File interfaceFile)
	{
		super(cause);
		this.databaseFile = databaseFile;
		this.interfaceFile = interfaceFile;
	}
}
