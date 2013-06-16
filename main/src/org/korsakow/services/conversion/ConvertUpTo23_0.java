package org.korsakow.services.conversion;


import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;

import org.korsakow.domain.interchange.ddg.DynamicPropertiesDDG;
import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ConvertUpTo23_0 extends ConversionModule
{
	public ConvertUpTo23_0(Document document)
	{
		super(document);
	}

	@Override
	public void convert() throws ConversionException
	{
		try {
			document.getDocumentElement().setAttribute("versionMajor", "5.0.6");
			document.getDocumentElement().setAttribute("versionMinor", "23.0");

			addNewSettings();
		} catch (XPathException e) {
			throw new ConversionException(e);
		} catch (NumberFormatException e) {
			throw new ConversionException(e);
		}
	}
	private void addNewSettings() throws XPathExpressionException
	{
		Element e;
		
		for (Node node : helper.xpathAsList("//Settings")) {
			if ((e=DomUtil.findChildByTagName((Element)node, "encodeVideoOnExport")) == null)
				DomUtil.appendBooleanNode(document, node, "encodeVideoOnExport", true)
					.setAttribute(DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE, DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE_TRUE);
			if ((e=DomUtil.findChildByTagName((Element)node, "showExperimentalWidgets")) == null)
				DomUtil.appendBooleanNode(document, node, "showExperimentalWidgets", false)
					.setAttribute(DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE, DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE_TRUE);
		}
	}
	
}
