package test.bugs;

import java.io.File;
import java.util.Arrays;

import org.dsrg.soenea.uow.UoW;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.command.Helper;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.command.UpdateSettingsCommand;
import org.korsakow.domain.interf.ISettings;
import org.korsakow.domain.mapper.input.SettingsInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.Main;
import org.korsakow.ide.ui.controller.ProjectLoader;
import org.korsakow.ide.ui.controller.action.HelpExampleAction;
import org.korsakow.ide.util.ResourceManager;
import org.korsakow.ide.util.UIUtil;

import test.util.BaseTestCase;

/**
 * The problem was that DataRegistry used a SoenEAConnection which kept a reference to the DomSession. Even though
 * a new Conncetion was being created whenever a new DomSession was instantiated, this still caused inconsistencies,
 * somewhere along the line a stale reference was maintained, perhaps by SoenEA.
 * 
 * The solution was to have DomSessionConnection make static calles to DataRegistry.
 */
public class Test961 extends BaseTestCase {

	@Override
	@Before
	public void setUp() throws Exception
	{
		Application.initializeInstance();
	}
	
	@Override
	@After
	public void tearDown() throws Exception
	{
		Application.destroyInstance();
	}
	
	@Test public void testReproduceReported() throws Throwable
	{
		Main.setupLogging();
		DataRegistry.initialize(DataRegistry.createDefaultEmptyDocument(), File.createTempFile("mama", "dada"));
		UIUtil.runUITaskNowThrow(new UIUtil.RunnableThrow() {
			@Override
			public void run() throws Throwable {
				ProjectLoader.loadProject(ResourceManager.getResourceFile(HelpExampleAction.getExampleProject().getPath()));
			}
		});
		ISettings settings = SettingsInputMapper.find();
		long settingsId = settings.getId();
		Helper request = new Request();
		request.set("id", settingsId);
		request.set("property_ids", Arrays.asList("testName"));
		request.set("property_values", Arrays.asList("testValue"));
		Helper response = new Response();
		CommandExecutor.executeCommand(UpdateSettingsCommand.class, request, response);
		UoW.newCurrent();
		Assert.assertEquals("testValue", SettingsInputMapper.find().getString("testName"));
	}
}
