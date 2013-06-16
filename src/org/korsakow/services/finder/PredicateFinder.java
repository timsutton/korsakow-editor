package org.korsakow.services.finder;

import java.sql.ResultSet;

import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.NodeListResultSet;
import org.korsakow.ide.util.DomUtil;
import org.korsakow.services.tdg.PredicateTDG;
import org.w3c.dom.Element;

public class PredicateFinder
{
	public static ResultSet find(long id) throws XPathExpressionException{
		Element element = DataRegistry.getHelper().findElementByIdTag(id);
		ListNodeList nodeList = new ListNodeList();
		if (element != null) {
			if (!element.getTagName().equals(PredicateTDG.NODE_NAME))
				throw new XPathExpressionException("Expected '" + PredicateTDG.NODE_NAME + "', found '" + element.getTagName() + "'");
			nodeList.add(element);
		}
		return new NodeListResultSet(nodeList);
	}
	public static ResultSet findAll() throws XPathExpressionException{
		return DataRegistry.getHelper().xpathAsResultSet("/korsakow/child::*/child::*/predicates/Predicate");
	}
	
	public static ResultSet findByResource(long id) throws XPathExpressionException{
		Element resourceElement = DataRegistry.getHelper().findElementByIdTag(id);
		if (resourceElement == null)
			return new NodeListResultSet(new ListNodeList());
		
		Element predicatesElement = DomUtil.findChildByTagName(resourceElement, "predicates");
		if (predicatesElement == null)
			return new NodeListResultSet(new ListNodeList());
		
		return new NodeListResultSet(DomUtil.getChildElements(predicatesElement));
	}
}
