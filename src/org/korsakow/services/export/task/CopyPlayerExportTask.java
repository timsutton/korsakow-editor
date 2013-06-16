/**
 * 
 */
package org.korsakow.services.export.task;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.antlr.stringtemplate.StringTemplate;
import org.korsakow.domain.interf.IProject;
import org.korsakow.ide.Build;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.task.AbstractTask;
import org.korsakow.ide.task.TaskException;
import org.korsakow.ide.util.FileUtil;
import org.korsakow.ide.util.ResourceManager;
import org.korsakow.services.export.Exporter;
import org.korsakow.services.util.ColorFactory;

public class CopyPlayerExportTask extends AbstractTask
{
	private final File rootDir;
	private final String indexFilename;
	private final String dataPath;
	private final IProject project;
	public CopyPlayerExportTask(File rootDir, String indexFilename, String dataPath, IProject project)
	{
		this.rootDir = rootDir;
		this.indexFilename = indexFilename;
		this.project = project;
		this.dataPath = dataPath;
	}
	@Override
	public String getTitleString()
	{
		return LanguageBundle.getString("export.task.copyplayer");
	}
	@Override
	public void runTask() throws TaskException
	{
		try {
			for (String resource : Exporter.FLASH_PLAYER_RESOURES) {
				FileUtil.copyFile(ResourceManager.getResourceFile(Exporter.FLASH_PLAYER_ROOT + resource), new File(rootDir, resource));
			}
			
			FileUtil.writeFileFromString(new File(rootDir, Exporter.FLASH_PLAYER_RESOURCE_CSS), createCSS());
			FileUtil.writeFileFromString(new File(rootDir, indexFilename), createIndex());
		} catch (IOException e) {
			throw new TaskException(e);
		}
	}
	private String createIndex() throws IOException
	{
		InputStream inputStream = ResourceManager.getResourceStream(Exporter.FLASH_PLAYER_ROOT + Exporter.FLASH_PLAYER_RESOURCE_INDEX);
		String template = FileUtil.readString(inputStream);
		StringTemplate st = new StringTemplate(template);
		st.setAttribute("title", project.getName());
		Color backgroundColor = project.getBackgroundColor()!=null?project.getBackgroundColor():Color.black;
		st.setAttribute("backgroundColor", ColorFactory.formatCSS(backgroundColor));
		st.setAttribute("dataPath", dataPath);
		st.setAttribute("width", project.getMovieWidth());
		st.setAttribute("height", project.getMovieHeight());
		st.setAttribute("requireVersion", Build.getRelease());
		return st.toString();
	}
	private String createCSS() throws IOException
	{
		InputStream inputStream = ResourceManager.getResourceStream(Exporter.FLASH_PLAYER_ROOT + Exporter.FLASH_PLAYER_RESOURCE_CSS);
		String template = FileUtil.readString(inputStream);
		StringTemplate st = new StringTemplate(template);
		Color backgroundColor = project.getBackgroundColor()!=null?project.getBackgroundColor():Color.black;
		st.setAttribute("backgroundColor", ColorFactory.formatCSS(backgroundColor));
		return st.toString();
	}
}
