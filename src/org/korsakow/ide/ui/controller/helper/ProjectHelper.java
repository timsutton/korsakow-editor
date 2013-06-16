package org.korsakow.ide.ui.controller.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.Settings;
import org.korsakow.domain.Settings.AdjustFilenames;
import org.korsakow.domain.command.AbstractCommand;
import org.korsakow.domain.command.InsertProjectCommand;
import org.korsakow.domain.command.InsertSettingsCommand;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.command.UpdateProjectCommand;
import org.korsakow.domain.command.UpdateSettingsCommand;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ISettings;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.mapper.input.ImageInputMapper;
import org.korsakow.domain.mapper.input.SettingsInputMapper;
import org.korsakow.domain.mapper.input.SoundInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.ui.settings.ExportSettingsPanel;
import org.korsakow.ide.ui.settings.MovieSettingsPanel;
import org.korsakow.ide.ui.settings.ProjectSettingsPanel;
import org.korsakow.ide.ui.settings.WorkspaceSettingsPanel;
import org.korsakow.ide.util.ResourceManager;
import org.korsakow.services.export.IVideoEncodingProfile;
import org.korsakow.services.export.PropertiesVideoEncodingProfile;

public class ProjectHelper {
	private static final String VIDEO_ENCODING_PROFILES = ProjectHelper.class.getCanonicalName() + ".VIDEO_ENCODING_PROFILES";
	private static final String RULES = ProjectHelper.class.getCanonicalName() + ".RULES";
	
