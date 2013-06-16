package org.korsakow.domain.command;

import java.lang.reflect.InvocationTargetException;

import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.CommandExecutor;


public abstract class AbstractCommand implements ICommand
{
	protected Helper request;
	protected Helper response;
	public AbstractCommand(Helper request, Helper response) {
		if (request == null) throw new NullPointerException("null request");
		if (response == null) throw new NullPointerException("null response");
		this.request = request;
		this.response = response;
	}
	
	protected void forward(Class<? extends AbstractCommand> clazz) throws CommandException
	{
		forward(clazz, request, response);
	}
	protected void forward(Class<? extends AbstractCommand> clazz, Helper fwdRequest, Helper fwdResponse) throws CommandException
	{
		ICommand command;
		try {
			command = CommandExecutor.getCommand(clazz, fwdRequest, fwdResponse);
		} catch (SecurityException e) {
			throw new CommandException(e);
		} catch (IllegalArgumentException e) {
			throw new CommandException(e);
		} catch (NoSuchMethodException e) {
			throw new CommandException(e);
		} catch (InstantiationException e) {
			throw new CommandException(e);
		} catch (IllegalAccessException e) {
			throw new CommandException(e);
		} catch (InvocationTargetException e) {
			throw new CommandException(e);
		}
		command.execute();
	}
}
