package org.korsakow.domain.interchange.mapper;

import org.korsakow.domain.interchange.mapper.input.InterchangeInterfaceInputMapper;
import org.korsakow.domain.interchange.mapper.input.InterchangeWidgetInputMapper;

public class InterchangeInputMapperFactory
{
	public static InterchangeInterfaceInputMapper createInterfaceInputMapper()
	{
		InterchangeWidgetInputMapper widgetInputMapper = createWidgetInputMapper();
		InterchangeInterfaceInputMapper interfaceInputMapper = new InterchangeInterfaceInputMapper(widgetInputMapper);
		return interfaceInputMapper;
	}
	public static InterchangeWidgetInputMapper createWidgetInputMapper()
	{
		InterchangeWidgetInputMapper widgetInputMapper = new InterchangeWidgetInputMapper();
		return widgetInputMapper;
	}
}
