package org.korsakow.services.conversion;


import org.korsakow.ide.Build;
import org.w3c.dom.Document;

public class ConvertUpToBuild extends ConversionModule
{
	public ConvertUpToBuild(Document document)
	{
		super(document);
	}

	@Override
	public void convert() throws ConversionException
	{
		document.getDocumentElement().setAttribute("versionMajor", Build.getVersion());
		document.getDocumentElement().setAttribute("versionMinor", ""+Build.getRelease());
	}
	
}
