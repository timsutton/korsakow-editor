package test.org.korsakow.dom;

import java.util.Random;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import test.util.BaseTestCase;

/**
 * Tests get/set value hepler methods in DomUtil. This is an important testcase as essentially all the serial/deserialization
 * in Korsakow makes use of these.
 * @author d
 *
 */
public class TestDomUtilValue extends BaseTestCase
{
	private Document document;
	private Element root;
	private Random random;
	private long before;
	private long after;
	
	@Override
	@Before
	public void setUp() throws Exception
	{
		random = new Random();
		document = DomUtil.createDocument();
		root = document.createElement("root");
		document.appendChild(root);
		
	}
	@Override
	@After
	public void tearDown()
	{
		random = null;
	}
	/**
	 * This test is somewhat for illustrative purposes. testSetXXX() tests the DomUtil.setXXX methods which
	 * should only ever leave 1 element of that name, taking into account if the element already exists.
	 * 
	 * This test does the opposite, calling the appendXXX method, which should result in multiple nodes.
	 * Currently setXXX implementation uses appendXXX, so its also a meaningful test.
	 * 
	 * @throws Exception
	 */
	@Test public void testMultiplicity() throws Exception
	{
		int expected = random.nextInt();
		int count = 10 + random.nextInt(100);
		for (int i = 0; i < count; ++i) // test multiplicity
			DomUtil.appendNumberNode(document, root, "test", expected);
		
		NodeList elements = root.getElementsByTagName("test");
		Assert.assertEquals("multiplicity", count, elements.getLength());
		for (int i = 0; i < elements.getLength(); ++i) {
			Element element = (Element)elements.item(i);
			int actual = Integer.parseInt(element.getTextContent());
			Assert.assertEquals("value"+i, expected, actual);
		}
	}
	/**
	 * In current impl, all/most of the set methods eventually delegate to a append/setString call.
	 * @throws Exception
	 */
	@Test public void testSetString() throws Exception
	{
		int strlen = random.nextInt(100);
		byte[] bytes = new byte[strlen];
		random.nextBytes(bytes);
		String expected = new String(bytes);
		
		for (int i = 0; i < 10; ++i) // test multiplicity
			DomUtil.setString(document, root, "test", expected);
		
		Assert.assertEquals("multiplicity", 1, root.getElementsByTagName("test").getLength());
		Element element = (Element)root.getElementsByTagName("test").item(0);
		String actual = element.getTextContent();
		Assert.assertEquals("value", expected, actual);
	}
	@Test public void testSetInt() throws Exception
	{
		int expected = random.nextInt();
		for (int i = 0; i < 10; ++i) // test multiplicity
			DomUtil.setInt(document, root, "test", expected);
		
		Assert.assertEquals("multiplicity", 1, root.getElementsByTagName("test").getLength());
		Element element = (Element)root.getElementsByTagName("test").item(0);
		int actual = Integer.parseInt(element.getTextContent());
		Assert.assertEquals("value", expected, actual);
	}
	@Test public void testSetLong() throws Exception
	{
		long expected = random.nextLong();
		for (int i = 0; i < 10; ++i) // test multiplicity
			DomUtil.setLong(document, root, "test", expected);
		
		Assert.assertEquals("multiplicity", 1, root.getElementsByTagName("test").getLength());
		Element element = (Element)root.getElementsByTagName("test").item(0);
		long actual = Long.parseLong(element.getTextContent());
		Assert.assertEquals(expected, actual);
	}
	@Test public void testSetFloat() throws Exception
	{
		float expected = random.nextFloat();
		for (int i = 0; i < 10; ++i) // test multiplicity
			DomUtil.setFloat(document, root, "test", expected);
		
		Assert.assertEquals("multiplicity", 1, root.getElementsByTagName("test").getLength());
		Element element = (Element)root.getElementsByTagName("test").item(0);
		float actual = Float.parseFloat(element.getTextContent());
		Assert.assertEquals(expected, actual, 0);
	}
	@Test public void testSetBoolean() throws Exception
	{
		boolean expected = random.nextBoolean();
		for (int i = 0; i < 10; ++i) // test multiplicity
			DomUtil.setBoolean(document, root, "test", expected);
		
		Assert.assertEquals("multiplicity", 1, root.getElementsByTagName("test").getLength());
		Element element = (Element)root.getElementsByTagName("test").item(0);
		boolean actual = Boolean.parseBoolean(element.getTextContent());
		Assert.assertEquals(expected, actual);
	}
	/**
	 * In current impl, all/most of the get methods eventually delegate to a getString call.
	 * @throws Exception
	 */
	@Test public void testGetString() throws Exception
	{
		int strlen = random.nextInt(100);
		byte[] bytes = new byte[strlen];
		random.nextBytes(bytes);
		String expected = new String(bytes);
		
		Element element = document.createElement("test");
		element.setTextContent(expected);
		root.appendChild(element);

		String actual = DomUtil.getString(root, "test");
		Assert.assertEquals(expected, actual);
		
	}
	@Test public void testGetInt() throws Exception
	{
		int expected = random.nextInt();
		Element element = document.createElement("test");
		element.setTextContent(""+expected);
		root.appendChild(element);

		int actual = DomUtil.getInt(root, "test");
		Assert.assertEquals(expected, actual);
		
	}
	@Test public void testGetLong() throws Exception
	{
		long expected = random.nextLong();
		Element element = document.createElement("test");
		element.setTextContent(""+expected);
		root.appendChild(element);

		long actual = DomUtil.getLong(root, "test");
		Assert.assertEquals(expected, actual);
		
	}
	@Test public void testGetFloat() throws Exception
	{
		float expected = random.nextFloat();
		Element element = document.createElement("test");
		element.setTextContent(""+expected);
		root.appendChild(element);

		float actual = DomUtil.getFloat(root, "test");
		Assert.assertEquals(expected, actual, 0);
		
	}
	@Test public void testGetBoolean() throws Exception
	{
		boolean expected = random.nextBoolean();
		Element element = document.createElement("test");
		element.setTextContent(""+expected);
		root.appendChild(element);

		boolean actual = DomUtil.getBoolean(root, "test");
		Assert.assertEquals(expected, actual);
		
	}
}
