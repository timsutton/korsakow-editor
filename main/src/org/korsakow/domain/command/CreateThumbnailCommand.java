package org.korsakow.domain.command;

import java.io.File;

import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.ide.exception.MediaRuntimeException;
import org.korsakow.ide.resources.media.MediaInfo;
import org.korsakow.ide.resources.media.MediaInfoFactory;
import org.korsakow.ide.util.FileUtil;
import org.korsakow.services.encoders.EncoderException;
import org.korsakow.services.encoders.UnsupportedFormatException;
import org.korsakow.services.encoders.video.AudioCodec;
import org.korsakow.services.encoders.video.ContainerFormat;
import org.korsakow.services.encoders.video.VideoCodec;
import org.korsakow.services.encoders.video.VideoEncoder;
import org.korsakow.services.encoders.video.VideoEncoderException;
import org.korsakow.services.encoders.video.VideoEncoderFactory;

public class CreateThumbnailCommand extends AbstractCommand {

	public static final String VIDEO_FILE = "videofile";
	public static final String THUMBNAIL_FILE = "thumbnailfile";
	public static final String OFFSET_MILLIS = "offsetmillis";
	public static final String RANDOM_OFFSET = "randomoffset";

	public CreateThumbnailCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		String videoFilename = request.getString(VIDEO_FILE);
		File videoFile = new File(videoFilename);
		
		String thumbnailFilename = request.getString(THUMBNAIL_FILE);
		File thumbnailFile = new File(thumbnailFilename);

		Long offset = request.getLong(OFFSET_MILLIS);
		if (request.get(RANDOM_OFFSET) != null && request.getBoolean(RANDOM_OFFSET)) {
			MediaInfo info;
			try {
				info =  MediaInfoFactory.getInfo(thumbnailFile);
			} catch (MediaRuntimeException e) {
				throw new CommandException(e);
			}
			offset = (long)(info.duration * Math.random());
		}
		
		try {
			thumbnailFile = createThumbnail(videoFile, thumbnailFile, offset);
		} catch (UnsupportedFormatException e) {
			throw new CommandException(e);
		} catch (EncoderException e) {
			throw new CommandException(e);
		} catch (InterruptedException e) {
			throw new CommandException(e);
		}
		
		response.set(CreateThumbnailCommand.THUMBNAIL_FILE, thumbnailFile.getAbsolutePath());
	}
	
	/**
	 * @return thumbnailFile with its file extension possibly modified
	 * @throws InterruptedException 
	 * @throws EncoderException 
	 * @throws VideoEncoderException 
	 */
	private File createThumbnail(File videoFile, File thumbnailFile, Long offset) throws InterruptedException, EncoderException
	{
		VideoEncoderFactory factory = VideoEncoderFactory.getNewFactory();
		factory.addRequiredInputFormat(VideoCodec.JPG);
		VideoEncoder encoder = factory.createVideoEncoder();
		encoder.setContainerFormat(ContainerFormat.JPG);
		encoder.setVideoCodec(VideoCodec.JPG);
		encoder.setAudioCodec(AudioCodec.NONE);
		encoder.setFrameCount(1L);
		if (offset != null)
			encoder.setOffset(offset);
		
		thumbnailFile = new File(FileUtil.setFileExtension(thumbnailFile.getAbsolutePath(), encoder.getFileExtension(ContainerFormat.JPG)));
		
		encoder.encode(VideoCodec.JPG, videoFile, thumbnailFile);
		return thumbnailFile;
	}
}
