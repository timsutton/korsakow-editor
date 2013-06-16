/**
 * 
 */
package org.korsakow.domain.interchange.ddg;

import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class TriggerDDG extends DomainObjectDDG
{
	public TriggerDDG(Document document)
	{
		super(document);
	}
	public Element create()
	{
		return create("Trigger");
	}
	public Element createList()
	{
		return create("triggers");
	}
	public void append(Node parent, Long id, String type, String scope, String time)
	{
		append(parent, id);
		DomUtil.appendTextNode(getDocument(), parent, "type", type);
		DomUtil.appendTextNode(getDocument(), parent, "scope", scope);
		DomUtil.appendTextNode(getDocument(), parent, "time", time);
	}
}