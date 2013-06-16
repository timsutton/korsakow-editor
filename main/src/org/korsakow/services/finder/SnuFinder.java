package org.korsakow.services.finder;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.NodeListResultSet;
import org.korsakow.ide.XPathHelper;
import org.korsakow.ide.util.DomUtil;
import org.korsakow.services.tdg.SnuTDG;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SnuFinder
{

	public static ResultSet find(long id) throws XPathExpressionException{
		Element element = DataRegistry.getHelper().findElementByIdTag(id);
		ListNodeList nodeList = new ListNodeList();
		if (element != null) {
			if (!element.getTagName().equals(SnuTDG.NODE_NAME))
				throw new XPathExpressionException("Expected '" + SnuTDG.NODE_NAME + "', found '" + element.getTagName() + "'");
			nodeList.add(element);
		}
		return new NodeListResultSet(nodeList);
	}
	public static ResultSet findByInterfaceId(long id) throws XPathExpressionException{
		return DataRegistry.getHelper().xpathAsResultSet(XPathHelper.formatQuery("/korsakow/snus/Snu[interfaceId=?]", id));
	}
	public static ResultSet findByMainMediaId(long id) throws XPathExpressionException{
		NodeList allSnuList = DataRegistry.getDocument().getElementsByTagName("Snu");
		int allSnuListLength = allSnuList.getLength();
		
		List<Node> results = new ArrayList<Node>();
		for (int i = 0; i < allSnuListLength; ++i)
		{
			Element item = (Element)allSnuList.item(i);
			Long mainMediaId = DomUtil.getLong(item, "mainMediaId");
			if (mainMediaId != null && mainMediaId == id)
				results.add(item);
		}
		return new NodeListResultSet(new ListNodeList(results));
	}
//	public static ResultSet findBySoundId(long id) throws XPathExpressionException{
//		return DataRegistry.getHelper().xpathAsResultSet(XPathHelper.formatQuery("/korsakow/snus/Snu[backgroundSoundId=?]", id));
//	}
	public static ResultSet findByPreviewMediaId(long id) throws XPathExpressionException {
		return DataRegistry.getHelper().xpathAsResultSet(XPathHelper.formatQuery("/korsakow/snus/Snu[previewMediaId=?]", id));
	}
	public static ResultSet findByName(String keyword) throws XPathExpressionException {
		List<Node> results = new ArrayList<Node>();
		Set<Node> resultSet = new HashSet<Node>();
		Element top = DomUtil.findElementByPath(DataRegistry.getDocument(), "/korsakow/snus");
		Collection<Element> snus = DomUtil.findChildrenByTagName(top, "Snu");
		for (Element snu : snus)
		{
			String name = DataRegistry.getHelper().getString(snu, "name");
			if (name == null)
				continue;
			if (resultSet.contains(snu))
				continue;
			if (keyword.equals(name)) {
				results.add(snu);
				resultSet.add(snu);
			}
		}
		return new NodeListResultSet(new ListNodeList(results));
	}
	public static ResultSet findByKeyword(String keyword) throws XPathExpressionException {
		List<Node> results = new ArrayList<Node>();
		Element top = DomUtil.findElementByPath(DataRegistry.getDocument(), "/korsakow/snus");
		Collection<Element> snus = DomUtil.findChildrenByTagName(top, "Snu");
		SNUS:
		for (Element snu : snus)
		{
			Element keywords = DomUtil.findChildByTagName(snu, "keywords");
			if (keywords == null)
				continue;
			NodeList valueList = keywords.getElementsByTagName("value");
			int length = valueList.getLength();
			for (int i = 0; i < length; ++i)
			{
				Element valueElement = (Element)valueList.item(i);
				if (keyword.equals(valueElement.getTextContent())) {
					results.add(snu);
					continue SNUS;
				}
			}
		}
		return new NodeListResultSet(new ListNodeList(results));
//		return DataRegistry.getHelper().xpathAsResultSet(XPathHelper.formatQuery("/korsakow/snus/Snu[keywords/Keyword/value=?]", keyword));
	}
	public static ResultSet findByOutKeyword(String keyword) throws XPathExpressionException {
		List<Node> results = new ArrayList<Node>();
		Element top = DomUtil.findElementByPath(DataRegistry.getDocument(), "/korsakow/snus");
		Collection<Element> snus = DomUtil.findChildrenByTagName(top, "Snu");
		SNUS:
		for (Element snu : snus)
		{
			Element rules = DomUtil.findChildByTagName(snu, "rules");
			if (rules == null)
				continue;
			NodeList valueList = rules.getElementsByTagName("Keyword");
			int length = valueList.getLength();
			for (int i = 0; i < length; ++i)
			{
				Element keywordElement = (Element)valueList.item(i);
				String value = DomUtil.getString(keywordElement, "value");
				if (keyword.equals(value)) {
					results.add(snu);
					continue SNUS;
				}
			}
		}
		return new NodeListResultSet(new ListNodeList(results));
//		return DataRegistry.getHelper().xpathAsResultSet(XPathHelper.formatQuery("/korsakow/snus/Snu[rules/rule/keywords/Keyword/value=?]", keyword));
	}
	public static ResultSet findAnyOtherOne(long id) throws XPathExpressionException{
		return DataRegistry.getHelper().xpathAsResultSet(XPathHelper.formatQuery("/korsakow/snus/Snu[id!=?]", id));
	}
	public static ResultSet findAll() throws XPathExpressionException{
		return DataRegistry.getHelper().xpathAsResultSet(XPathHelper.formatQuery("/korsakow/snus/Snu"));
	}
}
