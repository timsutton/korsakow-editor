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
public class UnsupportedText extends AbstractPlayableText
{
	private final JComponent component;
	
	public UnsupportedText(String url)
	{
		JLabel label = new JLabel(UIResourceManager.getIcon(UIResourceManager.ICON_ERROR));
		label.setToolTipText("The text could not be loaded: " + url);
		label.setHorizontalTextPosition(JLabel.CENTER);
		label.setVerticalTextPosition(JLabel.VERTICAL);
		label.setSize(label.getPreferredSize());
		component = label;
	}
	
	public Component getComponent() {
		return component;
	}

	public void dispose() {
	}

}
