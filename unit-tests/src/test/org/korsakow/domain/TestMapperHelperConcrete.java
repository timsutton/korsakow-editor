package test.org.korsakow.domain;

import java.io.File;
import java.util.Random;

import org.dsrg.soenea.uow.UoW;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.korsakow.domain.Image;
import org.korsakow.domain.ImageFactory;
import org.korsakow.domain.Interface;
import org.korsakow.domain.InterfaceFactory;
import org.korsakow.domain.KDomainObject;
import org.korsakow.domain.Project;
import org.korsakow.domain.ProjectFactory;
import org.korsakow.domain.Rule;
import org.korsakow.domain.RuleFactory;
import org.korsakow.domain.Settings;
import org.korsakow.domain.SettingsFactory;
import org.korsakow.domain.Snu;
import org.korsakow.domain.SnuFactory;
import org.korsakow.domain.Sound;
import org.korsakow.domain.SoundFactory;
import org.korsakow.domain.Text;
import org.korsakow.domain.TextFactory;
import org.korsakow.domain.Video;
import org.korsakow.domain.VideoFactory;
import org.korsakow.domain.Widget;
import org.korsakow.domain.WidgetFactory;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.domain.mapper.input.MapperHelper;
import org.korsakow.ide.util.Util;
import org.korsakow.services.finder.ResourceFinder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import test.util.BaseTestCase;
import test.util.DomainTestUtil;
import test.util.MoreAsserts;

public class TestMapperHelperConcrete extends BaseTestCase
{
	private final static String ID_NAME = "id";
	private File domFile;
	private Document doc;
	Random random;
	
	
	/**
	 * I usually like to have some noise, or false positives when testing functionality that searches for something specific
	 */
	private void setUpFalsePositives()
	{
		VideoFactory.createNew();
		SoundFactory.createNew();
		ImageFactory.createNew();
		TextFactory.createNew();
		Rule rule = RuleFactory.createNew();
		Snu snu  = SnuFactory.createNew();
		snu.setRules(Util.list(IRule.class, rule));
		Widget widget = WidgetFactory.createNew();
		Interface interf = InterfaceFactory.createNew();
		interf.setWidgets(Util.list(IWidget.class, widget));
		ProjectFactory.createNew();
		SettingsFactory.createNew();
	}
	@Override
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
		domFile = File.createTempFile("TestMapperHelperConcrete", "dom");
		DomainTestUtil.setupDataRegistry(domFile);
		random = new Random();
		
		UoW.newCurrent();
		setUpFalsePositives();
		UoW.getCurrent().commit();
		
