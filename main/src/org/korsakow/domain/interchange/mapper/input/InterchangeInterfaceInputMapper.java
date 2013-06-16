package org.korsakow.domain.interchange.mapper.input;

import org.korsakow.domain.Interface;
import org.korsakow.domain.InterfaceFactory;
import org.korsakow.domain.interchange.ddg.AbstractResourceDDG;
import org.korsakow.domain.interchange.ddg.InterfaceDDG;
import org.korsakow.domain.interchange.ddg.KeywordDDG;
import org.korsakow.domain.interchange.ddg.WidgetDDG;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Element;

public class InterchangeInterfaceInputMapper extends AbstractInputMapper
{
	private final InterchangeWidgetInputMapper widgetInputMapper;
	
	public InterchangeInterfaceInputMapper(InterchangeWidgetInputMapper widgetInputMapper)
	{
		this.widgetInputMapper = widgetInputMapper;
	}
	public IInterface input(Element element)
	{
		Interface interf = InterfaceFactory.createClean(
				DomUtil.getString(element, AbstractResourceDDG.NAME),
				inputKeywords(DomUtil.findChildByTagName(element, KeywordDDG.DOM_LIST_NAME)),
				widgetInputMapper.inputList(DomUtil.findChildByTagName(element, WidgetDDG.DOM_LIST_NAME)),
				DomUtil.getInt(element, InterfaceDDG.GRID_WIDTH),
				DomUtil.getInt(element, InterfaceDDG.GRID_HEIGHT)
		);		
		return interf;
	}
}
