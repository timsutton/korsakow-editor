package org.korsakow.services.export;

import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
import org.korsakow.domain.Image;
import org.korsakow.domain.Media;
import org.korsakow.domain.Settings;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.ISettings;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.interf.IText;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.domain.task.ITask;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.task.DelegateTask;
import org.korsakow.ide.task.TaskException;
import org.korsakow.ide.util.FileUtil;
import org.korsakow.ide.util.Util;
import org.korsakow.services.encoders.font.FontFormat;
import org.korsakow.services.encoders.image.ImageFormat;
import org.korsakow.services.encoders.sound.SoundFormat;
import org.korsakow.services.encoders.video.VideoCodec;
import org.korsakow.services.export.task.CopyPlayerExportTask;
import org.korsakow.services.export.task.CreateFilenameMapTask;
import org.korsakow.services.export.task.FontExportTask;
import org.korsakow.services.export.task.ImageExportTask;
import org.korsakow.services.export.task.SoundExportTask;
import org.korsakow.services.export.task.SubtitleExportTask;
import org.korsakow.services.export.task.TextExportTask;
import org.korsakow.services.export.task.ThumbnailExportTask;
import org.korsakow.services.export.task.VideoExportTask;
import org.korsakow.services.export.task.XMLExportTask;

public class Exporter
{
	public static final String DATA_DIR = "data";
	public static final String FLASH_PLAYER_ROOT = "players/flash/";
	public static final String FLASH_PLAYER_RESOURCE_INDEX = "index.html";
	public static final String FLASH_PLAYER_RESOURCE_CSS = "data/css/style.css";
	public static final String[] FLASH_PLAYER_RESOURES = {
		"data/KorsakowPlayer.swf",
		"data/Embed.swf",
		"data/DebugWindow.swf",
		"data/swf/expressInstall.swf",
		"data/js/swfobject.js",
		"data/js/swfaddress.js",
		"data/js/jquery-1.4.2.min.js",
		"data/js/embed.js",
		"data/js/korsakow.js",
		"data/css/cross.png",
		"data/css/embed.css",
	};
	public static final SoundFormat SOUND_EXPORT_FORMAT = SoundFormat.MP3;
	public static final VideoCodec VIDEO_EXPORT_FORMAT = VideoCodec.FLV;
	public static final FontFormat FONT_EXPORT_FORMAT = FontFormat.SWF;
	
	public static final String VIDEO_DIR = "video";
	public static final String TEXT_DIR = "text";
	public static final String IMAGE_DIR = "image";
	public static final String SOUND_DIR = "sound";
	public static final String FONT_DIR = "font";
	public static final String SUBTITLE_DIR = "subtitle";
	public static final String THUMBNAIL_DIR = "thumbnail";
		
	
	private final ExportOptions exportOptions = new ExportOptions();
	private Map<String, String> filenamemap;
	private File rootDir;

	private ISettings settings;
	private IProject projectToExport;
	private String indexFilename = FLASH_PLAYER_RESOURCE_INDEX;
	private Collection<ISnu> snusToExport = new HashSet<ISnu>();
	private Collection<ISound> soundsToExport = new HashSet<ISound>();
	private Collection<IImage> imagesToExport = new HashSet<IImage>();
	private Collection<IVideo> videosToExport = new HashSet<IVideo>();
	private Collection<IInterface> interfacesToExport = new HashSet<IInterface>();
	private Collection<IText> textsToExport = new HashSet<IText>();
	private Collection<Font> fontsToExport = new HashSet<Font>();
	private boolean exportFonts = false;
	private boolean randomizeDataFilename = false;
	private IVideoEncodingProfile videoEncodingProfile;
	
	public Exporter()
	{
	}
	
	/**
	 * CAVEAT: must synchronize on the returned instance
	 * @return
	 */
	public ExportOptions getExportOptions()
	{
		return exportOptions;
	}
	public void setFilenameMap(Map<String, String> map)
	{
		filenamemap = map;
	}
	private String getFilename(String key) throws ExportException
	{
		if (!filenamemap.containsKey(key))
			throw new ExportException("unexpected error: filename '" + key + "' not in map", rootDir);
		return filenamemap.get(key);
	}
	
