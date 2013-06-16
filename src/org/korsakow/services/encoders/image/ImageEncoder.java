package org.korsakow.services.encoders.image;

import java.io.File;
import java.util.Collection;

import org.korsakow.services.encoders.IEncoder;

public interface ImageEncoder extends IEncoder
{
	static interface ImageEncoderDescription
	{
		Collection<ImageFormat> getSupportedInputFormats();
		Collection<ImageFormat> getSupportedOutputFormats();
		Class<? extends ImageEncoder> getEncoderClass();
	}
	void setEncoderSpecificOption(Object name, Object value) throws UnsupportedOperationException;
	Object getEncoderSpecificOption(Object name) throws UnsupportedOperationException;
	
	void setSize(Integer width, Integer height) throws UnsupportedOperationException;

	void encode(File sourceFile, ImageFormat dstFormat, File destFile) throws ImageEncoderException;
	
	String getFileExtension(ImageFormat format) throws UnsupportedOperationException, ImageEncoderException;
}
