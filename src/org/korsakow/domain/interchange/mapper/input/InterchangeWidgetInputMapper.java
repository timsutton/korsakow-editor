package org.korsakow.domain.interchange.mapper.input;

import java.util.ArrayList;
import java.util.Collection;

import org.korsakow.domain.Widget;
import org.korsakow.domain.WidgetFactory;
import org.korsakow.domain.interchange.ddg.AbstractResourceDDG;
import org.korsakow.domain.interchange.ddg.KeywordDDG;
import org.korsakow.domain.interchange.ddg.WidgetDDG;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.ide.resources.widget.WidgetPersistAction;
import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Element;

public class InterchangeWidgetInputMapper extends AbstractInputMapper
{
	public IWidget input(Element element)
	{
		Widget widget = WidgetFactory.createNew(DomUtil.getString(element, WidgetDDG.TYPE)); // why createNew and not createClean? because its a dependent mapping?
		widget.setName(DomUtil.getString(element, AbstractResourceDDG.NAME));
		Element persistElement = DomUtil.findChildByTagName(element, WidgetDDG.PERSIST);
		if (persistElement != null) {
			widget.setPersistAction(WidgetPersistAction.forId(DomUtil.getString(persistElement, WidgetDDG.PERSIST_CONDITION)));
			widget.setPersistAction(WidgetPersistAction.forId(DomUtil.getString(persistElement, WidgetDDG.PERSIST_ACTION)));
		}
		widget.setX(DomUtil.getInt(element, WidgetDDG.X));
		widget.setY(DomUtil.getInt(element, WidgetDDG.Y));
		widget.setWidth(DomUtil.getInt(element, WidgetDDG.WIDTH));
		widget.setHeight(DomUtil.getInt(element, WidgetDDG.HEIGHT));
		
		widget.setKeywords(inputKeywords(DomUtil.findChildByTagName(element, KeywordDDG.DOM_LIST_NAME)));
		
		inputDynamicProperties(element, widget);
		
		return widget;
	}
	
	public Collection<IWidget> inputList(Element element)
	{
		Collection<IWidget> widgets = new ArrayList<IWidget>();
		Collection<Element> widgetElements = DomUtil.findChildrenByTagName(element, WidgetDDG.DOM_NAME);
		for (Element widgetElement : widgetElements)
			widgets.add(input(widgetElement));
		return widgets;
	}
}
