package org.korsakow.domain.interchange.mapper.input;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.korsakow.domain.Keyword;
import org.korsakow.domain.KeywordFactory;
import org.korsakow.domain.interchange.ddg.DynamicPropertiesDDG;
import org.korsakow.domain.interchange.ddg.KeywordDDG;
import org.korsakow.domain.interf.IDynamicProperties;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.ide.DomHelper;
import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class AbstractInputMapper
{
	public AbstractInputMapper()
	{
	}
	
	public List<IKeyword> inputKeywords(Element element)
	{
		List<IKeyword> keywords = new ArrayList<IKeyword>();
		Collection<Element> keywordElements = DomUtil.findChildrenByTagName(element, KeywordDDG.DOM_NAME);
		for (Element keywordElement : keywordElements) {
			Keyword keyword = KeywordFactory.createClean(keywordElement.getTextContent());
			keywords.add(keyword);
		}
		return keywords;
	}
	
	public void inputDynamicProperties(Element element, IDynamicProperties props)
	{
		Collection<String> dynamicPropertyIds = props.getDynamicPropertyIds();
		// you might think this block is how it would be done, but its not.
			//		for (String id : dynamicPropertyIds)
			//		{
			//			String value = DomUtil.getString(element, id);
			//			props.setDynamicProperty(id, value);
			//		}
		NodeList childElements = DomUtil.getChildElements(element);
		int childLength = childElements.getLength();
		for (int i = 0; i < childLength; ++i) {
			Element childElement = (Element)childElements.item(i);
			String childId = childElement.getTagName();
			if (childElement.hasAttribute(DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE) &&
				childElement.getAttribute(DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE).equals(DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE_TRUE))
			{
//				if (dynamicPropertyIds.contains(childId))
//				{
					String value = childElement.getTextContent();
					props.setDynamicProperty(childId, value);
//				}
			}
		}
	}
}
