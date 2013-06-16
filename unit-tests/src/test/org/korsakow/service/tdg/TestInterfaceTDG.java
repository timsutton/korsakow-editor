package test.org.korsakow.service.tdg;

import java.awt.Color;
import java.io.File;
import java.util.Random;

import javax.xml.xpath.XPathExpressionException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.util.DomUtil;
import org.korsakow.services.tdg.InterfaceTDG;
import org.korsakow.services.util.ColorFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import test.util.DomainTestUtil;

public class TestInterfaceTDG {
	@Before
	public void setUp() throws Exception
	{
		File file = File.createTempFile("korsakow", "test");
		file.deleteOnExit();
		Document document = DataRegistry.createDefaultEmptyDocument();
		DataRegistry.initialize(document, file);
	}
	@Test public void testCreateTable() throws Exception
	{
		InterfaceTDG.createInterfaceTable();
		Element element = DomUtil.findElementByPath(DataRegistry.getDocument(), "/korsakow/interfaces");
		Assert.assertNotNull(element);
		Assert.assertEquals("interfaces", element.getTagName());
		Assert.assertEquals("korsakow", element.getParentNode().getNodeName());
	}
	@Test public void testInsert() throws Exception
	{
		Random random = new Random();
		final long id = random.nextLong();
		final long version = random.nextLong();
		final String name = DomainTestUtil.getRandomString();
		final int gridWidth = random.nextInt();
		final int gridHeight = random.nextInt();
		final int viewWidth = random.nextInt();
		final int viewHeight = random.nextInt();
		final long clickSoundId = random.nextInt();
		final float clickSoundVolume = random.nextFloat();
		final long backgroundImageId = random.nextLong();
		final String backgroundColor = ColorFactory.formatCSS(ColorFactory.createRGB(random.nextInt(0xFFFFFF)));
		int result = InterfaceTDG.insert(id, version, name, gridWidth, gridHeight, viewWidth, viewHeight, clickSoundId, clickSoundVolume, backgroundImageId, backgroundColor);
		Assert.assertEquals(1, result);
		Element listElement = DomUtil.findElementByPath(DataRegistry.getDocument(), "/korsakow/interfaces");
		NodeList children = DomUtil.getChildElements(listElement);
		Element element = null;
		int count = 0;
		for (int i = 0; i < children.getLength(); ++i) {
			Element child = (Element)children.item(i);
			if (DomUtil.getLong(child, "id") == id) {
				element = child;
				++count;
			}
		}
		Assert.assertEquals(1, count);
		Assert.assertNotNull(element);
		Assert.assertEquals((Long)id, DomUtil.getLong(element, "id"));
		Assert.assertEquals((Long)(version+1), DomUtil.getLong(element, "version"));
		Assert.assertEquals(name, DomUtil.getString(element, "name"));
		Assert.assertEquals((Integer)gridWidth, DomUtil.getInt(element, "gridWidth"));
		Assert.assertEquals((Integer)gridHeight, DomUtil.getInt(element, "gridHeight"));
		Assert.assertEquals((Integer)viewWidth, DomUtil.getInt(element, "viewWidth"));
		Assert.assertEquals((Integer)viewWidth, DomUtil.getInt(element, "viewWidth"));
		Assert.assertEquals((Long)clickSoundId, DomUtil.getLong(element, "clickSoundId"));
		Assert.assertEquals((Float)clickSoundVolume, DomUtil.getFloat(element, "clickSoundVolume"));
	}
	@Test public void testInsertDuplicateFailure() throws Exception
	{
		Random random = new Random();
		final long id = random.nextLong();
		final long version = random.nextLong();
		final String name = DomainTestUtil.getRandomString();
		final int gridWidth = random.nextInt();
		final int gridHeight = random.nextInt();
		final int viewWidth = random.nextInt();
		final int viewHeight = random.nextInt();
		final long clickSoundId = random.nextLong();
		final float clickSoundVolume = random.nextFloat();
		final long backgroundImageId = random.nextLong();
		final String backgroundColor = ColorFactory.formatCSS(ColorFactory.createRGB(random.nextInt(0xFFFFFF)));
		Element interfacesNode = InterfaceTDG.createInterfaceTable();
		Element interfaceNode = DataRegistry.getDocument().createElement("Interface");
		interfacesNode.appendChild(interfaceNode);
		DomUtil.appendNumberNode(DataRegistry.getDocument(), interfaceNode, "id", id);
		DomUtil.appendNumberNode(DataRegistry.getDocument(), interfaceNode, "version", version);
		int result = InterfaceTDG.update(id, version, name, gridWidth, gridHeight, viewWidth, viewHeight, clickSoundId, clickSoundVolume, backgroundImageId, backgroundColor);
		Assert.assertEquals(1, result);
		try {
			InterfaceTDG.insert(id, version, name, gridWidth, gridHeight, viewWidth, viewHeight, clickSoundId, clickSoundVolume, backgroundImageId, backgroundColor);
			throw new Error("was able to insert a duplicate");
		} catch (XPathExpressionException e) {
			// expected
		}
	}
}
