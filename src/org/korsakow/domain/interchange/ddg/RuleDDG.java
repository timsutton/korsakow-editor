/**
 * 
 */
package org.korsakow.domain.interchange.ddg;

import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class RuleDDG extends DomainObjectDDG
{
	public RuleDDG(Document document) {
		super(document);
	}
	
	public Element create()
	{
		Element elm = create("Rule");
		return elm;
	}
	public Element createList()
	{
		Element rulesNode = getDocument().createElement("rules");
		return rulesNode;
	}
	public void append(Node parent, Long id, String name, String type, String triggerType)
	{
		append(parent, id);
		DomUtil.appendTextNode(getDocument(), parent, "name", name);
		DomUtil.appendTextNode(getDocument(), parent, "type", type);
		DomUtil.appendTextNode(getDocument(), parent, "triggerType", triggerType);
	}
}