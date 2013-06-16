package org.korsakow.services.encoders.video.ffmpeg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.korsakow.services.encoders.EncoderException;
import org.korsakow.services.encoders.UnsupportedFormatException;
import org.korsakow.services.encoders.video.AudioCodec;
import org.korsakow.services.encoders.video.ContainerFormat;
import org.korsakow.services.encoders.video.FileExternalVideoEncoder;
import org.korsakow.services.encoders.video.VideoCodec;
import org.korsakow.services.encoders.video.VideoEncoder;
import org.korsakow.services.encoders.video.VideoEncoderException;

public abstract class FFMpegEncoder extends FileExternalVideoEncoder
{
	public static final String OPTION_PRESET = "vpre";
	
	// options
	protected AudioCodec audioCodec;
	protected Integer audioSamplingRate;
	protected Integer audioBitRate;
	protected VideoCodec videoCodec;
	protected ContainerFormat containerFormat;
	protected Integer videoBitRate;
	protected Integer videoBitRateTolerance;
	protected Boolean deinterlace;
	protected Integer width;
	protected Integer height;
	protected Integer paddingTop;
	protected Integer paddingBottom;
	protected Integer paddingLeft;
	protected Integer paddingRight;
	protected Integer paddingColor;
	protected String presetsFile;
	protected boolean twoPass = false;
	protected Long frameCount;
	protected Long offsetMillis;
	
	
	protected int currentPass;
	protected File passlogfile;
	
