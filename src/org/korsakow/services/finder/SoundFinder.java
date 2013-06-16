package org.korsakow.services.finder;

import java.sql.ResultSet;

import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.NodeListResultSet;
import org.korsakow.services.tdg.SoundTDG;
import org.w3c.dom.Element;

public class SoundFinder
{
	public static ResultSet find(long id) throws XPathExpressionException{
		Element element = DataRegistry.getHelper().findElementByIdTag(id);
		ListNodeList nodeList = new ListNodeList();
		if (element != null) {
			if (!element.getTagName().equals(SoundTDG.NODE_NAME))
				throw new XPathExpressionException("Expected '" + SoundTDG.NODE_NAME + "', found '" + element.getTagName() + "'");
			nodeList.add(element);
		}
		return new NodeListResultSet(nodeList);
	}
	public static ResultSet findAll() throws XPathExpressionException{
		return DataRegistry.getHelper().xpathAsResultSet("/korsakow/sounds/Sound");
	}
}
