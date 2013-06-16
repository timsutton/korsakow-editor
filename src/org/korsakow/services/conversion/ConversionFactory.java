package org.korsakow.services.conversion;

import java.math.BigDecimal;

import javax.xml.xpath.XPathExpressionException;

import org.korsakow.ide.Build;
import org.korsakow.ide.DomHelper;
import org.w3c.dom.Document;

public class ConversionFactory extends ConversionModule
{
	public ConversionFactory(Document document)
	{
		super(document);
	}

	/**
	 * @return null if the document is up to date
	 * @throws ConversionException on error or if no module is found for the document's version
	 */
	private IConversionModule getConversionModule(Document document, BigDecimal targetVersion) throws ConversionException {
		BigDecimal versionMinor;
		String versionMajor;
		try {
			versionMinor = new BigDecimal(DomHelper.xpathAsString(document, "/korsakow/@versionMinor"));
			versionMajor = DomHelper.xpathAsString(document, "/korsakow/@versionMajor");
		} catch (XPathExpressionException e) {
			throw new ConversionException(e);
		} catch (NumberFormatException e) {
			throw new ConversionException(e);
		}
		if (versionMinor.compareTo(targetVersion) >= 0)
			return null;
		if (versionMinor.compareTo(new BigDecimal("22.4")) < 0)
			return new ConvertUpTo22_4(document);
		if (versionMinor.compareTo(new BigDecimal("22.91")) < 0)
			return new ConvertUpTo22_91(document);
		if (versionMinor.compareTo(new BigDecimal("22.94")) < 0)
			return new ConvertUpTo22_94(document);
		if (versionMinor.compareTo(new BigDecimal("23.0")) < 0)
			return new ConvertUpTo23_0(document);
		if (versionMinor.compareTo(new BigDecimal("23.10")) < 0)
			return new ConvertUpTo23_10(document);
		if (versionMinor.compareTo(new BigDecimal("24.10")) < 0)
			return new ConvertUpTo24_10(document);
		if (versionMinor.compareTo(new BigDecimal("25.02")) < 0)
			return new ConvertUpTo25_02(document);
		return new ConvertUpToBuild(document);
	}

	@Override
	public void convert() throws ConversionException
	{
		IConversionModule module = null;
		while ((module = getConversionModule(document, Build.getRelease2())) != null) {
			module.convert();
			warnings.addAll(module.getWarnings());
		}
	}
}
