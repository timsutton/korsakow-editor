package test.util;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.korsakow.domain.Settings;
import org.korsakow.domain.command.ExportFlashProjectCommand;
import org.korsakow.services.export.ExportOptions;
import org.korsakow.services.export.Exporter;
import org.korsakow.services.export.IVideoEncodingProfile;
import org.korsakow.services.export.PropertiesVideoEncodingProfile;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.ISettings;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.interf.IText;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.domain.mapper.input.SettingsInputMapper;
import org.korsakow.domain.task.ITask;
import org.korsakow.domain.task.IWorker;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.task.UIWorker;
import org.korsakow.ide.util.ResourceManager;

public class ExportHelper
{
	public static Exporter createExporter(File exportDir, IProject project) throws Exception
	{
		ISettings settings = SettingsInputMapper.find();

		Collection<IMedia> media = project.getMedia();
		Collection<ISound> sounds = new HashSet<ISound>();
		Collection<IVideo> videos = new HashSet<IVideo>();
		Collection<IImage> images = new HashSet<IImage>();
		Collection<IText> texts = new HashSet<IText>();
		for (IMedia medium : media)
		{
			switch (ResourceType.forId(medium.getType()))
			{
			case SOUND:
				sounds.add((ISound)medium);
				break;
			case VIDEO:
				videos.add((IVideo)medium);
				break;
			case IMAGE:
				images.add((IImage)medium);
				break;
			case TEXT:
				texts.add((IText)medium);
				break;
			default:
				Logger.getLogger(ExportFlashProjectCommand.class).error("media list contains unknown media: " + medium.getClass().getCanonicalName(), new Exception("just-for-stacktrace"));
				break;
			}
		}
		
		
		Exporter exporter = new Exporter();
		ExportOptions exportOptions = exporter.getExportOptions();
		synchronized (exportOptions) {
			exportOptions.overwriteExisting = false;
		}

		exporter.setExportFonts(settings.getBoolean(Settings.EXPORT_FONTS));
		exporter.setProject(project);
		exporter.setSettings(settings);
		exporter.setSnus(project.getSnus());
		exporter.setSounds(sounds);
		exporter.setImages(images);
		exporter.setVideos(videos);
		exporter.setInterfaces(project.getInterfaces());
		exporter.setTexts(texts);
		
		IVideoEncodingProfile videoEncodingProfile = new PropertiesVideoEncodingProfile(ResourceManager.getResourceStream("encodingprofiles/" + "flv_low" + ".properties"));
		exporter.setVideoEncodingProfile(videoEncodingProfile);
		
		return exporter;
	}
	public static IWorker createExportWorker(File exportDir, IProject project) throws Throwable
	{
		Exporter exporter = ExportHelper.createExporter(exportDir, project);
		List<ITask> exportTasks = exporter.createExportTasks(exportDir);
		// I suppose we could just run the tasks directly since this is a test. There's no particular reason for my using a worker
		// other than thats how the Application itself does it currently.
		IWorker worker = new UIWorker(exportTasks);
		return worker;
	}
	public static void export(File exportDir, IProject project) throws Throwable
	{
		IWorker worker = createExportWorker(exportDir, project);
		worker.execute();
		worker.waitFor();
		if (worker.getException() != null)
			throw worker.getException();
	}
}
