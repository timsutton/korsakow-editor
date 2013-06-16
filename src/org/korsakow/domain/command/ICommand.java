package org.korsakow.domain.command;

import org.dsrg.soenea.domain.command.CommandException;

public interface ICommand {
	public void execute() throws CommandException;
}
