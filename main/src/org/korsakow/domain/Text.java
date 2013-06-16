package org.korsakow.domain;

import java.awt.Font;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;

import javax.xml.xpath.XPathException;

import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IText;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Text extends Media implements IText
{
	String text;
	Text(long id, long version)
	{
		super(id, version);
	}
	Text(long id, long version, String name, Collection<IKeyword> keywords, MediaSource source, String value)
	{
		super(id, version, name, keywords, source);
		switch (source)
		{
		case INLINE:
			setText(value);
			break;
		case FILE:
			setFilename(value);
			break;
		}
	}
	public Text(long id, long version, String name, Collection<IKeyword> keywords, File file)
	{
		this(id, version, name, keywords, MediaSource.FILE, file.getAbsolutePath());
	}
	public Text(long id, long version, String name, Collection<IKeyword> keywords, String text)
	{
		this(id, version, name, keywords, MediaSource.INLINE, text);
	}
	public String getType()
	{
		return ResourceType.TEXT.getTypeId();
	}
	public String getText()
	{
		return text;
	}
	public void setText(String text)
	{
		this.text = text;
	}
	public String getValue()
	{
		switch (getSource())
		{
		case INLINE:
			return getText();
		case FILE:
			return getFilename();
		default:
			throw new IllegalStateException();
		}
	}
	public void setValue(String value)
	{
		switch (getSource())
		{
		case INLINE:
			setText(value);
			break;
		case FILE:
			setFilename(value);
			break;
		default:
			throw new IllegalStateException();
		}
	}
	/**
	 * 
	 * @return
	 * @throws Exception a whole slew of DOM/XML/IO stuffs
	 */
	public Collection<Font> getFonts() throws Exception
	{
		Document doc;
		switch (getSource())
		{
		case INLINE:
			doc = DomUtil.parseXMLString(getText());
			break;
		case FILE:
			doc = DomUtil.parseXMLFile(getFilename());
			break;
		default:
			throw new IllegalStateException();
		}
		HashSet<Font> fonts = new HashSet<Font>();
		NodeList fontElements = doc.getElementsByTagName("font");
		for (int i = 0; i < fontElements.getLength(); ++i)
		{
			String face = ((Element)fontElements.item(i)).getAttribute("face");
			if (face.length() == 0)
				continue;
			Font font = new Font(face, Font.PLAIN, 10);
			fonts.add(font);
		}
		return fonts;
	}
}
