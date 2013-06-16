package org.korsakow.domain.interchange.mapper.output;

import java.util.Collection;

import org.korsakow.domain.interchange.ddg.DynamicPropertiesDDG;
import org.korsakow.domain.interchange.ddg.KeywordDDG;
import org.korsakow.domain.interchange.ddg.WidgetDDG;
import org.korsakow.domain.interf.IWidget;
import org.w3c.dom.Element;

public class InterfaceWidgetOutputMapper extends AbstractOutputMapper
{
	private WidgetDDG widgetDDG;
	private KeywordDDG keywordDDG;
	private DynamicPropertiesDDG dynamicPropertiesDDG;
	public InterfaceWidgetOutputMapper(KeywordDDG keywordDDG, DynamicPropertiesDDG dynamicPropertiesDDG, WidgetDDG widgetDDG)
	{
		this.keywordDDG = keywordDDG;
		this.widgetDDG = widgetDDG;
		this.dynamicPropertiesDDG = dynamicPropertiesDDG;
	}
	public Element output(IWidget widget)
	{
		Element element = widgetDDG.create();
		widgetDDG.append(element, widget.getId(), widget.getWidgetId(), widget.getPersistCondition().getId(), widget.getPersistAction().getId(), widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight());
		element.appendChild(outputKeywords(keywordDDG, widget.getKeywords()));
		outputDynamicProperties(element, dynamicPropertiesDDG, widget);
		return element;
	}
	public Element output(Collection<IWidget> widgets)
	{
		Element element = widgetDDG.createList();
		for (IWidget widget : widgets)
			element.appendChild(output(widget));
		return element;
	}
}
