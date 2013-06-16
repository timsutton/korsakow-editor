package org.korsakow.ide.resources.media;

import java.awt.Component;

import javax.swing.JPanel;

import org.korsakow.ide.exception.MediaRuntimeException;


public class QTAudioWaveformUnsupported extends QTSound
{

	public QTAudioWaveformUnsupported(String url) throws MediaRuntimeException {
		super(url);
	}
	
	public Component getComponent() {
		return new JPanel();
	}
	
}
