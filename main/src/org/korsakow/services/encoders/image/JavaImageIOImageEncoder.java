package org.korsakow.services.encoders.image;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import org.korsakow.services.encoders.FileExternalEncoder;

public class JavaImageIOImageEncoder extends FileExternalEncoder implements ImageEncoder
{
	public static class JavaImageIOEncoderDescription implements ImageEncoder.ImageEncoderDescription
	{
		public Class<? extends ImageEncoder> getEncoderClass() {
			return JavaImageIOImageEncoder.class;
		}
		private static final Collection<ImageFormat> inputFormats = Collections.unmodifiableCollection(Arrays.asList(
				ImageFormat.JPG, ImageFormat.PNG
		));
		private static final Collection<ImageFormat> outputFormats = Collections.unmodifiableCollection(Arrays.asList(
				ImageFormat.JPG, ImageFormat.PNG
		));
		public Collection<ImageFormat> getSupportedInputFormats() {
			return inputFormats;
		}
		public Collection<ImageFormat> getSupportedOutputFormats() {
			return outputFormats;
		}
	}

	protected Integer width;
	protected Integer height;
	public void encode(File sourceFile, ImageFormat dstFormat, File destFile) throws ImageEncoderException
	{
		BufferedImage image;
		try {
			image = ImageIO.read(sourceFile);
		} catch (IOException e) {
			throw new ImageEncoderException(e);
		}
		if (width != null && height != null) {
			image = getScaledInstance(image, width, height, RenderingHints.VALUE_INTERPOLATION_BICUBIC, false);
		}
		try {
			if (!ImageIO.getImageWritersByFormatName(getFormatName(dstFormat)).hasNext())
				throw new ImageEncoderException("No Image writer for : " + getFormatName(dstFormat));
			ImageWriter writer = ImageIO.getImageWritersByFormatName(getFormatName(dstFormat)).next();
			ImageWriteParam params = writer.getDefaultWriteParam();
			params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			params.setCompressionQuality(1);
			writer.setOutput(new FileImageOutputStream(destFile));
			writer.write(null, new IIOImage(image, null, null), params);
		} catch (IOException e) {
			throw new ImageEncoderException(e);
		}
	}

	public String getFileExtension(ImageFormat format) throws ImageEncoderException
	{
		switch (format)
		{
		case JPG: return "jpg";
		case PNG: return "png";
		}
		throw new ImageEncoderException("unknown format: " + format);
	}

	public Object getEncoderSpecificOption(Object name) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	public void setEncoderSpecificOption(Object name, Object value) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	public void setSize(Integer width, Integer height) throws UnsupportedOperationException
	{
		this.width = width;
		this.height = height;
	}
	private static String getFormatName(ImageFormat format) throws ImageEncoderException
	{
		switch (format)
		{
		case JPG: return "jpeg";
		case PNG: return "png";
		}
		throw new ImageEncoderException("unknown format: " + format);
	}
	/**
	 * http://today.java.net/pub/a/today/2007/04/03/perils-of-image-getscaledinstance.html
	 */
	public BufferedImage getScaledInstance(
			BufferedImage img,
			int targetWidth,
			int targetHeight,
			Object hint,
			boolean higherQuality)
	{
		int type = (img.getTransparency() == Transparency.OPAQUE) ?
		BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = img;
		int w, h;
		if (higherQuality) {
			// Use multi-step technique: start with original size, then
			// scale down in multiple passes with drawImage()
			// until the target size is reached
			w = img.getWidth();
			h = img.getHeight();
		} else {
			// Use one-step technique: scale directly from original
			// size to target size with a single drawImage() call
			w = targetWidth;
			h = targetHeight;
		}
		
		do {
			if (higherQuality && w > targetWidth) {
				w /= 2;
				if (w < targetWidth) {
					w = targetWidth;
				}
			}
		
			if (higherQuality && h > targetHeight) {
				h /= 2;
				if (h < targetHeight) {
					h = targetHeight;
				}
			}
			
			BufferedImage tmp = new BufferedImage(w, h, type);
			Graphics2D g2 = tmp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
			g2.drawImage(ret, 0, 0, w, h, null);
			g2.dispose();
			
			ret = tmp;
		} while (w != targetWidth || h != targetHeight);
		
		return ret;
	}
}
