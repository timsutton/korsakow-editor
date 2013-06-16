package org.korsakow.services.tdg;

import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.XPathHelper;
import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Element;

/**
 * Keywords are identified by their value
 */
public class KeywordTDG
{
	public static final String NODE_NAME = "Keyword";
	public static int replace(long object_id, String keyword, float weight) throws XPathExpressionException{
		Element tableElement = createKeywordTable(object_id);
		Element element = XPathHelper.xpathAsElement(tableElement, XPathHelper.formatQuery("Keyword[value=?]", keyword));
		if (element == null) {
			element = DataRegistry.getDocument().createElement(NODE_NAME);
			tableElement.appendChild(element);
		}
//		Element element = DataRegistry.getHelper().xpathAsElement(XPathHelper.formatQuery("/korsakow/descendant::*[id=?]/keywords/Keyword[value=?]", object_id, keyword));
//		if (element == null) {
//			element = DataRegistry.getHelper().appendElement(XPathHelper.formatQuery("/korsakow/descendant::*[id=?]/keywords", object_id), NODE_NAME);
//		}
		DataRegistry.getHelper().setString(element, "value", keyword);
		DataRegistry.getHelper().setFloat(element, "weight", weight);
		return 1;
	}
	public static int insert(long object_id, String keyword, float weight) throws XPathExpressionException{
		return replace(object_id, keyword, weight);
	}
	
	public static int update(long object_id, String keyword, float weight) throws XPathExpressionException{
		return replace(object_id, keyword, weight);
	}
	public static int delete(long object_id, String keyword) throws XPathExpressionException {
		Element objectElement = DataRegistry.getHelper().findElementByIdTag(object_id);
		if (objectElement == null)
			return 0;
		Element tableElement = DomUtil.findChildByTagName(objectElement, "keywords");
		if (tableElement == null)
			return 0;
		Element element = XPathHelper.xpathAsElement(tableElement, XPathHelper.formatQuery("Keyword[value=?]", keyword));
		if (element != null)
			element.getParentNode().removeChild(element);
		return 1;
//		NodeList list = DataRegistry.getHelper().removeNodes(XPathHelper.formatQuery("/korsakow/descendant::*[id=?]/keywords/Keyword[value=?]", object_id, keyword));
//		return list.getLength();
	}
	public static int deleteByObject(long object_id) throws XPathExpressionException {
		Element objectElement = DataRegistry.getHelper().findElementByIdTag(object_id);
		if (objectElement == null)
			return 0;
		Element tableElement = DomUtil.findChildByTagName(objectElement, "keywords");
		if (tableElement == null)
			return 0;
		int count = tableElement.getChildNodes().getLength();
		while (tableElement.hasChildNodes())
			tableElement.removeChild(tableElement.getFirstChild());
		return count;
//		NodeList list = DataRegistry.getHelper().removeNodes("/korsakow/descendant::*[id=?]/keywords/Keyword", object_id);
//		return list.getLength();
	}
	
	public static Element createKeywordTable(long object_id) throws XPathExpressionException {
		Element objectElement = DataRegistry.getHelper().findElementByIdTag(object_id);
		Element tableElement = DomUtil.findChildByTagName(objectElement, "keywords");
		if (tableElement == null) {
			tableElement = DataRegistry.getHelper().appendUniqueElement(objectElement, "keywords");
		}
		return tableElement;
//		if (DataRegistry.getHelper().xpathAsElement("/korsakow/descendant::*[id=?]/keywords", object_id) == null) {
//			DataRegistry.getHelper().appendElement(XPathHelper.formatQuery("/korsakow/descendant::*[id=?]", object_id), "keywords");
//		}
	}
	public static void dropTable (long object_id) throws XPathExpressionException{
		Element objectElement = DataRegistry.getHelper().findElementByIdTag(object_id);
		Element tableElement = DomUtil.findChildByTagName(objectElement, "keywords");
		if (tableElement != null)
			tableElement.getParentNode().removeChild(tableElement);
//		DataRegistry.getHelper().removeNodes("/korsakow/descendant::*[id=?]/keywords", object_id);
	}
}












