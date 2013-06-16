package test.org.korsakow.command;

import java.util.Collection;

import org.junit.Assert;

import org.dsrg.soenea.domain.interf.IDomainObject;
import org.dsrg.soenea.uow.UoW;
import org.junit.Test;
import org.korsakow.domain.ImageFactory;
import org.korsakow.domain.InterfaceFactory;
import org.korsakow.domain.ProjectFactory;
import org.korsakow.domain.SnuFactory;
import org.korsakow.domain.SoundFactory;
import org.korsakow.domain.VideoFactory;
import org.korsakow.domain.command.DeleteImageCommand;
import org.korsakow.domain.command.DeleteSoundCommand;
import org.korsakow.domain.command.DeleteVideoCommand;
import org.korsakow.domain.command.RemoveReferencesToResourceCommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.interf.ISound;
import org.korsakow.ide.DataRegistry;

import test.org.korsakow.domain.AbstractDomainObjectTestCase;
import test.util.DomainTestUtil;

public class TestRemoveReferencesCommand extends AbstractDomainObjectTestCase
{
	@Test public void testDeleteSnuMainMediaFailure() throws Exception
	{
		IProject proj = ProjectFactory.createNew();
		IMedia media = VideoFactory.createNew();
		ISnu snu = SnuFactory.createNew();
		snu.setMainMedia(media);
		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		Request request = new Request();
		request.set("id", media.getId());
		new RemoveReferencesToResourceCommand(request, new Response()).execute();;

		request = new Request();
		request.set("id", media.getId());
		new DeleteVideoCommand(request, new Response()).execute();
		
		DataRegistry.flush();
		
		assertNotInUse(request, snu);
		DomainTestUtil.assertVideoExist(DataRegistry.getFile(), media.getId(), true);
	}
	@Test public void testDeleteSnuPreviewSuccess() throws Exception
	{
		IProject proj = ProjectFactory.createNew();
		IMedia media = VideoFactory.createNew();
		ISnu snu = SnuFactory.createNew();
		snu.setPreviewMedia(media);
		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		Request request = new Request();
		request.set("id", media.getId());
		new RemoveReferencesToResourceCommand(request, new Response()).execute();

		request = new Request();
		request.set("id", media.getId());
		new DeleteVideoCommand(request, new Response()).execute();
		
		DataRegistry.flush();
		
		assertNotInUse(request, snu);
		DomainTestUtil.assertVideoExist(DataRegistry.getFile(), media.getId(), false);
	}
	@Test public void testDeleteSnuBackgroundSoundSuccess() throws Exception
	{
		IProject proj = ProjectFactory.createNew();
		ISound media = SoundFactory.createNew();
		ISnu snu = SnuFactory.createNew();
		snu.setBackgroundSound(media);
		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		Request request = new Request();
		request.set("id", media.getId());
		new RemoveReferencesToResourceCommand(request, new Response()).execute();

		request = new Request();
		request.set("id", media.getId());
		new DeleteSoundCommand(request, new Response()).execute();
		
		DataRegistry.flush();
		
		assertNotInUse(request, snu);
		DomainTestUtil.assertSoundNotExist(DataRegistry.getFile(), media.getId());
	}
	@Test public void testDeleteProjectBackgroundSoundSuccess() throws Exception
	{
		ISound media = SoundFactory.createNew();
		IProject project = ProjectFactory.createNew();
		project.setBackgroundSound(media);
		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		Request request = new Request();
		request.set("id", media.getId());
		new RemoveReferencesToResourceCommand(request, new Response()).execute();

		request = new Request();
		request.set("id", media.getId());
		new DeleteSoundCommand(request, new Response()).execute();
		
		DataRegistry.flush();
		
		assertNotInUse(request, project);
		DomainTestUtil.assertSoundNotExist(DataRegistry.getFile(), media.getId());
	}
	@Test public void testDeleteProjectBackgroundImageSuccess() throws Exception
	{
		IImage media = ImageFactory.createNew();
		IProject project = ProjectFactory.createNew();
		project.setBackgroundImage(media);
		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		Request request = new Request();
		request.set("id", media.getId());
		new RemoveReferencesToResourceCommand(request, new Response()).execute();

		request = new Request();
		request.set("id", media.getId());
		new DeleteImageCommand(request, new Response()).execute();
		
		DataRegistry.flush();
		
		assertNotInUse(request, project);
		DomainTestUtil.assertImageNotExist(DataRegistry.getFile(), media.getId());
	}
	@Test public void testDeleteProjectClickSoundSuccess() throws Exception
	{
		ISound media = SoundFactory.createNew();
		IProject project = ProjectFactory.createNew();
		project.setClickSound(media);
		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		Request request = new Request();
		request.set("id", media.getId());
		new RemoveReferencesToResourceCommand(request, new Response()).execute();

		request = new Request();
		request.set("id", media.getId());
		new DeleteSoundCommand(request, new Response()).execute();
		
		DataRegistry.flush();
		
		assertNotInUse(request, project);
		DomainTestUtil.assertSoundNotExist(DataRegistry.getFile(), media.getId());
	}
	@Test public void testDeleteProjectSplashScreenSuccess() throws Exception
	{
		IMedia media = ImageFactory.createNew();
		IProject project = ProjectFactory.createNew();
		project.setSplashScreenMedia(media);
		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		Request request = new Request();
		request.set("id", media.getId());
		new RemoveReferencesToResourceCommand(request, new Response()).execute();

		request = new Request();
		request.set("id", media.getId());
		new DeleteImageCommand(request, new Response()).execute();
		
		DataRegistry.flush();
		
		assertNotInUse(request, project);
		DomainTestUtil.assertImageNotExist(DataRegistry.getFile(), media.getId());
	}
	@Test public void testDeleteInterfaceClickSoundSuccess() throws Exception
	{
		IProject proj = ProjectFactory.createNew();
		ISound media = SoundFactory.createNew();
		IInterface interf = InterfaceFactory.createNew();
		interf.setClickSound(media);
		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		Request request = new Request();
		request.set("id", media.getId());
		new RemoveReferencesToResourceCommand(request, new Response()).execute();

		request = new Request();
		request.set("id", media.getId());
		new DeleteSoundCommand(request, new Response()).execute();
		
		DataRegistry.flush();
		
		assertNotInUse(request, interf);
		DomainTestUtil.assertSoundNotExist(DataRegistry.getFile(), media.getId());
	}
	
	private void assertNotInUse(Request response, IDomainObject<Long> referee)
	{
		Boolean resourceInUse = (Boolean)response.get("resourceInUse");
		Assert.assertFalse("resourceInUse==FALSE", Boolean.FALSE.equals(resourceInUse));
		
		Collection<IResource> references = (Collection<IResource>)response.get("references");
		if (references != null)
			Assert.assertEquals(0, references.size());
	}
}
