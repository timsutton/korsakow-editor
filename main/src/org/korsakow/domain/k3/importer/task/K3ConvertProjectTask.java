/**
 * 
 */
package org.korsakow.domain.k3.importer.task;

import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.dsrg.soenea.service.Registry;
import org.korsakow.domain.Image;
import org.korsakow.domain.ImageFactory;
import org.korsakow.domain.InterfaceFactory;
import org.korsakow.domain.KeywordFactory;
import org.korsakow.domain.ProjectFactory;
import org.korsakow.domain.RuleFactory;
import org.korsakow.domain.Settings;
import org.korsakow.domain.SettingsFactory;
import org.korsakow.domain.SnuFactory;
import org.korsakow.domain.Sound;
import org.korsakow.domain.SoundFactory;
import org.korsakow.domain.VideoFactory;
import org.korsakow.domain.WidgetFactory;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ISettings;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.domain.k3.K3Interface;
import org.korsakow.domain.k3.K3Project;
import org.korsakow.domain.k3.K3ProjectSettings;
import org.korsakow.domain.k3.K3Rule;
import org.korsakow.domain.k3.K3Snu;
import org.korsakow.domain.k3.code.K3Lexeme;
import org.korsakow.domain.k3.code.K3RuleParser;
import org.korsakow.domain.k3.code.K3Symbol;
import org.korsakow.domain.k3.code.RuleParserException;
import org.korsakow.domain.k3.importer.K3ImportException;
import org.korsakow.domain.k3.importer.K3ImportReport;
import org.korsakow.domain.k3.importer.K3Importer;
import org.korsakow.domain.k3.importer.exception.K3InvalidRuleException;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.resources.widget.PreviewTextEffect;
import org.korsakow.ide.resources.widget.PreviewTextMode;
import org.korsakow.ide.resources.widget.WidgetPersistAction;
import org.korsakow.ide.resources.widget.WidgetPersistCondition;
import org.korsakow.ide.rules.RuleType;
import org.korsakow.ide.task.TaskException;
import org.korsakow.ide.ui.interfacebuilder.widget.InsertText;
import org.korsakow.ide.ui.interfacebuilder.widget.MainMedia;
import org.korsakow.ide.ui.interfacebuilder.widget.SnuAutoLink;
import org.korsakow.ide.ui.interfacebuilder.widget.SnuAutoMultiLink;
import org.korsakow.ide.ui.interfacebuilder.widget.Subtitles;
import org.korsakow.ide.util.FileUtil;
import org.korsakow.ide.util.StrongReference;

public class K3ConvertProjectTask extends K3ImportTask
{
	private final K3ImportReport report;
	private final StrongReference<K3Project> k3Project;
	private final StrongReference<IProject> project;
	private final StrongReference<K3Interface> newInterfaceRef;
	private IInterface interf;
	
