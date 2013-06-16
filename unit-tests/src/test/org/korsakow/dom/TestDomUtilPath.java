package test.org.korsakow.dom;

import org.junit.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.korsakow.ide.XPathHelper;
import org.korsakow.ide.util.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import test.util.BaseTestCase;

/**
 * Tests XPath vs Straight Dom.
 * Ensuring that our homebrew methods provide results consistent with xpath.
 * 
 * @author d
 *
 */
public class TestDomUtilPath extends BaseTestCase
{
	private static final int TREE_WIDTH = 3;
	private static final int TREE_DEPTH = 10;
	private static Document baseDocument;
	private Document document;
	private long before;
	private long after;
	
	static
	{
		try {
		System.out.println("Begin create tree");
			baseDocument = DomUtil.createDocument();
		Element root = baseDocument.createElement("root");
		baseDocument.appendChild(root);
		createTree(baseDocument, root, 0, TREE_DEPTH);
		System.out.println("End create tree");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static String createElementName(int depth, int count, int maxcount)
	{
		return "E" + depth + "_" + (count%(maxcount/1));
	}
	private static Element createTree(Document doc, Element parent, int depth, int maxdepth)
	{
		for (int i = 0; i < TREE_WIDTH; ++i)
		{
			String name = createElementName(depth, i, TREE_WIDTH);
			Element child = doc.createElement(name);
			parent.appendChild(child);
			if (depth < maxdepth)
				createTree(doc, child, depth+1, maxdepth);
		}
		return parent;
	}
	@Override
	@Before
	public void setUp() throws Exception
	{
		document = (Document)baseDocument.cloneNode(true);
		before = System.currentTimeMillis();
	}
	@Override
	@After
	public void tearDown()
	{
		after = System.currentTimeMillis();
		document = null;
		
		System.out.println("Time=" + (after-before)/1000.0f);
	}
	@Test public void testXPath() throws Exception
	{
		String xpath = "/root/E0_0/E1_0/E2_0/E3_0/E4_0/E5_0";///E6_0/E7_0/E8_0/E9_0";
		XPathHelper helper = new XPathHelper(document);
		Element element = helper.xpathAsElement(xpath);
		Assert.assertEquals("E5_0", element.getTagName());
	}
	@Test public void testPureDom() throws Exception
	{
		String xpath = "/root/E0_0/E1_0/E2_0/E3_0/E4_0/E5_0";///E6_0/E7_0/E8_0/E9_0";
		Element element = DomUtil.findElementByPath(document, xpath);
		Assert.assertEquals("E5_0", element.getTagName());
	}
}
