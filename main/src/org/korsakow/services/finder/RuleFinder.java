package org.korsakow.services.finder;

import java.sql.ResultSet;

import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.NodeListResultSet;
import org.korsakow.ide.util.DomUtil;
import org.korsakow.services.tdg.RuleTDG;
import org.w3c.dom.Element;

public class RuleFinder
{
	public static ResultSet find(long id) throws XPathExpressionException{
		Element element = DataRegistry.getHelper().findElementByIdTag(id);
		ListNodeList nodeList = new ListNodeList();
		if (element != null) {
			if (!element.getTagName().equals(RuleTDG.NODE_NAME))
				throw new XPathExpressionException("Expected '" + RuleTDG.NODE_NAME + "', found '" + element.getTagName() + "'");
			nodeList.add(element);
		}
		return new NodeListResultSet(nodeList);
	}
	public static ResultSet findAll() throws XPathExpressionException{
		return DataRegistry.getHelper().xpathAsResultSet("/korsakow/child::*/child::*/rules/Rule");
	}
	
	public static ResultSet findByResource(long id) throws XPathExpressionException{
		Element resourceElement = DataRegistry.getHelper().findElementByIdTag(id);
		if (resourceElement == null)
			return new NodeListResultSet(new ListNodeList());
		
		Element rulesElement = DomUtil.findChildByTagName(resourceElement, "rules");
		if (rulesElement == null)
			return new NodeListResultSet(new ListNodeList());
		
		return new NodeListResultSet(DomUtil.getChildElements(rulesElement));
//		NodeList list = XPathHelper.xpathAsNodeList(resourceElement, "rules/Rule");
//		return new NodeListResultSet(list);
//		return DataRegistry.getHelper().xpathAsResultSet("/korsakow/child::*/child::*[id=?]/rules/Rule", id);
	}
}
