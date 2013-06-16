package org.korsakow.domain.interchange.mapper.output;

import java.util.Collection;

import org.dsrg.soenea.domain.interf.IDomainObject;
import org.korsakow.domain.interchange.ddg.DynamicPropertiesDDG;
import org.korsakow.domain.interchange.ddg.KeywordDDG;
import org.korsakow.domain.interf.IDynamicProperties;
import org.korsakow.domain.interf.IKeyword;
import org.w3c.dom.Element;

public class AbstractOutputMapper
{
	protected Long getOptionalId(IDomainObject<Long> domainObject)
	{
		return domainObject==null?null:domainObject.getId();
	}
	protected Element outputKeywords(KeywordDDG keywordDDG, Collection<IKeyword> keywords)
	{
		Element listElement = keywordDDG.createList();
		for (IKeyword keyword : keywords)
		{
			keywordDDG.append(listElement, keyword.getValue(), keyword.getWeight());
		}
		return listElement;
	}
	protected void outputDynamicProperties(Element parent, DynamicPropertiesDDG dynamicPropertiesDDG, IDynamicProperties props)
	{
		for (String id : props.getDynamicPropertyIds()) {
			Object value = props.getDynamicProperty(id);
			dynamicPropertiesDDG.append(parent, id, value);
		}
	}
}
