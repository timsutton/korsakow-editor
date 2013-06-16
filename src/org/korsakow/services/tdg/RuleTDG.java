package org.korsakow.services.tdg;

import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Element;

public class RuleTDG
{
	public static final String NODE_NAME = "Rule";
	public static int insert(long object_id, long version, String type, String name, long trigger_time) throws XPathException{
		return insert(object_id, DataRegistry.getMaxId(), version, type, name, trigger_time);
	}
	public static int insert(long object_id, long id, long version, String type, String name, long trigger_time) throws XPathExpressionException{
		Element tableElement = createRuleTable(object_id);
		Element element = DomUtil.findChildByIdTag(tableElement, NODE_NAME, ""+id);
		if (element != null)
			return 0;
		
		element = DataRegistry.getDocument().createElement(NODE_NAME);
		tableElement.appendChild(element);

//		createRuleTable(object_id);
//		Element element = DataRegistry.getHelper().appendElement(XPathHelper.formatQuery("/korsakow/descendant::*[id=?]/rules", object_id), NODE_NAME);
		DataRegistry.getHelper().setLong(element, "id", id);
		DataRegistry.getHelper().setLong(element, "version", version);
		return update(id, version, type, name, trigger_time);
	}

	public static int update(long id, long version, String type, String name, long trigger_time) throws XPathExpressionException{
		Element element = DataRegistry.getHelper().findElementByIdTag(id);
//		Element element = DataRegistry.getHelper().xpathAsElement(XPathHelper.formatQuery("/korsakow/descendant::*/rules/Rule[id=? and version=?]", id, version));
		if (element == null)
			return 0;
		DataRegistry.getHelper().setLong(element, "id", id);
		DataRegistry.getHelper().setString(element, "name", name);
		DataRegistry.getHelper().setLong(element, "version", version+1);
		DataRegistry.getHelper().setString(element, "type", type);
		DataRegistry.getHelper().setLong(element, "triggerTime", trigger_time);
		return 1;
	}
	
	public static int delete(long id, long version) throws XPathExpressionException {
		Element element = DataRegistry.getHelper().findElementByIdTag(id);
		if (DomUtil.getLong(element, "version") != version)
			return 0;
		element.getParentNode().removeChild(element);
		return 1;
//		NodeList list = DataRegistry.getHelper().removeNodes(XPathHelper.formatQuery("/korsakow/descendant::*/rules/Rule[id=? and version=?]", id, version));
//		return list.getLength();
	}
	public static int deleteByResource(long resource_id) throws XPathExpressionException {
		Element objectElement = DataRegistry.getHelper().findElementByIdTag(resource_id);
		if (objectElement == null)
			return 0;
		Element tableElement = DomUtil.findChildByTagName(objectElement, "rules");
		if (tableElement == null)
			return 0;
		int count = tableElement.getChildNodes().getLength();
		while (tableElement.hasChildNodes())
			tableElement.removeChild(tableElement.getFirstChild());
		return count;
//		NodeList list = DataRegistry.getHelper().removeNodes(XPathHelper.formatQuery("/korsakow/descendant::*[id=?]/rules", resource_id));
//		return list.getLength();
	}
	
	public static Element createRuleTable(long object_id) throws XPathExpressionException {
		Element objectElement = DataRegistry.getHelper().findElementByIdTag(object_id);
		Element tableElement = DomUtil.findChildByTagName(objectElement, "rules");
		if (tableElement == null) {
			tableElement = DataRegistry.getHelper().appendUniqueElement(objectElement, "rules");
		}
		return tableElement;
//		if (DataRegistry.getHelper().xpathAsElement(XPathHelper.formatQuery("/korsakow/descendant::*[id=?]/rules", object_id)) == null) {
//			DataRegistry.getHelper().appendElement(XPathHelper.formatQuery("/korsakow/descendant::*[id=?]", object_id), "rules");
//		}
	}
	public static void dropTable (long object_id) throws XPathExpressionException{
		Element objectElement = DataRegistry.getHelper().findElementByIdTag(object_id);
		Element tableElement = DomUtil.findChildByTagName(objectElement, "rules");
		if (tableElement != null)
			tableElement.getParentNode().removeChild(tableElement);
//		DataRegistry.getHelper().removeNodes(XPathHelper.formatQuery("/korsakow/descendant::*[id=?]/rules", object_id));
	}
}
