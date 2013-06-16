package org.korsakow.ide.resources.media;

import java.awt.Component;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.log4j.Logger;
import org.korsakow.ide.exception.MediaException;
import org.korsakow.ide.exception.MediaRuntimeException;

/**
 * Implements the PlayableSound interface via javax.sound.*
 * 
 * @author d
 *
 */
public class JSound extends AbstractPlayableSound
{
	private Clip clip;
	private FloatControl volumeControl;
	
	private static AudioInputStream getAudioStream(String url) throws MediaException {
		try {
			return AudioSystem.getAudioInputStream(new URL(url));
		} catch (Exception e) {
			throw new MediaException(e);
		}
	}
	
	private static AudioInputStream getAudioStream(InputStream stream) throws MediaException {
		try {
			return AudioSystem.getAudioInputStream(new BufferedInputStream(stream));
		} catch (Exception e) {
			throw new MediaException(e);
		}
	}
	
	public JSound(String url) throws MediaException {
		this(getAudioStream(url));
	}
	
	public JSound(InputStream stream) throws MediaException, UnsupportedAudioFileException, IOException {
		this(getAudioStream(stream));
	}
	
	public JSound(AudioInputStream soundStream) throws MediaException
	{
		try {
			clip = AudioSystem.getClip();
			clip.open(soundStream);
		} catch (Exception e) {
			throw new MediaException(e);
		}

		try {
			volumeControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
		} catch (IllegalArgumentException e) { // if for some reason volume is not supported, though this should never happen
			Logger.getLogger(JSound.class).warn("JSound couldn't provide volume control", e);
			throw new MediaException(e);
		}
	}
	public float getVolume() {
		return clip.getLevel();
	}

	public void setVolume(float volume) {
		volumeControl.setValue(volume);
	}

	public void dispose() {
		clip.close();
	}

	public Component getComponent() {
		throw new MediaRuntimeException("no component for JSound yet");
	}

	public long getDuration() {
		return clip.getMicrosecondLength()*1000;
	}

	public long getTime() {
		return clip.getMicrosecondPosition()*1000;
	}

	public boolean isPlaying() {
		return clip.isRunning();
	}

	public void setTime(long time) {
		clip.setMicrosecondPosition(time*1000);
	}

	public void start() {
		clip.start();
	}

	public void stop() {
		clip.stop();
	}

	public Dimension getAspectRespectingDimension(Dimension outter) {
		Dimension inner = getComponent().getPreferredSize();
		float aspectRatio = inner.width/(float)inner.height;
		if(outter.width/aspectRatio < outter.height) {
			return new Dimension(outter.width, (int)(outter.width/aspectRatio));
		} else {
			return new Dimension((int)(outter.height*aspectRatio), outter.height);
		}
	}
}
