package org.korsakow.services.conversion;


import java.awt.Color;

import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;

import org.korsakow.domain.interchange.ddg.DynamicPropertiesDDG;
import org.korsakow.ide.XPathHelper;
import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.util.DomUtil;
import org.korsakow.services.util.ColorFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ConvertUpTo22_91 extends ConversionModule
{
	private static final String ALPHA_REGEXP = "#[a-zA-Z0-9]{2}([a-zA-Z0-9]{2})([a-zA-Z0-9]{2})([a-zA-Z0-9]{2})";
	public ConvertUpTo22_91(Document document)
	{
		super(document);
	}

	@Override
	public void convert() throws ConversionException
	{
		try {
			document.getDocumentElement().setAttribute("versionMajor", "5.0.5.2");
			document.getDocumentElement().setAttribute("versionMinor", "22.91");

			stripAlpha("fontColor");
			stripAlpha("foregroundColor");
			stripAlpha("backgroundColor");
			stripAlpha("loadingColor");
			
			convertBase10To16("fontColor");
			convertBase10To16("foregroundColor");
			convertBase10To16("backgroundColor");
			convertBase10To16("loadingColor");
			
			
			// #1475
			for (Node node : helper.xpathAsList("//Widget")) {
				applyFix1475(node, "fontColor");
				applyFix1475(node, "foregroundColor");
				applyFix1475(node, "backgroundColor");
			}
			for (Node node : helper.xpathAsList("//Widget[widgetType=? or widgetType=? or widgetType=? or widgetType=? or widgetType=?]", 
					WidgetType.SnuAutoLink.getId(),
					WidgetType.SnuAutoMultiLink.getId(),
					WidgetType.SnuFixedLink.getId(),
					WidgetType.InsertText.getId(),
					WidgetType.Subtitles.getId()
					)) {
				if (helper.getString((Element)node, "fontColor") == null)
					helper.setString((Element)node, "fontColor", ColorFactory.formatCSS(Color.white))
						.setAttribute(DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE, DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE_TRUE);
				if (helper.getString((Element)node, "fontFamily") == null)
					helper.setString((Element)node, "fontFamily", "Courier")
						.setAttribute(DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE, DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE_TRUE);
				if (helper.getString((Element)node, "fontSize") == null)
					helper.setString((Element)node, "fontSize", ""+10)
						.setAttribute(DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE, DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE_TRUE);
			}
		} catch (XPathException e) {
			throw new ConversionException(e);
		} catch (NumberFormatException e) {
			throw new ConversionException(e);
		}
	}

	private void stripAlpha(String colorPropertyName) throws XPathExpressionException
	{
		for (Node node : helper.xpathAsList("//"+colorPropertyName)) {
			String value = node.getTextContent();
			// alpha is the first pair if present
			if (value.matches(ALPHA_REGEXP)) {
				value = value.replaceAll(ALPHA_REGEXP, "#$1$2$3");
				node.setTextContent(value);
			}
		}
		
	}
	private void convertBase10To16(String colorPropertyName) throws XPathExpressionException
	{
		for (Node node : helper.xpathAsList("//"+colorPropertyName)) {
			String value = node.getTextContent();
			// sometimes colors were stored in integer form instead of CSS form
			try {
				int num = Integer.parseInt(value);
				// was number so convert
				value = ColorFactory.formatCSS(ColorFactory.createRGB(num));
			} catch (NumberFormatException e) {
				// sanity check, ensure it was in CSS form
				try {
					ColorFactory.createRGB(value);
					// all is well, leave value alone
				} catch (NumberFormatException ee) {
					// it was somehow otherwise invalid, so coerce and warn the user
					value = ColorFactory.formatCSS(Color.white);
					String interfaceName = XPathHelper.xpathAsString(node, "ancestor::Interface/name");
					addWarning(String.format("There was a problem with some of the widget colors in the interface '%s'. Please inspect the interface and resolve the issues.", interfaceName));
				}
			}
			node.setTextContent(value);
		}
	}
	private void applyFix1475(Node node, String colorPropertyName) throws XPathExpressionException
	{
		String value;
		if ((value=helper.getString((Element)node, colorPropertyName)) != null) {
			String dyn = DomUtil.findChildByTagName((Element)node, colorPropertyName).getAttribute(DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE);
			// #1475 incorrectly caused some colors to be stored as -1
			if ("-1".equals(value))
				value = ColorFactory.formatCSS(Color.white);

			helper.setString((Element)node, colorPropertyName, value)
				.setAttribute(DynamicPropertiesDDG.DYNAMIC_ATTRIBUTE, dyn);
		}
	}
	
}
