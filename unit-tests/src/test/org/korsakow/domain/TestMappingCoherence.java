package test.org.korsakow.domain;

import org.junit.Assert;

import org.dsrg.soenea.uow.UoW;
import org.junit.Test;
import org.korsakow.domain.ImageFactory;
import org.korsakow.domain.SoundFactory;
import org.korsakow.domain.TextFactory;
import org.korsakow.domain.VideoFactory;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.mapper.input.ImageInputMapper;
import org.korsakow.domain.mapper.input.SoundInputMapper;
import org.korsakow.domain.mapper.input.TextInputMapper;
import org.korsakow.domain.mapper.input.VideoInputMapper;

public class TestMappingCoherence extends AbstractDomainObjectTestCase
{
	@Test public void testMapImageAsSoundFailure() throws Exception
	{
		IMedia media = ImageFactory.createNew();
		UoW.getCurrent().commit();
		UoW.newCurrent();
		assertMapAsSoundFailure(media.getId());
	}
	@Test public void testMapImageAsVideoFailure() throws Exception
	{
		IMedia media = ImageFactory.createNew();
		UoW.getCurrent().commit();
		UoW.newCurrent();
		assertMapAsVideoFailure(media.getId());
	}
	@Test public void testMapImageAsTextFailure() throws Exception
	{
		IMedia media = ImageFactory.createNew();
		UoW.getCurrent().commit();
		UoW.newCurrent();
		assertMapAsTextFailure(media.getId());
	}
	
	@Test public void testMapVideoAsSoundFailure() throws Exception
	{
		IMedia media = VideoFactory.createNew();
		UoW.getCurrent().commit();
		UoW.newCurrent();
		assertMapAsSoundFailure(media.getId());
	}
	@Test public void testMapVideoAsImageFailure() throws Exception
	{
		IMedia media = VideoFactory.createNew();
		UoW.getCurrent().commit();
		UoW.newCurrent();
		assertMapAsImageFailure(media.getId());
	}
	@Test public void testMapVideoAsTextFailure() throws Exception
	{
		IMedia media = VideoFactory.createNew();
		UoW.getCurrent().commit();
		UoW.newCurrent();
		assertMapAsTextFailure(media.getId());
	}
	
	@Test public void testMapSoundAsImageFailure() throws Exception
	{
		IMedia media = SoundFactory.createNew();
		UoW.getCurrent().commit();
		UoW.newCurrent();
		assertMapAsImageFailure(media.getId());
	}
	@Test public void testMapSoundAsVideoFailure() throws Exception
	{
		IMedia media = SoundFactory.createNew();
		UoW.getCurrent().commit();
		UoW.newCurrent();
		assertMapAsVideoFailure(media.getId());
	}
	@Test public void testMapSoundAsTextFailure() throws Exception
	{
		IMedia media = SoundFactory.createNew();
		UoW.getCurrent().commit();
		UoW.newCurrent();
		assertMapAsTextFailure(media.getId());
	}
	
	@Test public void testMapTextAsImageFailure() throws Exception
	{
		IMedia media = TextFactory.createNew();
		UoW.getCurrent().commit();
		UoW.newCurrent();
		assertMapAsImageFailure(media.getId());
	}
	@Test public void testMapTextAsVideoFailure() throws Exception
	{
		IMedia media = TextFactory.createNew();
		UoW.getCurrent().commit();
		UoW.newCurrent();
		assertMapAsVideoFailure(media.getId());
	}
	@Test public void testMapTextAsSoundFailure() throws Exception
	{
		IMedia media = TextFactory.createNew();
		UoW.getCurrent().commit();
		UoW.newCurrent();
		assertMapAsSoundFailure(media.getId());
	}
	
	private void assertMapAsImageFailure(long id)
	{
		try {
			ImageInputMapper.map(id);
			Assert.assertTrue("Was able to map as image", false);
		} catch (Exception e) {
			// expected
		}
	}
	private void assertMapAsVideoFailure(long id)
	{
		try {
			VideoInputMapper.map(id);
			Assert.assertTrue("Was able to map as video", false);
		} catch (Exception e) {
			// expected
		}
	}
	private void assertMapAsSoundFailure(long id)
	{
		try {
			SoundInputMapper.map(id);
			Assert.assertTrue("Was able to map as sound", false);
		} catch (Exception e) {
			// expected
		}
	}
	private void assertMapAsTextFailure(long id)
	{
		try {
			TextInputMapper.map(id);
			Assert.assertTrue("Was able to map as text", false);
		} catch (Exception e) {
			// expected
			e.printStackTrace();
		}
	}
}
