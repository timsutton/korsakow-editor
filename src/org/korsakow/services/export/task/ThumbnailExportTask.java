/**
 * 
 */
package org.korsakow.services.export.task;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.ide.Application;
import org.korsakow.ide.DialogOptions;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.task.AbstractTask;
import org.korsakow.ide.task.TaskException;
import org.korsakow.ide.util.FileUtil;
import org.korsakow.ide.util.ResourceManager;
import org.korsakow.services.encoders.EncoderException;
import org.korsakow.services.encoders.image.ImageEncoder;
import org.korsakow.services.encoders.image.ImageEncoderException;
import org.korsakow.services.encoders.image.ImageEncoderFactory;
import org.korsakow.services.encoders.image.ImageFormat;
import org.korsakow.services.encoders.video.AudioCodec;
import org.korsakow.services.encoders.video.ContainerFormat;
import org.korsakow.services.encoders.video.VideoCodec;
import org.korsakow.services.encoders.video.VideoEncoder;
import org.korsakow.services.encoders.video.VideoEncoderFactory;
import org.korsakow.services.export.ExportException;
import org.korsakow.services.export.ExportOptions;

public class ThumbnailExportTask extends AbstractTask
{
	private final IMedia media;
	private final File srcFile;
	private final File destFile;
	private final File rootDir;
	private Integer maxWidth = null;
	private Integer maxHeight = null;
	public ThumbnailExportTask(ExportOptions options, IMedia media, File destFile, File rootDir) throws FileNotFoundException
	{
		super(options);
		this.media = media;
		this.destFile = destFile;
		this.rootDir = rootDir;
		srcFile = new File(media.getAbsoluteFilename());
	}
	public void setMaxSize(int width, int height)
	{
		maxWidth = width;
		maxHeight = height;
	}
	@Override
	public String getTitleString()
	{
		return srcFile.getName();
	}
	@Override
	public void runTask() throws TaskException
	{
		try {
		
		// the length check is because in creating the unique export filename we actually reserve the physical file // TODO: this is no longer the case
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
			throw new FileNotFoundException(srcFile.getPath());
		createThumbnail(media, srcFile, destFile, maxWidth, maxHeight);
		
		} catch (Exception e) {
			if (e instanceof ExportException == false)
				throw new TaskException(new ExportException(e, rootDir));
		}
	}
	public static void createThumbnail(IMedia media, File srcFile, File destFile, Integer width, Integer height) throws IOException, InterruptedException, EncoderException {
		switch (ResourceType.forId(media.getType()))
		{
		case VIDEO:
			createThumbnailFromVideo(srcFile, destFile, width, height);
			break;
		case IMAGE:
			createThumbnailFromImage(srcFile, destFile, width, height);
			break;
		default:
			throw new IllegalArgumentException("Can only create thumbnails for image and video: " + media.getClass().getCanonicalName());
		}
	}
	public static void createThumbnailFromVideo(File srcFile, File destFile, Integer maxWidth, Integer maxHeight) throws IOException, InterruptedException, EncoderException {
		VideoEncoderFactory fac = VideoEncoderFactory.getNewFactory();
		fac.addRequiredOutputFormat(VideoCodec.JPG);
		VideoEncoder encoder = fac.createVideoEncoder();
		
		encoder.setFrameCount(1L);
		if (maxWidth != null && maxHeight != null) {
			Dimension size = VideoExportTask.calculateVideoSize(srcFile, maxWidth, maxHeight);
			if (size != null)
				encoder.setSize(size.width, size.height);
		}
		encoder.setAudioCodec(AudioCodec.NONE);
		encoder.setContainerFormat(ContainerFormat.JPG);
		encoder.setVideoCodec(VideoCodec.JPG);
//		System.out.println("Creating Thumbnail from Video: " + srcFile);
		try {
			encoder.encode(null, srcFile, destFile);
		} catch ( EncoderException e ) { 
			Logger.getLogger(Application.class).error("", e);
			FileUtil.copyFile(ResourceManager.getResourceFile("players/flash/data/thumb_default.png"), destFile);
		}
	}
	public static void createThumbnailFromImage(File srcFile, File destFile, Integer width, Integer height) throws IOException, InterruptedException, ImageEncoderException {
		ImageEncoderFactory fac = ImageEncoderFactory.getNewFactory();
		fac.addRequiredOutputFormat(ImageFormat.JPG);
		ImageEncoder encoder = fac.createImageEncoder();
		
		if (width != null && height != null) {
			Dimension size = ImageExportTask.calculateImageSize(srcFile, width, height);
			encoder.setSize(size.width, size.height);
		}
		
//		System.out.println("Creating Thumbnail from Image: " + srcFile);
		encoder.encode(srcFile, ImageFormat.JPG, destFile);
	}
	
}

