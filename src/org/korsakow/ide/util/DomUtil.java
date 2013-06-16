package org.korsakow.ide.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.korsakow.services.finder.ListNodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class DomUtil
{
	/**
	 * Hack until i whip up a schema/dtd
	 * @param node
	 * @param id
	 * @return
	 */
	public static Element getElementById(Node node, String id)
	{
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element elm = (Element)node;
			if (elm.hasAttribute("id") && elm.getAttribute("id").equals(id))
				return elm;
		}
		
		NodeList childNodes = node.getChildNodes();
		int length = childNodes.getLength();
		for (int i = 0; i < length; ++i)
		{
			if (childNodes.item(i).getNodeType() != Node.ELEMENT_NODE)
				continue;
			Element child = (Element)childNodes.item(i);
			child = getElementById(child, id);
			if (child != null)
				return child;
		}
		return null;
	}
	public static Document parseXMLString(String xmltext) throws SAXException, ParserConfigurationException, IOException
	{
		return parseXML( new ByteArrayInputStream( xmltext.getBytes( "UTF-8" ) ) );
	}
	public static Document parseXMLFile(String filename) throws SAXException, ParserConfigurationException, IOException
	{
		return parseXML(new File(filename));
	}
	public static Document parseXML(File file) throws SAXException, ParserConfigurationException, IOException
	{
		return parseXML(new FileInputStream(file));
	}
	public static Document parseXML(InputStream input) throws SAXException, ParserConfigurationException, IOException
	{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        factory.setValidating(false);
        factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        Document doc = docBuilder.parse(input);
        return doc;
	}
	public static Document createDocument() throws ParserConfigurationException, SAXException
	{
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
//        dbfac.setSchema(schemaFactory.newSchema());
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        return doc;
	}
	public static Transformer createTransformer() throws TransformerException
	{
        TransformerFactory factory = TransformerFactory.newInstance();
//        factory.setAttribute("indent-number", 4);
        Transformer xformer = factory.newTransformer();
        xformer.setOutputProperty(OutputKeys.INDENT, "yes");
        xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        xformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        xformer.setOutputProperty(OutputKeys.METHOD, "xml");
        xformer.setOutputProperty(OutputKeys.MEDIA_TYPE, "text/xml");
        return xformer;
	}
	public static String toXMLString(Node node) throws TransformerException, IOException
	{
		Transformer xformer = createTransformer();
        //create string from xml tree
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(node);
        xformer.transform(source, result);
        sw.close();
        return sw.toString();
	}
	public static void writeDomXML(Document doc, File file) throws TransformerException, IOException
	{
		if (file == null)
			throw new NullPointerException();
		File parentFile = file.getParentFile();
		if (parentFile != null)
			file.getParentFile().mkdirs();
        writeDomXML(doc, new FileOutputStream(file));
	}
	public static void writeDomXML(Document doc, OutputStream output) throws TransformerException, IOException
	{
		// ug, the transformer stuff is broken under OSX for java 1.5, as far as pretty printing xml goes
        XMLSerializer serializer = new XMLSerializer(output, new OutputFormat("xml", "UTF-8", true));
        serializer.serialize(doc);
//		Transformer xformer = createTransformer(doc);
//        StreamResult result = new StreamResult(output);
//        DOMSource source = new DOMSource(doc);
//        xformer.transform(source, result);
        output.close();
	}
	public static Element findChildByTagName(Element parent, String tagName)
	{
		NodeList childNodes = parent.getChildNodes();
		int length = childNodes.getLength();
		for (int i = 0; i < length; ++i) {
			Node child = childNodes.item(i);
			if (child instanceof Element == false)
				continue;
			if (((Element)child).getTagName().equals(tagName))
				return (Element)child;
		}
		return null;
	}
	
	public static Element findElementByPath(Document parent, String path)
	{
		Element element = parent.getDocumentElement();
		String name = element.getTagName();
		if (path.length()==0)
			return null;
		if (path.charAt(0)!='/' || !path.startsWith(name, 1))
			return null;
		if (path.length() < name.length() +2 || path.charAt(name.length()+1)!='/')
			return null;
		path = path.substring(name.length()+2);
		return findElementByPath(parent.getDocumentElement(), path);
	}
	public static Element findElementByPath(Element parent, String path)
	{
		String[] bits = path.split("/");
		for (int i = 0; i < bits.length; ++i)
		{
			Element child = findChildByTagName(parent, bits[i]);
			if (child == null)
				return null;
			parent = child;
		}
		return parent;
	}
	
	public static NodeList getChildElements(Element parent)
	{
		ListNodeList list = new ListNodeList();
		NodeList children = parent.getChildNodes();
		int length = children.getLength();
		for (int i = 0; i < length; ++i) {
			Node item = children.item(i);
			if (item instanceof Element)
				list.add(item);
		}
		return list;
	}
	public static Collection<Element> findChildrenByTagName(Element parent, String tagName)
	{
		Collection<Element> children = new ArrayList<Element>();
		NodeList childNodes = parent.getChildNodes();
		int length = childNodes.getLength();
		for (int i = 0; i < length; ++i) {
			Node child = childNodes.item(i);
			if (child instanceof Element == false)
				continue;
			if (((Element)child).getTagName().equals(tagName))
				children.add((Element)child);
		}
		return children;
	}
	public static Element findChildByIdTag(Element parent, String tagName, String id)
	{
		Collection<Element> children = DomUtil.findChildrenByTagName(parent, tagName);
		for (Element child : children) {
			String childId = getString(child, "id");
			if (id.equals(childId))
				return child;
		}
		return null;
	}
	public static boolean isEqualAsXMLString(Document a, Document b)
	{
		try {
			String strA = toXMLString(a);
			String strB = toXMLString(b);
			return strA.equals(strB);
		} catch (Exception e) {
        	Logger.getLogger(DomUtil.class).debug(e);
			return false;
		}
	}
	/**
	 * Gets a value from child node whose sole contents is a text or cdata section
	 * @param elm
	 * @param childName
	 * @return null if not found
	 */
	public static String getString(Element elm, String childName)
	{
		Element child = findChildByTagName(elm, childName);
		if (child != null) {
			NodeList childNodes = child.getChildNodes();
			int length = childNodes.getLength();
			for (int i = 0; i < length; ++i ) {
				Node valueNode = childNodes.item(i);
				switch (valueNode.getNodeType())
				{
				case Node.CDATA_SECTION_NODE:
				case Node.TEXT_NODE:
					return valueNode.getNodeValue();
				}
			}
			return "";
		}
		return null;
	}
	public static Element setString(Document doc, Element elm, String childName, String value)
	{
		Collection<Element> list = findChildrenByTagName(elm, childName);
		for (Element child : list)
			elm.removeChild(child);
		return appendTextNode(doc, elm, childName, value);
	}
	public static Long getLong(Element elm, String childName)
	{
		String str = getString(elm, childName);
		if (str == null)
			return null;
		return Long.parseLong(str);
	}
	public static Element setLong(Document doc, Element elm, String childName, Long value)
	{
		Collection<Element> list = findChildrenByTagName(elm, childName);
		for (Element child : list)
			elm.removeChild(child);
		return appendNumberNode(doc, elm, childName, value);
	}
	public static Integer getInt(Element elm, String childName)
	{
		String str = getString(elm, childName);
		if (str == null)
			return null;
		return Integer.parseInt(str);
	}
	public static void setInt(Document doc, Element elm, String childName, Integer value)
	{
		Collection<Element> list = findChildrenByTagName(elm, childName);
		for (Element child : list)
			elm.removeChild(child);
		appendNumberNode(doc, elm, childName, value);
	}
	public static Float getFloat(Element elm, String childName)
	{
		String str = getString(elm, childName);
		if (str == null)
			return null;
		return Float.parseFloat(str);
	}
	public static void setFloat(Document doc, Element elm, String childName, Float value)
	{
		Collection<Element> list = findChildrenByTagName(elm, childName);
		for (Element child : list)
			elm.removeChild(child);
		appendNumberNode(doc, elm, childName, value);
	}
	public static Boolean getBoolean(Element elm, String childName)
	{
		String str = getString(elm, childName);
		if (str == null)
			return null;
		return Boolean.parseBoolean(str);
	}
	public static Element setBoolean(Document doc, Element elm, String childName, Boolean value)
	{
		Collection<Element> list = findChildrenByTagName(elm, childName);
		for (Element child : list)
			elm.removeChild(child);
		return appendBooleanNode(doc, elm, childName, value);
	}
	/**
	 * Creates and appends an element whose textvalue is value
	 * @param elm parent element
	 * @param name name of new child
	 * @param value inner text value
	 */
	public static Element appendTextNode(Document doc, Node elm, String name, Object value)
	{
		if (value == null)
			return null;
		Element valueNode = doc.createElement(name);
		valueNode.appendChild(doc.createCDATASection(value.toString()));
		elm.appendChild(valueNode);
		return valueNode;
	}
	public static Element appendNumberNode(Document doc, Node elm, String name, Number value)
	{
		return DomUtil.appendNumberNode(doc, elm, name, value, null);
	}
	public static Element appendNumberNode(Document doc, Node elm, String name, Number value, Object defaultValue)
	{
		return appendTextNode(doc, elm, name, value!=null?value:defaultValue);
	}
	public static Element appendBooleanNode(Document doc, Node elm, String name, boolean value)
	{
		return appendTextNode(doc, elm, name, value?"true":"false");
	}
}
