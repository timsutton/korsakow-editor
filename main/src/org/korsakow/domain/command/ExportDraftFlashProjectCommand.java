package org.korsakow.domain.command;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.ISettings;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.interf.IText;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.domain.mapper.input.MediaInputMapper;
import org.korsakow.domain.mapper.input.ProjectInputMapper;
import org.korsakow.domain.mapper.input.SettingsInputMapper;
import org.korsakow.domain.task.ITask;
import org.korsakow.domain.task.IWorker;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.task.AbstractTask;
import org.korsakow.ide.task.TaskException;
import org.korsakow.ide.task.UIWorker;
import org.korsakow.ide.util.Platform;
import org.korsakow.ide.util.ResourceManager;
import org.korsakow.services.export.ExportException;
import org.korsakow.services.export.Exporter;
import org.korsakow.services.export.IVideoEncodingProfile;
import org.korsakow.services.export.PropertiesVideoEncodingProfile;


public class ExportDraftFlashProjectCommand extends AbstractCommand{


	public static final String PROJECT_ID = ExportFlashProjectCommand.PROJECT_ID;
	public static final String EXPORTER = ExportFlashProjectCommand.EXPORTER;
	public static final String WORKER = ExportFlashProjectCommand.WORKER;
	public static final String EXPORT_FILE = "export_file";

	public ExportDraftFlashProjectCommand(Helper request, Helper response) {
		super(request, response);
	}

	public void execute() throws CommandException {

		try {
		    
			long projectId = request.getLong(PROJECT_ID);
			IProject project = ProjectInputMapper.map(projectId);
			ISettings settings = SettingsInputMapper.find();
			
			final String tmpdir;
			/*
			 * On OSX 10.6, java.io.tmpdir returns a directory like /var/folders/l6/l6UDkdbcFoyLmvTRA4UYYU+++TI/-Tmp-/
			 * Which always has + in it. This breaks the flash player, even if we never specify the full url explicitly.
			 * This is due to + being a special symbol in urls, the flash player always converts to space.
			 * (I tested and confirmed this)
			 */
			if (Platform.isMacOS())
				tmpdir = "/tmp";
			else
				tmpdir = System.getProperty("java.io.tmpdir");
			
			/**
			 * This is a flawed strategy since there is technically no guarantee this folder isn't already taken by some
			 * other process (though it is unlikely).
			 * A better solution would be to create true tempdir (File.createTemp...) and remember in some preference
			 * what it was, perhaps storing the uuid of the project in a hidden file as a means of verifying that the tempdir
			 * hasn't been allocated to some other process since last use.
			 */
			File exportDir = new File(tmpdir, "Korsakow_Draft_" + project.getUUID());
			System.out.println("\n\n\n"+exportDir);
			exportDir.deleteOnExit();

			String indexFilename = "index.html";
			
			Exporter exporter = new Exporter();
			
			IWorker exportWorker = new UIWorker();
			
			// we want to do the setup in a task since its lengthy
			// however we can't generate the other tasks from the exporter until that's done
			// so the setup tasks does that and then adds them to the worker
			exportWorker.addTask(new SetupExporterTask(exportWorker, exporter, exportDir, indexFilename, project, settings));
			
			response.set(WORKER, exportWorker);
			response.set(EXPORTER, exporter);
			response.set(EXPORT_FILE, new File(exportDir, indexFilename));
			
		} catch (Exception e) {
			throw new CommandException(e);
		}

	}
	
	private static class SetupExporterTask extends AbstractTask
	{
		private final IWorker uiWorker;
		private final String indexFilename;
		private final File exportDir;
		private final Exporter exporter;
		private final IProject project;
		private final ISettings settings;
		public SetupExporterTask(IWorker uiWorker, Exporter exporter, File exportDir, String indexFilename, IProject project, ISettings settings)
		{
			this.uiWorker = uiWorker;
			this.exporter = exporter;
			this.exportDir = exportDir;
			this.indexFilename = indexFilename;
			this.project = project;
			this.settings = settings;
		}

		@Override
		public void runTask() throws TaskException, InterruptedException {
			UoW.newCurrent();
			
			Collection<IMedia> media;
			try {
				media = MediaInputMapper.findReferencedMedia();
			} catch (XPathExpressionException e) {
				throw new TaskException(e);
			} catch (MapperException e) {
				throw new TaskException(e);
			} catch (SQLException e) {
				throw new TaskException(e);
			}
			Collection<ISound> sounds = new HashSet<ISound>();
			Collection<IVideo> videos = new HashSet<IVideo>();
			Collection<IImage> images = new HashSet<IImage>();
			Collection<IText> texts = new HashSet<IText>();
			int i = 0;
			int size = media.size();
			long before = System.currentTimeMillis();
			for (IMedia medium : media)
			{
				fireProgressChanged(0, ++i*100/size);
				fireDisplayStringChanged("", medium.getName());
				
				ResourceType resourceType = ResourceType.forId(medium.getType());
				switch (resourceType)
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
			long after = System.currentTimeMillis();
			System.out.println("Determination: " + (after-before)/1000 + "\t" + media.size());
			
			
			exporter.setExportFonts(false);
			exporter.setIndexFilename(indexFilename);
			exporter.setProject(project);
			exporter.setSettings(settings);
			exporter.setSnus(project.getSnus());
			exporter.setSounds(sounds);
			exporter.setImages(images);
			exporter.setVideos(videos);
			exporter.setInterfaces(project.getInterfaces());
			exporter.setTexts(texts);
			
			IVideoEncodingProfile videoEncodingProfile;
			try {
				videoEncodingProfile = new PropertiesVideoEncodingProfile(ResourceManager.getResourceStream("encodingprofiles/" + "flv_low" + ".properties"));
			} catch (IOException e) {
				throw new TaskException(e);
			}
			exporter.setVideoEncodingProfile(videoEncodingProfile);

			fireDisplayStringChanged("", LanguageBundle.getString("export.task.initializingtasks"));
			List<ITask> exportTasks;
			try {
				exportTasks = exporter.createExportTasks(exportDir);
			} catch (IOException e) {
				throw new TaskException(e);
			} catch (ExportException e) {
				throw new TaskException(e);
			}
			uiWorker.addTasks(exportTasks);
		}
		@Override
		public String getTitleString()
		{
			return LanguageBundle.getString("export.task.determiningmediatoexport");
		}
	}
}
