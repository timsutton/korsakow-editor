package org.korsakow.services.tdg;

import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.XPathHelper;
import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Element;

public class EventTDG
{
	public static final String NODE_NAME = "Event";
	public static int insert(long object_id, long version) throws XPathException{
		return insert(object_id, DataRegistry.getMaxId(), version);
	}
	public static int insert(long object_id, long id, long version) throws XPathExpressionException{
		Element tableElement = createEventTable(object_id);
		Element element = XPathHelper.xpathAsElement(tableElement, XPathHelper.formatQuery("Event[id=?]", id));
		if (element != null)
			return 0;
		
		element = DataRegistry.getDocument().createElement(NODE_NAME);
		tableElement.appendChild(element);

		DataRegistry.getHelper().setLong(element, "id", id);
		DataRegistry.getHelper().setLong(element, "version", version);
		return update(id, version);
	}

	public static int update(long id, long version) throws XPathExpressionException{
		Element element = DataRegistry.getHelper().findElementByIdTag(id);
		if (element == null)
			return 0;
		DataRegistry.getHelper().setLong(element, "id", id);
		DataRegistry.getHelper().setLong(element, "version", version+1);
		return 1;
	}
	
	public static int delete(long id, long version) throws XPathExpressionException {
		Element element = DataRegistry.getHelper().findElementByIdTag(id);
		if (DomUtil.getLong(element, "version") != version)
			return 0;
		element.getParentNode().removeChild(element);
		return 1;
	}
	public static int deleteByResource(long resource_id) throws XPathExpressionException {
		Element objectElement = DataRegistry.getHelper().findElementByIdTag(resource_id);
		if (objectElement == null)
			return 0;
		Element tableElement = DomUtil.findChildByTagName(objectElement, "events");
		if (tableElement == null)
			return 0;
		int count = tableElement.getChildNodes().getLength();
		while (tableElement.hasChildNodes())
			tableElement.removeChild(tableElement.getFirstChild());
		return count;
	}
	
	public static Element createEventTable(long object_id) throws XPathExpressionException {
		Element objectElement = DataRegistry.getHelper().findElementByIdTag(object_id);
		Element tableElement = DomUtil.findChildByTagName(objectElement, "events");
		if (tableElement == null) {
			tableElement = DataRegistry.getHelper().appendUniqueElement(objectElement, "events");
		}
		return tableElement;
	}
	public static void dropTable (long object_id) throws XPathExpressionException{
		Element objectElement = DataRegistry.getHelper().findElementByIdTag(object_id);
		Element tableElement = DomUtil.findChildByTagName(objectElement, "events");
		if (tableElement != null)
			tableElement.getParentNode().removeChild(tableElement);
	}
}
