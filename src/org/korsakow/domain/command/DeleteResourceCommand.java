package org.korsakow.domain.command;

import java.util.HashMap;
import java.util.Map;

import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IPattern;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ISettings;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.interf.IText;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.domain.mapper.input.ResourceInputMapper;

public class DeleteResourceCommand extends AbstractCommand{

	public static final String ID = "id";
	
	private static Map<Class<?>, Class<? extends AbstractCommand>> map = new HashMap<Class<?>, Class<? extends AbstractCommand>>();
	
	private static Class<? extends AbstractCommand> getCommand(Class<?> clazz) throws CommandException
	{
		if (!map.containsKey(clazz)) {
			if (IImage.class.isAssignableFrom(clazz)) {
				map.put(clazz, DeleteImageCommand.class);
			} else
			if (ISound.class.isAssignableFrom(clazz)) {
				map.put(clazz, DeleteSoundCommand.class);
			} else
			if (IVideo.class.isAssignableFrom(clazz)) {
				map.put(clazz, DeleteVideoCommand.class);
			} else
			if (IText.class.isAssignableFrom(clazz)) {
				map.put(clazz, DeleteTextCommand.class);
			} else
			if (IPattern.class.isAssignableFrom(clazz)) {
				map.put(clazz, DeletePatternCommand.class);
			} else
			if (ISnu.class.isAssignableFrom(clazz)) {
				map.put(clazz, DeleteSnuCommand.class);
			} else
			if (IInterface.class.isAssignableFrom(clazz)) {
				map.put(clazz, DeleteInterfaceCommand.class);
			} else
			if (IRule.class.isAssignableFrom(clazz)) {
				map.put(clazz, DeleteRuleCommand.class);
			} else
			if (IProject.class.isAssignableFrom(clazz)) {
				map.put(clazz, DeleteProjectCommand.class);
			} else
			if (ISettings.class.isAssignableFrom(clazz)) {
				map.put(clazz, DeleteSettingsCommand.class);
			} else
				throw new CommandException("Don't know how to delete: " + clazz.getCanonicalName());
		}
		return map.get(clazz);
	}

	public DeleteResourceCommand(Helper request, Helper response) {
		super(request, response);
	}

	public void execute() throws CommandException
	{
		IResource resource;
		try {
			resource = ResourceInputMapper.map(request.getLong(ID));
		} catch (NumberFormatException e) {
			throw new CommandException(e);
		} catch (MapperException e) {
			throw new CommandException(e);
		}
		
		final Class<? extends IResource> resourceClass = resource.getClass();
		Class<? extends AbstractCommand> commandClass = getCommand(resourceClass);
		forward(commandClass);
	}
}