	public K3ConvertProjectTask(File dataDir, File databaseFile, File interfaceFile, StrongReference<K3Project> k3Project, K3ImportReport report, StrongReference<IProject> project, StrongReference<K3Interface> interfaceRef)
	{
		super(dataDir, databaseFile, interfaceFile);
		this.report = report;
		this.project = project;
		newInterfaceRef = interfaceRef;
		this.k3Project = k3Project;
	}
	@Override
	public String getTitleString()
	{
		return LanguageBundle.getString("import.task.convertproject");
	}
	@Override
	public void runTask() throws TaskException
	{
		try {
			importProject(k3Project.get());
		} catch (K3ImportException e) {
			throw new TaskException(e);
		}
	}
	/**
	 * K3 uses a map from [1,255] to [R,G,B]. Its a custom algo.
	 * 
	 * @param c an integer [1,255]
	 * @return base 10 string of the rgb
	 */
	private static String convertK3Color(Integer c)
	{
		if (c == null) return null;
		return ""+c; // todo
	}
	private static void centerToStage(IInterface interf, K3ProjectSettings k3Settings)
	{
		// center the interface on the stage
		Rectangle bounds = interf.getBounds();
		int dx = (k3Settings.stageWidth - bounds.width)/2 - bounds.x;
		int dy = (k3Settings.stageHeight - bounds.height)/2 - bounds.y;
		for (IWidget widget : interf.getWidgets()) {
			widget.setX(widget.getX() + dx);
			widget.setY(widget.getY() + dy);
		}
	}
	private IInterface get3LinkInterface() throws K3ImportException
	{
		if (interf != null)
			return interf;
		
		K3ProjectSettings k3Settings = k3Project.get().settings;
		
		IWidget widget;
		Collection<IWidget> widgets = new HashSet<IWidget>();
		widgets.add(widget=WidgetFactory.createNew(WidgetType.MainMedia.getDisplayName(),
				new ArrayList<IKeyword>(),
				WidgetType.MainMedia.getId(), 
				WidgetPersistCondition.Never, 
				WidgetPersistAction.Replace, 
				0, 0, 
				k3Settings.videoWidth, k3Settings.videoHeight
		));
		for (String id : new MainMedia().getDynamicPropertyIds()) {
			widget.setDynamicProperty(id, new MainMedia().getDynamicProperty(id));
		}
		
		for (int i = 0; i < 3; ++i) {
			widgets.add(widget=WidgetFactory.createNew(WidgetType.SnuAutoLink.getDisplayName(),
					new ArrayList<IKeyword>(),
					WidgetType.SnuAutoLink.getId(), 
					WidgetPersistCondition.Never, 
					WidgetPersistAction.Replace, 
					i*k3Settings.videoWidth/3, k3Settings.videoHeight + 25,
					k3Settings.videoWidth/3, k3Settings.videoHeight/3
			));
			for (String id : new SnuAutoLink().getDynamicPropertyIds()) {
				widget.setDynamicProperty(id, new SnuAutoLink().getDynamicProperty(id));
			}
			widget.setDynamicProperty("index", i);
			widget.setDynamicProperty("fontColor", convertK3Color(k3Settings.previewTextColor));
			widget.setDynamicProperty("fontFamily", (k3Settings.previewTextFontFamily));
			widget.setDynamicProperty("fontSize", (k3Settings.previewTextSize));
			widget.setDynamicProperty("previewTextMode", PreviewTextMode.MOUSEOVER.getId());
			widget.setDynamicProperty("previewTextEffect", PreviewTextEffect.ANIMATE.getId());
		}
				
		widgets.add(widget=WidgetFactory.createNew(WidgetType.Subtitles.getDisplayName(),
				new ArrayList<IKeyword>(),
				WidgetType.Subtitles.getId(), 
				WidgetPersistCondition.Never, 
				WidgetPersistAction.Replace, 
				0, k3Settings.videoHeight, 
				k3Settings.videoWidth, 25
		));
		for (String id : new Subtitles().getDynamicPropertyIds()) {
			widget.setDynamicProperty(id, new Subtitles().getDynamicProperty(id));
		}
		widget.setDynamicProperty("fontColor", convertK3Color(k3Settings.subtitleTextColor));
		widget.setDynamicProperty("fontFamily", (k3Settings.subtitleTextFontFamily));
		widget.setDynamicProperty("fontSize", (k3Settings.subtitleTextSize));
		
		widgets.add(widget=WidgetFactory.createNew(WidgetType.InsertText.getDisplayName(),
				new ArrayList<IKeyword>(),
				WidgetType.InsertText.getId(), 
				WidgetPersistCondition.Never, 
				WidgetPersistAction.Replace, 
				0, 0, 
				k3Settings.videoWidth, k3Settings.videoHeight
		));
		for (String id : new InsertText().getDynamicPropertyIds()) {
			widget.setDynamicProperty(id, new InsertText().getDynamicProperty(id));
		}
		widget.setDynamicProperty("fontColor", convertK3Color(k3Settings.insertTextColor));
		widget.setDynamicProperty("fontFamily", (k3Settings.insertTextFontFamily));
		widget.setDynamicProperty("fontSize", (k3Settings.insertTextSize));
		
		interf = InterfaceFactory.createNew("3-Link Interface", new ArrayList<IKeyword>(), widgets, 
				20, 20, 
				null, null, 
				null, 1.0f, 
				null, null);
			
		centerToStage(interf, k3Settings);
		
		return interf;
	}
	private IInterface getManyLinkInterface() throws K3ImportException
	{
		if (interf != null)
			return interf;
		
		K3ProjectSettings k3Settings = k3Project.get().settings;
		
		IWidget widget;
		Collection<IWidget> widgets = new HashSet<IWidget>();
		widgets.add(widget=WidgetFactory.createNew(WidgetType.MainMedia.getDisplayName(),
				new ArrayList<IKeyword>(),
				WidgetType.MainMedia.getId(), 
				WidgetPersistCondition.Never, 
				WidgetPersistAction.Replace, 
				0, 0, 
				k3Settings.videoWidth, k3Settings.videoHeight
		));
		for (String id : new MainMedia().getDynamicPropertyIds()) {
			widget.setDynamicProperty(id, new MainMedia().getDynamicProperty(id));
		}
		
		widgets.add(widget=WidgetFactory.createNew(WidgetType.SnuAutoMultiLink.getDisplayName(),
				new ArrayList<IKeyword>(),
				WidgetType.SnuAutoMultiLink.getId(), 
				WidgetPersistCondition.Never, 
				WidgetPersistAction.Replace, 
				0, 505,
				k3Settings.videoWidth, k3Settings.manyLinkPreviewHeight
		));
		for (String id : new SnuAutoMultiLink().getDynamicPropertyIds()) {
			widget.setDynamicProperty(id, new SnuAutoMultiLink().getDynamicProperty(id));
		}
		widget.setDynamicProperty("fontColor", convertK3Color(k3Settings.previewTextColor));
		widget.setDynamicProperty("fontFamily", (k3Settings.previewTextFontFamily));
		widget.setDynamicProperty("fontSize", (k3Settings.previewTextSize));
		widget.setDynamicProperty("previewTextMode", PreviewTextMode.MOUSEOVER.getId());
		widget.setDynamicProperty("previewTextEffect", PreviewTextEffect.ANIMATE.getId());
		
		widgets.add(widget=WidgetFactory.createNew(WidgetType.Subtitles.getDisplayName(),
				new ArrayList<IKeyword>(),
				WidgetType.Subtitles.getId(), 
				WidgetPersistCondition.Never, 
				WidgetPersistAction.Replace, 
				0, k3Settings.videoHeight, 
				k3Settings.videoWidth, 25
		));
		for (String id : new Subtitles().getDynamicPropertyIds()) {
			widget.setDynamicProperty(id, new Subtitles().getDynamicProperty(id));
		}
		widget.setDynamicProperty("fontColor", convertK3Color(k3Settings.subtitleTextColor));
		widget.setDynamicProperty("fontFamily", (k3Settings.subtitleTextFontFamily));
		widget.setDynamicProperty("fontSize", (k3Settings.subtitleTextSize));
		
		widgets.add(widget=WidgetFactory.createNew(WidgetType.InsertText.getDisplayName(),
				new ArrayList<IKeyword>(),
				WidgetType.InsertText.getId(), 
				WidgetPersistCondition.Never, 
				WidgetPersistAction.Replace, 
				0, 0, 
				k3Settings.videoWidth, k3Settings.videoHeight
		));
		for (String id : new InsertText().getDynamicPropertyIds()) {
			widget.setDynamicProperty(id, new InsertText().getDynamicProperty(id));
		}
		widget.setDynamicProperty("fontColor", convertK3Color(k3Settings.insertTextColor));
		widget.setDynamicProperty("fontFamily", (k3Settings.insertTextFontFamily));
		widget.setDynamicProperty("fontSize", (k3Settings.insertTextSize));
		
		interf = InterfaceFactory.createNew("Many-Link Interface", new ArrayList<IKeyword>(), widgets, 
				20, 20, 
				null, null, 
				null, 1.0f, 
				null, null);
		
		centerToStage(interf, k3Settings);
		
		return interf;
	}
	private IInterface getNewInterface() throws K3ImportException
	{
		if (interf != null)
			return interf;

		if (newInterfaceRef.isNull())
			throw new K3ImportException("expecting interface from interfaces.txt", getDatabaseFile(), getInterfaceFile());
		StrongReference<IInterface> k5InterfaceRef = new StrongReference<IInterface>();
		try {
			new K3ConvertInterfaceTask(newInterfaceRef, report, k5InterfaceRef).runTask();
		} catch (Exception e) {
			throw new K3ImportException(e, getDatabaseFile(), getInterfaceFile());
		}
		interf = k5InterfaceRef.get();
		
		K3ProjectSettings k3Settings = k3Project.get().settings;

		Collection<IWidget> widgets = interf.getWidgets();
		for (IWidget widget : widgets) {
			switch (WidgetType.forId(widget.getWidgetId()))
			{
			case MainMedia:
				for (String id : new MainMedia().getDynamicPropertyIds()) {
					widget.setDynamicProperty(id, new MainMedia().getDynamicProperty(id));
				}
				break;
			case InsertText:
				for (String id : new InsertText().getDynamicPropertyIds()) {
					widget.setDynamicProperty(id, new InsertText().getDynamicProperty(id));
				}
				widget.setDynamicProperty("fontColor", k3Settings.insertTextColor);
				widget.setDynamicProperty("fontFamily", k3Settings.insertTextFontFamily);
				widget.setDynamicProperty("fontSize", k3Settings.insertTextSize);
				break;
			case SnuAutoLink:
				for (String id : new SnuAutoLink().getDynamicPropertyIds()) {
					widget.setDynamicProperty(id, new SnuAutoLink().getDynamicProperty(id));
				}
				widget.setDynamicProperty("fontColor", k3Settings.previewTextColor);
				widget.setDynamicProperty("fontFamily", k3Settings.previewTextFontFamily);
				widget.setDynamicProperty("fontSize", k3Settings.previewTextSize);
				widget.setDynamicProperty("previewTextMode", PreviewTextMode.MOUSEOVER.getId());
				widget.setDynamicProperty("previewTextEffect", PreviewTextEffect.ANIMATE.getId());
				break;
			case Subtitles:
				for (String id : new Subtitles().getDynamicPropertyIds()) {
					widget.setDynamicProperty(id, new Subtitles().getDynamicProperty(id));
				}
				widget.setDynamicProperty("fontColor", k3Settings.subtitleTextColor);
				widget.setDynamicProperty("fontFamily", k3Settings.subtitleTextFontFamily);
				widget.setDynamicProperty("fontSize", k3Settings.subtitleTextSize);
				break;
			}
		}
		
		centerToStage(interf, k3Settings);

		return interf;
	}
	private void importProject(K3Project k3Project) throws K3ImportException
	{
		Collection<ISnu> snus = new ArrayList<ISnu>();
//		Collection<IInterface> interfaces = new ArrayList<IInterface>();
		Collection<IMedia> media = new ArrayList<IMedia>();
		
		K3ProjectSettings k3Settings = k3Project.settings;
		
		IProject k5Project = ProjectFactory.createNew();
		k5Project.setName("Korsakow v3 Project");
		
		ISettings k5Settings = SettingsFactory.createNew();
		try {
			k5Settings.setString(Settings.VideoEncodingProfile, Registry.getProperty("defaultEncodingProfile"));
		} catch (Exception e) {
			throw new K3ImportException(e, getDatabaseFile(), getInterfaceFile());
		}

		k5Project.setRandomLinkMode(k3Project.settings.randomLinkMode);
		k5Project.setKeepLinksOnEmptySearch(k3Project.settings.keepOldLinksIfNoNewLinks);
		
		k5Project.setBackgroundSoundVolume(k3Settings.backgroundSoundVolume);
		// backgroundsound is indicated by presence of file
		Collection<String> backgroundsoundNames = Arrays.asList(
				"backgroundsound.mp3",
				"backgroundsound.wav",
				"backgroundsound.aiff"
		);
		final File soundDir = new File(getDataDir(), K3Importer.SOUND_DIR);
		final File imageDir = new File(getDataDir(), K3Importer.IMAGE_DIR);
		final File videoDir = new File(getDataDir(), K3Importer.VIDEO_DIR);
		File backgroundsoundFile = null;
		for (String name : backgroundsoundNames) {
			File file = new File(soundDir, name);
			if (file.exists()) {
				backgroundsoundFile = file;
				break;
			}
		}
		if (backgroundsoundFile != null) {
			ISound sound = SoundFactory.createNew();
			sound.setFilename(backgroundsoundFile.getPath());
			sound.setName(backgroundsoundFile.getName());
			media.add(sound);
			
			if (k3Settings.backgroundSound) {
				k5Project.setBackgroundSound(sound);
			}
		}
		
		if (k3Settings.useNewInterface) {
			k5Project.setMovieWidth(1024);
			k5Project.setMovieHeight(768);
		} else {
			k5Project.setMovieWidth(k3Settings.stageWidth);
			k5Project.setMovieHeight(k3Settings.stageHeight);
			
			if (k3Settings.use3LinkInterface) {
			} else {
				
			}
		}
		
		// startscreen is indicated by presence of file
		Collection<String> startscreenNames = Arrays.asList(
				"startscreen.jpg",
				"startscreen.jpeg",
				"startscreen.gif",
				"startscreen.png"
		);
		File startscreenFile = null;
		for (String name : startscreenNames) {
			File file = new File(imageDir, name);
			if (file.exists()) {
				startscreenFile = file;
				break;
			}
		}
		if (startscreenFile != null) {
			Image image = ImageFactory.createNew();
			image.setFilename(startscreenFile.getPath());
			image.setName(startscreenFile.getName());
			k5Project.setSplashScreenMedia(image);
			media.add(image);
		}
		
		// clicksound is indicated by presence of file
		Collection<String> clicksoundNames = Arrays.asList(
				"clicksound.mp3",
				"clicksound.wav",
				"clicksound.aiff"
		);
		File clicksoundFile = null;
		for (String name : clicksoundNames) {
			File file = new File(soundDir, name);
			if (file.exists()) {
				clicksoundFile = file;
				break;
			}
		}
		if (clicksoundFile != null) {
			Sound sound = SoundFactory.createNew();
			sound.setFilename(clicksoundFile.getPath());
			sound.setName(clicksoundFile.getName());
			k5Project.setClickSound(sound);
			media.add(sound);
		}
				
		K3RuleParser k3RuleParser = new K3RuleParser();
		
		for (K3Snu k3Snu : k3Project.snus) {
			IVideo mainMedia = VideoFactory.createNew();
			mainMedia.setName(k3Snu.filename); // this is how K3 displays stuff
			File parentDir = new File(videoDir, k3Snu.foldername);
			File mediaFile = new File(parentDir, k3Snu.filename);
			mainMedia.setFilename(mediaFile.getAbsolutePath());
			media.add(mainMedia);

			// k3 subtitle indicated by presence of similarly named file
			String subtitleFilename = FileUtil.getFilenameWithoutExtension(mainMedia.getFilename()) + "_s";
			subtitleFilename = FileUtil.setFileExtension(subtitleFilename, "txt");
			File subtitleFile = new File(subtitleFilename);
			if (subtitleFile.exists()) {
				mainMedia.setSubtitles(subtitleFile.getPath());
			}
			
			
			ISnu k5Snu = SnuFactory.createNew();
			k5Snu.setName(mainMedia.getName());
			k5Snu.setMainMedia(mainMedia);
			k5Snu.setRating(k3Snu.movieRating);
			k5Snu.setLooping(k3Snu.looping);
			k5Snu.setLives(k3Snu.lives);
			if (k3Settings.useSnuAsPreview)
				k5Snu.setPreviewMedia(mainMedia);
			else
				k5Snu.setPreviewText(k3Snu.previewText);
			
			k5Snu.setInsertText(k3Snu.insertText);
			
			if (k3Settings.useNewInterface) {
				k5Snu.setInterface(getNewInterface());
			} else {
				if (k3Settings.use3LinkInterface) {
					k5Snu.setInterface(get3LinkInterface());
				} else {
					k5Snu.setInterface(getManyLinkInterface());
				}
			}
			
			// k3 preview file indicated by presence of similarly named file
			Collection<String> thumbnailNames = Arrays.asList(
					FileUtil.getFilenameWithoutExtension(mainMedia.getFilename()) + ".jpg",
					FileUtil.getFilenameWithoutExtension(mainMedia.getFilename()) + ".jpeg",
					FileUtil.getFilenameWithoutExtension(mainMedia.getFilename()) + ".gif",
					FileUtil.getFilenameWithoutExtension(mainMedia.getFilename()) + ".png",
					FileUtil.getFilenameWithoutExtension(mainMedia.getFilename()) + "_p.mov"
			);
			File thumbnailFile = null;
			for (String name : thumbnailNames) {
				File file = new File(name);
				if (file.exists()) {
					thumbnailFile = file;
					break;
				}
			}
			if (thumbnailFile != null) {
				if (FileUtil.isImageFile(thumbnailFile.getName())) {
					IImage image = ImageFactory.createNew();
					image.setName(thumbnailFile.getName());
					image.setFilename(thumbnailFile.getPath());
					k5Snu.setPreviewMedia(image);
					media.add(image);
				} else
				if (FileUtil.isVideoFile(thumbnailFile.getName())) {
					IVideo video = VideoFactory.createNew();
					video.setName(thumbnailFile.getName());
					video.setFilename(thumbnailFile.getPath());
					k5Snu.setPreviewMedia(video);
					media.add(video);
				} else
					throw new K3ImportException("unknown preview media type: " + thumbnailFile.getAbsolutePath(), getDatabaseFile(), getInterfaceFile());
			}
			
			Collection<IKeyword> keywords = new HashSet<IKeyword>();
			List<IRule> k5Rules = new ArrayList<IRule>();
			boolean isFirstTime = true;
			for (K3Rule k3Rule : k3Snu.rules)
			{
				try {
					// K3 rules are essentially all search rules
					List<IRule> rules = k3RuleParser.parse(k3Rule.code);
					IRule searchRule = RuleFactory.createNew(RuleType.Search.getId());
					searchRule.setTriggerTime(k3Rule.time);
					searchRule.setRules(rules);
					searchRule.setDynamicProperty("maxLinks", k3Rule.maxLinks);
					k5Rules.addAll(Arrays.asList(searchRule));
					
					boolean clearorkeepIsExplicitlyStated = false;
					// inbound keywords are not rules in K5. we collect them
					// and add them directly to the snu
					List<K3Lexeme> lexemes = k3RuleParser.tokenize(k3Rule.code);
					for (K3Lexeme lexeme : lexemes) {
						switch (lexeme.getOpType())
						{
						case CLEAR_PREVIOUS_LINKS:
						case KEEP_PREVIOUS_LINKS:
							clearorkeepIsExplicitlyStated = true;
							break;
						case INBOUND_KEYWORD:
						case INBOUND_AND_LOOKUP_KEYWORD:
							// exclude reserved words
							if (K3Symbol.ENDFILM_KEYWORD.equals(lexeme.getToken()) ||
								K3Symbol.RANDOM_KEYWORD.equals(lexeme.getToken()))
								break;
							// names are used in lookups as if they were keywords. k3 does this explicitly, but its implicit in k5, so we needn't include them here.
							if (lexeme.getToken().equals(k3Snu.filename))
								break;
							keywords.add(KeywordFactory.createNew(lexeme.getToken()));
							break;
						}
					}
					if (!clearorkeepIsExplicitlyStated)
					{
						if (isFirstTime) {
							rules.add(0, RuleFactory.createNew(RuleType.ClearScores.getId()));
						}
						// else do nothing since there is no (and no need for) a KeepScoresRule
					}
				} catch (RuleParserException e) {
					throw new K3InvalidRuleException("Invalid Rule on line " + (k3Rule.lineNumber+1) + ": " + k3Rule.code + " ( " + e.getMessage() + " ) ", e, k3Rule.lineNumber+1, k3Rule.code, getDatabaseFile(), getInterfaceFile());
				}
				isFirstTime = false;
			}
			
			k5Snu.setKeywords(keywords);
			k5Snu.setRules(k5Rules);
			
			if (k3Snu.filename.equals(k3Settings.startFilmFilename) && k3Snu.foldername.equals(k3Settings.startFilmFoldername))
				k5Snu.setStarter(true);
			if (k3Snu.filename.equals(k3Settings.endFilmFilename) && k3Snu.foldername.equals(k3Settings.endFilmFoldername))
				k5Snu.setEnder(true);
			
			snus.add(k5Snu);
		}

		report.addUnsupported("BackgroundSound Enable", "SNU");//backgroundSoundEnabled
		
		report.addUnsupported("Database History", "Project");//databaseHistory
		// this is a manually maintained list of features which are known not to be implemented (ie they are never checked against in the code)
		report.addUnsupported("Auto Link Mode", "Project");//autoLinkMode
		report.addUnsupported("Auto Link Timeout", "Project");//autoLinkTimeout
		report.addUnsupported("Automatic Click", "Project");//automaticClick
		report.addUnsupported("BackgroundColor", "Project");//backgroundColor
		report.addUnsupported("\"chair\"", "Project");//chair
		report.addUnsupported("Delay Value", "Project");//delayValue
		report.addUnsupported("ForegroundColor", "Project");//foregroundColor
		report.addUnsupported("\"kairo\" Project Special Setting", "Project");//kairo
		report.addUnsupported("\"link3Lines\"", "Project");//link3Lines
		report.addUnsupported("Log Window", "Project");//logWindow
		report.addUnsupported("Loop Previews", "Project");//loopPreviews
		report.addUnsupported("ManyLinks Max Links", "Project");//manyLinksMaxLinks
		report.addUnsupported("ManyLinks IconWidth", "Project");//manyLinksIconWidth
		report.addUnsupported("ManyLinks IconHeight", "Project");//manyLinksIconHeight
		report.addUnsupported("MovieRatingFactor", "Project");//movieRatingFactor
		report.addUnsupported("Positive Linking", "Project");//positiveLinking
		report.addUnsupported("Presentation Mode", "Project");//presentationMode
		report.addUnsupported("PseudoRandomLink", "Project");
		report.addUnsupported("RandomLink Icon", "Project");
		report.addUnsupported("SatelliteId", "Project");
		report.addUnsupported("SatelliteMode", "Project");
		report.addUnsupported("SaveHistory", "Project");
		
		k5Project.setSnus(snus);
		Collection<IInterface> interfaces = new ArrayList<IInterface>();
		interfaces.add(interf);
		k5Project.setDefaultInterface(interf);
		k5Project.setInterfaces(interfaces);
		k5Project.setMedia(media);
		project.set(k5Project);
	}
}