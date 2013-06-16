package test.org.korsakow.domain;

import java.io.File;

import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.util.DomUtil;

import test.util.BaseTestCase;
import test.util.DomainTestUtil;

public abstract class AbstractDomainObjectTestCase extends BaseTestCase
{
	protected File dataFile;
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
	    dataFile = File.createTempFile("DataRegistry", ".xml", parentDir);
	    System.out.println(String.format("DataRegistry: %s", dataFile.getPath()));
	    DomainTestUtil.setupDataRegistry(dataFile);
	}
	@Override
	public void tearDown() throws Exception
	{
		super.tearDown();
		
		System.out.println(DomUtil.toXMLString(DataRegistry.getDocument()));
		DataRegistry.getConnection().close();
		//DbRegistry.closeDbConnection();
		dataFile = null;
	}
}
