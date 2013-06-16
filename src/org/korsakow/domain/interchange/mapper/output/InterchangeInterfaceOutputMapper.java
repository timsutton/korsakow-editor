package org.korsakow.domain.interchange.mapper.output;

import org.korsakow.domain.interchange.ddg.InterfaceDDG;
import org.korsakow.domain.interchange.ddg.KeywordDDG;
import org.korsakow.domain.interchange.ddg.WidgetDDG;
import org.korsakow.domain.interf.IInterface;
import org.w3c.dom.Element;

public class InterchangeInterfaceOutputMapper extends AbstractOutputMapper
{
	private InterfaceWidgetOutputMapper widgetOutputMapper;
	private InterfaceDDG interfaceDDG;
	private KeywordDDG keywordDDG;
	public InterchangeInterfaceOutputMapper(KeywordDDG keywordDDG, InterfaceDDG interfaceDDG, InterfaceWidgetOutputMapper widgetOutputMapper)
	{
		this.keywordDDG = keywordDDG;
		this.interfaceDDG = interfaceDDG;
		this.widgetOutputMapper = widgetOutputMapper;
	}
	public Element output(IInterface interf)
	{
		Element element = interfaceDDG.create();
		interfaceDDG.append(element, interf.getId(), interf.getName(), interf.getGridWidth(), interf.getGridHeight(), getOptionalId(interf.getClickSound()), interf.getClickSoundVolume());
		element.appendChild(outputKeywords(keywordDDG, interf.getKeywords()));
		element.appendChild(widgetOutputMapper.output(interf.getWidgets()));
		return element;
	}
}
