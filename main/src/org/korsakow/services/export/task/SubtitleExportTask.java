/**
 * 
 */
package org.korsakow.services.export.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.korsakow.ide.Application;
import org.korsakow.ide.DialogOptions;
import org.korsakow.ide.task.AbstractTask;
import org.korsakow.ide.task.TaskException;
import org.korsakow.ide.util.FileUtil;
import org.korsakow.services.export.ExportOptions;

public class SubtitleExportTask extends AbstractTask
{
	private final File srcFile;
	private final File destFile;
	public SubtitleExportTask(ExportOptions options, File destFile, File srcFile) throws FileNotFoundException
	{
		super(options);
		this.srcFile = srcFile;
		this.destFile = destFile;
	}
	@Override
	public String getTitleString()
	{
		return srcFile.getName();
	}
	@Override
	public void runTask() throws TaskException, InterruptedException
	{
		// TODO: I think this is outdated 2013/01/20 // the length check is because in creating the unique export filename we actually reserve the physical file
		if (destFile.exists() && destFile.length() > 0) {
			Boolean overwriteOption;
			synchronized (exportOptions) {
				overwriteOption = exportOptions.overwriteExisting;
			}
			// if option already set to false, then abort
			if (overwriteOption == Boolean.FALSE)
				return;
			
			// if undecided, ask
			if (overwriteOption == null) {
				DialogOptions dialogOptions =  Application.getInstance().showFileOverwriteDialog("File exists", destFile.getName() + " already exists, YES to overwrite or NO to skip.");
				if (dialogOptions.applyToAll) {
					// apply to all means set the global option
					synchronized (exportOptions) {
						exportOptions.overwriteExisting = dialogOptions.dialogResult;
					}
				}
				if (!dialogOptions.dialogResult)
					return;
			}
		}
		
		destFile.getParentFile().mkdirs();
		try {
			FileUtil.copyFile(srcFile, destFile);
		} catch (FileNotFoundException e) {
			throw new TaskException(e);
		} catch (IOException e) {
			throw new TaskException(e);
		}
		// this is precautionary, in light of #1401
		if (!destFile.exists())
			throw new TaskException(new IOException("There was a problem exporting subtitles file: " + srcFile.getAbsolutePath()));
	}
}
