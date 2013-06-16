package test.org.korsakow.service.plugin;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.korsakow.ide.Application;
import org.korsakow.ide.DataRegistry;
import org.w3c.dom.Document;

import test.util.BaseTestCase;

public abstract class AbstractPluginTest extends BaseTestCase {
	@Override
	@Before
	public void setUp() throws Exception
	{
		Application.initializeInstance(new MockApplication());
		File file = File.createTempFile("korsakow", "test");
		file.deleteOnExit();
		Document document = DataRegistry.createDefaultEmptyDocument();
		DataRegistry.initialize(document, file);
	}
	@Override
	@After
	public void tearDown() throws Exception
	{
		Application.destroyInstance();
	}
	protected static class MockApplication extends Application
	{
		protected MockApplication() throws Exception {
			super();
		}
	}
}
