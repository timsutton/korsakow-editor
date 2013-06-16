package org.korsakow.services.finder;

import java.sql.ResultSet;

import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.NodeListResultSet;
import org.korsakow.services.tdg.ProjectTDG;
import org.w3c.dom.Element;

public class ProjectFinder
{
	public static ResultSet find() throws XPathExpressionException{
		Element element = DataRegistry.getHelper().xpathAsElement("/korsakow/projects/"+ProjectTDG.NODE_NAME);
		ListNodeList nodeList = new ListNodeList();
		if (element != null)
			nodeList.add(element);
		return new NodeListResultSet(nodeList);
	}
	
	public static ResultSet find(long id) throws XPathExpressionException{
		Element element = DataRegistry.getHelper().findElementByIdTag(id);
		ListNodeList nodeList = new ListNodeList();
		if (element != null) {
			if (!element.getTagName().equals(ProjectTDG.NODE_NAME))
				throw new XPathExpressionException("Expected '" + ProjectTDG.NODE_NAME + "', found '" + element.getTagName() + "'");
			nodeList.add(element);
		}
		return new NodeListResultSet(nodeList);
	}
	public static ResultSet findBySoundId(long sound_id) throws XPathExpressionException{
		return DataRegistry.getHelper().xpathAsResultSet("/korsakow/projects/Project[backgroundSoundId=? or clickSoundId=?]", sound_id, sound_id);
	}
}
