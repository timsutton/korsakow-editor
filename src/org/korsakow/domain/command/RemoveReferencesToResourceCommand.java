package org.korsakow.domain.command;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.Snu;
import org.korsakow.domain.interf.IDynamicProperties;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.domain.mapper.input.MapperHelper;
import org.korsakow.domain.mapper.input.ProjectInputMapper;
import org.korsakow.domain.mapper.input.ResourceInputMapper;

public class RemoveReferencesToResourceCommand extends AbstractCommand{
	private final Log log = LogFactory.getLog(getClass());
	
	private IProject project;

	public RemoveReferencesToResourceCommand(Helper request, Helper response) {
		super(request, response);
		
	}
	public void execute()
			throws CommandException {
		try {
			project = ProjectInputMapper.find();
			
			IResource referent = ResourceInputMapper.map(request.getLong("id"));
			
			Set<IResource> dirty = new HashSet<IResource>();
			Set<IResource> locked = new HashSet<IResource>();

			if (referent instanceof ISnu) {
				ISnu snu = (ISnu) referent;
				if (snu.getMainMedia() != null)
				{
					IResource mainMedia = snu.getMainMedia();
					Collection<IResource> references = MapperHelper.findResourcesReferencing(mainMedia.getId());
					for (IResource referrer : references)
					{
						if (removeReferences(referrer, mainMedia)) {
							UoW.getCurrent().registerDirty(referrer);
							dirty.add(referrer);
						} else {
							locked.add(referrer);
							log.warn(String.format("unexpected: referrer(%s)'s reference(%s) not removed", referrer.getName(), mainMedia.getName()));
						}
					}
				}
			}

			Collection<IResource> references = MapperHelper.findResourcesReferencing(referent.getId());
			for (IResource referrer : references)
			{
				if (removeReferences(referrer, referent)) {
					UoW.getCurrent().registerDirty(referrer);
					dirty.add(referrer);
				} else {
					locked.add(referrer);
					log.warn(String.format("unexpected: referrer(%s)'s reference(%s) not removed", referrer.getName(), referent.getName()));
				}
			}
			
			response.set("resourceInUse", true);
			response.set("references", dirty);
			response.set("lockedBy", locked);

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
		} catch (XPathExpressionException e) {
			throw new CommandException(e);
		}
	}
	private boolean removeReferences(IResource from, IResource to)
	{
		if (from == null || to == null)
			throw new NullPointerException();
		boolean dirty = false;
		if (from instanceof ISnu && removeReferences((ISnu)from, to))
			dirty = true;
		if (from instanceof IInterface && removeReferences((IInterface)from, to))
			dirty = true;
		if (from instanceof IWidget && removeReferences((IWidget)from, to))
			dirty = true;
		if (from instanceof IRule && removeReferences((IRule)from, to))
			dirty = true;
		if (from instanceof IProject && removeReferences((IProject)from, to))
			dirty = true;
		if (from instanceof IDynamicProperties && removeReferences((IDynamicProperties)from, to))
			dirty = true;
		return dirty;
	}
	private boolean removeReferences(IRule from, IResource to)
	{
		return false;
	}
	private boolean removeReferences(ISnu from, IResource to)
	{
		if (from == null || to == null)
			throw new NullPointerException();
		boolean dirty = false;
		if (to.equals(from.getBackgroundSound())) {
			from.setBackgroundSound(null);
			from.setBackgroundSoundMode(Snu.DEFAULT_BACKGROUNDSOUNDMODE);
			dirty = true;
		}
		if (to.equals(from.getMainMedia())) {
			// TODO: it would be nice to be ale to add messages here indicating why this case is invalid
		}
		if (to.equals(from.getPreviewMedia())) {
			from.setPreviewMedia(null);
			dirty = true;
		}
		if (to.equals(from.getInterface())) {
			if ( !to.equals( project.getDefaultInterface() ) ) {
				from.setInterface( project.getDefaultInterface() );
				dirty = true;
			}
		}
		return dirty;
	}
	private boolean removeReferences(IInterface from, IResource to)
	{
		boolean dirty = false;
		if (to.equals(from.getClickSound())) {
			from.setClickSound(null);
			dirty = true;
		}
		return dirty;
	}
	private boolean removeReferences(IWidget from, IResource to)
	{
		return false;
	}
	private boolean removeReferences(IProject from, IResource to)
	{
		boolean dirty = false;
		if (to.equals(from.getBackgroundImage())) {
			from.setBackgroundImage(null);
			dirty = true;
		}
		if (to.equals(from.getBackgroundSound())) {
			from.setBackgroundSound(null);
			dirty = true;
		}
		if (to.equals(from.getClickSound())) {
			from.setClickSound(null);
			dirty = true;
		}
		if (to.equals(from.getSplashScreenMedia())) {
			from.setSplashScreenMedia(null);
			dirty = true;
		}
		return dirty;
	}
	private boolean removeReferences(IDynamicProperties from, IResource to)
	{
		boolean dirty = false;
		Collection<String> ids = new HashSet<String>(from.getDynamicPropertyIds()); // concurrent modification
		for (String id : ids)
		{
			// TODO: this is a terrible way of checking but at the moment its the best we can do.
			if (!id.endsWith("Id"))
				continue;
			if (from.getDynamicProperty(id) != null &&
					(from.getDynamicProperty(id).equals(to.getId()) ||
					from.getDynamicProperty(id).equals(""+to.getId())) // this really sucks, I hope to fix the DynamicProperties soon.
					)
			{
				from.setDynamicProperty(id, null);
				dirty = true;
			}
		}
		return dirty;
	}
}