	public static void save(ProjectSettingsPanel view, Long projectId, Long settingsId) throws CommandException, InterruptedException
	{
		{
			Class<? extends AbstractCommand> command = projectId==null?InsertProjectCommand.class:UpdateProjectCommand.class;
			Response response = new Response();
			Request request = createProjectRequest(view, projectId);
			CommandExecutor.executeCommand(command, request, response);
			Application.getInstance().notifyResourceModified(((IProject)response.get(UpdateProjectCommand.PROJECT)));
		}
		{
			Class<? extends AbstractCommand> command = settingsId==null?InsertSettingsCommand.class:UpdateSettingsCommand.class;
			Response response = new Response();
			Request request = createSettingsRequest(view, settingsId);
			CommandExecutor.executeCommand(command, request, response);
		}
	}
	public static void edit(ProjectSettingsPanel view, IProject project, ISettings settings) throws MapperException
	{
		Long id = project!=null?project.getId():null;
		if (project != null && settings != null)
			initView(view, project, settings);
	}
	public static void initView(ProjectSettingsPanel view, IProject project, ISettings settings) throws MapperException
	{
		initView(view.getMoviePanel(), project);
		initView(view.getExportPanel(), project, settings);
		initView(view.getWorkspaceSettingsPanel(), project, settings);
		
		view.putClientProperty(RULES, project.getRules());
	}
	private static void initView(MovieSettingsPanel view, IProject project) throws MapperException
	{
		ISettings settings = SettingsInputMapper.find();
		final String similarName = settings.getBoolean(Settings.PutSimilarResourcesAtTop)?project.getName():null;
		
		view.setNameFieldText(project.getName());
		view.setMovieWidth(project.getMovieWidth());
		view.setMovieHeight(project.getMovieHeight());
		view.setBackgroundSoundChoices(ViewHelper.sort(SoundInputMapper.findAll(), ISound.class, similarName));
		view.setBackgroundSound(project.getBackgroundSound());
		view.setBackgroundSoundVolume(project.getBackgroundSoundVolume());
		view.setClickSoundChoices(ViewHelper.sort(SoundInputMapper.findAll(), ISound.class, similarName));
		view.setClickSound(project.getClickSound());
		view.setClickSoundVolume(project.getClickSoundVolume());
		view.setBackgroundImageChoices(ViewHelper.sort(ImageInputMapper.findAll(), IImage.class, similarName));
		view.setBackgroundImage(project.getBackgroundImage());
		view.setBackgroundColor(project.getBackgroundColor());
		view.setSplashScreenMediaChoices(ViewHelper.sort(ImageInputMapper.findAll(), IImage.class, similarName));
		view.setSplashScreenMedia(project.getSplashScreenMedia());
		view.setRandomLinkMode(project.getRandomLinkMode());
		view.setKeepLinksOnEmptySearch(project.getKeepLinksOnEmptySearch());
		view.setMaxLinks(project.getMaxLinks());
		
		view.repaint();
		view.revalidate();
	}
	private static void initView(ExportSettingsPanel view, IProject project, ISettings settings) throws MapperException
	{
		Map<IVideoEncodingProfile, String> profileReverseLookup = new HashMap<IVideoEncodingProfile, String>();
		Map<String, IVideoEncodingProfile> profileLookup = new HashMap<String, IVideoEncodingProfile>();
		view.putClientProperty(VIDEO_ENCODING_PROFILES, profileReverseLookup);
		List<IVideoEncodingProfile> profiles = Collections.emptyList();
		try {
			profiles = getVideoEncodingProfiles(profileLookup, profileReverseLookup);
		} catch (IOException e) {
			Logger.getLogger(ProjectHelper.class).error("", e);
		}
		view.setVideoEncodingProfileChoices(profiles);
		IVideoEncodingProfile profile = profileLookup.get(settings.getString(Settings.VideoEncodingProfile));
		if (profile == null && !profiles.isEmpty()) {
			profile = profiles.get(0);
		}
		view.setVideoEncodingProfile(profile);
		
		view.setEncodeVideoOnExport(settings.getBoolean(Settings.EncodeVideoOnExport));
		
		view.setExportVideos( settings.getBoolean(Settings.ExportVideos) );
		view.setExportImages( settings.getBoolean(Settings.ExportImages) );
		view.setExportSounds( settings.getBoolean(Settings.ExportSounds) );
		view.setExportSubtitles( settings.getBoolean(Settings.ExportSubtitles) );
		view.setExportWebFiles( settings.getBoolean(Settings.ExportWebFiles) );
		view.setExportDirectory( settings.getString(Settings.ExportDirectory) );
		
		view.repaint();
		view.revalidate();
	}
	private static void initView(WorkspaceSettingsPanel view, IProject project, ISettings settings)
	{
		AdjustFilenames adjustFilenames = AdjustFilenames.Disabled;
		try {
			adjustFilenames = AdjustFilenames.fromId(settings.getString(Settings.AdjustFilenamesOnSave));
		} catch (Exception e) {
			Logger.getLogger(ProjectHelper.class).error("", e);
		}

		switch (adjustFilenames)
		{
		case Disabled:
			view.setDontAdjustFilename(true);
			break;
		case Absolute:
			view.setAbsoluteAdjustFilename(true);
			break;
		case Relative:
			view.setRelativeAdjustFilename(true);
			break;
		case Smart:
			view.setSmartAdjustFilename(true);
			break;
		}
		
		view.setShowExperimentalWidgets(settings.getBoolean(Settings.ShowExperimentalWidgets));
		
		view.setPutSimilarAtTop(settings.getBoolean(Settings.PutSimilarResourcesAtTop));
	}
	public static Request createRequest(ISettings settings) {
		Request request = new Request();
		request.set(UpdateSettingsCommand.ID, settings.getId());
		List<String> propertyIds = new ArrayList<String>(settings.getDynamicPropertyIds());
		List<Object> propertyValues = new ArrayList<Object>();
		for (String propertyId : propertyIds)
			propertyValues.add(settings.getDynamicProperty(propertyId));
		request.set(UpdateSettingsCommand.PROPERTY_IDS, propertyIds);
		request.set(UpdateSettingsCommand.PROPERTY_VALUES, propertyValues);
		return request;
	}
	private static Request createProjectRequest(ProjectSettingsPanel view, Long id)
	{
		Request request = new Request();
		
		fillRequest(request, view.getMoviePanel(), id);
		
		request.set(UpdateProjectCommand.KEYWORDS, new ArrayList<IKeyword>());
				
		List<IRule> rules = (List<IRule>)view.getClientProperty(RULES);
		if (rules != null)
			ViewHelper.addRulesToRequest(request, rules);
		view.putClientProperty(RULES, null);
		
		return request;
	}
	private static Request createSettingsRequest(ProjectSettingsPanel view, Long id)
	{
		Request request = new Request();
		
		request.set(UpdateSettingsCommand.ID, id);
		List<String> propertyIds = (List<String>)request.get(UpdateSettingsCommand.PROPERTY_IDS);
		if (propertyIds == null) {
			propertyIds = new ArrayList<String>();
			request.set(UpdateSettingsCommand.PROPERTY_IDS, propertyIds);
		}
		List<Object> propertyValues = (List<Object>)request.get(UpdateSettingsCommand.PROPERTY_VALUES);
		if (propertyValues == null) {
			propertyValues = new ArrayList<Object>();
			request.set(UpdateSettingsCommand.PROPERTY_VALUES, propertyValues);
		}
		
		fillRequest(request, view.getExportPanel(), id);
		fillRequest(request, view.getWorkspaceSettingsPanel(), id);
		fillRequest(request, view.getWorkspaceSettingsPanel(), id);
		
		return request;
	}
	private static void fillRequest(Request request, MovieSettingsPanel view, Long id)
	{
		
		request.set(UpdateProjectCommand.ID, id);
		request.set(UpdateProjectCommand.NAME, view.getNameFieldText().trim());
		
		request.set(UpdateProjectCommand.MOVIE_WIDTH, view.getMovieWidth());
		request.set(UpdateProjectCommand.MOVIE_HEIGHT, view.getMovieHeight());
		request.set(UpdateProjectCommand.BACKGROUND_SOUND_ID, view.getBackgroundSoundId());
		request.set(UpdateProjectCommand.BACKGROUND_SOUND_VOLUME, view.getBackgroundSoundVolume());
		request.set(UpdateProjectCommand.CLICK_SOUND_ID, view.getClickSoundId());
		request.set(UpdateProjectCommand.CLICK_SOUND_VOLUME, view.getClickSoundVolume());
		request.set(UpdateProjectCommand.BACKGROUND_IMAGE_ID, view.getBackgroundImageId());
		request.set(UpdateProjectCommand.BACKGROUND_COLOR, view.getBackgroundColor());
		request.set(UpdateProjectCommand.SPLASH_SCREEN_MEDIA_ID, view.getSplashScreenMediaId());
		request.set(UpdateProjectCommand.RANDOM_LINK_MODE, view.getRandomLinkMode());
		request.set(UpdateProjectCommand.KEEP_LINKS, view.getKeepLinksOnEmptySearch());
		request.set(UpdateProjectCommand.MAX_LINKS, view.getMaxLinks());
	}
	private static void fillRequest(Request request, ExportSettingsPanel view, Long id)
	{
		List<String> propertyIds = (List<String>)request.get(UpdateSettingsCommand.PROPERTY_IDS);
		List<Object> propertyValues = (List<Object>)request.get(UpdateSettingsCommand.PROPERTY_VALUES);

		Map<Object, String> profileLookup = (Map<Object, String>)view.getClientProperty(VIDEO_ENCODING_PROFILES);
		view.putClientProperty(VIDEO_ENCODING_PROFILES, null);
		propertyIds.add(Settings.VideoEncodingProfile);
		propertyValues.add(profileLookup.get(view.getVideoEncodingProfile()));

		propertyIds.add(Settings.EncodeVideoOnExport);
		propertyValues.add(Boolean.valueOf(view.getEncodeVideoOnExport()));
		
		propertyIds.add(Settings.ExportVideos);
		propertyValues.add(view.getExportVideos());
		propertyIds.add(Settings.ExportSounds);
		propertyValues.add(view.getExportSounds());
		propertyIds.add(Settings.ExportImages);
		propertyValues.add(view.getExportImages());
		propertyIds.add(Settings.ExportSubtitles);
		propertyValues.add(view.getExportSubtitles());
		propertyIds.add(Settings.ExportWebFiles);
		propertyValues.add(view.getExportWebFiles());
		propertyIds.add(Settings.ExportDirectory);
		propertyValues.add(view.getExportDirectory().trim());
	}
	public static void fillRequest(Request request, WorkspaceSettingsPanel view, Long id)
	{
		List<String> propertyIds = (List<String>)request.get(UpdateSettingsCommand.PROPERTY_IDS);
		List<Object> propertyValues = (List<Object>)request.get(UpdateSettingsCommand.PROPERTY_VALUES);
		
		AdjustFilenames adjustFilenames = AdjustFilenames.Disabled;
		if (view.getDontAdjustFilename()) {
			adjustFilenames = AdjustFilenames.Disabled;
		} else if (view.getAbsoluteAdjustFilename()) {
			adjustFilenames = AdjustFilenames.Absolute;
		} else if (view.getRelativeAdjustFilename()) {
			adjustFilenames = AdjustFilenames.Relative;
		} else if (view.getSmartAdjustFilename()) {
			adjustFilenames = AdjustFilenames.Smart;
		}
		
		propertyIds.add(Settings.AdjustFilenamesOnSave);
		propertyValues.add(adjustFilenames.getId());
		
		propertyIds.add(Settings.ShowExperimentalWidgets);
		propertyValues.add(view.getShowExperimentalWidgets());
		
		propertyIds.add(Settings.PutSimilarResourcesAtTop);
		propertyValues.add(view.getPutSimilarAtTop());
	}
	public static List<IVideoEncodingProfile> getVideoEncodingProfiles() throws IOException
	{
		Map<IVideoEncodingProfile, String> profileReverseLookup = new HashMap<IVideoEncodingProfile, String>();
		Map<String, IVideoEncodingProfile> profileLookup = new HashMap<String, IVideoEncodingProfile>();
		return getVideoEncodingProfiles(profileLookup, profileReverseLookup);
	}
	public static List<IVideoEncodingProfile> getVideoEncodingProfiles(Map<String, IVideoEncodingProfile> profileLookup, Map<IVideoEncodingProfile, String> profileReverseLookup) throws IOException
	{
		String[] profileFiles = ResourceBundle.getBundle("MyResources").getString("encodingProfiles").split(",");
		List<IVideoEncodingProfile> profiles = new ArrayList<IVideoEncodingProfile>();
		for (String profileFile : profileFiles) {
			Properties props = new Properties();
			props.load(ResourceManager.getResourceStream("encodingprofiles/" + profileFile + ".properties"));
			IVideoEncodingProfile profile = new PropertiesVideoEncodingProfile(props);
			profiles.add(profile);
			profileLookup.put(profileFile, profile);
			profileReverseLookup.put(profile, profileFile);
		}
		return profiles;
	}
}
