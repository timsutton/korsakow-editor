package org.korsakow.services.conversion;


import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ConvertUpTo22_94 extends ConversionModule
{
	public ConvertUpTo22_94(Document document)
	{
		super(document);
	}

	@Override
	public void convert() throws ConversionException
	{
		try {
			document.getDocumentElement().setAttribute("versionMajor", "5.0.5.5");
			document.getDocumentElement().setAttribute("versionMinor", "22.94");

			applyFix1488();
		} catch (XPathException e) {
			throw new ConversionException(e);
		} catch (NumberFormatException e) {
			throw new ConversionException(e);
		}
	}
	private void applyFix1488() throws XPathExpressionException
	{
		Element e;
		
		for (Node node : helper.xpathAsList("//Widget[widgetType=?]", WidgetType.MediaArea.getId())) {
			if ((e=DomUtil.findChildByTagName((Element)node, "fontFamily")) != null)
				e.getParentNode().removeChild(e);
			if ((e=DomUtil.findChildByTagName((Element)node, "fontSize")) != null)
				e.getParentNode().removeChild(e);
		}
	}
	
}
