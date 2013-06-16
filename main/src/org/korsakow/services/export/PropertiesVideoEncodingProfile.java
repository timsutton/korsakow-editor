package org.korsakow.services.export;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;

import org.korsakow.services.encoders.video.AudioCodec;
import org.korsakow.services.encoders.video.ContainerFormat;
import org.korsakow.services.encoders.video.VideoCodec;

public class PropertiesVideoEncodingProfile implements IVideoEncodingProfile
{
	private static final String PREFIX = "special_";
	private final Properties properties;
	
	public PropertiesVideoEncodingProfile(InputStream inputStream) throws IOException
	{
		Properties props = new Properties();
		props.load(inputStream);
		properties = props;
	}
	public PropertiesVideoEncodingProfile(Properties properties)
	{
		this.properties = properties;
	}
	public String getName()
	{
		return getString("name");
	}
	public Integer getAudioSamplingRate()
	{
		return getInt("audioSamplingRate");
	}
	public Integer getAudioBitRate()
	{
		return getInt("audioBitRate");
	}
	public Integer getVideoBitRate()
	{
		return getInt("videoBitRate");
	}
	public Integer getVideoBitRateTolerance()
	{
		return getInt("videoBitRateTolerance");
	}
	public Boolean getDeinterlace()
	{
		return getBoolean("deinterlace");
	}
	public AudioCodec getAudioCodec()
	{
		String value = getString("audioCodec");
		if (value == null)
			return null;
		return AudioCodec.valueOf(value);
	}
	public VideoCodec getVideoCodec()
	{
		String value = getString("videoCodec");
		if (value == null)
			return null;
		return VideoCodec.valueOf(value);
	}
	public ContainerFormat getContainerFormat()
	{
		String value = getString("containerFormat");
		if (value == null)
			return null;
		return ContainerFormat.valueOf(value);
	}
	
	private String getString(String key)
	{
		return properties.getProperty(key);
	}
	
	private Number parseNumber(String key)
	{
		final String regex = "(\\d*)([KkMmGg])$";
		String value = getString(key);
		if (value == null)
			return null;
		int mul = 1;
		if (value.matches(regex)) {
			char m = value.replaceAll(regex, "$2").charAt(0);
			value = value.replaceAll(regex, "$1");
			switch (m)
			{
			case 'G':
			case 'g':
				mul *= 1024;
			case 'M':
			case 'm':
				mul *= 1024;
			case 'K':
			case 'k':
				mul *= 1024;
				break;
			}
		}
		double d = Double.parseDouble(value);
		return d * mul;
	}
	private Integer getInt(String key)
	{
		Number n = parseNumber(key);
		return n!=null?n.intValue():null;
	}
	
	private Boolean getBoolean(String key)
	{
		String value = getString(key);
		if (value == null)
			return null;
		return Boolean.parseBoolean(value);
	}
	
	public Collection<String> getEncoderSpecificKeys() {
		HashSet<String> keys = new HashSet<String>();
		for (Object key : properties.keySet()) {
			String s = key.toString();
			if (!s.startsWith(PREFIX))
				continue;
			s = s.substring(PREFIX.length());
			keys.add(s);
		}
		return keys;
	}
	public String getEncoderSpecificValue(String key) {
		return getString(PREFIX + key);
	}
}
