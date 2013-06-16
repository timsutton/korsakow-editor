package org.korsakow.domain.command;

import java.sql.SQLException;
import java.util.Collection;

import javax.xml.xpath.XPathExpressionException;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.mapper.input.MapperHelper;

public class FindResourcesReferencingCommand extends AbstractCommand{

	public static final String ID = "id";
	public static final String REFERENCES = "references";
	public static final String IN_USE = "resourceInUse";

	public FindResourcesReferencingCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		try {
			Collection<IResource> list = MapperHelper.findResourcesReferencing(request.getLong(ID));
			response.set(REFERENCES, list);
			response.set(IN_USE, !list.isEmpty());
		} catch (MapperException e) {
			throw new CommandException(e);
		} catch (NumberFormatException e) {
			throw new CommandException(e);
		} catch (XPathExpressionException e) {
			throw new CommandException(e);
		} catch (SQLException e) {
			throw new CommandException(e);
		}
	}

}
