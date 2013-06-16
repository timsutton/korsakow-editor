package org.korsakow.services.finder;

import java.sql.ResultSet;

import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.NodeListResultSet;
import org.korsakow.ide.XPathHelper;
import org.korsakow.services.tdg.InterfaceTDG;
import org.w3c.dom.Element;

public class InterfaceFinder
{
	public static ResultSet find(long id) throws XPathExpressionException{
		Element element = DataRegistry.getHelper().findElementByIdTag(id);
		ListNodeList nodeList = new ListNodeList();
		if (element != null) {
			if (!element.getTagName().equals(InterfaceTDG.NODE_NAME))
				throw new XPathExpressionException("Expected '" + InterfaceTDG.NODE_NAME + "', found '" + element.getTagName() + "'");
			nodeList.add(element);
		}
		return new NodeListResultSet(nodeList);
	}
	public static ResultSet findByWidget(long widget_id) throws XPathExpressionException{
		return DataRegistry.getHelper().xpathAsResultSet(XPathHelper.formatQuery("/korsakow/interfaces/Interface[widgets/Widgets/id=?]", widget_id));
	}
	public static ResultSet findBySound(long sound_id) throws XPathExpressionException{
		return DataRegistry.getHelper().xpathAsResultSet(XPathHelper.formatQuery("/korsakow/interfaces/Interface[clickSoundId=?]", sound_id));
	}
	public static ResultSet findAll() throws XPathExpressionException{
		return DataRegistry.getHelper().xpathAsResultSet(XPathHelper.formatQuery("/korsakow/interfaces/Interface"));
	}
}
