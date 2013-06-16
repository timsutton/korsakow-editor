package test.org.korsakow.service.updater;

import java.awt.Component;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.korsakow.ide.Application;
import org.korsakow.ide.Build;
import org.korsakow.services.updater.Updater;
import org.xml.sax.SAXException;

import test.mock.MockBuild;

public class TestUpdater {
	protected static class MockApplication extends Application
	{
		public boolean haveMessage = false;
		public Throwable exception;
		protected MockApplication() throws Exception {
			super();
		}
		@Override
		public boolean showOKCancelDialog(Component parent, String title, Object message)
		{
			haveMessage = true;
			System.out.println(title + ":" + message);
			return false;
		}
		@Override
		public void showUnhandledErrorDialog(String title, String message, String details, Throwable cause)
		{
			exception = cause;
		}
	}
	private MockApplication mockApp;
	@Before
	public void setUp() throws Exception
	{
		Application.initializeInstance(mockApp = new MockApplication());
	}
	@After
	public void tearDown() throws Exception
	{
		Application.destroyInstance();
	}
	
	@Test public void testUpdateFindsNewerVersionAndAlerts() throws XPathExpressionException, IOException, SAXException, ParserConfigurationException
	{
		Build.setInstance(new MockBuild("0", 0.0F, "0.0.0"));
		Updater updater = new Updater();
		updater.synchronousCheck();
		Assert.assertTrue(mockApp.haveMessage);
		Assert.assertNull(mockApp.exception);
	}
	@Test public void testUpdateFindsOlderVersionAndDoesNothing() throws XPathExpressionException, IOException, SAXException, ParserConfigurationException
	{
		Build.setInstance(new MockBuild("0", Float.MAX_VALUE, "0.0.0"));
		Updater updater = new Updater();
		updater.synchronousCheck();
		Assert.assertFalse(mockApp.haveMessage);
		Assert.assertNull(mockApp.exception);
	}
}
