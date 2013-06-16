package org.korsakow.domain.k3.importer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.k3.K3Interface;
import org.korsakow.domain.k3.K3Project;
import org.korsakow.domain.k3.importer.task.K3ConvertProjectTask;
import org.korsakow.domain.k3.importer.task.K3ParseTask;
import org.korsakow.ide.task.AbstractTask;
import org.korsakow.ide.task.Task;
import org.korsakow.ide.task.TaskException;
import org.korsakow.ide.util.StrongReference;

/**
 * Imports a K3 project to K5
 * @author d
 *
 */
public class K3Importer
{
	public static final String DATABASE_DIR = "database";
	public static final String VIDEO_DIR = "movies";
	public static final String IMAGE_DIR = "pictures";
	public static final String SOUND_DIR = "sounds";
	
	private final StrongReference<IProject> project = new StrongReference<IProject>();
	private final K3ImportReport report = new K3ImportReport();
	private K3Project k3Project;
	
	private final File dataDir;
	private final File databaseDir;
	private final File databaseFile;
	private final File interfaceFile;
	
	
	public K3Importer(File dataDir)
	{
		this.dataDir = dataDir;
		databaseDir = new File(dataDir, K3Importer.DATABASE_DIR);
		databaseFile = new File(databaseDir, "database.txt");
		interfaceFile = new File(databaseDir, "interface.txt");
	}
	public File getDatabaseDir()
	{
		return databaseDir;
	}
	public File getDatabaseFile()
	{
		return databaseFile;
	}
	/**
	 * may not exist
	 * @return
	 */
	public File getInterfaceFile()
	{
		return interfaceFile;
	}
	public List<Task> createImportTasks()
	{
		List<Task> importTasks = new ArrayList<Task>();
		
		final StrongReference<K3Project> k3ProjectRef = new StrongReference<K3Project>();
		StrongReference<K3Interface> k3InterfaceRef = new StrongReference<K3Interface>();
		
		importTasks.add(new K3ParseTask(dataDir, databaseFile, interfaceFile, report, k3ProjectRef, k3InterfaceRef));
		importTasks.add(new K3ConvertProjectTask(dataDir, databaseFile, interfaceFile, k3ProjectRef, report, project, k3InterfaceRef));
		importTasks.add(new AbstractTask() {
			@Override
			public void runTask() throws TaskException {
				k3Project = k3ProjectRef.get();
			}
		});
		
		return importTasks;
	}
	
	public K3Project getK3Project()
	{
		return k3Project;
	}
	public IProject getProject()
	{
		return project.get();
	}
	public K3ImportReport getReport()
	{
		return report;
	}
	
}
