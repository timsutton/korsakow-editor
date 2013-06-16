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
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.interf.ISound;

import test.org.korsakow.domain.AbstractDomainObjectTestCase;

public class TestDeleteCommandWhenReferenced extends AbstractDomainObjectTestCase
{
	@Test public void testDeleteSnuMainMediaFailure() throws Exception
	{
		IMedia media = VideoFactory.createNew();
		ISnu snu = SnuFactory.createNew();
		snu.setMainMedia(media);
		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		Request request = new Request();
		request.set("id", media.getId());
		Response response = new Response();
		
		new DeleteVideoCommand(request, response).execute();
		
		assertInUse(response, snu);
	}
	@Test public void testDeleteSnuPreviewFailure() throws Exception
	{
		IMedia media = VideoFactory.createNew();
		ISnu snu = SnuFactory.createNew();
		snu.setPreviewMedia(media);
		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		Request request = new Request();
		request.set("id", media.getId());
		Response response = new Response();
		
		new DeleteVideoCommand(request, response).execute();
		
		assertInUse(response, snu);
	}
	@Test public void testDeleteSnuMainMediaAndPreviewFailure() throws Exception
	{
		IMedia media = VideoFactory.createNew();
		ISnu snu = SnuFactory.createNew();
		snu.setMainMedia(media);
		snu.setPreviewMedia(media);
		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		Request request = new Request();
		request.set("id", media.getId());
		Response response = new Response();
		
		new DeleteVideoCommand(request, response).execute();
		
		assertInUse(response, snu);
	}
	@Test public void testDeleteSnuBackgroundSoundFailure() throws Exception
	{
		ISound media = SoundFactory.createNew();
		ISnu snu = SnuFactory.createNew();
		snu.setBackgroundSound(media);
		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		Request request = new Request();
		request.set("id", media.getId());
		Response response = new Response();
		
		new DeleteSoundCommand(request, response).execute();
		
		assertInUse(response, snu);
	}
	@Test public void testDeleteProjectBackgroundSoundFailure() throws Exception
	{
		ISound media = SoundFactory.createNew();
		IProject project = ProjectFactory.createNew();
		project.setBackgroundSound(media);
		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		Request request = new Request();
		request.set("id", media.getId());
		Response response = new Response();
		
		new DeleteSoundCommand(request, response).execute();
		
		assertInUse(response, project);
	}
	@Test public void testDeleteProjectClickSoundFailure() throws Exception
	{
		ISound media = SoundFactory.createNew();
		IProject project = ProjectFactory.createNew();
		project.setClickSound(media);
		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		Request request = new Request();
		request.set("id", media.getId());
		Response response = new Response();
		
		new DeleteSoundCommand(request, response).execute();
		
		assertInUse(response, project);
	}
	@Test public void testDeleteProjectSplashScreenFailure() throws Exception
	{
		IImage media = ImageFactory.createNew();
		IProject project = ProjectFactory.createNew();
		project.setSplashScreenMedia(media);
		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		Request request = new Request();
		request.set("id", media.getId());
		Response response = new Response();
		
		new DeleteImageCommand(request, response).execute();
		
		assertInUse(response, project);
	}
	@Test public void testDeleteInterfaceClickSoundFailure() throws Exception
	{
		ISound media = SoundFactory.createNew();
		IInterface interf = InterfaceFactory.createNew();
		interf.setClickSound(media);
		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		Request request = new Request();
		request.set("id", media.getId());
		Response response = new Response();
		
		new DeleteSoundCommand(request, response).execute();
		
		assertInUse(response, interf);
	}
	private void assertInUse(Response response, IDomainObject<Long> referer)
	{
		assertInUse(response, referer, 1);
	}
	private void assertInUse(Response response, IDomainObject<Long> referer, long expectedUseCount)
	{
		Boolean resourceInUse = response.has("resourceInUse") && response.getBoolean("resourceInUse");
		Collection<IResource> references = (Collection<IResource>)response.get("references");

		Assert.assertEquals(Boolean.TRUE, resourceInUse);
		Assert.assertEquals(expectedUseCount, references.size());
		Assert.assertEquals(referer, references.iterator().next());
	}
}
