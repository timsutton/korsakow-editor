/**
 * 
 */
package org.korsakow.domain.interchange.ddg;

import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class WidgetDDG extends DomainObjectDDG
{
	public static final String DOM_NAME = "Widget";
	public static final String DOM_LIST_NAME = "widgets";
	public static final String TYPE = "widgetType";
	public static final String PERSIST = "persist";
	public static final String PERSIST_CONDITION = "condition";
	public static final String PERSIST_ACTION = "action";
	public static final String X = "x";
	public static final String Y = "y";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	
	public WidgetDDG(Document document) {
		super(document);
	}
	@Override
	public Element create()
	{
		return create(DOM_NAME);
	}
	@Override
	public Element createList()
	{
		return create(DOM_LIST_NAME);
	}
	public void append(Node parent, Long id, String type, String persistCondition, String persistAction, Number x, Number y, Number width, Number height)
	{
		append(parent, id);
		DomUtil.appendTextNode(getDocument(), parent, TYPE, type);
		Element persist = getDocument().createElement(PERSIST);
		DomUtil.appendTextNode(getDocument(), persist, PERSIST_CONDITION, persistCondition);
		DomUtil.appendTextNode(getDocument(), persist, PERSIST_ACTION, persistAction);
		DomUtil.appendTextNode(getDocument(), parent, X, x);
		DomUtil.appendTextNode(getDocument(), parent, Y, y);
		DomUtil.appendTextNode(getDocument(), parent, WIDTH, width);
		DomUtil.appendTextNode(getDocument(), parent, HEIGHT, height);
	}
}