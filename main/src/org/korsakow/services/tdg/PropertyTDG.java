package org.korsakow.services.tdg;

import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.DataRegistry;
import org.w3c.dom.Element;

public class PropertyTDG
{
	public static final String DYNAMIC_ATTRIBUTE_NAME = "dynamic";
	public static final String DYNAMIC_ATTRIBUTE_VALUE = "true";
	/**
	 * Adds the "dynamic=true" attribute, which marks the element as being dynamic/abstract property.
	 * @param element
	 */
	public static void setDynamicAttribute(Element element)
	{
		element.setAttribute(DYNAMIC_ATTRIBUTE_NAME, DYNAMIC_ATTRIBUTE_VALUE);
	}
	public static boolean isDynamic(Element element)
	{
		String value = element.getAttribute(DYNAMIC_ATTRIBUTE_NAME);
		if (value == null)
			return false;
		return value.equals(DYNAMIC_ATTRIBUTE_VALUE);
	}
	public static void insert(long object_id, String property_id, Object value) throws XPathExpressionException
	{
		Element element = DataRegistry.getHelper().findElementByIdTag(object_id);
//		Element element = DataRegistry.getHelper().xpathAsElement("/korsakow/descendant::*[id=?]", object_id);
		Element property = DataRegistry.getHelper().setString(element, property_id, value!=null?value.toString():null);
		if (property != null)
			setDynamicAttribute(property);
	}
	public static void insert(long object_id, String property_id, long value) throws XPathExpressionException
	{
		Element element = DataRegistry.getHelper().findElementByIdTag(object_id);
//		Element element = DataRegistry.getHelper().xpathAsElement("/korsakow/descendant::*[id=?]", object_id);
		Element property = DataRegistry.getHelper().setLong(element, property_id, value);
		setDynamicAttribute(property);
	}
	public static void insert(long object_id, String property_id, String value) throws XPathExpressionException
	{
		Element element = DataRegistry.getHelper().findElementByIdTag(object_id);
//		Element element = DataRegistry.getHelper().xpathAsElement("/korsakow/descendant::*[id=?]", object_id);
		Element property = DataRegistry.getHelper().setString(element, property_id, value);
		setDynamicAttribute(property);
	}
	public static void delete(long object_id, String property_id) throws XPathExpressionException
	{
		DataRegistry.getHelper().removeNodes("/korsakow/descendant::*[id=?]/?", object_id, property_id);
	}
}
