package test.bugs;

import java.io.File;
import java.util.Arrays;

import org.junit.Assert;

import org.dsrg.soenea.uow.UoW;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.KeywordFactory;
import org.korsakow.domain.command.RenameResourceCommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.Main;

import test.util.BaseTestCase;
import test.util.DOFactory;

/**
 */
public class Test1263 extends BaseTestCase {

	@Override
	@Before
	public void setUp() throws Exception
	{
	}
	
	@Override
	@After
	public void tearDown() throws Exception
	{
	}
	
	@Test public void testReproduceReported() throws Throwable
	{
		Main.setupLogging();
		DataRegistry.initialize(DataRegistry.createDefaultEmptyDocument(), File.createTempFile(getClass().getName(), "test"));
		
		ISnu snu = DOFactory.createSnuWithDummyMedia(parentDir);
		IRule subRule = DOFactory.createKeywordLookupRule("myKeyword");
		IRule searchRule = DOFactory.createSearchRule(subRule);
		snu.setRules(Arrays.asList(searchRule));
		
		UoW.getCurrent().commit();
		
		Request request = new Request();
		request.set(RenameResourceCommand.ID, snu.getId());
		request.set(RenameResourceCommand.NAME, "myName");
		Response response = CommandExecutor.executeCommand(RenameResourceCommand.class, request);
		snu = (ISnu)response.get(RenameResourceCommand.RESOURCE);
		
		Assert.assertFalse(snu.getRules().isEmpty());
		Assert.assertFalse(snu.getRules().get(0).getRules().isEmpty());
		Assert.assertTrue(snu.getRules().get(0).getRules().get(0).getKeywords().contains(KeywordFactory.createClean("myKeyword")));
	}
}
