/**
 * 
 */
package org.korsakow.domain.interchange.ddg;

import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class DomainObjectDDG extends DomDataGateway
{
	public static final String ID = "id";
	public DomainObjectDDG(Document document) {
		super(document);
	}
	protected Element create(String typeName)
	{
		Element elm = getDocument().createElement(typeName);
		return elm;
	}
	public abstract Element create();
	public abstract Element createList();
	public void append(Node parent, Long id)
	{
		DomUtil.appendTextNode(getDocument(), parent, "id", id);
	}
}