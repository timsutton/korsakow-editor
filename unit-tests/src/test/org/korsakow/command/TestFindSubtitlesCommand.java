package test.org.korsakow.command;

import java.io.File;
import java.util.Collection;

import org.dsrg.soenea.uow.UoW;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.korsakow.domain.ProjectFactory;
import org.korsakow.domain.VideoFactory;
import org.korsakow.domain.command.FindSubtitlesCommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.domain.mapper.input.VideoInputMapper;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.util.FileUtil;

import test.org.korsakow.domain.AbstractDomainObjectTestCase;
import test.util.DomainTestUtil;

public class TestFindSubtitlesCommand extends AbstractDomainObjectTestCase
{
	@Override
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
		DomainTestUtil.setupDataRegistry(DataRegistry.getFile(), DataRegistry.createDefaultDocument());
	}
	@Test public void testFindsSrt() throws Exception
	{
		final String filenameBase = "resources/couple_naked";
		final String filename = new File( parentDir, filenameBase + ".mov").getPath();
		final String subtitle = new File( parentDir, filenameBase + ".srt").getPath();
		
		Assert.assertTrue(new File(parentDir, "resources").mkdirs());
		Assert.assertTrue(new File(filename).createNewFile()); // necessary precondition for the test
		Assert.assertTrue(new File(subtitle).createNewFile()); // necessary precondition for the test
		
		IProject project = ProjectFactory.createNew();
		IVideo video = VideoFactory.createNew();
		video.setFilename(filename);
		video.setSubtitles(null);
		
		UoW.getCurrent().commit();
		UoW.newCurrent(); // important, as we test the domain store was updated
		
		Request request = new Request();
		request.set("projectId", project.getId());
		Response response = new Response();
		new FindSubtitlesCommand(request, response).execute();
		
		Collection<IVideo> updated = (Collection<IVideo>)response.get("updated");
		
		// correct info returned in response
		Assert.assertNotNull(updated);
		Assert.assertEquals(1, updated.size());
		Assert.assertEquals(video.getId(), updated.iterator().next().getId());
		Assert.assertEquals(subtitle, updated.iterator().next().getSubtitles());
		// domain store is properly updated
		Assert.assertEquals(subtitle, VideoInputMapper.map(video.getId()).getSubtitles());
	}
	@Test public void testFindsK3() throws Exception
	{
		final String MAGIC = "[subtitle.tool]";
		final String filenameBase = "resources/man01";
		final String filename = new File( parentDir, filenameBase + ".mov" ).getPath();
		final String subtitle = new File( parentDir, filenameBase + ".txt" ).getPath();
		
		Assert.assertTrue(new File(parentDir, "resources").mkdirs());
		Assert.assertTrue(new File(filename).createNewFile()); // necessary precondition for the test
		Assert.assertTrue(new File(subtitle).createNewFile()); // necessary precondition for the test
		FileUtil.writeFileFromString(new File(subtitle), MAGIC + "\r\n");
		
		IProject project = ProjectFactory.createNew();
		IVideo video = VideoFactory.createNew();
		video.setFilename(filename);
		video.setSubtitles(null);
		
		UoW.getCurrent().commit();
		UoW.newCurrent(); // important, as we test the domain store was updated
		
		Request request = new Request();
		request.set("projectId", project.getId());
		Response response = new Response();
		new FindSubtitlesCommand(request, response).execute();
		
		Collection<IVideo> updated = (Collection<IVideo>)response.get("updated");
		
		// correct info returned in response
		Assert.assertNotNull(updated);
		Assert.assertEquals(1, updated.size());
		Assert.assertEquals(video.getId(), updated.iterator().next().getId());
		Assert.assertEquals(subtitle, updated.iterator().next().getSubtitles());
		// domain store is properly updated
		Assert.assertEquals(subtitle, VideoInputMapper.map(video.getId()).getSubtitles());
	}
	@Test public void testDoesNotFindsNonK3Txt() throws Exception
	{
		File file = File.createTempFile(getClass().getCanonicalName(), "testDoesNotFindsNonK3Txt", parentDir);
		final String filenameBase = file.getAbsolutePath();
		final String filename = filenameBase + ".mov";
		final String subtitle = filenameBase + ".txt";
		
		Assert.assertTrue(new File(filename).createNewFile());
		Assert.assertTrue(new File(subtitle).createNewFile());
		
//		FileUtil.writeFileFromString(new File(subtitle), "[subtitle.tool]"); // verify the test works
		
		IProject project = ProjectFactory.createNew();
		IVideo video = VideoFactory.createNew();
		video.setFilename(filename);
		video.setSubtitles(null);
		
		UoW.getCurrent().commit();
		UoW.newCurrent(); // important, as we test the domain store was updated
		
		Request request = new Request();
		request.set("projectId", project.getId());
		Response response = new Response();
		new FindSubtitlesCommand(request, response).execute();
		
		Collection<IVideo> updated = (Collection<IVideo>)response.get("updated");
		
		// correct info returned in response
		Assert.assertNull(updated);
		// domain store is untouched
		Assert.assertNull(VideoInputMapper.map(video.getId()).getSubtitles());
	}
	@Test public void testFindsSrtForMedia() throws Exception
	{
		final String filenameBase = "resources/couple_naked";
		final String filename = new File( parentDir, filenameBase + ".mov").getPath();
		final String subtitle = new File( parentDir, filenameBase + ".srt").getPath();
		
		Assert.assertTrue(new File(parentDir, "resources").mkdirs());
		Assert.assertTrue(new File(filename).createNewFile()); // necessary precondition for the test
		Assert.assertTrue(new File(subtitle).createNewFile()); // necessary precondition for the test
		
		IVideo video = VideoFactory.createNew();
		video.setFilename(filename);
		video.setSubtitles(null);
		
		UoW.getCurrent().commit();
		UoW.newCurrent(); // important, as we test the domain store was updated
		
		Request request = new Request();
		request.set("videoId", video.getId());
		Response response = new Response();
		new FindSubtitlesCommand(request, response).execute();
		
		Collection<IVideo> updated = (Collection<IVideo>)response.get("updated");
		
		// correct info returned in response
		Assert.assertNotNull(updated);
		Assert.assertEquals(1, updated.size());
		Assert.assertEquals(video.getId(), updated.iterator().next().getId());
		Assert.assertEquals(subtitle, updated.iterator().next().getSubtitles());
		// domain store is properly updated
		Assert.assertEquals(subtitle, VideoInputMapper.map(video.getId()).getSubtitles());
	}
}
