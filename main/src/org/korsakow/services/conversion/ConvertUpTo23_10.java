package org.korsakow.services.conversion;


import java.awt.Color;
import java.util.List;

import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.resources.widget.HorizontalTextAlignment;
import org.korsakow.ide.resources.widget.VerticalTextAlignment;
import org.korsakow.ide.util.DomUtil;
import org.korsakow.services.tdg.PropertyTDG;
import org.korsakow.services.util.ColorFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ConvertUpTo23_10 extends ConversionModule
{
	public ConvertUpTo23_10(Document document)
	{
		super(document);
	}

	@Override
	public void convert() throws ConversionException
	{
		try {
			document.getDocumentElement().setAttribute("versionMajor", "5.0.6");
			document.getDocumentElement().setAttribute("versionMinor", "23.10");

			addMissingFields();
		} catch (XPathException e) {
			throw new ConversionException(e);
		} catch (NumberFormatException e) {
			throw new ConversionException(e);
		}
	}
	private void addMissingFields() throws XPathExpressionException
	{
		addIfMissing( "fontColor", "#ffffff", "//Widget[widgetType=? or widgetType=? or widgetType=? or widgetType=? or widgetType=? or widgetType=? or widgetType=? or widgetType=?]",
				WidgetType.SnuFixedLink.getId(), 
				WidgetType.SnuAutoLink.getId(), 
				WidgetType.SnuAutoMultiLink.getId(), 
				WidgetType.Subtitles.getId(), 
				WidgetType.InsertText.getId(), 
				WidgetType.Comments.getId(), 
				WidgetType.PlayTime.getId(), 
				WidgetType.TotalTime.getId() );
		addIfMissing( "fontFamily", "Arial", "//Widget[widgetType=? or widgetType=? or widgetType=?]", WidgetType.SnuFixedLink.getId(), WidgetType.SnuAutoLink.getId(), WidgetType.SnuAutoMultiLink.getId() );
		addIfMissing( "fontSize", "10", "//Widget[widgetType=? or widgetType=? or widgetType=?]", WidgetType.SnuFixedLink.getId(), WidgetType.SnuAutoLink.getId(), WidgetType.SnuAutoMultiLink.getId() );
		addIfMissing( "horizontalTextAlignment", HorizontalTextAlignment.Left.getId(), "//Widget[widgetType=? or widgetType=? or widgetType=?]", WidgetType.SnuFixedLink.getId(), WidgetType.SnuAutoLink.getId(), WidgetType.SnuAutoMultiLink.getId() );
		addIfMissing( "verticalTextAlignment", VerticalTextAlignment.Top.getId(), "//Widget[widgetType=? or widgetType=? or widgetType=?]", WidgetType.SnuFixedLink.getId(), WidgetType.SnuAutoLink.getId(), WidgetType.SnuAutoMultiLink.getId() );
		
		addIfMissing( "backgroundColor", "#000000", "//Widget[widgetType=?]", WidgetType.Scrubber.getId() );
		addIfMissing( "loadingColor", "#dddddd", "//Widget[widgetType=?]", WidgetType.Scrubber.getId() );
		addIfMissing( "loading", "true", "//Widget[widgetType=?]", WidgetType.Scrubber.getId() );
		addIfMissing( "interactive", "true", "//Widget[widgetType=?]", WidgetType.Scrubber.getId() );
		
		fixColor( "fontColor", "//Widget" );
	}
	
	private void addIfMissing( String name, String value, String xpath, Object... args ) throws XPathExpressionException {
		addIfMissing( name, value, helper.xpathAsList( xpath, args ) );
	}
	private void addIfMissing( String name, String value, List<Node> nodes ) {
		for (Node node :  nodes ) {
			if (DomUtil.findChildByTagName( (Element)node, name ) == null) {
				Element e = DomUtil.setString( document, (Element)node, name, value );
				PropertyTDG.setDynamicAttribute( e );
			}
		}
	}
	private void fixColor( String name, String xpath ) throws XPathExpressionException {
		for (Node node :  helper.xpathAsList( xpath ) ) {
			if (DomUtil.findChildByTagName( (Element)node, name ) != null) {
				String value = DomUtil.getString( (Element)node, name);
				if ( value != null && !value.startsWith("#")) {
					int intValue;
					try {
						intValue = Integer.parseInt( value );
					} catch (NumberFormatException e ) {
						throw e;
					}
					Color c = ColorFactory.createRGB( intValue );
					Element e = DomUtil.setString( document, (Element)node, name, ColorFactory.formatCSS( c ) ); 
					PropertyTDG.setDynamicAttribute( e );
				}
			}
		}
	}
}
