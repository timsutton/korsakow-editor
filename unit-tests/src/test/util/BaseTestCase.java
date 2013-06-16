package test.util;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.korsakow.ide.Application;
import org.korsakow.ide.util.FileUtil;
import org.korsakow.ide.util.ResourceManager;

public class BaseTestCase
{
	static
	{
		System.setProperty("org.korsakow.log.filename", Application.getLogfilename());
		ResourceManager.setResourceSource(new TestResourceSource());
	}
	
	protected final String name;
	protected File parentDir;
	public BaseTestCase()
	{
		name = getClass().getName();
	}
	
	protected String getName() {
		return name;
	}
	
	@Before
	public void setUp() throws Exception
	{
		parentDir = FileUtil.createTempDirectory("test", getName());
		parentDir.deleteOnExit();
	}
	
	@After
	public void tearDown() throws Exception
	{
		parentDir.delete();
		parentDir = null;
	}
}
