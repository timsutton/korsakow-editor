/**
 * 
 */
package org.korsakow.services.export.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.korsakow.domain.Media;
import org.korsakow.domain.interf.ISound;
import org.korsakow.ide.Application;
import org.korsakow.ide.DialogOptions;
import org.korsakow.ide.task.AbstractTask;
import org.korsakow.ide.task.TaskException;
import org.korsakow.ide.util.FileUtil;
import org.korsakow.services.encoders.EncoderException;
import org.korsakow.services.encoders.sound.SoundEncoder;
import org.korsakow.services.encoders.sound.SoundEncoderFactory;
import org.korsakow.services.encoders.sound.SoundFormat;
import org.korsakow.services.export.ExportOptions;
import org.korsakow.services.export.Exporter;

public class SoundExportTask extends AbstractTask
{
	private final ISound sound;
	private final File subtitleFile;
	private final File srcFile;
	private final File destFile;
	public SoundExportTask(ExportOptions options, ISound sound, File destFile, File subtitleFile) throws FileNotFoundException
	{
		super(options);
		this.sound = sound;
		this.subtitleFile = subtitleFile;
		this.destFile = destFile;
		srcFile = new File(sound.getAbsoluteFilename());
	}
	@Override
	public String getTitleString()
	{
		return srcFile.getName();
	}
	@Override
	public void runTask() throws TaskException, InterruptedException
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
		if (sound.getSubtitles() != null) {
			subtitleFile.getParentFile().mkdirs();
			try {
				FileUtil.copyFile(Media.getAbsoluteFilename(sound.getSubtitles()), subtitleFile.getAbsolutePath());
			} catch (FileNotFoundException e) {
				throw new TaskException(e);
			} catch (IOException e) {
				throw new TaskException(e);
			}
			// this is precautionary, in light of #1401
			if (!subtitleFile.exists())
				throw new TaskException(new IOException("There was a problem exporting subtitles file: " + subtitleFile.getAbsolutePath()));
		}
		if (!srcFile.exists())
			throw new TaskException(new FileNotFoundException(srcFile.getPath()));
		
		destFile.getParentFile().mkdirs();
		
		if ("mp3".equals(FileUtil.getFileExtension(srcFile.getName()).toLowerCase()))
			try {
				FileUtil.copyFile(sound.getAbsoluteFilename(), destFile.getAbsolutePath());
			} catch (FileNotFoundException e) {
				throw new TaskException(e);
			} catch (IOException e) {
				throw new TaskException(e);
			}
		else
			SoundEncoderFactory.getDefaultFactory().addRequiredInputFormat(SoundFormat.WAV);
			SoundEncoderFactory.getDefaultFactory().addRequiredOutputFormat(SoundFormat.MP3);
			System.out.println(srcFile.getPath() + "->" + destFile.getPath());
			try {
				SoundEncoder soundEncoder = SoundEncoderFactory.getDefaultFactory().createSoundEncoder();
				soundEncoder.encode(Exporter.SOUND_EXPORT_FORMAT, srcFile, destFile);
			} catch (EncoderException e) {
				throw new TaskException(e);
			}
	}
}