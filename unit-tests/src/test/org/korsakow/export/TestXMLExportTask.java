package test.org.korsakow.export;

import org.junit.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.korsakow.services.export.task.XMLExportTask;

import test.util.BaseTestCase;

public class TestXMLExportTask extends BaseTestCase
{
	@Override
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
	}
	@Override
	@After
	public void tearDown() throws Exception
	{
		super.tearDown();
	}
	/**
	 * #941
	 * @throws Throwable
	 */
	@Test public void testFormatExportUrl_SpacesEncodedAsPercent() throws Throwable
	{
		String hasSpaces = "this uri has spaces.png";
		String expected = hasSpaces.replace(" ", "%20");
		String encoded = XMLExportTask.formatExportUrl(hasSpaces);
		Assert.assertEquals(expected, encoded);
	}
	/**
	 * #1180
	 * @throws Throwable
	 */
	@Test public void testFormatExportUrl_BackslashesConvertedToForwardslashes() throws Throwable
	{
		String source = "file:\\c:\\Document and Settings\\My User\\Desktop\\My Film\\Media\\myMovie.mov";
		String actual = XMLExportTask.formatExportUrl(source);
		Assert.assertFalse(actual.contains("\\"));
		Assert.assertFalse(actual.contains("%5c"));
		Assert.assertFalse(actual.contains("%5C"));
	}
}
