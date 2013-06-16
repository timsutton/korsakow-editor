package test.org.korsakow.command;

import java.io.File;

import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.uow.UoW;
import org.junit.Assert;
import org.junit.Test;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.command.AbstractCommand;
import org.korsakow.domain.command.Helper;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.util.DomUtil;

import test.util.DomainTestUtil;

public class TestCommandExecutor {
	@Test public void testRollbackOnException() throws Exception
	{
		File dataFile = File.createTempFile("TestCommandExecutor", "testRollbackOnException");
		dataFile.deleteOnExit();
		DomainTestUtil.setupDataRegistry(dataFile);
		
		DomUtil.appendTextNode(DataRegistry.getDocument(), DataRegistry.getDocument().getDocumentElement(), "version0Node", "version0Value");
		DataRegistry.commit();
		DataRegistry.flush();
		
		try {
			CommandExecutor.executeCommand(testRollbackOnExceptionCommand.class);
			
			throw new Exception("expected an exception");
		} catch (CommandException e) {
			// expected
		}

		Assert.assertEquals("version0Value", DomUtil.getString(DataRegistry.getDocument().getDocumentElement(), "version0Node"));
		Assert.assertNull("commandValue", DomUtil.getString(DataRegistry.getDocument().getDocumentElement(), "commandNode"));
	}
	public static class testRollbackOnExceptionCommand extends AbstractCommand
	{
		public testRollbackOnExceptionCommand(Helper request, Helper response) {
			super(request, response);
		}

		public void execute() throws CommandException {
			DomUtil.appendTextNode(DataRegistry.getDocument(), DataRegistry.getDocument().getDocumentElement(), "commandNode", "commandValue");
			
			throw new CommandException();
		}
	}
	@Test public void testRollbackWhenNoCommit() throws Exception
	{
		File dataFile = File.createTempFile("TestCommandExecutor", "testRollbackOnException");
		dataFile.deleteOnExit();
		DomainTestUtil.setupDataRegistry(dataFile);
		
		DomUtil.appendTextNode(DataRegistry.getDocument(), DataRegistry.getDocument().getDocumentElement(), "version0Node", "version0Value");
		DataRegistry.commit();
		DataRegistry.flush();
		
		CommandExecutor.executeCommand(testRollbackWhenNoCommitCommand.class);

		Assert.assertEquals("version0Value", DomUtil.getString(DataRegistry.getDocument().getDocumentElement(), "version0Node"));
		Assert.assertNull("commandValue", DomUtil.getString(DataRegistry.getDocument().getDocumentElement(), "commandNode"));
	}
	public static class testRollbackWhenNoCommitCommand extends AbstractCommand
	{
		public testRollbackWhenNoCommitCommand(Helper request, Helper response) {
			super(request, response);
		}
		public void execute() throws CommandException {
			DomUtil.appendTextNode(DataRegistry.getDocument(), DataRegistry.getDocument().getDocumentElement(), "commandNode", "commandValue");
		}
	}
	@Test public void testCommitKeepsChanges() throws Exception
	{
		File dataFile = File.createTempFile("TestCommandExecutor", "testRollbackOnException");
		dataFile.deleteOnExit();
		DomainTestUtil.setupDataRegistry(dataFile);
		
		DomUtil.appendTextNode(DataRegistry.getDocument(), DataRegistry.getDocument().getDocumentElement(), "version0Node", "version0Value");
		DataRegistry.commit();
		DataRegistry.flush();
		
		CommandExecutor.executeCommand(testCommitKeepsChangesCommand.class);

		Assert.assertEquals("version0Value", DomUtil.getString(DataRegistry.getDocument().getDocumentElement(), "version0Node"));
		Assert.assertEquals("commandValue", DomUtil.getString(DataRegistry.getDocument().getDocumentElement(), "commandNode"));
	}
	public static class testCommitKeepsChangesCommand extends AbstractCommand
	{
		public testCommitKeepsChangesCommand(Helper request, Helper response) {
			super(request, response);
		}
		public void execute() throws CommandException {
			DomUtil.appendTextNode(DataRegistry.getDocument(), DataRegistry.getDocument().getDocumentElement(), "commandNode", "commandValue");
			try {
				UoW.getCurrent().commit();
			} catch (Exception e) {
				throw new CommandException(e);
			}
		}
	}
}