	public void setRandomizeDataFilename(boolean randomize)
	{
		randomizeDataFilename = randomize;
	}
	public void setExportFonts(boolean exportFonts)
	{
		this.exportFonts = exportFonts;
	}
	public void setIndexFilename(String indexFilename)
	{
		this.indexFilename = indexFilename;
	}
	public void setProject(IProject proj)
	{
		projectToExport = proj;
	}
	public void setSettings(ISettings settings)
	{
		this.settings = settings;
	}
	public void setSnus(Collection<ISnu> snus)
	{
		snusToExport = new HashSet<ISnu>(snus);
	}
	public void setSounds(Collection<ISound> sounds)
	{
		soundsToExport = new HashSet<ISound>(sounds);
	}
	public void setImages(Collection<IImage> images)
	{
		imagesToExport = new HashSet<IImage>(images);
	}
	public void setVideos(Collection<IVideo> videos)
	{
		videosToExport = new HashSet<IVideo>(videos);
	}
	public void setInterfaces(Collection<IInterface> interfaces)
	{
		interfacesToExport = new HashSet<IInterface>(interfaces);
	}
	public void setTexts(Collection<IText> texts)
	{
		textsToExport = new HashSet<IText>(texts);
	}
	
	public IVideoEncodingProfile getVideoEncodingProfile()
	{
		return videoEncodingProfile;
	}

	public void setVideoEncodingProfile(IVideoEncodingProfile videoEncodingProfile)
	{
		this.videoEncodingProfile = videoEncodingProfile;
	}

