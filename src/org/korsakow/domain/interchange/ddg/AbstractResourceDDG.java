/**
 * 
 */
package org.korsakow.domain.interchange.ddg;

import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public abstract class AbstractResourceDDG extends DomainObjectDDG
{
	public static final String NAME = "name";
	public AbstractResourceDDG(Document document) {
		super(document);
	}
	public void append(Node parent, Long id, String name)
	{
		append(parent, id);
		DomUtil.appendTextNode(getDocument(), parent, NAME, name);
	}
}