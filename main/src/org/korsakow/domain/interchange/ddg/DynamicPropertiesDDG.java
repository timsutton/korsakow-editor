/**
 * 
 */
package org.korsakow.domain.interchange.ddg;

import java.util.Map;

import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DynamicPropertiesDDG extends DomDataGateway
{
	public static final String DYNAMIC_ATTRIBUTE="dynamic";
	public static final String DYNAMIC_ATTRIBUTE_TRUE="true";
	
	public DynamicPropertiesDDG(Document document) {
		super(document);
	}
	
	public void append(Node parent, Map<String, Object> properties)
	{
		for (String id : properties.keySet())
			append(parent, id, properties.get(id));
	}
	public void append(Node parent, String name, Object value)
	{
		Element element = DomUtil.appendTextNode(getDocument(), parent, name, value);
		element.setAttribute(DYNAMIC_ATTRIBUTE, "true");
	}
}