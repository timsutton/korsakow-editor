package org.korsakow.services.finder;

import java.sql.ResultSet;
import java.util.Collection;

import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.NodeListResultSet;
import org.korsakow.ide.util.DomUtil;
import org.korsakow.services.tdg.PropertyTDG;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PropertyFinder
{
	public static ResultSet find(long object_id, String property_id) throws XPathExpressionException
	{
		Element element = DataRegistry.getHelper().findElementByIdTag(object_id);
		Collection<Element> elements = DomUtil.findChildrenByTagName(element, property_id);
		ListNodeList list = new ListNodeList();
		list.addAll(elements);
		return new NodeListResultSet(list);
//		return DataRegistry.getHelper().xpathAsResultSet("/korsakow/descendant::*[id=?]/?", object_id, property_id);
	}
	public static NodeList findByObject(long object_id) throws XPathExpressionException
	{
		Element element = DataRegistry.getHelper().findElementByIdTag(object_id);
		NodeList childList = DomUtil.getChildElements(element);
		int length = childList.getLength();
		ListNodeList returnList = new ListNodeList();
		for (int i = 0; i < length; ++i) {
			Element child = (Element)childList.item(i);
			if (PropertyTDG.isDynamic(child))
				returnList.add(child);
		}
		return returnList;
//		return DataRegistry.getHelper().xpathAsNodeList("/korsakow/descendant::*[id=?]/child::*", object_id);
	}
}
