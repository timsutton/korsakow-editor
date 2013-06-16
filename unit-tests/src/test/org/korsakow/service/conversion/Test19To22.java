package test.org.korsakow.service.conversion;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;
import org.korsakow.ide.util.DomUtil;
import org.korsakow.ide.util.ShellExec.ShellException;
import org.korsakow.services.conversion.ConversionException;
import org.korsakow.services.conversion.ConvertUpTo22_4;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import test.org.korsakow.domain.AbstractDomainObjectTestCase;

/**
 * Probably the minimal test would involve trying to pull out one of each kind of domain object.
 */
public class Test19To22 extends AbstractDomainObjectTestCase
{
	@Test public void testSuccess() throws XPathExpressionException, ConversionException, SAXException, ParserConfigurationException, IOException, TransformerException, ShellException
	{
		Document dom = DomUtil.parseXML(new File("resources/conversion/19_1.krw"));
		ConvertUpTo22_4 cm = new ConvertUpTo22_4(dom);
		cm.convert();
		File outFile = File.createTempFile("19_1", ".krw", parentDir);
		DomUtil.writeDomXML(dom, outFile);
//		ShellExec.revealInPlatformFilesystemBrowser(outFile.getAbsolutePath());
	}
}
