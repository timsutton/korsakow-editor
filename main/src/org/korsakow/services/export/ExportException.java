package org.korsakow.services.export;

import java.io.File;

public class ExportException extends Exception
{
	/**
	 * 
	 */
	
	private File projectFile;
	
	public ExportException(String message, Throwable cause, File projectFile) {
		super(message, cause);
		this.projectFile = projectFile;
	}
	private static final long serialVersionUID = 1L;
	public ExportException(String message, File projectFile)
	{
		super(message);
		this.projectFile = projectFile;
	}
	public ExportException(Throwable cause, File projectFile)
	{
		super(cause);
		this.projectFile = projectFile;
	}
	
	public void setProjectFile(File projectFile) {
		this.projectFile = projectFile;
	}
	public File getProjectFile() {
		return projectFile;
	}
}
