package org.korsakow.domain.interf;

import java.awt.Font;
import java.util.Collection;

import org.korsakow.domain.MediaSource;


public interface IText extends IMedia
{
	String getText();
	void setText(String text);
	void setSource(MediaSource source);
	/**
	 * 
	 * @return getText() or getFilename() depending on getSource()
	 */
	String getValue();
	Collection<Font> getFonts() throws Exception;
}
