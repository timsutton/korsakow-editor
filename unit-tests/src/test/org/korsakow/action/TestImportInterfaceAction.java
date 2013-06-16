package test.org.korsakow.action;

import java.awt.Component;
import java.io.File;
import java.util.Random;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.mapper.input.ProjectInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.ui.controller.action.interf.ImportInterfaceAction;
import org.korsakow.ide.util.UIUtil;

import test.org.korsakow.domain.AbstractDomainObjectTestCase;

@Ignore
public class TestImportInterfaceAction extends AbstractDomainObjectTestCase
{
	private Random random;
	@Override
	@Before
	public void setUp() throws Exception
	{
		final long SEED = System.nanoTime();
		System.out.println(String.format("Random Seed: %d", SEED)); // printed in case you want/need to repro a particular run
		random = new Random(SEED);
		
		UIUtil.runUITaskNowThrow(new UIUtil.RunnableThrow() {
			public void run() throws Throwable {
				TestImportInterfaceAction.super.setUp();
				
				Application.initializeInstance(new MockApplication());
			}
		});
	}
	@Override
	@After
	public void tearDown() throws Exception
	{
		UIUtil.runUITaskNowThrow(new UIUtil.RunnableThrow() {
			public void run() throws Throwable {
				Application.destroyInstance();

				TestImportInterfaceAction.super.tearDown();
			}
		});
	}
	@Test public void testImport() throws Throwable
	{
		((MockApplication)Application.getInstance()).setShowFileOpenDialogFile(new File("resources/interfaces/TestInterface.kif"));
		ImportInterfaceAction action = new ImportInterfaceAction();
		action.performAction();
		IProject project = ProjectInputMapper.find();
		Assert.assertEquals(1, project.getInterfaces().size());
		IInterface interf = project.getInterfaces().iterator().next();
		Assert.assertEquals("Test Interface", interf.getName());
	}
	private static class MockApplication extends Application
	{
		private File file;
		protected MockApplication() throws Exception {
			super();
		}

		public void setShowFileOpenDialogFile(File file)
		{
			this.file = file;
		}
		@Override
		public File showFileOpenDialog(Component parent, File defaultFile)
		{
			return file;
		}
	}
}
