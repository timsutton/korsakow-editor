package org.korsakow.services.encoders.font;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;

public class FontEncoderFactory
{
	private static FontEncoderFactory defaultFactory;
	public static FontEncoderFactory getDefaultFactory()
	{
		if (defaultFactory == null)
			defaultFactory = new FontEncoderFactory();
		return defaultFactory;
	}
	private final Collection<FontEncoder.FontEncoderDescription> descriptions = new ArrayList<FontEncoder.FontEncoderDescription>();
	private final Collection<FontFormat> requiredInputFormats = new HashSet<FontFormat>();
	private final Collection<FontFormat> requiredOutputFormats = new HashSet<FontFormat>();
	private final Hashtable<String, Object> requiredOptions = new Hashtable<String, Object>();
	public void addEncoder(FontEncoder.FontEncoderDescription description)
	{
		descriptions.add(description);
	}
	public void addRequiredInputFormat(FontFormat format)
	{
		requiredInputFormats.add(format);
	}
	public void addRequiredOutputFormat(FontFormat format)
	{
		requiredOutputFormats.add(format);
	}
	public void setRequiredOption(String name, Object value)
	{
		requiredOptions.put(name, value);
	}
	public FontEncoder createFontEncoder() throws FontEncoderException
	{
		for (FontEncoder.FontEncoderDescription desc : descriptions) {
			if (!desc.getSupportedInputFormats().containsAll(requiredInputFormats))
				continue;
			if (!desc.getSupportedOutputFormats().containsAll(requiredOutputFormats))
				continue;
			FontEncoder fontEncoder;
			try {
				fontEncoder = desc.getEncoderClass().newInstance();
			} catch (InstantiationException e) {
				throw new FontEncoderException(e);
			} catch (IllegalAccessException e) {
				throw new FontEncoderException(e);
			}
			return fontEncoder;
		}
		throw new FontEncoderException("No encoder matching the requirements was found");
	}
}
