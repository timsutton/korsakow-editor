package org.korsakow.domain.mapper.input;

import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.services.finder.PropertyFinder;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PropertyInputMapper {
	
	public static Map<String, String> map(long object_id) throws MapperException {
		try {
			NodeList list = PropertyFinder.findByObject(object_id);
			HashMap<String, String> props = new HashMap<String, String>();
			int length = list.getLength();
			for (int i = 0; i < length; ++i) {
				Element elm = (Element)list.item(i);
				props.put(elm.getTagName(), elm.getTextContent());
			}
			return props;
		} catch (XPathExpressionException e) {
			throw new MapperException(e);
		}
	}
}
