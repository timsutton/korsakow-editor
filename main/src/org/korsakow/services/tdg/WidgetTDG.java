package org.korsakow.services.tdg;

import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.XPathHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class WidgetTDG
{
	public static final String NODE_NAME = "Widget";
	
	public static int insert(long interface_id, long version, String name, String widgetId, String persistCondition, String persistAction, int x, int y, int width, int height) throws XPathException{
		return insert(interface_id, DataRegistry.getMaxId(), version, name, widgetId, persistCondition, persistAction, x, y, width ,height);
	}
	public static int insert(long interface_id, long id, long version, String name, String widgetId, String persistCondition, String persistAction, int x, int y, int width, int height) throws XPathExpressionException{
		createWidgetTable(interface_id);
		Element element = DataRegistry.getHelper().appendElement(XPathHelper.formatQuery("/korsakow/interfaces/Interface[id=?]/widgets", interface_id), NODE_NAME);
		DataRegistry.getHelper().setLong(element, "id", id);
		DataRegistry.getHelper().setLong(element, "version", version);
		return update(id, version, name, widgetId, persistCondition, persistAction, x, y, width, height);

	}

	public static int delete(long id, long version) throws XPathExpressionException {
		NodeList list = DataRegistry.getHelper().removeNodes(XPathHelper.formatQuery("/korsakow/interfaces/Interface/widgets/Widget[id=? and version=?]", id, version));
		return list.getLength();
	}
	public static int deleteByInterface(long interface_id) throws XPathExpressionException {
		NodeList list = DataRegistry.getHelper().removeNodes(XPathHelper.formatQuery("/korsakow/interfaces/Interface[id=?]/widgets", interface_id));
		return list.getLength();
	}
	
	public static int update(long id, long version, String name, String widgetId, String persistCondition, String persistAction, int x, int y, int width, int height) throws XPathExpressionException{
		Element element = DataRegistry.getHelper().findElementByIdTag(id);
//		Element element = DataRegistry.getHelper().xpathAsElement(XPathHelper.formatQuery("/korsakow/interfaces/Interface/widgets/Widget[id=? and version=?]", id, version));
		if (element == null)
			return 0;
		DataRegistry.getHelper().setLong(element, "id", id);
		DataRegistry.getHelper().setLong(element, "version", version+1);
		DataRegistry.getHelper().setString(element, "name", name);
		DataRegistry.getHelper().setString(element, "widgetType", widgetId);
		DataRegistry.getHelper().setString(element, "persistCondition", persistCondition);
		DataRegistry.getHelper().setString(element, "persistAction", persistAction);
		DataRegistry.getHelper().setInt(element, "x", x);
		DataRegistry.getHelper().setInt(element, "y", y);
		DataRegistry.getHelper().setInt(element, "width", width);
		DataRegistry.getHelper().setInt(element, "height", height);
		return 1;

	}
	
	public static void  createWidgetTable(long interface_id) throws XPathExpressionException {
		if (DataRegistry.getHelper().xpathAsElement(XPathHelper.formatQuery("/korsakow/interfaces/Interface[id=?]/widgets", interface_id)) == null) {
			DataRegistry.getHelper().appendElement(XPathHelper.formatQuery("/korsakow/interfaces/Interface[id=?]", interface_id), "widgets");
		}
	}
	public static void dropTable (long interface_id) throws XPathExpressionException{
		DataRegistry.getHelper().removeNodes(XPathHelper.formatQuery("/korsakow/interfaces/Interface[id=?]/widgets", interface_id));
	}
}
