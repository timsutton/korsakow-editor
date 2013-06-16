package org.korsakow.services.encoders.sound;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class SoundEncoderFactory
{
	private static SoundEncoderFactory defaultFactory;
	public static SoundEncoderFactory getDefaultFactory()
	{
		if (defaultFactory == null)
			defaultFactory = new SoundEncoderFactory();
		return defaultFactory;
	}
	private final Collection<SoundEncoder.SoundEncoderDescription> descriptions = new ArrayList<SoundEncoder.SoundEncoderDescription>();
	private final Collection<SoundFormat> requiredInputFormats = new HashSet<SoundFormat>();
	private final Collection<SoundFormat> requiredOutputFormats = new HashSet<SoundFormat>();
	public void addEncoder(SoundEncoder.SoundEncoderDescription description)
	{
		descriptions.add(description);
	}
	public void addRequiredInputFormat(SoundFormat format)
	{
		requiredInputFormats.add(format);
	}
	public void addRequiredOutputFormat(SoundFormat format)
	{
		requiredOutputFormats.add(format);
	}
	public SoundEncoder createSoundEncoder() throws SoundEncoderException
	{
		for (SoundEncoder.SoundEncoderDescription desc : descriptions) {
			if (!desc.getSupportedInputFormats().containsAll(requiredInputFormats))
				continue;
			if (!desc.getSupportedOutputFormats().containsAll(requiredOutputFormats))
				continue;
			SoundEncoder soundEncoder;
			try {
				soundEncoder = desc.getEncoderClass().newInstance();
			} catch (InstantiationException e) {
				throw new SoundEncoderException(e);
			} catch (IllegalAccessException e) {
				throw new SoundEncoderException(e);
			}
			return soundEncoder;
		}
		throw new SoundEncoderException("No encoder matching the requirements was found");
	}
}
