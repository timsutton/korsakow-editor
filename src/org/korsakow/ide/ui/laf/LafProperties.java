package org.korsakow.ide.ui.laf;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.DomHelper;
import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LafProperties
{
	private static final String ROOT = "/laf";
	private static final String PATH_SEPARATOR = "/";
	private static final Map<String, TextAttribute> FONT_ATTRIBUTES = new HashMap<String, TextAttribute>();
	{
		FONT_ATTRIBUTES.put("family", TextAttribute.FAMILY);
		FONT_ATTRIBUTES.put("size", TextAttribute.SIZE);
		FONT_ATTRIBUTES.put("weight", TextAttribute.WEIGHT);
//		attributes.put("", TextAttribute.);
//		attributes.put("", TextAttribute.);
//		attributes.put("", TextAttribute.);
	}
	private Document document;
	private DomHelper helper;
	public LafProperties(InputStream input) throws LafException
	{
		try {
			document = DomUtil.parseXML(input);
			helper = new DomHelper(document);
		} catch (IOException e) {
			throw new LafException(e);
		} catch (SAXException e) {
			throw new LafException(e);
		} catch (ParserConfigurationException e) {
			throw new LafException(e);
		}
	}
	private Insets parseInsets(String key) throws XPathExpressionException
	{
		Element elm = helper.xpathAsElement(key);
		int left = helper.getInt(elm, "left");
		int top = helper.getInt(elm, "top");
		int right = helper.getInt(elm, "right");
		int bottom = helper.getInt(elm, "bottom");
		Insets insets = new Insets(top, left, bottom, right);
		return insets;
	}
	private Font parseFont(String key) throws XPathExpressionException
	{
		Map<TextAttribute, Object> atts = new HashMap<TextAttribute, Object>();
		NodeList list = helper.xpathAsNodeList(key + "/child::*");
		int length = list.getLength();
		for (int i = 0; i < length; ++i)
		{
			Element element = (Element)list.item(i);
			String name = element.getTagName();
			TextAttribute att = FONT_ATTRIBUTES.get(name);
			if (att == null)
				continue;
			Object value = element.getTextContent();
			if (att == TextAttribute.SIZE) {
				value = Float.parseFloat(value.toString());
			} else
			if (att == TextAttribute.WEIGHT) {
				value = Float.parseFloat(value.toString());
			}
			atts.put(att, value);
		}
		return new Font(atts);
	}
	private Color parseColor(String key) throws XPathExpressionException, LafException
	{
		Element element = helper.xpathAsElement(key);
		if (element == null)
			return null;
		String value = element.getTextContent();
		String format = element.getAttribute("format");
		Color color = null;
		if ("hex".equals(format)) {
			Integer hex = Integer.valueOf(value, 16);
			color = new Color(hex, false);
		} else
			throw new LafException("unknown color format: " + format);
		return color;
	}
	private Integer parseInteger(String key) throws LafException, XPathExpressionException
	{
		Element element = helper.xpathAsElement(key);
		if (element == null)
			return null;
		String value = element.getTextContent();
		try {
			return Integer.valueOf(value, 10);
		} catch (NumberFormatException e) {
			throw new LafException(e);
		}
	}
	private Boolean parseBoolean(String key) throws LafException, XPathExpressionException
	{
		Element element = helper.xpathAsElement(key);
		if (element == null)
			return null;
		String value = element.getTextContent();
		try {
			return Boolean.valueOf(value);
		} catch (NumberFormatException e) {
			throw new LafException(e);
		}
	}
	private static String getLastPathComponent(String path)
	{
		String[] bits = path.split(PATH_SEPARATOR);
		if (bits.length == 0)
			return path;
		return bits[bits.length-1];
	}
	private String getLastPathComponentType(String path) throws XPathExpressionException
	{
		Node node = helper.xpathAsNode(path + "/attribute::type");
		if (node == null)
			return "";
		return node.getNodeValue();
	}
	public Object get(String key) throws LafException
	{
		try {
			return getImpl(ROOT + PATH_SEPARATOR + key);
		} catch (XPathExpressionException e) {
			throw new LafException(e);
		}
	}
	public Object getComponent(String key) throws LafException
	{
		try {
			return getImpl(ROOT + PATH_SEPARATOR + "component" + PATH_SEPARATOR + key);
		} catch (XPathExpressionException e) {
			throw new LafException(e);
		}
	}
	public Object getDefault(String key) throws LafException
	{
		try {
			return getImpl(ROOT + PATH_SEPARATOR + "default" + PATH_SEPARATOR + key);
		} catch (XPathExpressionException e) {
			throw new LafException(e);
		}
	}
	private Object getImpl(String key) throws LafException, XPathExpressionException
	{
		if (getLastPathComponentType(key).equals("Font")) {
			return parseFont(key);
		}
		if (getLastPathComponentType(key).equals("Color")) {
			return parseColor(key);
		}
		if (getLastPathComponentType(key).equals("Insets")) {
			return parseInsets(key);
		}
		if (getLastPathComponentType(key).equals("Integer")) {
			return parseInteger(key);
		}
		if (getLastPathComponentType(key).equals("Boolean")) {
			return parseBoolean(key);
		}
		throw new LafException("unknown type: " + key);
//		if (key.endsWith(FONT_SUFFIX)) {
//			
//		}
	}
	public Map<String, Object> getDefaultValues() throws XPathExpressionException, LafException
	{
		Map<String, Object> map = new HashMap<String, Object>();
		NodeList list = helper.xpathAsNodeList(ROOT + "/default/child::*");
		int length = list.getLength();
		for (int i = 0; i < length; ++i)
		{
			Element element = (Element)list.item(i);
			String tagName = element.getTagName();
			Object value = getImpl(ROOT + "/default/" + tagName);
			map.put(tagName, value);
		}
		return map;
	}
	public void install(UIDefaults defaults) throws LafException
	{
		try {
			Map<String, Object> defaultValues = getDefaultValues();
			
			installColors(defaults);
			installComponents(defaults, defaultValues);
		} catch (XPathExpressionException e) {
			throw new LafException(e);
		}
	}
	public void installColors(UIDefaults defaults) throws LafException, XPathExpressionException
	{
		NodeList complist = helper.xpathAsNodeList(ROOT + "/color/child::*");
		int complength = complist.getLength();
		for (int i = 0; i < complength; ++i)
		{
			Element element = (Element)complist.item(i);
			String tagName = element.getTagName();
			
			NodeList childlist = DomUtil.getChildElements(element);
			Color value = parseColor(ROOT + "/color/" + tagName);
			defaults.put(tagName, new ColorUIResource(value));
		}
	}
	public void installComponents(UIDefaults defaults, Map<String, Object> defaultValues) throws LafException, XPathExpressionException
	{
		NodeList complist = helper.xpathAsNodeList(ROOT + "/component/child::*");
		int complength = complist.getLength();
		for (int i = 0; i < complength; ++i)
		{
			Element element = (Element)complist.item(i);
			String tagName = element.getTagName();
			
			for (String defaultKey : defaultValues.keySet())
				UIManager.put(tagName + "." + defaultKey, defaultValues.get(defaultKey));
			
			NodeList childlist = DomUtil.getChildElements(element);
			int childlength = childlist.getLength();
			for (int j = 0; j < childlength; ++j)
			{
				Element childElement = (Element)childlist.item(j);
				String childName = childElement.getTagName();
				Object value = getImpl(ROOT + "/component/" + tagName + "/" + childName);
				defaults.put(tagName + "." + childName, value);
			}
		}
	}
	public static class LafException extends Exception
	{
		public LafException(String msg)
		{
			super(msg);
		}
		public LafException(Throwable cause)
		{
			super(cause);
		}
	}
}
