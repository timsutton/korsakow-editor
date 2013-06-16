package org.korsakow.services.tdg;

import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.XPathHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PatternTDG
{
	public static final String NODE_NAME = "Pattern";
	public static int insert(long version, String pattern_type) throws XPathException{
		return insert(DataRegistry.getMaxId(), version, pattern_type);
	}
	public static int insert(long id, long version, String pattern_type) throws XPathExpressionException{
		Element element = DataRegistry.getHelper().appendElement("/korsakow/patterns", NODE_NAME);
		DataRegistry.getHelper().setLong(element, "id", id);
		DataRegistry.getHelper().setLong(element, "version", version);
		return update(id, version, pattern_type);
	}

	public static int update(long id, long version, String pattern_type) throws XPathExpressionException{
		Element element = DataRegistry.getHelper().findElementByIdTag(id);
//		Element element = DataRegistry.getHelper().xpathAsElement(XPathHelper.formatQuery("/korsakow/patterns/Pattern[id=? and version=?]", id, version));
		if (element == null)
			return 0;
		DataRegistry.getHelper().setLong(element, "id", id);
		DataRegistry.getHelper().setLong(element, "version", version+1);
		DataRegistry.getHelper().setString(element, "patternType", pattern_type);
		return 1;
	}
	
	public static int delete(long id, long version) throws XPathExpressionException {
		NodeList list = DataRegistry.getHelper().removeNodes(XPathHelper.formatQuery("/korsakow/patterns/Pattern[id=? and version=?]", id, version));
		return list.getLength();
	}
	public static void  createPatternTable() throws XPathExpressionException {
		if (DataRegistry.getHelper().xpathAsElement("/korsakow/patterns") == null) {
			DataRegistry.getHelper().appendElement("/korsakow", "patterns");
		}
	}
	public static void dropTable () throws XPathExpressionException{
		DataRegistry.getHelper().removeNodes(XPathHelper.formatQuery("/korsakow/patterns"));
	}
}
