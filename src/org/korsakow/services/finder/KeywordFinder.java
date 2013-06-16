package org.korsakow.services.finder;

import java.sql.ResultSet;
import java.util.Collection;

import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.NodeListResultSet;
import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class KeywordFinder
{
	public static NodeList findAll() throws XPathExpressionException{
		return DataRegistry.getDocument().getElementsByTagName("Keyword");
	}
	public static NodeList findAllSnu() throws XPathExpressionException{
		return DataRegistry.getHelper().xpathAsNodeList("/korsakow/snus/Snu/keywords/Keyword");
	}
	public static ResultSet find(String value) throws XPathExpressionException{
		return DataRegistry.getHelper().xpathAsResultSet("/korsakow/descendant::*/keywords/Keyword[value=?]", value);
	}
	public static ResultSet findByObject(long id) throws XPathExpressionException{
		ListNodeList list = new ListNodeList();
		Element objectElement = DataRegistry.getHelper().findElementByIdTag(id);
		if (objectElement == null)
			return new NodeListResultSet(list);
		Element tableElement = DomUtil.findChildByTagName(objectElement, "keywords");
		if (tableElement == null)
			return new NodeListResultSet(list);
		Collection<Element> children = DomUtil.findChildrenByTagName(tableElement, "Keyword");
		list.addAll(children);
		return new NodeListResultSet(list);
//		return DataRegistry.getHelper().xpathAsResultSet("/korsakow/descendant::*[id=?]/keywords/Keyword", id);
	}
	public static ResultSet findByObjectRecursive(long parentId) throws XPathExpressionException {
		ListNodeList list = new ListNodeList();
		Element objectElement = DataRegistry.getHelper().findElementByIdTag(parentId);

		NodeList tableList = objectElement.getElementsByTagName("keywords");
		int tableLength = tableList.getLength();
		for (int j = 0; j < tableLength; ++j)
		{
			Element tableElement = (Element)tableList.item(j);
			Collection<Element> children = DomUtil.findChildrenByTagName(tableElement, "Keyword");
			list.addAll(children);
		}
		return new NodeListResultSet(list);
	}
	public static ResultSet findByObjectTypeRecursive(String type) throws XPathExpressionException {
		ListNodeList list = new ListNodeList();
		NodeList objects = DataRegistry.getDocument().getElementsByTagName(type);
		int length = objects.getLength();
		for (int i = 0; i < length; ++i) {
			Element objectElement = (Element)objects.item(i);
			NodeList tableList = objectElement.getElementsByTagName("keywords");
			int tableLength = tableList.getLength();
			for (int j = 0; j < tableLength; ++j)
			{
				Element tableElement = (Element)tableList.item(j);
				Collection<Element> children = DomUtil.findChildrenByTagName(tableElement, "Keyword");
				list.addAll(children);
			}
		}
		return new NodeListResultSet(list);
	}
	public static ResultSet findByObjectType(String type) throws XPathExpressionException{
		ListNodeList list = new ListNodeList();
		NodeList objects = DataRegistry.getDocument().getElementsByTagName(type);
		int length = objects.getLength();
		for (int i = 0; i < length; ++i) {
			Element objectElement = (Element)objects.item(i);
			Element tableElement = DomUtil.findChildByTagName(objectElement, "keywords");
			if (tableElement == null)
				continue;
			Collection<Element> children = DomUtil.findChildrenByTagName(tableElement, "Keyword");
			list.addAll(children);
		}
		return new NodeListResultSet(list);
//		return DataRegistry.getHelper().xpathAsResultSet("/korsakow/descendant::*[id=?]/keywords/Keyword", id);
	}
}
