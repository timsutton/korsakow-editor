/**
 * 
 */
package org.korsakow.services.export.task;

import java.awt.Component;
import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.ide.Application;
import org.korsakow.ide.DialogOptions;
import org.korsakow.ide.io.NullOutputStream;
import org.korsakow.ide.resources.media.MediaFactory;
import org.korsakow.ide.resources.media.PlayableVideo;
import org.korsakow.ide.task.AbstractTask;
import org.korsakow.ide.task.TaskException;
import org.korsakow.ide.util.ExternalsResourceManager;
import org.korsakow.ide.util.FileUtil;
import org.korsakow.ide.util.Platform;
import org.korsakow.services.encoders.EncoderException;
import org.korsakow.services.encoders.video.VideoEncoder;
import org.korsakow.services.encoders.video.VideoEncoderException;
import org.korsakow.services.encoders.video.VideoEncoderFactory;
import org.korsakow.services.encoders.video.ffmpeg.FFMpegEncoder;
import org.korsakow.services.export.ExportException;
import org.korsakow.services.export.ExportOptions;
import org.korsakow.services.export.IVideoEncodingProfile;

public class VideoExportTask extends AbstractTask
{
	private final IVideoEncodingProfile encodingProfile;
	private final IVideo video;
	private final File srcFile;
	private final File destFile;
	private final File rootDir;
	private Integer maxWidth = null;
	private Integer maxHeight = null;
	public VideoExportTask(ExportOptions options, IVideoEncodingProfile encodignProfile, IVideo video, File destFile, File rootDir) throws FileNotFoundException
	{
		super(options);
		encodingProfile = encodignProfile;
		this.video = video;
		this.destFile = destFile;
		this.rootDir = rootDir;
		srcFile = new File(video.getAbsoluteFilename());
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
				
		if (!srcFile.exists())
			throw new TaskException(new FileNotFoundException(srcFile.getPath()));
		try {
			boolean encode;
			synchronized (exportOptions) {
				encode = exportOptions.encodeVideo;
			}
			
			if (encode)
				encodeVideo(srcFile, destFile, encodingProfile, maxWidth, maxHeight);
			else
				copyVideo(srcFile, destFile);
		} catch (EncoderException e) {
			throw new TaskException(e);
		} catch (IOException e) {
			throw new TaskException(e);
		}
		if (!destFile.exists() || destFile.length() == 0)
			throw new TaskException(new ExportException("Video File Missing:" + destFile.getPath(), rootDir));
	}
	public static void encodeVideo(File srcFile, File destFile, IVideoEncodingProfile encodingProfile, Integer maxWidth, Integer maxHeight) throws IOException, InterruptedException, EncoderException {

		destFile.getParentFile().mkdirs();

		VideoEncoder videoEncoder = getEncoder(encodingProfile);

		if (maxWidth != null && maxHeight != null) {
			Dimension size = calculateVideoSize(srcFile, maxWidth, maxHeight);
			if (size != null)
				videoEncoder.setSize(size.width, size.height);
		}
		videoEncoder.encode(null, srcFile, destFile);
		
		if (!destFile.exists() || destFile.length() ==0)
			throw new IOException("video file was not created?");
		
		switch (encodingProfile.getContainerFormat())
		{
		case FLV:
			fixFLVMetaData(destFile);
			break;
		case MP4:
			fixH264Streaming(destFile);
			break;
		}
	}
	private static void copyVideo(File srcFile, File destFile) throws IOException {
		FileUtil.copyFile(srcFile, destFile);
	}
	
