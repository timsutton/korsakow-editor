package org.korsakow.services.encoders.font;

import java.awt.Font;
import java.io.OutputStream;
import java.util.Collection;

import org.korsakow.services.encoders.IEncoder;

public interface FontEncoder extends IEncoder
{
	static interface FontEncoderDescription
	{
		Collection<FontFormat> getSupportedInputFormats();
		Collection<FontFormat> getSupportedOutputFormats();
		Class<? extends FontEncoder> getEncoderClass();
	}
	/**
	 * Adds a font to be encoded. Not all output formats support packaging more than one font into a single package.
	 * @param font
	 */
	void addFont(Font font);
	/**
	 * Removes a font from being encoded.
	 */
	void removeFont(Font font);
	/**
	 * Clears the list of fonts to be encoded.
	 */
	void clearFonts();
	void addAllFonts(Collection<Font> fonts);
	/**
	 * Encodes all fonts that have been added.
	 * @param destFormat
	 * @param destStream
	 * @throws FontEncoderException
	 */
	void encode(FontFormat destFormat, OutputStream destStream) throws FontEncoderException;
}
