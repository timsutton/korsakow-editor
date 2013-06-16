package org.korsakow.domain.k3.importer.task;

import java.io.File;

import org.korsakow.ide.task.AbstractTask;

public abstract class K3ImportTask extends AbstractTask {
	private File dataDir;
	private File databaseFile;
	private File interfaceFile;
	public K3ImportTask(File dataDir, File databaseFile, File interfaceFile)
	{
		this.dataDir = dataDir;
		this.databaseFile = databaseFile;
		this.interfaceFile = interfaceFile;
	}
	public File getDataDir()
	{
		return dataDir;
	}
	public File getDatabaseFile()
	{
		return databaseFile;
	}
	/**
	 * May be null.
	 * @return
	 */
	public File getInterfaceFile()
	{
		return interfaceFile;
	}
}
