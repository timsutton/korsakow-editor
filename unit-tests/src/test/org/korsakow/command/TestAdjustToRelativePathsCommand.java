package test.org.korsakow.command;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import org.junit.Assert;

import org.dsrg.soenea.uow.UoW;
import org.junit.Before;
import org.junit.Test;
import org.korsakow.domain.ProjectFactory;
import org.korsakow.domain.command.AdjustToRelativePathsCommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.util.FileUtil;

import test.org.korsakow.domain.AbstractDomainObjectTestCase;
import test.util.DOFactory;
import test.util.DomainTestUtil;

public class TestAdjustToRelativePathsCommand extends AbstractDomainObjectTestCase
{
	@Override
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
		DomainTestUtil.setupDataRegistry(DataRegistry.getFile(), DataRegistry.createDefaultDocument());
	}
	@Test public void testNotInSubfolderFailure() throws Exception
	{
		IProject project = DOFactory.createDefaultTestProject(parentDir);
		Assert.assertFalse(project.getMedia().isEmpty());
		HashMap<IMedia, String> relativePathMap = new HashMap<IMedia, String>();
		HashMap<IMedia, String> absolutePathMap = new HashMap<IMedia, String>();
		final String basePath = dataFile.getParentFile().getAbsolutePath();
//		System.out.println(String.format("BasePath: %s", basePath));
		for (IMedia medium : project.getMedia()) {
//			System.out.println("Media: " + medium.getFilename());
			Assert.assertTrue(new File(medium.getFilename()).exists());
			Assert.assertTrue(medium.getFilename().startsWith(basePath));
			absolutePathMap.put(medium, medium.getFilename());
			relativePathMap.put(medium, medium.getFilename().substring(basePath.length()+1));
			UoW.getCurrent().registerDirty(medium);
		}
		
		IMedia failureMedium = DOFactory.createDummyMedia(FileUtil.createTempDirectory("mama", "dada"));
		project.getMedia().add(failureMedium);
		
		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		Request request = new Request();
		request.set("id", project.getId());
		request.set("basePath", basePath);
		
		Response response = new Response();
		new AdjustToRelativePathsCommand(request, response).execute();
		Assert.assertEquals(Boolean.FALSE, response.getBoolean("status"));
		Assert.assertNull(response.get("media"));
	}
	@Test public void testInitialFilenameRelativeSuccess() throws Exception
	{
		IProject project = DOFactory.createDefaultTestProject(parentDir);
		Assert.assertFalse(project.getMedia().isEmpty());
		HashMap<IMedia, String> absolutePathMap = new HashMap<IMedia, String>();
		final String basePath = dataFile.getParentFile().getAbsolutePath();
//		System.out.println(String.format("BasePath: %s", basePath));
		for (IMedia medium : project.getMedia()) {
//			System.out.println("Media: " + medium.getFilename());
			Assert.assertTrue(new File(medium.getFilename()).exists());
			Assert.assertTrue(medium.getFilename().startsWith(basePath));
			absolutePathMap.put(medium, medium.getFilename());
			System.out.println(medium.getFilename());
			medium.setFilename(medium.getFilename().substring(basePath.length()+1));
			UoW.getCurrent().registerDirty(medium);
		}
		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		Request request = new Request();
		request.set("id", project.getId());
		request.set("basePath", basePath);
		Response response = new Response();
		new AdjustToRelativePathsCommand(request, response).execute();
		Collection<IMedia> actualMedia = (Collection<IMedia>)response.get("media");
		Assert.assertEquals(Boolean.TRUE, response.getBoolean("status"));
		for (IMedia medium : actualMedia) {
			String absolutePath = absolutePathMap.get(medium);
			Assert.assertEquals(absolutePath, basePath + File.separator + medium.getFilename());
		}
	}
	@Test public void testInitialFilenameAbsoluteSuccess() throws Exception
	{
		IProject project = DOFactory.createDefaultTestProject(parentDir);
		Assert.assertFalse(project.getMedia().isEmpty());
		HashMap<IMedia, String> absolutePathMap = new HashMap<IMedia, String>();
		final String basePath = dataFile.getParentFile().getAbsolutePath();
//		System.out.println(String.format("BasePath: %s", basePath));
		for (IMedia medium : project.getMedia()) {
//			System.out.println("Media: " + medium.getFilename());
			Assert.assertTrue(new File(medium.getAbsoluteFilename()).exists());
			absolutePathMap.put(medium, medium.getAbsoluteFilename());
			UoW.getCurrent().registerDirty(medium);
		}
		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		Request request = new Request();
		request.set("id", project.getId());
		request.set("basePath", basePath);
		Response response = new Response();
		new AdjustToRelativePathsCommand(request, response).execute();
		Collection<IMedia> actualMedia = (Collection<IMedia>)response.get("media");
		
		Assert.assertEquals(Boolean.TRUE, response.getBoolean("status"));
		for (IMedia medium : actualMedia) {
			String absolutePath = absolutePathMap.get(medium);
			Assert.assertEquals(absolutePath, basePath + File.separator + medium.getFilename());
		}
	}
	@Test public void testSubtitlesWithInitialFilenameAbsoluteSuccess() throws Exception
	{
		IProject project = ProjectFactory.createNew();
		IVideo video = DOFactory.createVideo(DOFactory.getEmptyPNGFile(parentDir));
		File subtitleFile = File.createTempFile("subtitle", ".txt", parentDir);
		video.setSubtitles(subtitleFile.getAbsolutePath());
		
		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		final String basePath = dataFile.getParentFile().getAbsolutePath();
		
		Request request = new Request();
		request.set("id", project.getId());
		request.set("basePath", basePath);
		Response response = new Response();
		new AdjustToRelativePathsCommand(request, response).execute();
		Collection<IMedia> actualMedia = (Collection<IMedia>)response.get("media");
		IVideo actualVideo = (IVideo)actualMedia.iterator().next();
		
		Assert.assertEquals(Boolean.TRUE, response.getBoolean("status"));
		Assert.assertEquals(subtitleFile.getAbsolutePath(), basePath + File.separator + actualVideo.getSubtitles());
	}
	@Test public void testSubtitlesWithInitialFilenameRelativeSuccess() throws Exception
	{
		IProject project = ProjectFactory.createNew();
		IVideo video = DOFactory.createVideo(DOFactory.getEmptyPNGFile(parentDir));
		File subtitleFile = File.createTempFile("subtitle", ".txt", parentDir);
		final String basePath = dataFile.getParentFile().getAbsolutePath();
		video.setSubtitles(subtitleFile.getAbsolutePath().substring(basePath.length()+1));
		
		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		
		Request request = new Request();
		request.set("id", project.getId());
		request.set("basePath", basePath);
		Response response = new Response();
		new AdjustToRelativePathsCommand(request, response).execute();
		Collection<IMedia> actualMedia = (Collection<IMedia>)response.get("media");
		IVideo actualVideo = (IVideo)actualMedia.iterator().next();
		
		Assert.assertEquals(Boolean.TRUE, response.getBoolean("status"));
		Assert.assertEquals(subtitleFile.getAbsolutePath(), basePath + File.separator + actualVideo.getSubtitles());
	}
}
