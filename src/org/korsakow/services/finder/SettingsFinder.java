package org.korsakow.services.finder;

import java.sql.ResultSet;

import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.NodeListResultSet;
import org.korsakow.services.tdg.SettingsTDG;
import org.w3c.dom.Element;

public class SettingsFinder
{
	public static ResultSet find() throws XPathExpressionException{
		Element element = DataRegistry.getHelper().xpathAsElement("/korsakow/settings/"+SettingsTDG.NODE_NAME);
		ListNodeList nodeList = new ListNodeList();
		if (element != null)
			nodeList.add(element);
		return new NodeListResultSet(nodeList);
	}
	public static ResultSet find(long id) throws XPathExpressionException{
		return DataRegistry.getHelper().xpathAsResultSet("/korsakow/settings/Settings[id=?]", id);
	}
}