	/**
	 * @return null if the dimensions can't be calculated or would result in a size larger than the media's actual size
	 */
	public static Dimension calculateVideoSize(File srcFile,
			int maxWidth,
			int maxHeight)
	{
		Dimension d = null;
		try {
			PlayableVideo playable = (PlayableVideo)MediaFactory.getMedia(srcFile.getAbsolutePath());
			Component comp = playable.getComponent();
			Dimension pref = comp.getPreferredSize();
			// don't enlarge.
			if (maxWidth < pref.width && maxHeight < pref.height)
				d = playable.getAspectRespectingDimension(new Dimension(maxWidth, maxHeight));
			playable.dispose();
		} catch (Exception e) {
			Logger.getLogger(VideoExportTask.class).error("", e);
		}
		return d;
	}
	public static VideoEncoder getEncoder(IVideoEncodingProfile profile) throws IOException, VideoEncoderException
	{
		VideoEncoderFactory fac = VideoEncoderFactory.getNewFactory();
//		fac.addRequiredInputFormat(profile.getVideoCodec());
		if (profile.getVideoCodec() != null) fac.addRequiredOutputFormat(profile.getVideoCodec());
		VideoEncoder encoder = fac.createVideoEncoder();
		if (profile.getAudioBitRate() != null) encoder.setAudioBitRate(profile.getAudioBitRate());
		if (profile.getAudioSamplingRate() != null) encoder.setAudioSamplingRate(profile.getAudioSamplingRate());
		if (profile.getAudioCodec() != null) encoder.setAudioCodec(profile.getAudioCodec());
		if (profile.getVideoCodec() != null) encoder.setVideoCodec(profile.getVideoCodec());
		if (profile.getContainerFormat() != null) encoder.setContainerFormat(profile.getContainerFormat());
		if (profile.getVideoBitRate() != null) encoder.setVideoBitRate(profile.getVideoBitRate());
		if (profile.getVideoBitRateTolerance() != null) encoder.setVideoBitRateTolerance(profile.getVideoBitRateTolerance());
		Collection<String> keys = profile.getEncoderSpecificKeys();
		for (String key : keys) {
			String value = profile.getEncoderSpecificValue(key);
			// TODO: think of a better way to handle this
			if (encoder instanceof FFMpegEncoder && FFMpegEncoder.OPTION_PRESET.equals(key)) {
				value = ExternalsResourceManager.getExternalFile(ExternalsResourceManager.FFMPEG_PRESETS + File.separator + value).getAbsolutePath();
			}
			encoder.setEncoderSpecificOption(key, value);
		}
		return encoder;
	}
	
	/**
	 * Yamdi is a handy little tool that makes sure our FLV has all the proper metadata.
	 * In particular this ensures our FLV is nice and seekable.
	 * @param file
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static void fixFLVMetaData(File file) throws IOException, InterruptedException
	{
		// Yamdi doesn't work in-place on files, so we create a new one and swap
		// c.f. #1051
		// c.f. http://java.sun.com/j2se/1.5.0/docs/api/java/io/File.html#renameTo%28java.io.File%29
		// 		basically we can't assume too much so its best to create the temp file in the same directory as the one we
		//		want to do the renameTo() on.
		File tempFile = File.createTempFile("videoExportTask", "YAMDI", file.getParentFile());
		File execFile = null;
		switch (Platform.getOS())
		{
		case MAC:
			execFile = ExternalsResourceManager.getExternalFile(ExternalsResourceManager.YAMDI_OSX);
			break;
		case WIN:
			execFile = ExternalsResourceManager.getExternalFile(ExternalsResourceManager.YAMDI_WIN);
			break;
		}
		List<String> cmds = new ArrayList<String>();
		cmds.add(execFile.getAbsolutePath());
		cmds.add("-i");
		cmds.add(file.getAbsolutePath());
		cmds.add("-o");
		cmds.add(tempFile.getAbsolutePath());
		ProcessBuilder pb = new ProcessBuilder(cmds);
		Process p = pb.start();
		ByteArrayOutputStream errBuff = new ByteArrayOutputStream();
		OutputStream outBuff = new NullOutputStream();
		int exitCode = FileUtil.executeProcess(p, outBuff, errBuff);
		if (exitCode != 0) {
			throw new IOException(errBuff.toString("UTF-8"));
		}
		
		FileUtil.delete(file);
		if (!tempFile.renameTo(file))
			throw new IOException(String.format("Failed to rename '%s' to '%s'", tempFile.getAbsolutePath(), file.getAbsolutePath()));
	}
	/**
	 * MP4Box fixes MP4 files so they can be streamed
	 * @param file
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static void fixH264Streaming(File file) throws IOException, InterruptedException
	{
		File execFile = null;
		switch (Platform.getOS())
		{
		case MAC:
			execFile = ExternalsResourceManager.getExternalFile(ExternalsResourceManager.MP4BOX_OSX);
			break;
		case WIN:
			execFile = ExternalsResourceManager.getExternalFile(ExternalsResourceManager.MP4BOX_WIN);
			break;
		}
		File tmpDir = FileUtil.createTempDirectory(".mp4box", "tmp", file.getParentFile());
		tmpDir.deleteOnExit();
		try {
			List<String> cmds = new ArrayList<String>();
			cmds.add(execFile.getAbsolutePath());
			cmds.add("-isma");
			cmds.add("-hint");
			// setting the tmp dir to the same location as the file avoids issues as with #1051 
			cmds.add("-tmp"); cmds.add(tmpDir.getAbsolutePath());
			cmds.add(file.getAbsolutePath());
			ProcessBuilder pb = new ProcessBuilder(cmds);
			System.out.println(pb.command());
			Process p = pb.start();
			ByteArrayOutputStream errBuff = new ByteArrayOutputStream();
			OutputStream outBuff = System.out;
			int exitCode = FileUtil.executeProcess(p, outBuff, errBuff);
			if (exitCode != 0) {
				throw new IOException(errBuff.toString("UTF-8"));
			}
		} finally {
			tmpDir.delete();
		}
	}
}