		UoW.newCurrent();
	}
	@Override
	@After
	public void tearDown() throws Exception
	{
		super.tearDown();
		doc = null;
		random = null;
		domFile = null;
	}
	@Test public void testMapUnknownVideo() throws Exception
	{
		KDomainObject object = VideoFactory.createNew();
		UoW.getCurrent().registerNew(object);
		UoW.getCurrent().commit();
		UoW.newCurrent(); // otherwise the test is meaningless
		
		IResource concrete = MapperHelper.mapUnknown((Element)ResourceFinder.find(object.getId()).item(0)); // this is half the text
		
		MoreAsserts.assertInstanceOf(Video.class, concrete); // this is the other half
		Assert.assertEquals(object.getId(), concrete.getId());
	}
	@Test public void testMapUnknownSound() throws Exception
	{
		KDomainObject object = SoundFactory.createNew();
		UoW.getCurrent().registerNew(object);
		UoW.getCurrent().commit();
		UoW.newCurrent(); // otherwise the test is meaningless
		
		IResource concrete = MapperHelper.mapUnknown((Element)ResourceFinder.find(object.getId()).item(0)); // this is half the text
		
		MoreAsserts.assertInstanceOf(Sound.class, concrete); // this is the other half
		Assert.assertEquals(object.getId(), concrete.getId());
	}
	@Test public void testMapUnknownImage() throws Exception
	{
		KDomainObject object = ImageFactory.createNew();
		UoW.getCurrent().registerNew(object);
		UoW.getCurrent().commit();
		UoW.newCurrent(); // otherwise the test is meaningless
		
		IResource concrete = MapperHelper.mapUnknown((Element)ResourceFinder.find(object.getId()).item(0)); // this is half the text
		
		MoreAsserts.assertInstanceOf(Image.class, concrete); // this is the other half
		Assert.assertEquals(object.getId(), concrete.getId());
	}
	@Test public void testMapUnknownText() throws Exception
	{
		KDomainObject object = TextFactory.createNew();
		UoW.getCurrent().registerNew(object);
		UoW.getCurrent().commit();
		UoW.newCurrent(); // otherwise the test is meaningless
		
		IResource concrete = MapperHelper.mapUnknown((Element)ResourceFinder.find(object.getId()).item(0)); // this is half the text
		
		MoreAsserts.assertInstanceOf(Text.class, concrete); // this is the other half
		Assert.assertEquals(object.getId(), concrete.getId());
	}
	@Test public void testMapUnknownProject() throws Exception
	{
		KDomainObject object = ProjectFactory.createNew();
		UoW.getCurrent().registerNew(object);
		UoW.getCurrent().commit();
		UoW.newCurrent(); // otherwise the test is meaningless
		
		IResource concrete = MapperHelper.mapUnknown((Element)ResourceFinder.find(object.getId()).item(0)); // this is half the text
		
		MoreAsserts.assertInstanceOf(Project.class, concrete); // this is the other half
		Assert.assertEquals(object.getId(), concrete.getId());
	}
	@Test public void testMapUnknownSnu() throws Exception
	{
		KDomainObject object = SnuFactory.createNew();
		UoW.getCurrent().registerNew(object);
		UoW.getCurrent().commit();
		UoW.newCurrent(); // otherwise the test is meaningless
		
		IResource concrete = MapperHelper.mapUnknown((Element)ResourceFinder.find(object.getId()).item(0)); // this is half the text
		
		MoreAsserts.assertInstanceOf(Snu.class, concrete); // this is the other half
		Assert.assertEquals(object.getId(), concrete.getId());
	}
	@Test public void testMapUnknownRule() throws Exception
	{
		Rule object = RuleFactory.createNew();
		Snu host = SnuFactory.createNew();
		host.setRules(Util.list(IRule.class, object));
		UoW.getCurrent().registerNew(object);
		UoW.getCurrent().commit();
		UoW.newCurrent(); // otherwise the test is meaningless
		
		IResource concrete = MapperHelper.mapUnknown((Element)ResourceFinder.find(object.getId()).item(0)); // this is half the text
		
		MoreAsserts.assertInstanceOf(Rule.class, concrete); // this is the other half
		Assert.assertEquals(object.getId(), concrete.getId());
	}
	@Test public void testMapUnknownWidget() throws Exception
	{
		Widget object = WidgetFactory.createNew();
		Interface host = InterfaceFactory.createNew();
		host.setWidgets(Util.list(IWidget.class, object));
		UoW.getCurrent().registerNew(object);
		UoW.getCurrent().commit();
		UoW.newCurrent(); // otherwise the test is meaningless
		
		IResource concrete = MapperHelper.mapUnknown((Element)ResourceFinder.find(object.getId()).item(0)); // this is half the text
		
		MoreAsserts.assertInstanceOf(Widget.class, concrete); // this is the other half
		Assert.assertEquals(object.getId(), concrete.getId());
	}
	@Test public void testMapUnknownInterface() throws Exception
	{
		KDomainObject object = InterfaceFactory.createNew();
		UoW.getCurrent().registerNew(object);
		UoW.getCurrent().commit();
		UoW.newCurrent(); // otherwise the test is meaningless
		
		IResource concrete = MapperHelper.mapUnknown((Element)ResourceFinder.find(object.getId()).item(0)); // this is half the text
		
		MoreAsserts.assertInstanceOf(Interface.class, concrete); // this is the other half
		Assert.assertEquals(object.getId(), concrete.getId());
	}
	@Ignore
	@Test public void testMapUnknownSettings() throws Exception
	{
		KDomainObject object = SettingsFactory.createNew();
		UoW.getCurrent().registerNew(object);
		UoW.getCurrent().commit();
		UoW.newCurrent(); // otherwise the test is meaningless
		
		IResource concrete = MapperHelper.mapUnknown((Element)ResourceFinder.find(object.getId()).item(0)); // this is half the text
		
		MoreAsserts.assertInstanceOf(Settings.class, concrete); // this is the other half
		Assert.assertEquals(object.getId(), concrete.getId());
	}
}
