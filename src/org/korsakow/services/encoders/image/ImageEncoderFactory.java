package org.korsakow.services.encoders.image;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class ImageEncoderFactory
{
	public static ImageEncoderFactory getNewFactory()
	{
		return new ImageEncoderFactory();
	}
	private static Collection<ImageEncoder.ImageEncoderDescription> descriptions = new ArrayList<ImageEncoder.ImageEncoderDescription>();
	public static void addEncoder(ImageEncoder.ImageEncoderDescription description)
	{
		synchronized (descriptions) {
			descriptions.add(description);
		}
	}
	
	private final Collection<ImageFormat> requiredInputFormats = new HashSet<ImageFormat>();
	private final Collection<ImageFormat> requiredOutputFormats = new HashSet<ImageFormat>();
	
	private ImageEncoderFactory()
	{
		
	}
	
	public void addRequiredInputFormat(ImageFormat format)
	{
		requiredInputFormats.add(format);
	}
	public void addRequiredOutputFormat(ImageFormat format)
	{
		requiredOutputFormats.add(format);
	}
	public void clear()
	{
		requiredInputFormats.clear();
		requiredOutputFormats.clear();
	}
	public ImageEncoder createImageEncoder() throws ImageEncoderException
	{
		List<ImageEncoder.ImageEncoderDescription> descs;
		synchronized (descriptions) {
			descs = new ArrayList<ImageEncoder.ImageEncoderDescription>(descriptions);
		}
		
		for (ImageEncoder.ImageEncoderDescription desc : descs) {
			if (!desc.getSupportedInputFormats().containsAll(requiredInputFormats))
				continue;
			if (!desc.getSupportedOutputFormats().containsAll(requiredOutputFormats))
				continue;
			ImageEncoder videoEncoder;
			try {
				videoEncoder = desc.getEncoderClass().newInstance();
			} catch (InstantiationException e) {
				throw new ImageEncoderException(e);
			} catch (IllegalAccessException e) {
				throw new ImageEncoderException(e);
			}
			return videoEncoder;
		}
		throw new ImageEncoderException("No encoder matching the requirements was found");
	}
}
