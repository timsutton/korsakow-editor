package org.korsakow.services.encoders.video;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class VideoEncoderFactory
{
	public static VideoEncoderFactory getNewFactory()
	{
		return new VideoEncoderFactory();
	}
	private static Collection<VideoEncoder.VideoEncoderDescription> descriptions = new ArrayList<VideoEncoder.VideoEncoderDescription>();
	public static void addEncoder(VideoEncoder.VideoEncoderDescription description)
	{
		synchronized (descriptions) {
			descriptions.add(description);
		}
	}
	
	private final Collection<VideoCodec> requiredInputFormats = new HashSet<VideoCodec>();
	private final Collection<VideoCodec> requiredOutputFormats = new HashSet<VideoCodec>();
	
	private VideoEncoderFactory()
	{
		
	}
	
	public void addRequiredInputFormat(VideoCodec format)
	{
		requiredInputFormats.add(format);
	}
	public void addRequiredOutputFormat(VideoCodec format)
	{
		requiredOutputFormats.add(format);
	}
	public void clear()
	{
		requiredInputFormats.clear();
		requiredOutputFormats.clear();
	}
	public VideoEncoder createVideoEncoder() throws VideoEncoderException
	{
		List<VideoEncoder.VideoEncoderDescription> descs;
		synchronized (descriptions) {
			descs = new ArrayList<VideoEncoder.VideoEncoderDescription>(descriptions);
		}
		
		for (VideoEncoder.VideoEncoderDescription desc : descs) {
			if (!desc.getSupportedInputFormats().containsAll(requiredInputFormats))
				continue;
			if (!desc.getSupportedOutputFormats().containsAll(requiredOutputFormats))
				continue;
			VideoEncoder videoEncoder;
			try {
				videoEncoder = desc.getEncoderClass().newInstance();
			} catch (InstantiationException e) {
				throw new VideoEncoderException(e);
			} catch (IllegalAccessException e) {
				throw new VideoEncoderException(e);
			}
			return videoEncoder;
		}
		throw new VideoEncoderException("No encoder matching the requirements was found");
	}
}
