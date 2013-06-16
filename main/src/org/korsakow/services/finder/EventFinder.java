package org.korsakow.services.finder;

import java.sql.ResultSet;

import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.NodeListResultSet;
import org.korsakow.ide.util.DomUtil;
import org.korsakow.services.tdg.EventTDG;
import org.w3c.dom.Element;

public class EventFinder
{
	public static ResultSet find(long id) throws XPathExpressionException{
		Element element = DataRegistry.getHelper().findElementByIdTag(id);
		ListNodeList nodeList = new ListNodeList();
		if (element != null) {
			if (!element.getTagName().equals(EventTDG.NODE_NAME))
				throw new XPathExpressionException("Expected '" + EventTDG.NODE_NAME + "', found '" + element.getTagName() + "'");
			nodeList.add(element);
		}
		return new NodeListResultSet(nodeList);
	}
	public static ResultSet findAll() throws XPathExpressionException{
		return DataRegistry.getHelper().xpathAsResultSet("/korsakow/child::*/child::*/events/Event");
	}
	
	public static ResultSet findByResource(long id) throws XPathExpressionException{
		Element resourceElement = DataRegistry.getHelper().findElementByIdTag(id);
		if (resourceElement == null)
			return new NodeListResultSet(new ListNodeList());
		
		Element eventsElement = DomUtil.findChildByTagName(resourceElement, "events");
		if (eventsElement == null)
			return new NodeListResultSet(new ListNodeList());
		
		return new NodeListResultSet(DomUtil.getChildElements(eventsElement));
	}
}
