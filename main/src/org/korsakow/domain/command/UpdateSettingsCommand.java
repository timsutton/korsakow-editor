package org.korsakow.domain.command;

import java.sql.SQLException;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.Settings;
import org.korsakow.domain.mapper.input.SettingsInputMapper;

public class UpdateSettingsCommand extends AbstractCommand{


	public static final String SETTINGS = "settings";
	public static final String PROPERTY_VALUES = "property_values";
	public static final String PROPERTY_IDS = "property_ids";
	public static final String ID = "id";

	public UpdateSettingsCommand(Helper request, Helper response) {
		super(request, response);
		
	}

	public void execute()
			throws CommandException {
		try {
			Settings p = null;
			p = SettingsInputMapper.map(request.getLong(ID));
			
			List<String> propertyIds = (List<String>)request.get(PROPERTY_IDS);
			List<Object> propertyValues = (List<Object>)request.get(PROPERTY_VALUES);
			for (int j = 0; j < propertyIds.size(); ++j)
				p.setDynamicProperty(propertyIds.get(j), propertyValues.get(j));
			response.set(SETTINGS, p);
			UoW.getCurrent().registerDirty(p);
			UoW.getCurrent().commit();
			UoW.newCurrent();
		} catch (MapperException e) {
			throw new CommandException(e);
		} catch (SQLException e) {
			throw new CommandException(e);
		} catch (KeyNotFoundException e) {
			throw new CommandException(e);
		} catch (CreationException e) {
			throw new CommandException(e);
		}
	}

}
