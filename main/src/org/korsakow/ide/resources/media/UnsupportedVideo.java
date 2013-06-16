package org.korsakow.ide.resources.media;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.korsakow.ide.util.UIResourceManager;

/**
 * This is a dummy implementation. 
 * @author d
 *
 */
public class UnsupportedVideo extends AbstractPlayableVideo
{
	public static final long DURATION = 5000;
	private final JComponent component;
	private boolean isPlaying = false;
	private long time;
	float volume;
	
	public UnsupportedVideo(String url)
	{
		JLabel label = new JLabel(UIResourceManager.getIcon(UIResourceManager.ICON_ERROR));
		label.setToolTipText("The video could not be loaded: " + url);
		label.setHorizontalTextPosition(JLabel.CENTER);
		label.setVerticalTextPosition(JLabel.VERTICAL);
		label.setSize(label.getPreferredSize());
		component = label;
	}
	
	public Component getComponent() {
		return component;
	}

	public long getDuration() {
		return DURATION;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	public void dispose() {
	}

	public void start() {
		isPlaying = true;
	}

	public void stop() {
		isPlaying = false;
	}

	public boolean isPlaying() {
		return isPlaying;
	}
	public void setVolume(float volume) {
		this.volume = volume;
	}
	public float getVolume() {
		return volume;
	}
}
