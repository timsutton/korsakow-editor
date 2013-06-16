package org.korsakow.domain.k3.importer.exception;

import java.io.File;

import org.korsakow.domain.k3.importer.K3ImportException;

public class K3InvalidRuleException extends K3ImportException
{
	File databaseFile;
	File interfaceFile;
	
	private int line;
	private String code;
	
	public File getDatabaseFile() {
		return databaseFile;
	}
	public File getInterfaceFile() {
		return interfaceFile;
	}
	public int getLine() {
		return line;
	}
	public String getCode() {
		return code;
	}
	public K3InvalidRuleException(String message, Throwable cause, int line, String code, File databaseFile, File interfaceFile)
	{
		super(message, cause, databaseFile, interfaceFile);
		this.line = line;
		this.code = code;
		this.databaseFile = databaseFile;
		this.interfaceFile = interfaceFile;
	}
}
