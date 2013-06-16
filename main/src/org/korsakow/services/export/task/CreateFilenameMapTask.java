/**
 * 
 */
package org.korsakow.services.export.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.korsakow.domain.Media;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.interf.IText;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.task.AbstractTask;
import org.korsakow.ide.task.TaskException;
import org.korsakow.ide.util.FileUtil;
import org.korsakow.ide.util.Triple;
import org.korsakow.services.export.Exporter;

public class CreateFilenameMapTask extends AbstractTask
{
	private final Exporter exporter;
	private final String imageDir;
	private final Collection<IImage> imageCollection; 
	private final String videoDir;
	private final Collection<IVideo> videoCollection; 
	private final String soundDir;
	private final Collection<ISound> soundCollection; 
	private final String textDir;
	private final Collection<IText> textCollection; 
	private final String fontDir;
	public CreateFilenameMapTask(
			Exporter exporter,
			String imageDir, Collection<IImage> imageCollection, 
			String videoDir, Collection<IVideo> videoCollection, 
			String soundDir, Collection<ISound> soundCollection, 
			String textDir, Collection<IText> textCollection,
			String fontDir
			)
	{
		this.exporter = exporter;
		this.imageCollection = imageCollection;
		this.imageDir = imageDir;
		this.videoCollection = videoCollection;
		this.videoDir = videoDir;
		this.soundCollection = soundCollection;
		this.soundDir = soundDir;
		this.textCollection = textCollection;
		this.textDir = textDir;
		this.fontDir = fontDir;
	}
	@Override
	public String getTitleString()
	{
		return "Analysing filenames...";
	}
	@Override
	public void runTask() throws TaskException
	{
		Map<String, String> map;
		try {
			map = createFilenameMap(imageDir, imageCollection, videoDir, videoCollection, soundDir, soundCollection, textDir, textCollection, fontDir);
		} catch (FileNotFoundException e) {
			throw new TaskException(e);
		}
//		System.out.println(map.toString().replace(",", "\n"));
		exporter.setFilenameMap(map);
	}
	public static void createMangledMap(String mediaDir, List<? extends IMedia> media,
			Map<String, Triple<String, String, String>> mangledMap, String fileExtension)
			throws FileNotFoundException
	{
		for (IMedia medium : media) {
			String filenameBase = FileUtil.getFilenameWithoutExtension(new File(medium.getFilename()).getName());
			String filenameOriginal = new File(mediaDir, filenameBase).getPath();
			String filenameMangled = new File(mediaDir, filenameBase + "-" + medium.getId()).getPath();
			Triple<String,String,String> desc = new Triple<String,String, String>(filenameOriginal, filenameMangled, fileExtension!=null?fileExtension:FileUtil.getFileExtension(medium.getFilename()));
			mangledMap.put(medium.getAbsoluteFilename(), desc);
			
			if (ResourceType.forId(medium.getType()) == ResourceType.VIDEO) {
				IVideo video = (IVideo)medium;
				if (video.getSubtitles() != null) {
					String subtitleExtension = FileUtil.getFileExtension(video.getSubtitles());
					String subtitleBase = FileUtil.getFilenameWithoutExtension(new File(video.getSubtitles()).getName());
					String subtitleOriginal = new File(Exporter.SUBTITLE_DIR, subtitleBase).getPath();
					String subtitleMangled = new File(Exporter.SUBTITLE_DIR, subtitleBase + "-" + medium.getId()).getPath();
					Triple<String,String,String> subtitleDesc = new Triple<String,String, String>(subtitleOriginal, subtitleMangled, subtitleExtension);
					mangledMap.put(Media.getAbsoluteFilename(video.getSubtitles()), subtitleDesc);
				}
			}
			if (ResourceType.forId(medium.getType()) == ResourceType.SOUND) {
				ISound sound = (ISound)medium;
				if (sound.getSubtitles() != null) {
					String subtitleExtension = FileUtil.getFileExtension(sound.getSubtitles());
					String subtitleBase = FileUtil.getFilenameWithoutExtension(new File(sound.getSubtitles()).getName());
					String subtitleOriginal = new File(Exporter.SUBTITLE_DIR, subtitleBase).getPath();
					String subtitleMangled = new File(Exporter.SUBTITLE_DIR, subtitleBase + "-" + medium.getId()).getPath();
					Triple<String,String,String> subtitleDesc = new Triple<String,String, String>(subtitleOriginal, subtitleMangled, subtitleExtension);
					mangledMap.put(Media.getAbsoluteFilename(sound.getSubtitles()), subtitleDesc);
				}
			}
		}
	}
	public static Map<String, String> createFilenameMap(
			String imageDir, Collection<IImage> imageCollection, 
			String videoDir, Collection<IVideo> videoCollection, 
			String soundDir, Collection<ISound> soundCollection, 
			String textDir, Collection<IText> textCollection,
			String fontDir
			) throws FileNotFoundException
	{
		final CreateFilenameMapTask.SortMediaById sortById = new CreateFilenameMapTask.SortMediaById();
		List<IImage> images = new ArrayList<IImage>(imageCollection);
		Collections.sort(images, sortById);
		List<IVideo> videos = new ArrayList<IVideo>(videoCollection);
		Collections.sort(videos, sortById);
		List<ISound> sounds = new ArrayList<ISound>(soundCollection);
		Collections.sort(sounds, sortById);
		List<IText> texts = new ArrayList<IText>(textCollection);
		Collections.sort(texts, sortById);
		// mangle all
		Map<String, Triple<String,String, String>> mangledMap = new LinkedHashMap<String, Triple<String,String, String>>(); // want the choice of non-mangling to be consistent
		CreateFilenameMapTask.createMangledMap(imageDir, images, mangledMap, null);
		CreateFilenameMapTask.createMangledMap(videoDir, videos, mangledMap, FileUtil.getFileExtension(Exporter.VIDEO_EXPORT_FORMAT));
		CreateFilenameMapTask.createMangledMap(soundDir, sounds, mangledMap, FileUtil.getFileExtension(Exporter.SOUND_EXPORT_FORMAT));
		CreateFilenameMapTask.createMangledMap(textDir, texts, mangledMap, "txt");
		mangledMap.put(new File(fontDir, "font.swf").getPath(), new Triple<String, String, String>("fonts", "font", "swf"));
		
		Set<String> usedNamedMap = new LinkedHashSet<String>();
		
		// demangle as many as possible
		Map<String, String> resultMap = new LinkedHashMap<String, String>();
		for (String key : mangledMap.keySet()) {
			Triple<String,String, String> desc = mangledMap.get(key);
			String filenameOriginal = desc.getFirst() + "." + desc.getThird();
			String filenameMangled = desc.getSecond() + "." + desc.getThird();
			// unique up to extension: this works well because media are split into folders by type, whereas a common scenario is something.jpg accompanied by something.mpg
			if (!usedNamedMap.contains(filenameOriginal)) {
				resultMap.put(key, filenameOriginal);
				usedNamedMap.add(filenameOriginal);
			} else {
				resultMap.put(key, filenameMangled);
			}
		}
		return resultMap;
	}
	public static final class SortMediaById implements Comparator<IMedia>
	{
		public int compare(IMedia o1, IMedia o2)
		{
			return (int)(o1.getId() - o2.getId());
		}
	}
}
