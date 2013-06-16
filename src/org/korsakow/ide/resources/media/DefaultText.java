package org.korsakow.ide.resources.media;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.korsakow.ide.util.FileUtil;

/**
 * This is a naive and pretty terrible implementation. It reads and rescales the image whenver layout changes.
 * Originally it cached the image but this turned out to be a pretty bad idea when dealing with large images.
 * Java has poor support for getting image info without reading the whole damned thing.
 * @author d
 *
 */
public class DefaultText extends AbstractPlayableText
{
	private JComponent component;
	private String text;
	
	public DefaultText(String text)
	{
		this.text = text;
	}
	public DefaultText(File file) throws IOException
	{
		this(FileUtil.readFileAsString(file));
	}
	public void dispose()
	{
		component = null;
	}

	public Component getComponent() {
		if (component == null) {
			component = new TextLabel(text);
		}
		return component;
	}
	private static class TextLabel extends JLabel
	{
		public TextLabel(String text)
		{
			setText(text.replaceAll("<br\\s* (?:\\/)?>", "<br>")); // JLabel html support is weak, can't even handle xhtml
		}
	}
}
