package org.korsakow.ide.resources.media;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.korsakow.ide.util.UIResourceManager;

/**
 * This is a dummy implementation. 
 * @author d
 *
 */
public class UnsupportedMedia implements Playable
{
	private final JComponent component;
	private boolean isPlaying = false;
	private long time;
	float volume;
	
	public UnsupportedMedia(String url)
	{
		JLabel label = new JLabel(UIResourceManager.getIcon(UIResourceManager.ICON_ERROR));
		label.setToolTipText("The media could not be loaded: " + url);
		label.setHorizontalTextPosition(JLabel.CENTER);
		label.setVerticalTextPosition(JLabel.VERTICAL);
		label.setSize(label.getPreferredSize());
		component = label;
	}
	
	public Component getComponent() {
		return component;
	}

	public long getDuration() {
		return UnsupportedVideo.DURATION;
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

	public boolean isTemporal() {
		return false;
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
