package org.korsakow.services.finder;

import java.sql.ResultSet;

import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.NodeListResultSet;
import org.korsakow.ide.XPathHelper;
import org.korsakow.services.tdg.WidgetTDG;
import org.w3c.dom.Element;

public class WidgetFinder
{
	public static ResultSet find(long id) throws XPathExpressionException{
		Element element = DataRegistry.getHelper().findElementByIdTag(id);
		ListNodeList nodeList = new ListNodeList();
		if (element != null) {
			if (!element.getTagName().equals(WidgetTDG.NODE_NAME))
				throw new XPathExpressionException("Expected '" + WidgetTDG.NODE_NAME + "', found '" + element.getTagName() + "'");
			nodeList.add(element);
		}
		return new NodeListResultSet(nodeList);
	}
	public static ResultSet findAll() throws XPathExpressionException{
		return DataRegistry.getHelper().xpathAsResultSet(XPathHelper.formatQuery("/korsakow/interfaces/Interface/widgets/Widget"));
	}
	public static ResultSet findByInterfaceId(long interface_id) throws XPathExpressionException{
		return DataRegistry.getHelper().xpathAsResultSet(XPathHelper.formatQuery("/korsakow/interfaces/Interface[id=?]/widgets/Widget", interface_id));
	}
}
