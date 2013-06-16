package org.korsakow.services.encoders.video;

import java.io.File;
import java.util.Collection;

import org.korsakow.services.encoders.EncoderException;
import org.korsakow.services.encoders.IEncoder;

public interface VideoEncoder extends IEncoder
{
	static interface VideoEncoderDescription
	{
		Collection<VideoCodec> getSupportedInputFormats();
		Collection<VideoCodec> getSupportedOutputFormats();
		Class<? extends VideoEncoder> getEncoderClass();
	}
	void setEncoderSpecificOption(Object name, Object value) throws UnsupportedOperationException;
	Object getEncoderSpecificOption(Object name) throws UnsupportedOperationException;
	
	void setAudioSamplingRate(Integer rate) throws UnsupportedOperationException;
	Integer getAudioSamplingRate();
	/**
	 * This method is not required to do anything smart like preserving aspect ratio.
	 * Both width and height may be null at the same time to indicate that the default size should be used (this is the default behavior).
	 * 
	 * @param width may be null only if height is also null
	 * @param height may be null only if width is also null
	 */
	void setSize(Integer width, Integer height) throws UnsupportedOperationException;
	void setDeinterlace(Boolean deinterlace) throws UnsupportedOperationException;
	void setVideoBitRate(Integer bitrate) throws UnsupportedOperationException;
	void setVideoBitRateTolerance(Integer tolerance) throws UnsupportedOperationException;
	void setAudioBitRate(Integer bitrate) throws UnsupportedOperationException;
	void setPadding(Integer color, Integer top, Integer right, Integer bottom, Integer left) throws UnsupportedOperationException;
	void setAudioCodec(AudioCodec format) throws UnsupportedOperationException;
	void setVideoCodec(VideoCodec format) throws UnsupportedOperationException;
	void setContainerFormat(ContainerFormat format) throws UnsupportedOperationException;
	void setFrameCount(Long frameCount) throws UnsupportedOperationException;
	void setOffset(Long millis) throws UnsupportedOperationException;
//	void encode(VideoFormat srcFormat, InputStream sourceStream, VideoFormat destFormat, OutputStream destStream) throws VideoEncoderException;
	void encode(VideoCodec srcFormat, File sourceFile, File destFile) throws EncoderException, InterruptedException;
	
	String getFileExtension(ContainerFormat format) throws UnsupportedOperationException;
}
