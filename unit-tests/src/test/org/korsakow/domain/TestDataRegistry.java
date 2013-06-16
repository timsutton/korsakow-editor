package test.org.korsakow.domain;

import java.io.File;

import org.junit.Assert;

import org.dsrg.soenea.uow.UoW;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.korsakow.ide.Application;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.DomHelper;
import org.korsakow.ide.Main;
import org.korsakow.ide.util.ResourceManager;
import org.korsakow.ide.util.UIUtil;
import org.korsakow.services.tdg.PropertyTDG;
import org.w3c.dom.Element;

import test.bugs.Test961;
import test.util.TestResourceSource;

public class TestDataRegistry
{
	@BeforeClass
	public static void setUpStatic() {
		ResourceManager.setResourceSource(new TestResourceSource());
	}
	public TestDataRegistry()
	{
		Main.setupLogging();
	}
	@Before
	public void setUp() throws Exception
	{
		Application.initializeInstance();
	}
	@After
	public void tearDown()
	{
		Application.destroyInstance();
	}
	/**
	 * @see Test961
	 */
	@Test public void testInitializeAgainInOtherThreadDoesntCauseCommitToFail() throws Throwable
	{
		DataRegistry.initialize(DataRegistry.createDefaultDocument(), File.createTempFile("prefix", "suffix"));
		UIUtil.runUITaskNowThrow(new UIUtil.RunnableThrow() {
			public void run() throws Throwable {
				DataRegistry.initialize(DataRegistry.createDefaultDocument(), File.createTempFile("prefix", "suffix"));
			}
		});

		Element element = new DomHelper(DataRegistry.getDocument()).appendElement("/korsakow", "testName", "testValue");
		element.setAttribute(PropertyTDG.DYNAMIC_ATTRIBUTE_NAME, PropertyTDG.DYNAMIC_ATTRIBUTE_VALUE);
		UoW.getCurrent().commit();
		DataRegistry.rollback();

		Assert.assertEquals("testValue", new DomHelper(DataRegistry.getDocument()).xpathAsString("/korsakow/testName"));
	}}