	public abstract static class FFMpegEncoderDescription implements VideoEncoder.VideoEncoderDescription
	{
		private static final Collection<VideoCodec> inputFormats = Collections.unmodifiableCollection(Arrays.asList(
				VideoCodec.FLV, VideoCodec.H264, VideoCodec.JPG
		));
		private static final Collection<VideoCodec> outputFormats = Collections.unmodifiableCollection(Arrays.asList(
				VideoCodec.FLV, VideoCodec.H264, VideoCodec.JPG
		));
		public Collection<VideoCodec> getSupportedInputFormats() {
			return inputFormats;
		}
		public Collection<VideoCodec> getSupportedOutputFormats() {
			return outputFormats;
		}
	}
	public void setEncoderSpecificOption(Object name, Object value) throws UnsupportedOperationException
	{
		if (OPTION_PRESET.equals(name))
			presetsFile = value.toString();
		else
			throw new UnsupportedOperationException(String.format("can't set '%s'", name!=null?name.toString():null));
	}
	public Object getEncoderSpecificOption(Object name) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException(String.format("can't get '%s'", name!=null?name.toString():null));
	}
	
	public void setSize(Integer width, Integer height)
	{
		if ((width == null || height == null) && (width != height))
			throw new NullPointerException("both width and height may be null to leave the original size, but only one was null!");
		this.width = width;
		this.height = height;
	}
	public Integer getWidth()
	{
		return width;
	}
	public Integer getHeight()
	{
		return height;
	}
	public void setPadding(Integer color, Integer top, Integer right, Integer bottom, Integer left)
	{
		paddingColor = color;
		paddingBottom = bottom;
		paddingTop = top;
		paddingLeft = left;
		paddingRight = right;
	}
	public Integer getAudioSamplingRate()
	{
		return audioSamplingRate;
	}
	public void setAudioSamplingRate(Integer rate)
	{
		audioSamplingRate = rate;
	}
	public void setAudioBitRate(Integer rate) throws UnsupportedOperationException
	{
		audioBitRate = rate;
	}
	public void setVideoBitRate(Integer rate) throws UnsupportedOperationException
	{
		videoBitRate = rate;
	}
	public void setVideoBitRateTolerance(Integer tolerance) throws UnsupportedOperationException
	{
		videoBitRateTolerance = tolerance;
	}
	public void setDeinterlace(Boolean deinterlace) throws UnsupportedOperationException
	{
		this.deinterlace = deinterlace;
	}
	public void setAudioCodec(AudioCodec codec)
	{
		audioCodec = codec;
	}
	public void setVideoCodec(VideoCodec codec)
	{
		videoCodec = codec;
	}
	public void setContainerFormat(ContainerFormat format)
	{
		containerFormat = format;
	}
	public void setPresetsFile(String presets)
	{
		presetsFile = presets;
	}
	public void setFrameCount(Long frameCount)
	{
		this.frameCount = frameCount;
	}
	public void setOffset(Long offsetMillis)
	{
		this.offsetMillis = offsetMillis;
	}
	protected List<String> createCommandLine(VideoCodec sourceFormat, File srcFile, File dstFile)
	{
		List<String> cmds = new ArrayList<String>();
		cmds.add("-i");
		cmds.add(srcFile.getAbsolutePath());
		cmds.add("-y");

		boolean isFinalPass = !twoPass || currentPass > 1;
		
		if (isFinalPass) {
			if (audioCodec != null) {
				if (audioCodec == AudioCodec.NONE) {
					cmds.add("-an");
				} else {
					final String aCodec = getACodec(audioCodec);
					cmds.add("-acodec");
					cmds.add(aCodec);
				}
			}
			if (audioSamplingRate != null) {
				cmds.add("-ar");
				cmds.add(""+audioSamplingRate);
			}
			if (audioBitRate != null) {
				cmds.add("-ab");
				cmds.add(""+(audioBitRate/1024)+"k");
			}
		} else {
			cmds.add("-an");
		}
		if (frameCount != null) {
			cmds.add("-vframes");
			cmds.add(""+frameCount);
		}
		if (offsetMillis != null) {
			cmds.add("-ss");
			cmds.add(""+(int)(offsetMillis/1000.0));
		}
		if (presetsFile != null) {
			cmds.add("-fpre"); 
			cmds.add(presetsFile);
		}
		if (videoBitRate != null) {
			cmds.add("-b");
			cmds.add(""+(videoBitRate/1024)+"k");
		}
		if (videoBitRateTolerance != null) {
			cmds.add("-bt");
			cmds.add(""+(videoBitRateTolerance/1024)+"k");
		}
		if (deinterlace != null) {
			cmds.add("-deinterlace");
		}
		if (width != null && height != null)
		{
			// ffmpeg requries the size to be a multiple of 2
			int w = width;
			int h = height;
			if (w%2!=0) --w;
			if (h%2!=0) --h;
			cmds.add("-s");
			cmds.add(w+"x"+h);
		}
		if (paddingLeft != null && paddingLeft != 0) {
			cmds.add("-padleft");
			cmds.add("" + multof(paddingLeft, 2));
		}
		if (paddingRight != null && paddingRight != 0) {
			cmds.add("-padright");
			cmds.add("" + multof(paddingRight, 2));
		}
		if (paddingTop != null && paddingTop != 0) {
			cmds.add("-padtop");
			cmds.add("" + multof(paddingTop, 2));
		}
		if (paddingBottom != null && paddingBottom != 0) {
			cmds.add("-padbottom");
			cmds.add("" + multof(paddingBottom, 2));
		}
		if (paddingColor != null) {
			cmds.add("-padcolor");
			cmds.add("" + paddingColor);
		}
		if (videoCodec != null) {
			final String vCodec = getVCodec(videoCodec);
			cmds.add("-vcodec");
			cmds.add(vCodec);
		}
		if (containerFormat != null) {
			cmds.add("-f");
			cmds.add(getVFormat(containerFormat));
		}

		// current builds dont support threads: verify x264, mjpeg, ffmpeg
//		cmds.add("-threads");
//		cmds.add("2");
		cmds.add("-g");
		cmds.add("12"); // keyframes 

//		cmds.add("-r");
//		cmds.add("29.97");
		
		if (twoPass) {
			cmds.add("-pass");
			cmds.add(""+currentPass);
			cmds.add("-passlogfile");
			cmds.add(passlogfile.getAbsolutePath());
		}

		if (isFinalPass)
			cmds.add(dstFile.getAbsolutePath());
		else
			cmds.add(getNullDevice());

		return cmds;
	}
	public String getFileExtension(ContainerFormat format)
	{
		switch (format)
		{
		case FLV:
			return "flv";
		case MP4:
			return "mp4";
		case JPG:
			return "jpg";
		default:
			throw new IllegalArgumentException();
		}
	}
	/**
	 * See ffmpeg -formats
	 * @param codec
	 * @return null to indicate the parameter should not be specified. this is not the same as VideoCodec being null
	 */
	protected String getVCodec(VideoCodec codec)
	{
		switch (codec)
		{
		case FLV:
			return "flv";
		case H264:
			return "libx264";
		case JPG:
			return "mjpeg";
		default:
			throw new IllegalArgumentException();
		}
	}
	protected String getVFormat(ContainerFormat format)
	{
		switch (format)
		{
		case FLV:
			return "flv";
		case MP4:
			return "mp4";
		case JPG:
			return "image2";
		default:
			throw new IllegalArgumentException();
		}
	}
	/**
	 * 
	 * @param codec
	 */
	protected String getACodec(AudioCodec codec)
	{
		switch (codec)
		{
		case MP3:
			return "libmp3lame";
		case AAC:
			return "libfaac";
		default:
			throw new IllegalArgumentException();
		}
	}
	
	protected abstract String getNullDevice();
	
	@Override
	public void encode(VideoCodec srcFormat, File srcFile, File destFile) throws EncoderException, InterruptedException
	{
		try {
			if (twoPass)
			{
				try {
					passlogfile = File.createTempFile("FFMpegEncoder", "passlogfile");
				} catch (IOException e) {
					throw new EncoderException(e, destFile);
				}
			}
			encodeOnePass(1, srcFormat, srcFile, destFile);
			if (twoPass)
				encodeOnePass(2, srcFormat, srcFile, destFile);
		} finally {
			if (twoPass) {
				passlogfile.delete();
				passlogfile = null;
			}
		}
	}
	public void encodeOnePass(int pass, VideoCodec srcFormat, File srcFile, File destFile) throws EncoderException, InterruptedException
	{
		currentPass = pass;
		Process process = null;
		try {
			process = createProcess(srcFormat, srcFile, destFile);
		} catch (IOException e) {
			throw new VideoEncoderException(e, srcFile);
		}
		encode(process, srcFile, destFile);
	}
	@Override
	protected void encode(Process process, File sourceFile, File destFile) throws EncoderException, InterruptedException
	{
		try {
			super.encode(process, sourceFile, destFile);
		} catch (EncoderException e) {
			// lame output buffer too small is not a real error. the file is fine.
			if (e.getMessage().contains("lame: output buffer too small"))
				return;
			// catch TMCD data track errors and report them better Unknown format is not supported as input pixel format
			if (e.getMessage().contains("Unknown format") && e.getMessage().contains("Data: tmcd"))
				throw new UnsupportedFormatException("Quicktime TimeCode tracks (TMCD) are not supported.", e.getDetails(), e.getFile());
			throw e;
		}
	}
	private static int multof(double num, int factor)
	{
		return (int)(num + num%factor);
	}
}
