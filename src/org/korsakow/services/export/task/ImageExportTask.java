/**
 * 
 */
package org.korsakow.services.export.task;

import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.korsakow.domain.interf.IImage;
import org.korsakow.ide.Application;
import org.korsakow.ide.DialogOptions;
import org.korsakow.ide.resources.media.MediaFactory;
import org.korsakow.ide.resources.media.PlayableImage;
import org.korsakow.ide.task.AbstractTask;
import org.korsakow.ide.task.TaskException;
import org.korsakow.ide.util.FileUtil;
import org.korsakow.services.export.ExportOptions;

public class ImageExportTask extends AbstractTask
{
	private final IImage image;
	private final File srcFile;
	private final File destFile;
	public ImageExportTask(ExportOptions options, IImage image, File destFile) throws FileNotFoundException
	{
		super(options);
		this.image = image;
		this.destFile = destFile;
		srcFile = new File(image.getAbsoluteFilename());
	}
	@Override
	public String getTitleString()
	{
		return srcFile.getName();
	}
	@Override
	public void runTask() throws TaskException
	{
		// the length check is because in creating the unique export filename we actually reserve the physical file
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
		if (!srcFile.exists())
			throw new TaskException(new FileNotFoundException(srcFile.getPath()));
		try {
			FileUtil.copyFile(image.getAbsoluteFilename(), destFile.getAbsolutePath());
		} catch (FileNotFoundException e) {
			throw new TaskException(e);
		} catch (IOException e) {
			throw new TaskException(e);
		}
	}
	public static Dimension calculateImageSize(File srcFile,
			Integer width,
			Integer height)
	{
		Dimension d = new Dimension(width, height);
		try {
			PlayableImage playable = (PlayableImage)MediaFactory.getMedia(srcFile.getAbsolutePath());
			Component comp = playable.getComponent();
			Dimension pref = comp.getPreferredSize();
			int w = width!=null?width:pref.width;
			int h = height!=null?height:pref.height;
			if (width != null && height != null)
			{
				d = playable.getAspectRespectingDimension(new Dimension(w, h));
			}
			playable.dispose();
		} catch (Exception e) {
			Logger.getLogger(VideoExportTask.class).error("", e);
		}
		return d;
	}
}