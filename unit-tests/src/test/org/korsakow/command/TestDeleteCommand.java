package test.org.korsakow.command;



import org.junit.Before;
import org.junit.Test;
import org.korsakow.domain.MediaSource;
import org.korsakow.domain.command.DeleteImageCommand;
import org.korsakow.domain.command.DeleteInterfaceCommand;
import org.korsakow.domain.command.DeleteSnuCommand;
import org.korsakow.domain.command.DeleteSoundCommand;
import org.korsakow.domain.command.DeleteTextCommand;
import org.korsakow.domain.command.DeleteVideoCommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.interf.ISnu.BackgroundSoundMode;
import org.korsakow.ide.DataRegistry;
import org.korsakow.services.tdg.ImageTDG;
import org.korsakow.services.tdg.InterfaceTDG;
import org.korsakow.services.tdg.ProjectTDG;
import org.korsakow.services.tdg.SnuTDG;
import org.korsakow.services.tdg.SoundTDG;
import org.korsakow.services.tdg.TextTDG;
import org.korsakow.services.tdg.VideoTDG;
import org.korsakow.services.util.ColorFactory;

import test.org.korsakow.domain.AbstractDomainObjectTestCase;
import test.util.DOFactory;
import test.util.DomainTestUtil;

public class TestDeleteCommand extends AbstractDomainObjectTestCase
{
	@Override
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
	}
	@Test public void testFalsePositive() throws Exception
	{
		try {
			final long id = 0;
			SoundTDG.insert(0, 0, "sound", "sound.ext", "subtitles.ext");
			DataRegistry.flush();
			
			DomainTestUtil.assertSoundNotExist(dataFile, id);
		} catch (AssertionError e) {
			// good!
			return;
		}
		throw new AssertionError("false positive!");
	}
	@Test public void testDeleteText() throws Exception
	{
		final long id = 6;
		TextTDG.insert(id, 0, "text", MediaSource.FILE.getId(), "value");
		DataRegistry.flush();
		
		Request request = new Request();
		request.set("id", id);
		DeleteTextCommand command = new DeleteTextCommand(request, new Response());
		command.execute();
		
		DataRegistry.flush();
		
		DomainTestUtil.assertTextNotExist(dataFile, id);
	}
	@Test public void testDeleteSound() throws Exception
	{
		final long id = 1;
		SoundTDG.insert(id, 0, "sound", "sound.ext", "subtitles.ext");
		DataRegistry.flush();
		
		Request request = new Request();
		request.set("id", id);
		DeleteSoundCommand command = new DeleteSoundCommand(request, new Response());
		command.execute();
		
		DataRegistry.flush();
		
		DomainTestUtil.assertSoundNotExist(dataFile, id);
	}
	@Test public void testDeleteVideo() throws Exception
	{
		final long id = 2;
		VideoTDG.insert(id, 0, "video", "video.ext", "subtitles.ext");
		DataRegistry.flush();
		
		Request request = new Request();
		request.set("id", id);
		DeleteVideoCommand command = new DeleteVideoCommand(request, new Response());
		command.execute();
		
		DataRegistry.flush();
		
		DomainTestUtil.assertVideoExist(dataFile, id, false);
	}
	@Test public void testDeleteImage() throws Exception
	{
		final long id = 3;
		ImageTDG.insert(id, 0, "image", "image.ext", 0L);
		DataRegistry.flush();
		
		Request request = new Request();
		request.set("id", id);
		DeleteImageCommand command = new DeleteImageCommand(request, new Response());
		command.execute();
		
		DataRegistry.flush();
		
		DomainTestUtil.assertImageNotExist(dataFile, id);
	}
	@Test public void testDeleteSnu() throws Exception
	{
		final long id = 0L;
		SnuTDG.insert(id, 1L, "2", null, 3.0f, null, BackgroundSoundMode.KEEP.getId(), 4.0f, false, null, 6L, false, 7L, true, false, null, "8", "9");
		DataRegistry.flush();
		
		Request request = new Request();
		request.set("id", id);
		DeleteSnuCommand command = new DeleteSnuCommand(request, new Response());
		command.execute();
		
		DataRegistry.flush();
		
		DomainTestUtil.assertSnuNotExist(dataFile, id);
	}
	@Test public void testDeleteInterface() throws Exception
	{
		final long id = 5;
		InterfaceTDG.insert(id, 0L, "interface", 0, 1, 2, 3, 4L, 5f, 6L, ColorFactory.toString(ColorFactory.createRGB(7)));
		ProjectTDG.insert(id+1, 0L, "project", null, 1, 2, null, 1.0f, true, null, 1.5f, null, null, null, false, true, null, null, null);
		DataRegistry.flush();
		Request request = new Request();
		request.set("id", id);
		DeleteInterfaceCommand command = new DeleteInterfaceCommand(request, new Response());
		command.execute();
		
		DataRegistry.flush();
		
		DomainTestUtil.assertInterfaceNotExist(dataFile, id);
	}
}
