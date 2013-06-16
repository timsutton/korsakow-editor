/**
 * 
 */
package org.korsakow.ide.ui.interfacebuilder.widget;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.korsakow.ide.util.UIUtil;

public class ImageLabel extends JLabel
{
	private final ImageIcon icon;
	public ImageLabel(ImageIcon icon)
	{
		this.icon = icon;
		setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
		setIcon(null);
	}
	@Override
	public void doLayout()
	{
		super.doLayout();
		UIUtil.runUITaskLater(new Runnable() {
			public void run() {
				if (getWidth() == 0 || getHeight() == 0)
					return;
				if (getIcon() == null || (icon.getIconWidth() != getWidth() || icon.getIconHeight() != getHeight())) {
					Image image = icon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_FAST);
					setIcon(new ImageIcon(image));
				}
			}
		});
	}
}