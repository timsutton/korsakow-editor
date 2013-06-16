package org.korsakow.services.conversion;


import java.util.List;

import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;

import org.korsakow.domain.Settings;
import org.korsakow.ide.util.DomUtil;
import org.korsakow.services.tdg.PropertyTDG;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ConvertUpTo25_02 extends ConversionModule
{
	public ConvertUpTo25_02(Document document)
	{
		super(document);
	}

	@Override
	public void convert() throws ConversionException
	{
		try {
			document.getDocumentElement().setAttribute("versionMajor", "5.0.6.2");
			document.getDocumentElement().setAttribute("versionMinor", "25.02");

			addMissingFields();
		} catch (XPathException e) {
			throw new ConversionException(e);
		} catch (NumberFormatException e) {
			throw new ConversionException(e);
		}
	}
	private void addMissingFields() throws XPathExpressionException
	{
		addIfMissing( Settings.ExportDirectory, "", "//Settings");
		addIfMissing( Settings.ExportVideos, String.valueOf(true), "//Settings");
		addIfMissing( Settings.ExportImages, String.valueOf(true), "//Settings");
		addIfMissing( Settings.ExportSounds, String.valueOf(true), "//Settings");
		addIfMissing( Settings.ExportSubtitles, String.valueOf(true), "//Settings");
		addIfMissing( Settings.ExportWebFiles, String.valueOf(true), "//Settings");
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
}
