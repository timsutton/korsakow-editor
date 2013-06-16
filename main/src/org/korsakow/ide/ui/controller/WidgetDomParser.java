/**
 * 
 */
package org.korsakow.ide.ui.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.korsakow.ide.Application;
import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Parses a DOM tree and populates a WidgetCanvas from it.
 * @author d
 *
 */
public class WidgetDomParser
{
	private List<WidgetModel> widgets = new ArrayList<WidgetModel>();
	private Map<Long, WidgetModel> idMap = new HashMap<Long, WidgetModel>();
	public WidgetDomParser()
	{
	}
	public List<? extends WidgetModel> getWidgets()
	{
		return widgets;
	}
	public void parseDom(Document doc)
	{
		widgets = new ArrayList<WidgetModel>();
		idMap = new HashMap<Long, WidgetModel>();
		parseDom(doc.getDocumentElement());
	}
	public void parseDom(Element elm)
	{
		if (elm.getTagName().equals("WidgetModel")) {
			widgets.add(parseDomWidget(elm));
		}
		NodeList childNodes = elm.getChildNodes();
		int length = childNodes.getLength();
		for (int i = 0; i < length; ++i) {
			Node child = childNodes.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE)
				parseDom((Element)child);
		}
	}
	private WidgetModel parseDomWidget(Element elm)
	{
		Long id = Long.parseLong(elm.getAttribute("id"));
		String widgetType = DomUtil.getString(elm, "type");
		WidgetModel widget = WidgetType.forId(widgetType).newInstance();
		widget.setId(id);
		widget.setX(DomUtil.getInt(elm, "x"));
		widget.setY(DomUtil.getInt(elm, "y"));
		widget.setWidth(DomUtil.getInt(elm, "width"));
		widget.setHeight(DomUtil.getInt(elm, "height"));
		for (String propertyId : widget.getDynamicPropertyIds()) {
			Node propNode = DomUtil.findChildByTagName(elm, propertyId);
			if (propNode == null) // missing means null valued
				continue;
			widget.setDynamicProperty(propertyId, DomUtil.getString(elm, propertyId));
		}
		return widget;
	}
}