	public List<ITask> createExportTasks(File rootDir) throws IOException, ExportException, InterruptedException
	{
		//List<IVideo> videos = Command.listVideo();
		IProject project = projectToExport;
		
		FileUtil.mkdirs(rootDir);
		
		File dataDir = new File(rootDir, DATA_DIR);
		FileUtil.mkdirs(dataDir);
		
		try {
			fontsToExport = calculateFontsToExport(exportFonts, textsToExport, interfacesToExport);
		} catch (Exception e) {
			throw new ExportException(e, dataDir);
		}
		
		String dataPath = "project.xml";
		if (randomizeDataFilename)
			dataPath = String.format("project%8d.xml", new Random().nextInt());
		
		ITask createFilenameMapTask = new CreateFilenameMapTask(this, IMAGE_DIR, imagesToExport, VIDEO_DIR, videosToExport, SOUND_DIR, soundsToExport, TEXT_DIR, textsToExport, FONT_DIR);
		try {
			createFilenameMapTask.run();
		} catch (TaskException e) {
			ExportException ee;
			if (e.getCause() instanceof ExportException)
				throw ee = (ExportException)e.getCause();
			else
				ee = new ExportException(e.getCause(), rootDir);
			throw ee;
		}		
		
		List<ITask> exportTasks = new ArrayList<ITask>();
		exportTasks.addAll(createFontExportTasks(this, dataDir, fontsToExport));
		exportTasks.addAll(createTextExportTasks(exportOptions, dataDir, textsToExport));
		if (settings.getBoolean(Settings.ExportImages))
			exportTasks.addAll(createImageExportTasks(exportOptions, dataDir, imagesToExport));
		if (settings.getBoolean(Settings.ExportSounds))
			exportTasks.addAll(createSoundExportTasks(exportOptions, dataDir, soundsToExport));

		Dimension maxVideoSize = calculateMaxVideoSize(project, interfacesToExport);
		Logger.getLogger(Exporter.class).info(String.format("Video Encoding Profile: %s", videoEncodingProfile.getName()));
		if (settings.getBoolean(Settings.ExportVideos))
			exportTasks.addAll(createVideoExportTasks(exportOptions, dataDir, videoEncodingProfile, maxVideoSize, videosToExport));
		if (settings.getBoolean(Settings.ExportSubtitles))
			exportTasks.addAll(createSubtitleExportTasks(exportOptions, dataDir, videosToExport));
		
		// for hackish reasons we check the ExportImages settings inside the createThumbnail task
		exportTasks.addAll(createThumbnailExportTasks(exportOptions, rootDir, maxVideoSize, snusToExport));
		
		if (settings.getBoolean(Settings.ExportWebFiles))
			exportTasks.add(new CopyPlayerExportTask(rootDir, indexFilename, dataPath, project));

		exportTasks.addAll(createXMLExportTasks(dataPath, project, snusToExport, textsToExport, imagesToExport, soundsToExport, videosToExport, interfacesToExport, fontsToExport, dataDir, filenamemap));
		
		return exportTasks;
	}
	public Map<String, String> getFilenameMap()
	{
		return filenamemap;
	}
	/**
	 * Calculates the largest dimension a video would play at. Currently this means the
	 * largest size of any video playing widget.
	 * 
	 * @param interfacesToExport
	 * @return
	 */
	private static Dimension calculateMaxVideoSize(IProject projectToExport, Collection<IInterface> interfacesToExport)
	{
		Dimension d = new Dimension(0, 0);
		for (IInterface interf : interfacesToExport)
		{
			Collection<IWidget> widgets = interf.getWidgets();
			for (IWidget widget : widgets)
			{
				if (widget.getWidgetId().equals(WidgetType.MainMedia.getId()) ||
					widget.getWidgetId().equals(WidgetType.MediaArea.getId()) ||
					widget.getWidgetId().equals(WidgetType.SnuAutoLink.getId()) ||
					widget.getWidgetId().equals(WidgetType.SnuFixedLink.getId()))
				{
					if (widget.getWidth() > d.width)
						d.width = widget.getWidth();
					if (widget.getHeight() > d.height)
						d.height = widget.getHeight();
				}
			}
		}
		if (d.width == 0)
			d.width = projectToExport.getMovieWidth();
		if (d.height == 0)
			d.height = projectToExport.getMovieHeight();
		return d;
	}
	private static Collection<Font> calculateFontsToExport(boolean exportFonts, Collection<IText> textsToExport, Collection<IInterface> interfacesToExport) throws Exception
	{
		Collection<Font> fontsToExport = new HashSet<Font>();
		if (!exportFonts)
			return fontsToExport;
		
		for (IText text : textsToExport)
			fontsToExport.addAll(text.getFonts());
		for (IInterface interf : interfacesToExport) {
			for (IWidget widget : interf.getWidgets()) {
				if (widget.getDynamicProperty("fontFamily") != null) {
					Font font = new Font((String)widget.getDynamicProperty("fontFamily"), Font.PLAIN, widget.getDynamicProperty("fontSize")!=null?(Integer)widget.getDynamicProperty("fontSize"):10);
					fontsToExport.add(font);
				}
			}
		}
		return fontsToExport;
	}
	private static List<ITask> createXMLExportTasks(String dataPath, IProject project, Collection<ISnu> snusToExport, Collection<IText> textsToExport, Collection<IImage> imagesToExport, Collection<ISound> soundsToExport, Collection<IVideo> videosToExport, Collection<IInterface> interfacesToExport, Collection<Font> fontsToExport, File rootDir, Map<String, String> filenamemap) throws IOException
	{
		List<ITask> tasks = new ArrayList<ITask>();
		XMLExportTask task = new XMLExportTask(dataPath, project, snusToExport, textsToExport, imagesToExport, soundsToExport, videosToExport, interfacesToExport, fontsToExport, rootDir, filenamemap);
		tasks.add(task);
		return Util.list(ITask.class, new DelegateTask(LanguageBundle.getString("export.task.processingproject"), tasks));
	}
	private List<ITask> createVideoExportTasks(ExportOptions options, File rootDir, IVideoEncodingProfile encodingProfile, Dimension maxVideoSize, Collection<IVideo> videosToExport) throws IOException, ExportException
	{
		Set<String> alreadyTasked = new HashSet<String>();
		List<ITask> tasks = new ArrayList<ITask>();
		for (IVideo video : videosToExport)
		{
			String dest = getFilename(video.getAbsoluteFilename());
			if (!alreadyTasked.add(dest))
				continue;
			File destFile = new File(rootDir.getAbsolutePath() + File.separatorChar + dest);
			destFile.getParentFile().mkdirs();
			VideoExportTask task = new VideoExportTask(options, encodingProfile, video, destFile, rootDir);
			if (maxVideoSize != null)
				task.setMaxSize(maxVideoSize.width, maxVideoSize.height);
			tasks.add(task);
		}
		return Util.list(ITask.class, new DelegateTask(LanguageBundle.getString("export.task.encodingvideo"), tasks));
	}
	private List<ITask> createSubtitleExportTasks(ExportOptions options, File rootDir, Collection<IVideo> videosToExport) throws IOException, ExportException
	{
		Set<String> alreadyTasked = new HashSet<String>();
		List<ITask> tasks = new ArrayList<ITask>();
		for (IVideo video : videosToExport)
		{
			if (video.getSubtitles() == null)
				continue;
			String subtitles = Media.getAbsoluteFilename(video.getSubtitles());
			if (!alreadyTasked.add(subtitles))
				continue;
			File destFile = new File(rootDir.getAbsolutePath() + File.separatorChar + getFilename(subtitles));
			destFile.getParentFile().mkdirs();
			SubtitleExportTask task = new SubtitleExportTask(options, destFile, new File(subtitles));
			tasks.add(task);
		}
		return Util.list(ITask.class, new DelegateTask(LanguageBundle.getString("export.task.encodingvideo"), tasks));
	}
    private List<ITask> createThumbnailExportTasks(ExportOptions options, File rootDir, Dimension maxVideoSize, Collection<ISnu> snusToExport) throws IOException, ExportException
    {
        List<ITask> tasks = new ArrayList<ITask>();
        for (ISnu snu : snusToExport)
        {
                final IMedia mainMedia = snu.getMainMedia();
                String mainMediaFile = getFilename(mainMedia.getAbsoluteFilename());
                String base = FileUtil.getFilenameWithoutExtension(new File(mainMediaFile).getName());
                String ext = FileUtil.getFileExtension(ImageFormat.JPG);
                String dest = base + "." + ext;
                File destFile = new File(rootDir, Exporter.DATA_DIR + File.separatorChar + Exporter.THUMBNAIL_DIR + File.separatorChar + dest);
                destFile.getParentFile().mkdirs();
                ThumbnailExportTask task = new ThumbnailExportTask(options, mainMedia, destFile, rootDir);
                if (maxVideoSize != null)
                        task.setMaxSize(maxVideoSize.width, maxVideoSize.height);
                
                // we do all the other work and check this setting here because of how hackish thumbs are
                // and we still want them to appear in the xml!
        		if (settings.getBoolean(Settings.ExportImages))
        			tasks.add(task);
                
                // hackish: temporarily setting the thumbnail this way
                if (filenamemap.containsKey(destFile.getAbsoluteFile()))
                        throw new ExportException(
                                        "Internal Error: filename already in use "
                                                        + destFile.getAbsolutePath(), rootDir);
                filenamemap.put(destFile.getAbsolutePath(), new File(Exporter.THUMBNAIL_DIR + File.separatorChar + dest).getPath());
                IImage thumbnail = new Image(DataRegistry.getMaxId(), 0);
                thumbnail.setFilename(destFile.getPath());
                thumbnail.setName(destFile.getName());
                snu.setThumbnail(thumbnail);
        }
        return Util.list(ITask.class, new DelegateTask(LanguageBundle.getString("export.task.encodingthumbnail"), tasks));
    }
	private static List<ITask> createFontExportTasks(Exporter exporter, File rootDir, Collection<Font> fontsToExport) throws ExportException
	{
		List<ITask> tasks = new ArrayList<ITask>();
		if (!exporter.exportFonts)
			return tasks;
		if (!fontsToExport.isEmpty()) {
			String dest = exporter.getFilename("font.swf");
			File destFile = new File(rootDir.getAbsolutePath() + File.separatorChar + dest);
			destFile.getParentFile().mkdirs();
			//
			tasks.add(new FontExportTask(fontsToExport, destFile));
		}
		return Util.list(ITask.class, new DelegateTask(LanguageBundle.getString("export.task.encodingfonts"), tasks));
	}
	private List<ITask> createTextExportTasks(ExportOptions options, File rootDir, Collection<IText> textsToExport) throws IOException, ExportException
	{
		List<ITask> tasks = new ArrayList<ITask>();
		for (IText text : textsToExport)
		{
			String dest = getFilename(text.getAbsoluteFilename());
			File destFile = new File(rootDir.getAbsolutePath() + File.separatorChar + dest);
			destFile.getParentFile().mkdirs();
			tasks.add(new TextExportTask(options, text, destFile));
		}
		return Util.list(ITask.class, new DelegateTask(LanguageBundle.getString("export.task.encodingtext"), tasks));
	}
	private List<ITask> createImageExportTasks(ExportOptions options, File rootDir, Collection<IImage> imagesToExport) throws IOException, ExportException
	{
		List<ITask> tasks = new ArrayList<ITask>();
		for (IImage image : imagesToExport)
		{
			String dest = getFilename(image.getAbsoluteFilename());
			File destFile = new File(rootDir.getAbsolutePath() + File.separatorChar + dest);
			destFile.getParentFile().mkdirs();
			tasks.add(new ImageExportTask(options, image, destFile));
		}
		return Util.list(ITask.class, new DelegateTask(LanguageBundle.getString("export.task.encodingimage"), tasks));
	}
	private List<ITask> createSoundExportTasks(ExportOptions options, File rootDir, Collection<ISound> soundsToExport) throws IOException, ExportException
	{
		List<ITask> tasks = new ArrayList<ITask>();
		for (ISound sound : soundsToExport)
		{
			String dest = getFilename(sound.getAbsoluteFilename());
			File destFile = new File(rootDir.getAbsolutePath() + File.separatorChar + dest);
			destFile.getParentFile().mkdirs();
			File subtitleFile = null;
			if (sound.getSubtitles() != null)
				subtitleFile = new File(rootDir.getAbsolutePath() + File.separatorChar + getFilename(Media.getAbsoluteFilename(sound.getSubtitles())));
			tasks.add(new SoundExportTask(options, sound, destFile, subtitleFile));
		}
		return Util.list(ITask.class, new DelegateTask(LanguageBundle.getString("export.task.encodingsound"), tasks));
	}

}
