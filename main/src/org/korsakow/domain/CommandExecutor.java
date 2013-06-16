package org.korsakow.domain;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.command.AbstractCommand;
import org.korsakow.domain.command.Helper;
import org.korsakow.domain.command.ICommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.ide.DataRegistry;

/**
 * We require AbstractCommand (not just ICommand) because of the constructor requirements.
 * @author d
 *
 */
public class CommandExecutor {
	private static volatile Class<? extends AbstractCommand> lastLogged = null;
	private static volatile long timeLastLogged = System.currentTimeMillis();
	private static final long timeThreshold = 1000;
	
	/**
	 * Attemps to log in such a way that it avoids spam.
	 * 
	 * Some commands are executed repeatedly, especially in large projects where big batches of media/snu are
	 * handled.
	 * 
	 * So we limit by only logging the same command twice in a row if "enough" time has passed.
	 * 
	 * @param clazz
	 */
	private static void logCommandExecution(Class<? extends AbstractCommand> clazz, Helper request)
	{
		long now = System.currentTimeMillis();
		if (lastLogged == null || lastLogged != clazz ||
			(now - timeLastLogged > timeThreshold))
		{
			StringBuilder sb = new StringBuilder();
			for ( String key : request.getKeys() )
				sb.append(key).append("=").append(request.get(key)).append(",");
			Logger.getLogger(CommandExecutor.class).info("executeCommand: " + clazz.getCanonicalName() + " ; " + sb.toString());
			lastLogged = clazz;
			timeLastLogged = now;
		}
		
	}
	
	public static ICommand getCommand(Class<? extends AbstractCommand> clazz, Helper request, Helper response) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
	{
		Constructor<? extends AbstractCommand> ctor = clazz.getConstructor(Helper.class, Helper.class);
		ICommand command = ctor.newInstance(request, response);
		return command;
	}
	
	public static Response executeCommand(Class<? extends AbstractCommand> clazz) throws CommandException
	{
		return executeCommand(clazz, new Request());
	}
	public static Response executeCommand(Class<? extends AbstractCommand> clazz, Helper request) throws CommandException
	{
		Response response = new Response();
		executeCommand(clazz, request, response);
		return response;
	}
	public static void executeCommand(Class<? extends AbstractCommand> clazz, Helper request, Helper response) throws CommandException
	{
		try {
			UoW.newCurrent();

			executeCommandNoCommit(clazz, request, response);
			
		} finally {
 			try {
				DataRegistry.rollback();
			} catch (Exception e2) {
				Logger.getLogger(CommandExecutor.class).error("", e2);
			}
		}
	}
	public static void executeCommandNoCommit(Class<? extends AbstractCommand> clazz, Helper request, Helper response) throws CommandException
	{
		logCommandExecution(clazz, request);
		try {
			ICommand command = getCommand(clazz, request, response);
			command.execute();
		} catch (SecurityException e) {
			throw new CommandException(e);
		} catch (NoSuchMethodException e) {
			throw new CommandException(e);
		} catch (IllegalArgumentException e) {
			throw new CommandException(e);
		} catch (InstantiationException e) {
			throw new CommandException(e);
		} catch (IllegalAccessException e) {
			throw new CommandException(e);
		} catch (InvocationTargetException e) {
			throw new CommandException(e);
		}
	}
}
