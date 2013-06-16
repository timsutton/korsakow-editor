package org.korsakow.services.finder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.NodeListResultSet;
import org.korsakow.ide.XPathHelper;
import org.korsakow.ide.util.DomUtil;
import org.korsakow.services.tdg.SoundTDG;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ResourceFinder
{
	public static NodeList find(long id) throws XPathExpressionException{
		Element element = DataRegistry.getHelper().findElementByIdTag(id);
		ListNodeList nodeList = new ListNodeList();
		if (element != null) {
			nodeList.add(element);
		}
		return nodeList;
	}
	public static NodeList findSnuableMediaNotUsedAsSnuMainMedia() throws XPathExpressionException {
		// this is a somewhat inefficient implementation (not sure how to do it faster though except cutting out the xpath), but currently this method is rarely used so optimization is put off
		NodeList snus = DataRegistry.getHelper().xpathAsNodeList("/korsakow/snus/Snu");
		Set<Long> mainMediaIds = new HashSet<Long>();
		int snusLength = snus.getLength();
		for (int i = 0; i < snusLength; ++i)
		{
			Long id = DomUtil.getLong((Element)snus.item(i), "mainMediaId");
			mainMediaIds.add(id);
		}
		
		ListNodeList nodes = new ListNodeList();
		nodes.addAll(DataRegistry.getHelper().xpathAsNodeList("/korsakow/images/Image"));
		nodes.addAll(DataRegistry.getHelper().xpathAsNodeList("/korsakow/texts/Text"));
//		nodes.addAll(DataRegistry.getHelper().xpathAsNodeList("/korsakow/sounds/Sound")); // Sound currently not snu-able
		nodes.addAll(DataRegistry.getHelper().xpathAsNodeList("/korsakow/videos/Video"));
		nodes.addAll(DataRegistry.getHelper().xpathAsNodeList("/korsakow/patterns/Pattern"));
		
		Collection<Node> toRemove = new ArrayList<Node>();
		for (Node node : nodes) {
			long id = DomUtil.getLong((Element)node, "id");
			if (mainMediaIds.contains(id))
				toRemove.add(node);
		}
		nodes.removeAll(toRemove);
		
		return nodes;
	}
	public static NodeList findAllMedia() throws XPathExpressionException {
		ListNodeList nodes = new ListNodeList();
		nodes.addAll(DataRegistry.getHelper().xpathAsNodeList("/korsakow/images/Image"));
		nodes.addAll(DataRegistry.getHelper().xpathAsNodeList("/korsakow/texts/Text"));
		nodes.addAll(DataRegistry.getHelper().xpathAsNodeList("/korsakow/sounds/Sound"));
		nodes.addAll(DataRegistry.getHelper().xpathAsNodeList("/korsakow/videos/Video"));
		nodes.addAll(DataRegistry.getHelper().xpathAsNodeList("/korsakow/patterns/Pattern"));
		return nodes;
	}
	public static NodeList findByClickSoundId(long id) throws XPathExpressionException{
		return DataRegistry.getHelper().xpathAsNodeList(XPathHelper.formatQuery("/korsakow/descendant::*[clickSoundId=?]", id));
	}
	public static NodeList findByBackgroundSoundId(long id) throws XPathExpressionException{
		return DataRegistry.getHelper().xpathAsNodeList(XPathHelper.formatQuery("/korsakow/descendant::*[backgroundSoundId=?]", id));
	}
	public static NodeList findByPreviewMediaId(long id) throws XPathExpressionException {
		Element rootNode = DataRegistry.getDocument().getDocumentElement();
		Element snusNode = DomUtil.findChildByTagName(rootNode, "snus");
		ListNodeList resultList = new ListNodeList();
		if (snusNode != null) {
			Collection<Element> snusList = DomUtil.findChildrenByTagName(snusNode, "Snu");
			for (Element snuElement : snusList) {
				Long previewMediaId = DomUtil.getLong(snuElement, "previewMediaId");
				if (previewMediaId != null && previewMediaId == id)
					resultList.add(snuElement);
			}
		}
		return resultList;
//		return DataRegistry.getHelper().xpathAsNodeList(XPathHelper.formatQuery("/korsakow/snus/Snu[previewMediaId=?]", id));
	}
}
