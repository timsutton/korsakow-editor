/**
 * 
 */
package org.korsakow.domain.interchange.ddg;

import java.util.Map;

import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class KeywordDDG extends DomainObjectDDG
{
	public static final String DOM_NAME = "Keyword";
	public static final String DOM_LIST_NAME = "keywords";
	public KeywordDDG(Document document) {
		super(document);
	}
	public void append(Node parent, String keyword, Float weight)
	{
		Element element = DomUtil.appendTextNode(getDocument(), parent, DOM_NAME, keyword);
//		element.setAttribute("weight", ""+weight);
	}
	@Override
	public Element create() {
		return create(DOM_NAME);
	}
	@Override
	public Element createList() {
		return create(DOM_LIST_NAME);
	}
}