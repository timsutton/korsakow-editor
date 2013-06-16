package org.korsakow.ide.resources.media;

import java.awt.Component;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.korsakow.ide.exception.MediaRuntimeException;
import org.korsakow.ide.ui.resources.SingleWaveformPanel;


public class QTSound extends AbstractPlayableSound implements PlayableSound
{
	private Component waveform;
	private QTVideo innerQT;
	
	public QTSound(String url) throws MediaRuntimeException {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream (new FileInputStream (url)));
			AudioInfo audioInfo = new AudioInfo(audioInputStream);
			waveform = new SingleWaveformPanel(audioInfo, 0);
			innerQT = new QTVideo(url);
		} catch (Exception e) {
			throw new MediaRuntimeException(e);
		}
	}
	
	public Component getComponent() {
		return waveform;
	}

	public Dimension getAspectRespectingDimension(Dimension outter) {
		return outter;
	}

	public void dispose() {
		innerQT.dispose();
		innerQT = null; // mostly to help catch usage errors, technically GC should work as well assuming 'this' is GC'd as expected
	}

	public long getDuration() {
		return innerQT.getDuration();
	}

	public boolean isPlaying() {
		return innerQT.isPlaying();
	}

	public void setTime(long time) {
		innerQT.setTime(time);
	}
	public long getTime() {
		return innerQT.getTime();
	}

	public void start() {
		innerQT.start();
	}

	public void stop() {
		innerQT.stop();
	}
	public void setVolume(float volume)
	{
		innerQT.setVolume(volume);
	}
	public float getVolume()
	{
		return innerQT.getVolume();
	}
}
