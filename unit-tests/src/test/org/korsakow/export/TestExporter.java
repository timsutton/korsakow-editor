package test.org.korsakow.export;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.korsakow.domain.ImageFactory;
import org.korsakow.domain.MediaSource;
import org.korsakow.domain.SoundFactory;
import org.korsakow.domain.TextFactory;
import org.korsakow.domain.VideoFactory;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.interf.IText;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.ide.DataRegistry;
import org.korsakow.services.export.Exporter;
import org.korsakow.services.export.task.CreateFilenameMapTask;

import test.util.BaseTestCase;

public class TestExporter extends BaseTestCase
{
	@Override
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
	}
	@Override
	@After
	public void tearDown() throws Exception
	{
		super.tearDown();
	}
	/**
	 * #944
	 * @throws Throwable
	 */
	@Test public void testGetExportRelativeUniqueFilename_filenameWithFewerThanThreeCharsAndAlreadyExists() throws Throwable
	{
		final File dataFile = File.createTempFile("mama", "dada", parentDir);
		DataRegistry.setFile(dataFile);
		DataRegistry.initUoW();
		
		Collection<IMedia> allMedia = new HashSet<IMedia>();
		final Collection<IKeyword> emptyKeywords = Collections.emptyList();
		long maxId = 0;
		Collection<IVideo> videos = Arrays.asList((IVideo)
				VideoFactory.createClean(maxId, 0L, ""+maxId++, emptyKeywords, "a/a", null),
				VideoFactory.createClean(maxId, 0L, ""+maxId++, emptyKeywords, "b/a", null),
				VideoFactory.createClean(maxId, 0L, ""+maxId++, emptyKeywords, "c/a", null)
		);
		Collection<ISound> sounds = Collections.emptyList();
		Collection<IImage> images = Collections.emptyList();
		Collection<IText> texts = Collections.emptyList();
		
		allMedia.addAll(videos);
		allMedia.addAll(sounds);
		allMedia.addAll(images);
		allMedia.addAll(texts);
	
		for (IMedia medium : allMedia) {
			final File file = new File(parentDir, medium.getFilename());
			Assert.assertTrue(file.getParentFile().exists() || file.getParentFile().mkdirs());
			Assert.assertTrue(file.getPath(), file.createNewFile());
		}
		Map<String, String> map = CreateFilenameMapTask.createFilenameMap(
				Exporter.IMAGE_DIR, images,
				Exporter.VIDEO_DIR, videos,
				Exporter.SOUND_DIR, sounds,
				Exporter.TEXT_DIR, texts,
				Exporter.FONT_DIR
		);
		System.out.println(map.toString().replace(',', '\n'));
		Set<String> set = new HashSet<String>();
		set.addAll(map.values());
		Assert.assertEquals(map.size(), set.size());
	}
	
	/**
	 * tests that if two media have the same name and one is unmanged that the same one is always chose for unmangling
	 * across multiple attempts.
	 */
	@Test public void testUnmangedNameConsistency() throws Exception
	{
		// ignored //throw new Exception("TODO");
	}
	
	@Test public void testFilenameMapPreservesDuplicatesFromDifferentDirectories() throws Exception
	{
		final File dataFile = File.createTempFile("mama", "dada", parentDir);
		DataRegistry.setFile(dataFile);
		DataRegistry.initUoW();
		
		Collection<IMedia> allMedia = new LinkedHashSet<IMedia>();
		final Collection<IKeyword> emptyKeywords = Collections.emptyList();
		long maxId = 0;
		Collection<IVideo> videos = Arrays.asList((IVideo)
				VideoFactory.createClean(maxId, 0L, ""+maxId++, emptyKeywords, "videosA/video1.mov", null),
				VideoFactory.createClean(maxId, 0L, ""+maxId++, emptyKeywords, "videosB/video1.mov", null)
		);
		Collection<ISound> sounds = Arrays.asList((ISound)
				SoundFactory.createClean(maxId, 0L, ""+maxId++, emptyKeywords, "soundsA/sound1.wav", null),
				SoundFactory.createClean(maxId, 0L, ""+maxId++, emptyKeywords, "soundsB/sound1.wav", null)
		);
		Collection<IImage> images = Arrays.asList((IImage)
				ImageFactory.createClean(maxId, 0L, ""+maxId++, emptyKeywords, "imagesA/image.png", 0L),
				ImageFactory.createClean(maxId, 0L, ""+maxId++, emptyKeywords, "imagesB/image.png", 0L)
		);
		Collection<IText> texts = Arrays.asList((IText)
				TextFactory.createClean(maxId, 0L, ""+maxId++, emptyKeywords, MediaSource.FILE, "textsA/text1.txt"),
				TextFactory.createClean(maxId, 0L, ""+maxId++, emptyKeywords, MediaSource.FILE, "textsB/text1.txt")
		);
		
		allMedia.addAll(videos);
		allMedia.addAll(sounds);
		allMedia.addAll(images);
		allMedia.addAll(texts);
	
		for (IMedia medium : allMedia) {
			final File file = new File(parentDir, medium.getFilename());
			Assert.assertTrue(file.getParentFile().exists() || file.getParentFile().mkdirs());
			Assert.assertTrue(file.createNewFile());
		}
		Map<String, String> map = CreateFilenameMapTask.createFilenameMap(
				Exporter.IMAGE_DIR, images,
				Exporter.VIDEO_DIR, videos,
				Exporter.SOUND_DIR, sounds,
				Exporter.TEXT_DIR, texts,
				Exporter.FONT_DIR
		);
		System.out.println(map.toString().replace(',', '\n'));
		Set<String> set = new LinkedHashSet<String>();
		set.addAll(map.values());
		Assert.assertEquals(map.size(), set.size());
	}
}
