package test.org.korsakow.domain;

import java.util.Collection;

import org.dsrg.soenea.uow.UoW;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.korsakow.domain.ImageFactory;
import org.korsakow.domain.InterfaceFactory;
import org.korsakow.domain.ProjectFactory;
import org.korsakow.domain.RuleFactory;
import org.korsakow.domain.SnuFactory;
import org.korsakow.domain.SoundFactory;
import org.korsakow.domain.TextFactory;
import org.korsakow.domain.VideoFactory;
import org.korsakow.domain.WidgetFactory;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.domain.mapper.input.MapperHelper;
import org.korsakow.ide.util.Util;

public class TestMapperHelperFindResourcesReferencing extends AbstractDomainObjectTestCase
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
	
	@Test public void testSnuBackgroundSound() throws Exception
	{
		ISnu referer = SnuFactory.createNew();
		Collection<IResource> references;
		ISound reference;
		
		referer.setBackgroundSound(reference = SoundFactory.createNew());
		
		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		references = MapperHelper.findResourcesReferencing(reference.getId());
		Assert.assertEquals(1, references.size());
		Assert.assertTrue(references.contains(referer));
	}
	@Test public void testSnuInterface() throws Exception
	{
		ISnu referer = SnuFactory.createNew();
		Collection<IResource> references;
		IInterface reference;
		
		referer.setInterface(reference = InterfaceFactory.createNew());

		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		references = MapperHelper.findResourcesReferencing(reference.getId());
		Assert.assertEquals(1, references.size());
		Assert.assertTrue(references.contains(referer));
	}
	@Test public void testSnuMainMedia() throws Exception
	{
		ISnu referer = SnuFactory.createNew();
		Collection<IResource> references;
		IMedia reference;
		
		referer.setMainMedia(reference = VideoFactory.createNew());

		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		references = MapperHelper.findResourcesReferencing(reference.getId());
		Assert.assertEquals(1, references.size());
		Assert.assertTrue(references.contains(referer));
	}
	@Test public void testSnuPreviewMedia() throws Exception
	{
		ISnu referer = SnuFactory.createNew();
		Collection<IResource> references;
		IMedia reference;
		
		referer.setPreviewMedia(reference = ImageFactory.createNew());

		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		references = MapperHelper.findResourcesReferencing(reference.getId());
		Assert.assertEquals(1, references.size());
		Assert.assertTrue(references.contains(referer));
	}
	@Ignore
	@Test public void testSnuRule() throws Exception
	{
		ISnu referer = SnuFactory.createNew();
		Collection<IResource> references;
		IRule reference;
		
		referer.setRules(Util.list(IRule.class, reference = RuleFactory.createNew()));

		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		references = MapperHelper.findResourcesReferencing(reference.getId());
		Assert.assertEquals(1, references.size());
		Assert.assertTrue(references.contains(reference));
	}
	@Test public void testProjectBackgroundSound() throws Exception
	{
		IProject referer = ProjectFactory.createNew();
		Collection<IResource> references;
		ISound reference;
		
		referer.setBackgroundSound(reference = SoundFactory.createNew());

		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		references = MapperHelper.findResourcesReferencing(reference.getId());
		Assert.assertEquals(1, references.size());
		Assert.assertTrue(references.contains(referer));
	}
	@Test public void testProjectBackgroundImage() throws Exception
	{
		IProject referer = ProjectFactory.createNew();
		Collection<IResource> references;
		IImage reference;
		
		referer.setBackgroundImage(reference = ImageFactory.createNew());

		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		references = MapperHelper.findResourcesReferencing(reference.getId());
		System.out.println(references);
		Assert.assertEquals(1, references.size());
		Assert.assertTrue(references.contains(referer));
	}
	@Test public void testProjectClickSound() throws Exception
	{
		IProject referer = ProjectFactory.createNew();
		Collection<IResource> references;
		ISound reference;
		
		referer.setClickSound(reference = SoundFactory.createNew());

		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		references = MapperHelper.findResourcesReferencing(reference.getId());
		Assert.assertEquals(1, references.size());
		Assert.assertTrue(references.contains(referer));
	}
	@Test public void testProjectSplashScreenMedia() throws Exception
	{
		IProject referer = ProjectFactory.createNew();
		Collection<IResource> references;
		IMedia reference;
		
		referer.setSplashScreenMedia(reference = TextFactory.createNew());

		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		references = MapperHelper.findResourcesReferencing(reference.getId());
		Assert.assertEquals(1, references.size());
		Assert.assertTrue(references.contains(referer));
	}
	@Test public void testInterfaceClickSound() throws Exception
	{
		IInterface referer = InterfaceFactory.createNew();
		Collection<IResource> references;
		ISound reference;
		
		referer.setClickSound(reference = SoundFactory.createNew());

		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		references = MapperHelper.findResourcesReferencing(reference.getId());
		Assert.assertEquals(1, references.size());
		Assert.assertTrue(references.contains(referer));
	}
	@Ignore
	@Test public void testInterfaceWidget() throws Exception
	{
		IInterface referer = InterfaceFactory.createNew();
		Collection<IResource> references;
		IWidget reference;
		
		referer.setWidgets(Util.list(IWidget.class, reference = WidgetFactory.createNew()));

		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		references = MapperHelper.findResourcesReferencing(reference.getId());
		Assert.assertEquals(1, references.size());
		Assert.assertTrue(references.contains(reference));
	}
	@Test public void testWidgetSnu() throws Exception
	{
		IWidget referer = WidgetFactory.createNew();
		IInterface interf = InterfaceFactory.createNew();
		interf.setWidgets(Util.list(IWidget.class, referer));
		Collection<IResource> references;
		ISnu reference;
		
		referer.setDynamicProperty("snuId", (reference = SnuFactory.createNew()).getId());

		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		references = MapperHelper.findResourcesReferencing(reference.getId());
		Assert.assertEquals(1, references.size());
		Assert.assertTrue(references.contains(referer));
	}
	@Test public void testWidgetMedia() throws Exception
	{
		IWidget referer = WidgetFactory.createNew();
		IInterface interf = InterfaceFactory.createNew();
		interf.setWidgets(Util.list(IWidget.class, referer));
		Collection<IResource> references;
		IMedia reference;
		
		referer.setDynamicProperty("mediaId", (reference = SoundFactory.createNew()).getId());

		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		references = MapperHelper.findResourcesReferencing(reference.getId());
		Assert.assertEquals(1, references.size());
		Assert.assertTrue(references.contains(referer));
	}
	@Test public void testWidgetSound() throws Exception
	{
		IWidget referer = WidgetFactory.createNew();
		IInterface interf = InterfaceFactory.createNew();
		interf.setWidgets(Util.list(IWidget.class, referer));
		Collection<IResource> references;
		IMedia reference;
		
		referer.setDynamicProperty("soundId", (reference = SoundFactory.createNew()).getId());

		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		references = MapperHelper.findResourcesReferencing(reference.getId());
		Assert.assertEquals(1, references.size());
		Assert.assertTrue(references.contains(referer));
	}
	@Test public void testWidgetImage() throws Exception
	{
		IWidget referer = WidgetFactory.createNew();
		IInterface interf = InterfaceFactory.createNew();
		interf.setWidgets(Util.list(IWidget.class, referer));
		Collection<IResource> references;
		IMedia reference;
		
		referer.setDynamicProperty("imageId", (reference = ImageFactory.createNew()).getId());

		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		references = MapperHelper.findResourcesReferencing(reference.getId());
		Assert.assertEquals(1, references.size());
		Assert.assertTrue(references.contains(referer));
	}
	@Test public void testWidgetVideo() throws Exception
	{
		IWidget referer = WidgetFactory.createNew();
		IInterface interf = InterfaceFactory.createNew();
		interf.setWidgets(Util.list(IWidget.class, referer));
		Collection<IResource> references;
		IMedia reference;
		
		referer.setDynamicProperty("videoId", (reference = VideoFactory.createNew()).getId());

		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		references = MapperHelper.findResourcesReferencing(reference.getId());
		Assert.assertEquals(1, references.size());
		Assert.assertTrue(references.contains(referer));
	}
	@Test public void testWidgetText() throws Exception
	{
		IWidget referer = WidgetFactory.createNew();
		IInterface interf = InterfaceFactory.createNew();
		interf.setWidgets(Util.list(IWidget.class, referer));
		Collection<IResource> references;
		IMedia reference;
		
		referer.setDynamicProperty("textId", (reference = TextFactory.createNew()).getId());

		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		references = MapperHelper.findResourcesReferencing(reference.getId());
		Assert.assertEquals(1, references.size());
		Assert.assertTrue(references.contains(referer));
	}
}
