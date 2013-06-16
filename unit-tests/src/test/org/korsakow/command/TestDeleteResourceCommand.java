package test.org.korsakow.command;

import org.dsrg.soenea.domain.mapper.DomainObjectNotFoundException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.korsakow.domain.MediaSource;
import org.korsakow.domain.command.DeleteResourceCommand;
import org.korsakow.domain.command.ICommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.interf.ISnu.BackgroundSoundMode;
import org.korsakow.domain.mapper.input.ImageInputMapper;
import org.korsakow.domain.mapper.input.InterfaceInputMapper;
import org.korsakow.domain.mapper.input.ProjectInputMapper;
import org.korsakow.domain.mapper.input.SettingsInputMapper;
import org.korsakow.domain.mapper.input.SnuInputMapper;
import org.korsakow.domain.mapper.input.SoundInputMapper;
import org.korsakow.domain.mapper.input.TextInputMapper;
import org.korsakow.domain.mapper.input.VideoInputMapper;
import org.korsakow.ide.DataRegistry;
import org.korsakow.services.tdg.ImageTDG;
import org.korsakow.services.tdg.InterfaceTDG;
import org.korsakow.services.tdg.ProjectTDG;
import org.korsakow.services.tdg.SettingsTDG;
import org.korsakow.services.tdg.SnuTDG;
import org.korsakow.services.tdg.SoundTDG;
import org.korsakow.services.tdg.TextTDG;
import org.korsakow.services.tdg.VideoTDG;

import test.org.korsakow.domain.AbstractDomainObjectTestCase;

public class TestDeleteResourceCommand extends AbstractDomainObjectTestCase
{
	@Override
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
	}
	@Test public void testDeleteText() throws Exception
	{
		final long id = 6;
		TextTDG.insert(id, 0, "text", MediaSource.FILE.getId(), "value");
		DataRegistry.flush();
		
		Request request = new Request();
		request.set("id", id);
		ICommand command = new DeleteResourceCommand(request, new Response());
		command.execute();
		
		DataRegistry.flush();
		
		try {
			TextInputMapper.map(id);
			
			throw new IllegalStateException("should have an error");
		} catch (DomainObjectNotFoundException e) {
			// expected
		}
	}
	@Test public void testDeleteSound() throws Exception
	{
		final long id = 1;
		SoundTDG.insert(id, 0, "sound", "sound.ext", "subtitles.ext");
		DataRegistry.flush();
		
		Request request = new Request();
		request.set("id", id);
		ICommand command = new DeleteResourceCommand(request, new Response());
		command.execute();
		
		DataRegistry.flush();
		
		try {
			SoundInputMapper.map(id);
			
			throw new IllegalStateException("should have an error");
		} catch (DomainObjectNotFoundException e) {
			// expected
		}
	}
	@Test public void testDeleteVideo() throws Exception
	{
		final long id = 2;
		VideoTDG.insert(id, 0, "video", "video.ext", "subtitles.ext");
		DataRegistry.flush();
		
		Request request = new Request();
		request.set("id", id);
		ICommand command = new DeleteResourceCommand(request, new Response());
		command.execute();
		
		DataRegistry.flush();
		
		try {
			VideoInputMapper.map(id);
			
			throw new IllegalStateException("should have an error");
		} catch (DomainObjectNotFoundException e) {
			// expected
		}
	}
	@Test public void testDeleteImage() throws Exception
	{
		final long id = 3;
		ImageTDG.insert(id, 0, "image", "image.ext", 0L);
		DataRegistry.flush();
		
		Request request = new Request();
		request.set("id", id);
		ICommand command = new DeleteResourceCommand(request, new Response());
		command.execute();
		
		DataRegistry.flush();
		
		try {
			ImageInputMapper.map(id);
			
			throw new IllegalStateException("should have an error");
		} catch (DomainObjectNotFoundException e) {
			// expected
		}
	}
	@Test public void testDeleteSnu() throws Exception
	{
		final long id = 0L;
		SnuTDG.insert(id, 1L, "2", null, 3.0f, null, BackgroundSoundMode.KEEP.getId(), 4.0f, false, null, 6L, false, 7L, true, false, null, "8", "9");
		DataRegistry.flush();
		
		Request request = new Request();
		request.set("id", id);
		ICommand command = new DeleteResourceCommand(request, new Response());
		command.execute();
		
		DataRegistry.flush();
		
		try {
			SnuInputMapper.map(id);
			
			throw new IllegalStateException("should have an error");
		} catch (DomainObjectNotFoundException e) {
			// expected
		}
	}
	@Test public void testDeleteInterface() throws Exception
	{
		final long id = 5;
		InterfaceTDG.insert(id, 0L, "interface", 0, 1, 2, 3, 4L, 5f, 6L, "#aabbcc");
		ProjectTDG.insert(id+1, 0L, "project", null, 1, 2, null, 1.0f, true, null, 1.5f, null, null, null, false, true, null, null, null);
		DataRegistry.flush();
		
		Request request = new Request();
		request.set("id", id);
		ICommand command = new DeleteResourceCommand(request, new Response());
		command.execute();
		
		DataRegistry.flush();
		
		try {
			InterfaceInputMapper.map(id);
			
			throw new IllegalStateException("should have an error");
		} catch (DomainObjectNotFoundException e) {
			// expected
		}
	}
	@Test public void testDeleteProject() throws Exception
	{
		final long id = 5;
		ProjectTDG.insert(id, 0L, "project", null, 1, 2, null, 1.0f, true, null, 1.5f, null, null, null, false, true, null, null, null);
		DataRegistry.flush();
		
		Request request = new Request();
		request.set("id", id);
		ICommand command = new DeleteResourceCommand(request, new Response());
		command.execute();
		
		DataRegistry.flush();

		try {
			ProjectInputMapper.map(id);
			
			throw new IllegalStateException("should have an error");
		} catch (DomainObjectNotFoundException e) {
			// expected
		}
	}
	@Ignore
	@Test public void testDeleteSettings() throws Exception
	{
		final long id = 5;
		SettingsTDG.insert(id, 0L);
		DataRegistry.flush();
		
		Request request = new Request();
		request.set("id", id);
		ICommand command = new DeleteResourceCommand(request, new Response());
		command.execute();
		
		DataRegistry.flush();
		
		try {
			SettingsInputMapper.map(id);
			
			throw new IllegalStateException("should have an error");
		} catch (DomainObjectNotFoundException e) {
			// expected
		}
	}
}
