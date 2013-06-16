package org.korsakow.ide.resources.media;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.korsakow.ide.util.UIUtil;

/**
 * This is a naive and pretty terrible implementation. It reads and rescales the image whenver layout changes.
 * Originally it cached the image but this turned out to be a pretty bad idea when dealing with large images.
 * Java has poor support for getting image info without reading the whole damned thing.
 * @author d
 *
 */
public class DefaultImage extends AbstractPlayableImage
{
	private JComponent component;
	private String filename;
	
	public DefaultImage(String filename)
	{
		this.filename = filename;
	}
	public void dispose()
	{
		component = null;
	}

	public Component getComponent() {
		if (component == null) {
			component = new ImageLabel(filename);
		}
		return component;
	}
	private static class ImageLabel extends JLabel
	{
		private String filename;
		public ImageLabel(String filename)
		{
			this.filename = filename;
			ImageIcon icon = new ImageIcon(filename);
			setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
			setIcon(new ImageIcon(""));
		}
		public void doLayout()
		{
			super.doLayout();
			UIUtil.runUITaskLater(new Runnable() {
				public void run() {
					if (getIcon().getIconWidth() != getWidth() || getIcon().getIconHeight() != getHeight()) {
						ImageIcon icon = new ImageIcon(filename);
						Image image = icon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_FAST);
						setIcon(new ImageIcon(image));
					}
				}
			});
		}
	}
}
