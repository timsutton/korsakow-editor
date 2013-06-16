package org.korsakow.ide;

import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DomHelper extends XPathHelper
{
	private final Document document;
	public DomHelper(Document document)
	{
		super(document);
		this.document = document;
	}
	public Document getDocument()
	{
		return document;
	}

	public Element findElementByIdTag(String id)
	{
		return findElementByIdTag(document.getDocumentElement(), id);
	}
	/**
	 * Finds elements containing a tag of the form <id>id</id>. It is a design contract of the application
	 * that such ids are unique.
	 * 
	 * @param id
	 * @return
	 */
	public static Element findElementByIdTag(Element parent, String id)
	{
		NodeList list = parent.getElementsByTagName("id");
		int length = list.getLength();
		for (int i = 0; i < length; ++i)
		{
			Element elm = (Element)list.item(i);
			if (elm.getTextContent().equals(id))
				return (Element)elm.getParentNode();
		}
		return null;
	}
	public Element findElementByIdTag(Object id)
	{
		return findElementByIdTag(id.toString());
	}
	
	public NodeList removeNodes(String query) throws XPathExpressionException
	{
		NodeList nodes = xpathAsNodeList(query);
		int length = nodes.getLength();
		for (int i = 0; i < length; ++i) {
			Node child = nodes.item(i);
			if (child.getParentNode() != null)
				child.getParentNode().removeChild(child);
		}
		return nodes;
	}
	public NodeList removeNodes(String query, Object... args) throws XPathExpressionException
	{
		return removeNodes(formatQuery(query, args));
	}
	public Element appendElement(String query, String name) throws XPathExpressionException
	{
		Element parent = xpathAsElement(query);
		if (parent == null)
			throw new XPathExpressionException("parent node not found: " + query);
		Element child = document.createElement(name);
		parent.appendChild(child);
		return child;
	}
	public Element appendElement(String query, String name, String text) throws XPathExpressionException
	{
		Element element = appendElement(query, name);
		element.appendChild(document.createCDATASection(text));
		return element;
	}
	public Element appendUniqueElement(Element parent, String name)
	{
		Element element = DomUtil.findChildByTagName(parent, name);
		if (element == null) {
			element = document.createElement(name);
			parent.appendChild(element);
		}
		return element;
	}
	public Element setString(Element elm, String childName, String value)
	{
		return DomUtil.setString(document, elm, childName, value);
	}
	public String getString(Element elm, String childName)
	{
		return DomUtil.getString(elm, childName);
	}
	public Long getLong(Element elm, String childName)
	{
		return DomUtil.getLong(elm, childName);
	}
	public Element setLong(Element elm, String childName, Long value)
	{
		return DomUtil.setLong(document, elm, childName, value);
	}
	public Integer getInt(Element elm, String childName)
	{
		return DomUtil.getInt(elm, childName);
	}
	public void setInt(Element elm, String childName, Integer value)
	{
		DomUtil.setInt(document, elm, childName, value);
	}
	public Float getFloat(Element elm, String childName)
	{
		return DomUtil.getFloat(elm, childName);
	}
	public void setFloat(Element elm, String childName, Float value)
	{
		DomUtil.setFloat(document, elm, childName, value);
	}
	public Element setBoolean(Element elm, String childName, Boolean value)
	{
		return DomUtil.setBoolean(document, elm, childName, value);
	}
	public Boolean getBoolean(Element elm, String childName)
	{
		return DomUtil.getBoolean(elm, childName);
	}
}
