package org.korsakow.domain.interchange.mapper;

import org.korsakow.domain.interchange.ddg.DynamicPropertiesDDG;
import org.korsakow.domain.interchange.ddg.InterfaceDDG;
import org.korsakow.domain.interchange.ddg.KeywordDDG;
import org.korsakow.domain.interchange.ddg.WidgetDDG;
import org.korsakow.domain.interchange.mapper.output.InterchangeInterfaceOutputMapper;
import org.korsakow.domain.interchange.mapper.output.InterfaceWidgetOutputMapper;
import org.w3c.dom.Document;

public class OutputMapperFactory {
	public static InterchangeInterfaceOutputMapper createInterfaceOutputMapper(Document document)
	{
		KeywordDDG keywordDDG = new KeywordDDG(document);
		DynamicPropertiesDDG dynamicPropertiesDDG = new DynamicPropertiesDDG(document);
		InterfaceDDG interfaceDDG = new InterfaceDDG(document);
		InterfaceWidgetOutputMapper widgetOutputMapper = createWidgetOutputMapper(document, keywordDDG, dynamicPropertiesDDG);
		InterchangeInterfaceOutputMapper interfaceOutputMapper = new InterchangeInterfaceOutputMapper(keywordDDG, interfaceDDG, widgetOutputMapper);
		return interfaceOutputMapper;
	}
	public static InterfaceWidgetOutputMapper createWidgetOutputMapper(Document document)
	{
		KeywordDDG keywordDDG = new KeywordDDG(document);
		DynamicPropertiesDDG dynamicPropertiesDDG = new DynamicPropertiesDDG(document);
		return createWidgetOutputMapper(document, keywordDDG, dynamicPropertiesDDG);
	}
	public static InterfaceWidgetOutputMapper createWidgetOutputMapper(Document document, KeywordDDG keywordDDG, DynamicPropertiesDDG dynamicPropertiesDDG)
	{
		WidgetDDG widgetDDG = new WidgetDDG(document);
		InterfaceWidgetOutputMapper widgetOutputMapper = new InterfaceWidgetOutputMapper(keywordDDG, dynamicPropertiesDDG, widgetDDG);
		return widgetOutputMapper;
	}
}
