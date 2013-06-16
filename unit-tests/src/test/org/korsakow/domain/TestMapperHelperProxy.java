package test.org.korsakow.domain;

import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ISettings;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.interf.IText;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.domain.mapper.input.MapperHelper;
import org.korsakow.ide.util.DomUtil;
import org.korsakow.services.tdg.ImageTDG;
import org.korsakow.services.tdg.InterfaceTDG;
import org.korsakow.services.tdg.ProjectTDG;
import org.korsakow.services.tdg.RuleTDG;
import org.korsakow.services.tdg.SettingsTDG;
import org.korsakow.services.tdg.SnuTDG;
import org.korsakow.services.tdg.SoundTDG;
import org.korsakow.services.tdg.TextTDG;
import org.korsakow.services.tdg.VideoTDG;
import org.korsakow.services.tdg.WidgetTDG;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import test.util.BaseTestCase;
import test.util.MoreAsserts;

public class TestMapperHelperProxy extends BaseTestCase
{
	private final static String ID_NAME = "id";
	private Document doc;
	Random random;
	
	@Override
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
		doc = DomUtil.createDocument();
		random = new Random();
	}
	@Override
	@After
	public void tearDown() throws Exception
	{
		super.tearDown();
		doc = null;
		random = null;
	}

	private Element createElement(String tagName, long id)
	{
		id = random.nextLong();
		Element elm = doc.createElement(tagName);
		DomUtil.setLong(doc, elm, ID_NAME, id);
		return elm;
	}
	@Test public void testProxyUnknownVideo() throws Exception
	{
		long id = random.nextLong();
		Element elm = createElement(VideoTDG.NODE_NAME, id);
		
		IResource proxy = MapperHelper.proxyUnknown(elm); // this is half the text
		
		MoreAsserts.assertInstanceOf(IVideo.class, proxy); // this is the other half
	}
	@Test public void testProxyUnknownSound() throws Exception
	{
		long id = random.nextLong();
		Element elm = createElement(SoundTDG.NODE_NAME, id);
		
		IResource proxy = MapperHelper.proxyUnknown(elm); // this is half the text
		
		MoreAsserts.assertInstanceOf(ISound.class, proxy); // this is the other half
	}
	@Test public void testProxyUnknownImage() throws Exception
	{
		long id = random.nextLong();
		Element elm = createElement(ImageTDG.NODE_NAME, id);
		
		IResource proxy = MapperHelper.proxyUnknown(elm); // this is half the text
		
		MoreAsserts.assertInstanceOf(IImage.class, proxy); // this is the other half
	}
	@Test public void testProxyUnknownText() throws Exception
	{
		long id = random.nextLong();
		Element elm = createElement(TextTDG.NODE_NAME, id);
		
		IResource proxy = MapperHelper.proxyUnknown(elm); // this is half the text
		
		MoreAsserts.assertInstanceOf(IText.class, proxy); // this is the other half
	}
	@Test public void testProxyUnknownProject() throws Exception
	{
		long id = random.nextLong();
		Element elm = createElement(ProjectTDG.NODE_NAME, id);
		
		IResource proxy = MapperHelper.proxyUnknown(elm); // this is half the text
		
		MoreAsserts.assertInstanceOf(IProject.class, proxy); // this is the other half
	}
	@Test public void testProxyUnknownSnu() throws Exception
	{
		long id = random.nextLong();
		Element elm = createElement(SnuTDG.NODE_NAME, id);
		
		IResource proxy = MapperHelper.proxyUnknown(elm); // this is half the text
		
		MoreAsserts.assertInstanceOf(ISnu.class, proxy); // this is the other half
	}
	@Test public void testProxyUnknownWidget() throws Exception
	{
		long id = random.nextLong();
		Element elm = createElement(WidgetTDG.NODE_NAME, id);
		
		IResource proxy = MapperHelper.proxyUnknown(elm); // this is half the text
		
		MoreAsserts.assertInstanceOf(IWidget.class, proxy); // this is the other half
	}
	@Test public void testProxyUnknownRule() throws Exception
	{
		long id = random.nextLong();
		Element elm = createElement(RuleTDG.NODE_NAME, id);
		
		IResource proxy = MapperHelper.proxyUnknown(elm); // this is half the text
		
		MoreAsserts.assertInstanceOf(IRule.class, proxy); // this is the other half
	}
	@Test public void testProxyUnknownInterface() throws Exception
	{
		long id = random.nextLong();
		Element elm = createElement(InterfaceTDG.NODE_NAME, id);
		
		IResource proxy = MapperHelper.proxyUnknown(elm); // this is half the text
		
		MoreAsserts.assertInstanceOf(IInterface.class, proxy); // this is the other half
	}
	@Ignore
	@Test public void testProxyUnknownSettings() throws Exception
	{
		long id = random.nextLong();
		Element elm = createElement(SettingsTDG.NODE_NAME, id);
		
		IResource proxy = MapperHelper.proxyUnknown(elm); // this is half the text
		
		MoreAsserts.assertInstanceOf(ISettings.class, proxy); // this is the other half
	}
}
