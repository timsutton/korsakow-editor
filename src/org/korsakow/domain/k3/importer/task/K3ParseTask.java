/**
 * 
 */
package org.korsakow.domain.k3.importer.task;

import java.io.File;
import java.io.IOException;

import org.korsakow.domain.k3.K3Interface;
import org.korsakow.domain.k3.K3Project;
import org.korsakow.domain.k3.importer.K3ImportException;
import org.korsakow.domain.k3.importer.K3ImportReport;
import org.korsakow.domain.k3.parser.K3DatabaseParser;
import org.korsakow.domain.k3.parser.K3InterfaceParser;
import org.korsakow.domain.k3.parser.K3ParserException;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.task.TaskException;
import org.korsakow.ide.util.StrongReference;

public class K3ParseTask extends K3ImportTask
{
	private final K3ImportReport report;
	private final StrongReference<K3Project> project;
	private final StrongReference<K3Interface> interf;
	
	public K3ParseTask(File dataDir, File databaseFile, File interfaceFile, K3ImportReport report, StrongReference<K3Project> project, StrongReference<K3Interface> interf)
	{
		super(dataDir, databaseFile, interfaceFile);
		this.report = report;
		this.project = project;
		this.interf = interf;
	}
	@Override
	public String getTitleString()
	{
		return LanguageBundle.getString("import.task.parse");
	}
	@Override
	public void runTask() throws TaskException
	{
		String basedir = getDataDir().getPath();
				
		K3DatabaseParser dbParser = new K3DatabaseParser();
		K3Project k3Project = null;
		try {
			k3Project = dbParser.parse(getDatabaseFile());
		} catch (IOException e) {
			throw new TaskException(new K3ImportException(e, getDatabaseFile(), null));
		} catch (K3ParserException e) {
			throw new TaskException(new K3ImportException(e, getDatabaseFile(), null));
		}
		
		if (k3Project.settings.useNewInterface)
		{
			if (getInterfaceFile().exists()) {
				K3InterfaceParser ifParser = new K3InterfaceParser();
				K3Interface k3Interface = null;
				try {
					k3Interface = ifParser.parse(getInterfaceFile());
				} catch (IOException e) {
					throw new TaskException(new K3ImportException(e, getDatabaseFile(), getInterfaceFile()));
				} catch (K3ParserException e) {
					throw new TaskException(new K3ImportException(e, getDatabaseFile(), getInterfaceFile()));
				}
				interf.set(k3Interface);
			} else
				report.addWarning("Missing interface.txt", "File System");
		}


		project.set(k3Project);
	}
}