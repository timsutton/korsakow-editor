package org.korsakow.services.tdg;

import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.XPathHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SettingsTDG
{
	public static final String NODE_NAME = "Settings";
	public static int insert(long version) throws XPathException{
		return insert(DataRegistry.getMaxId(), version);
	}
	public static int insert(long id, long version) throws XPathExpressionException{
		Element element = DataRegistry.getHelper().appendElement("/korsakow/settings", NODE_NAME);
		DataRegistry.getHelper().setLong(element, "id", id);
		DataRegistry.getHelper().setLong(element, "version", version);
		return update(id, version);
	}

	public static int delete(long id, long version) throws XPathExpressionException {
		NodeList list = DataRegistry.getHelper().removeNodes(XPathHelper.formatQuery("/korsakow/settings/Settings[id=? and version=?]", id, version));
		return list.getLength();
	}
	
	public static int update(long id, long version) throws XPathExpressionException{
		Element element = DataRegistry.getHelper().findElementByIdTag(id);
//		Element element = DataRegistry.getHelper().xpathAsElement(XPathHelper.formatQuery("/korsakow/settings/Settings[id=? and version=?]", id, version));
		if (element == null)
			return 0;
		DataRegistry.getHelper().setLong(element, "id", id);
		DataRegistry.getHelper().setLong(element, "version", version+1);
		return 1;
	}
	
	public static void  createSettingsTable() throws XPathExpressionException {
		if (DataRegistry.getHelper().xpathAsElement("/korsakow/settings") == null) {
			DataRegistry.getHelper().appendElement("/korsakow", "settings");
		}
	}
	public static void dropTable () throws XPathExpressionException{
		DataRegistry.getHelper().removeNodes(XPathHelper.formatQuery("/korsakow/settings"));
	}
}
