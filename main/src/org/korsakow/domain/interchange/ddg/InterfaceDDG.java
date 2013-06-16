/**
 * 
 */
package org.korsakow.domain.interchange.ddg;

import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class InterfaceDDG extends AbstractResourceDDG
{
	public static final String DOM_NAME = "Interface";
	public static final String DOM_LIST_NAME = "interfaces";
	public static final String GRID_WIDTH = "gridWidth";
	public static final String GRID_HEIGHT = "gridHeight";
	public InterfaceDDG(Document document) {
		super(document);
	}
	@Override
	public Element create() {
		return create("Interface");
	}
	@Override
	public Element createList() {
		return create("interfaces");
	}
	public void append(Node parent, Long id, String name, int gridWidth, int gridHeight, Long clickSoundId, float clickSoundVolume)
	{
		append(parent, id, name);
    	DomUtil.appendNumberNode(getDocument(), parent, GRID_WIDTH, gridWidth);
    	DomUtil.appendNumberNode(getDocument(), parent, GRID_HEIGHT, gridHeight);
	}
}