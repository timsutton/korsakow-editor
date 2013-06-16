package org.korsakow.services.export;

import java.util.Collection;

import org.korsakow.services.encoders.video.AudioCodec;
import org.korsakow.services.encoders.video.ContainerFormat;
import org.korsakow.services.encoders.video.VideoCodec;

public interface IVideoEncodingProfile
{
	String getName();
	Integer getAudioSamplingRate();
	Integer getAudioBitRate();
	Integer getVideoBitRate();
	Integer getVideoBitRateTolerance();
	Boolean getDeinterlace();
	AudioCodec getAudioCodec();
	VideoCodec getVideoCodec();
	ContainerFormat getContainerFormat();
	Collection<String> getEncoderSpecificKeys();
	String getEncoderSpecificValue(String key);
}
