package test.util;

import javax.xml.xpath.XPathExpressionException;

import org.junit.Assert;

import org.korsakow.ide.XPathHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class DomAssert
{
	public static void assertDomPathExists(Document doc, String path, Object... args) throws XPathExpressionException
	{
		Node node = XPathHelper.xpathAsNode(doc, path, args);
		Assert.assertTrue(node != null);
	}
	public static void assertDomPathNotExists(Document doc, String path, Object... args) throws XPathExpressionException
	{
		Node node = XPathHelper.xpathAsNode(doc, path, args);
		Assert.assertTrue(node == null);
	}
	public static void assertDomPathEquals(Long expected, Document doc, String path, Object... args) throws XPathExpressionException
	{
		Long actual = XPathHelper.xpathAsLong(doc, path, args);
		Assert.assertEquals(expected, actual);
	}
	public static void assertDomPathEquals(Integer expected, Document doc, String path, Object... args) throws XPathExpressionException
	{
		Integer actual = XPathHelper.xpathAsInt(doc, path, args);
		Assert.assertEquals(expected, actual);
	}
	public static void assertDomPathEquals(String expected, Document doc, String path, Object... args) throws XPathExpressionException
	{
		String actual = XPathHelper.xpathAsString(doc, path, args);
		Assert.assertEquals(expected, actual);
	}
	public static void assertDomPathEquals(Boolean expected, Document doc, String path, Object... args) throws XPathExpressionException
	{
		Boolean actual = XPathHelper.xpathAsBoolean(doc, path, args);
		Assert.assertEquals(expected, actual);
	}
	public static void assertDomPathEquals(Float expected, Document doc, String path, Object... args) throws XPathExpressionException
	{
		Float actual = XPathHelper.xpathAsFloat(doc, path, args);
		Assert.assertEquals(expected, actual);
	}
}

