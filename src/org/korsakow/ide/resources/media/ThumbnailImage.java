package org.korsakow.ide.resources.media;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.korsakow.ide.util.UIUtil;

/**
 * Assumes you'll only ever use it to display at a small size and so keeps a scaled down version in cache.
 * @author d
 *
 */
public class ThumbnailImage extends AbstractPlayableImage
{
	private JComponent component;
	private final String filename;
	
	public ThumbnailImage(String filename)
	{
		this.filename = filename;
	}
	public void dispose()
	{
		component = null;
	}

	public Component getComponent() {
		if (component == null) {
			// 320x240 is just a random number that will probably be a "good enough" compromise
			// the worst case scenario is showing a large image say 2000x2000 or more
			// but for previewing in the interfacer the quality loss from the scale down has so far
			// been acceptable
			component = new ImageLabel(filename, 320, 240);
		}
		return component;
	}
	private static class ImageLabel extends JLabel
	{
		private final String filename;
		private final Image cache;
		private int width;
		private int height;
		public ImageLabel(String filename, int width, int height)
		{
			this.filename = filename;
			
			ImageIcon icon = new ImageIcon(filename);
			// no need for the cache to be larger than the original
			width = Math.min(width, icon.getIconWidth());
			height = Math.min(height, icon.getIconHeight());
			
			Dimension scaledDim = AbstractPlayableImage.getAspectRespectingDimension(width, height, icon.getIconWidth() / (float)icon.getIconHeight());
			cache = icon.getImage().getScaledInstance(scaledDim.width, scaledDim.height, Image.SCALE_FAST);
			setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
			setIcon(icon);
		}
		@Override
		public void doLayout()
		{
			super.doLayout();
			UIUtil.runUITaskLater(new Runnable() {
				public void run() {
					if (getWidth()>0 && getHeight()>0 &&
						(getIcon().getIconWidth() != getWidth() || getIcon().getIconHeight() != getHeight())) {
						Image image = cache.getScaledInstance(getWidth(), getHeight(), Image.SCALE_FAST);
						setIcon(new ImageIcon(image));
					}
				}
			});
		}
	}
}
