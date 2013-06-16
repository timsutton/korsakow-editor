/**
 * 
 */
package org.korsakow.services.export.task;

import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.EventFactory;
import org.korsakow.domain.KeywordFactory;
import org.korsakow.domain.Media;
import org.korsakow.domain.PredicateFactory;
import org.korsakow.domain.RuleFactory;
import org.korsakow.domain.TriggerFactory;
import org.korsakow.domain.interf.IDynamicProperties;
import org.korsakow.domain.interf.IEvent;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IPredicate;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.interf.IText;
import org.korsakow.domain.interf.ITrigger;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.ide.Build;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.PredicateType;
import org.korsakow.ide.resources.TriggerType;
import org.korsakow.ide.rules.RuleType;
import org.korsakow.ide.task.AbstractTask;
import org.korsakow.ide.task.TaskException;
import org.korsakow.ide.util.DomUtil;
import org.korsakow.ide.util.FileUtil;
import org.korsakow.ide.util.Util;
import org.korsakow.services.export.ExportException;
import org.korsakow.services.export.Exporter;
import org.korsakow.services.plugin.predicate.IArgumentInfo;
import org.korsakow.services.plugin.predicate.IPredicateTypeInfo;
import org.korsakow.services.plugin.predicate.PredicateTypeInfoFactory;
import org.korsakow.services.plugin.rule.IRuleTypeInfo;
import org.korsakow.services.plugin.rule.RuleTypeInfoFactory;
import org.korsakow.services.util.ColorFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLExportTask extends AbstractTask
{
	@Deprecated
	private static class MyDynamicProperties implements IDynamicProperties
	{
		private String id;
		private Object value;
		public MyDynamicProperties() {
		}
		public MyDynamicProperties(String id, Object value) {
			this.id = id;
			this.value = value;
		}
		public Object getDynamicProperty(String id) {
			return value;
		}
		public Collection<String> getDynamicPropertyIds() {
			if (id == null) return Collections.emptyList();
			return Arrays.asList(id);
		}
		public void setDynamicProperty(String id, Object value) {
			throw new IllegalStateException();
		}
	}
	private final Collection<IVideo> videosToExport;// = new HashSet<IVideo>();
	private final Collection<ISound> soundsToExport;// = new HashSet<ISound>();
	private final Collection<IImage> imagesToExport;// = new HashSet<IImage>();
	private final Collection<IText> textsToExport;// = new HashSet<Text>();
	private final Collection<IInterface> interfacesToExport;// = new HashSet<IInterface>();;
	private final Collection<ISnu> snusToExport;// = new HashSet<ISnu>();
	private final Collection<Font> fontsToExport;
	private final IProject project;
	private final File rootDir;
	private final String dataPath;
	private final Map<String, String> filenamemap;
	public XMLExportTask(String dataPath, IProject project, Collection<ISnu> snusToExport, Collection<IText> textsToExport, Collection<IImage> imagesToExport, Collection<ISound> soundsToExport, Collection<IVideo> videosToExport, Collection<IInterface> interfacesToExport, Collection<Font> fontsToExport, File rootDir, Map<String, String> filenamemap)
//	public XMLExportTask(IProject project, Collection<ISnu> snusToExport, Collection<IInterface> interfacesToExport, File rootDir)
	{
		this.dataPath = dataPath;
		this.videosToExport = videosToExport;
		this.soundsToExport = soundsToExport;
		this.imagesToExport = imagesToExport;
		this.textsToExport = textsToExport;
		this.snusToExport = snusToExport;
		this.interfacesToExport = interfacesToExport;
		this.fontsToExport = fontsToExport;
		this.project = project;
		this.rootDir = rootDir;
		this.filenamemap = filenamemap;
	}
	@Override
	public String getTitleString()
	{
		return LanguageBundle.getString("export.task.xml");
	}
	@Override
	public void runTask() throws TaskException
	{
		Document doc;
		try {
			doc = projectToDOM(project, snusToExport, textsToExport, imagesToExport, soundsToExport, videosToExport, interfacesToExport);
			DomUtil.writeDomXML(doc, new File(rootDir, dataPath));
		} catch (ExportException e) {
			throw new TaskException(e);
		} catch (IOException e) {
			throw new TaskException(e);
		} catch (TransformerException e) {
			throw new TaskException(e);
		} catch (DOMException e) {
			throw new TaskException(e);
		} catch (MapperException e) {
			throw new TaskException(e);
		}
	}
	private static Element keywordsToDom(Document doc, Collection<IKeyword> keywords, String extraKeyword)
	{
		TreeSet<IKeyword> set = new TreeSet<IKeyword>(keywords); // treeset sorts them "naturally"
		Element elm = doc.createElement("keywords");
		if (extraKeyword != null)
			set.add(KeywordFactory.createNew(extraKeyword));
		for (IKeyword keyword : set) {
			DomUtil.appendTextNode(doc, elm, "Keyword", keyword.getValue());
		}
		return elm;
	}
	private static void dynamicPropertiesToDom(Document doc, Element elm, IDynamicProperties absprops)
	{
		for (String id : absprops.getDynamicPropertyIds())
			DomUtil.appendTextNode(doc, elm, id, absprops.getDynamicProperty(id));
	}
	private static void dynamicPropertiesToDom(Document doc, Element elm, IRule rule) throws MapperException
	{
		IRuleTypeInfo typeInfo = RuleTypeInfoFactory.getFactory().getTypeInfo(rule.getRuleType());
		for (String id : rule.getDynamicPropertyIds()) {
			IArgumentInfo argInfo = typeInfo.getArgument(id);
			String value = argInfo.serialize(rule.getDynamicProperty(id));
			DomUtil.appendTextNode(doc, elm, id, value);
		}
	}
	private static void dynamicPropertiesToDom(Document doc, Element elm, IPredicate pred) throws MapperException
	{
		IPredicateTypeInfo typeInfo = PredicateTypeInfoFactory.getFactory().getTypeInfo(pred.getPredicateType());
		for (String id : pred.getDynamicPropertyIds()) {
			IArgumentInfo argInfo = typeInfo.getArgument(id);
			String value = argInfo.serialize(pred.getDynamicProperty(id));
			DomUtil.appendTextNode(doc, elm, id, value);
		}
	}
	@Deprecated
	private static Element ruleToDomDepricated(Document doc, IRule rule) throws MapperException
	{
		Element elm = doc.createElement("Rule");
		elm.appendChild(doc.createComment(rule.getRuleType()));
		DomUtil.appendTextNode(doc, elm, "id", rule.getId());
		DomUtil.appendTextNode(doc, elm, "type", rule.getRuleType());
		dynamicPropertiesToDom(doc, elm, rule);
		DomUtil.setString(doc, elm, "triggerType", null); // kind of hack, Triggers are not yet properly implemented

		Element rulesNode = doc.createElement("rules");
		elm.appendChild(rulesNode);
		for (IRule subrule : rule.getRules()) {
			rulesNode.appendChild(ruleToDomDepricated(doc, subrule));
		}
		
		elm.appendChild(keywordsToDom(doc, rule.getKeywords(), null));
		return elm;
	}
	private static Element triggerToDom(Document doc, long id, String type, IDynamicProperties dynamicProperties)
	{
		Element elm = doc.createElement("Trigger");
		DomUtil.appendTextNode(doc, elm, "id", id);
		DomUtil.appendTextNode(doc, elm, "type", type);
		dynamicPropertiesToDom(doc, elm, dynamicProperties);
		return elm;
	}
	private static Element predicateToDom(Document doc, long id, String type, IPredicate dynamicProperties, List<IPredicate> predicates) throws MapperException
	{
		Element elm = doc.createElement("Predicate");
		DomUtil.appendTextNode(doc, elm, "id", id);
		DomUtil.appendTextNode(doc, elm, "type", type);
		if (dynamicProperties != null) // TODO: depricated check, needed until Snu.rules is eliminated
			dynamicPropertiesToDom(doc, elm, dynamicProperties);

		Element predicatesNode = doc.createElement("predicates");
		elm.appendChild(predicatesNode);
		for (IPredicate sub : predicates) {
			predicatesNode.appendChild(predicateToDom(doc, sub.getId(), sub.getPredicateType(), sub, sub.getPredicates()));
		}
		return elm;
	}
	private static Element ruleToDom(Document doc, long id, String type, Collection<IKeyword> keywords, IRule dynamicProperties, List<IRule> rules) throws MapperException
	{
		Element elm = doc.createElement("Rule");
		DomUtil.appendTextNode(doc, elm, "id", id);
		DomUtil.appendTextNode(doc, elm, "type", type);
		dynamicPropertiesToDom(doc, elm, dynamicProperties);
		
		elm.appendChild(keywordsToDom(doc, keywords, null));

		Element rulesNode = doc.createElement("rules");
		elm.appendChild(rulesNode);
		if (RuleType.Search.getId().equals(type)) {
			boolean clearLinks = false;
			for (IRule sub : rules) {
				if (RuleType.ClearScores.getId().equals(sub.getRuleType())) {
					clearLinks = true;
					break;
				}
			
			}
			DomUtil.appendBooleanNode(doc, elm, "keepLinks", !clearLinks);
 		}
		for (IRule sub : rules) {
			if (RuleType.ClearScores.getId().equals(sub.getRuleType())) {
				continue;
			}
			rulesNode.appendChild(ruleToDom(doc, sub.getId(), sub.getRuleType(), sub.getKeywords(), sub, sub.getRules()));
		}
		return elm;
	}
	private static Element eventToDom(Document doc, long id, ITrigger trigger, IPredicate predicate, IRule rule) throws DOMException, MapperException
	{
		Element elm = doc.createElement("Event");
		DomUtil.appendTextNode(doc, elm, "id", id);
		elm.appendChild(triggerToDom(doc, trigger.getId(), trigger.getTriggerType(), trigger));
		elm.appendChild(predicateToDom(doc, predicate.getId(), predicate.getPredicateType(), predicate, predicate.getPredicates()));
		elm.appendChild(ruleToDom(doc, rule.getId(), rule.getRuleType(), rule.getKeywords(), rule, rule.getRules()));
		return elm;
	}
	private static Element widgetToDom(Document doc, IWidget widget) throws ExportException
	{
		Element elm = doc.createElement("Widget");
		DomUtil.appendTextNode(doc, elm, "id", widget.getId());
		if (widget.getId() == null)
			throw new ExportException(new NullPointerException(), null);
		DomUtil.appendTextNode(doc, elm, "type", widget.getWidgetId());
		Element persist = doc.createElement("persist");
		DomUtil.appendTextNode(doc, persist, "condition", widget.getPersistCondition().getId());
		DomUtil.appendTextNode(doc, persist, "action", widget.getPersistAction().getId());
		elm.appendChild(persist);
		DomUtil.appendNumberNode(doc, elm, "x", widget.getX());
		DomUtil.appendNumberNode(doc, elm, "y", widget.getY());
		DomUtil.appendNumberNode(doc, elm, "width", widget.getWidth());
		DomUtil.appendNumberNode(doc, elm, "height", widget.getHeight());
		dynamicPropertiesToDom(doc, elm, widget);
		elm.appendChild(keywordsToDom(doc, widget.getKeywords(), widget.getName()));
		return elm;
	}
	private static void resourceToDom(Document doc, IResource resource, Element elm)
	{
		DomUtil.appendTextNode(doc, elm, "id", resource.getId());
		DomUtil.appendTextNode(doc, elm, "name", resource.getName());
		elm.appendChild(keywordsToDom(doc, resource.getKeywords(), resource.getName()));
	}
	private Element videoToDom(Document doc, IVideo video) throws IOException
	{
		Element elm = doc.createElement("Video");
		resourceToDom(doc, video, elm);

		String filename = getExportFilename(video.getAbsoluteFilename());

		DomUtil.appendTextNode(doc, elm, "filename", formatExportUrl(filename));
		if (video.getSubtitles() != null) {
			String subtitlefilename = getExportFilename(Media.getAbsoluteFilename(video.getSubtitles()));
			DomUtil.appendTextNode(doc, elm, "subtitles", formatExportUrl(subtitlefilename));
		}
		return elm;
	}
	private Element textToDom(Document doc, IText text) throws IOException
	{
		Element elm = doc.createElement("Text");
		resourceToDom(doc, text, elm);
		switch (text.getSource())
		{
		case INLINE:
			DomUtil.appendTextNode(doc, elm, "text", text.getText());
			break;
		case FILE:
			String filename = getExportFilename(text.getAbsoluteFilename());
			DomUtil.appendTextNode(doc, elm, "filename", formatExportUrl(filename));
			break;
		}
		return elm;
	}
	private Element imageToDom(Document doc, IImage image) throws IOException
	{
		Element elm = doc.createElement("Image");
		resourceToDom(doc, image, elm);
		String filename = getExportFilename(image.getAbsoluteFilename());
		DomUtil.appendTextNode(doc, elm, "filename", formatExportUrl(filename));
		DomUtil.appendNumberNode(doc, elm, "duration", image.getDuration());
		return elm;
	}
	private Element soundToDom(Document doc, ISound sound) throws IOException
	{
		Element elm = doc.createElement("Sound");
		resourceToDom(doc, sound, elm);
		
		String filename = FileUtil.setFileExtension(sound.getFilename(), FileUtil.getFileExtension(Exporter.SOUND_EXPORT_FORMAT));
		filename = getExportFilename(sound.getAbsoluteFilename());
		
		DomUtil.appendTextNode(doc, elm, "filename", formatExportUrl(filename));
		if (sound.getSubtitles() != null) {
			String subtitlefilename = getExportFilename(Media.getAbsoluteFilename(sound.getSubtitles()));
			DomUtil.appendTextNode(doc, elm, "subtitles", formatExportUrl(subtitlefilename));
		}
		return elm;
	}
	private static Element interfaceToDom(Document doc, IInterface interf) throws ExportException
	{
		Element elm = doc.createElement("Interface");
		resourceToDom(doc, interf, elm);
        if (interf.getClickSound()!=null) {
        	DomUtil.appendTextNode(doc, elm, "clickSoundId", interf.getClickSound().getId());
        	DomUtil.appendNumberNode(doc, elm, "clickSoundVolume", interf.getClickSoundVolume());
        }
        if (interf.getBackgroundImage()!=null) {
        	DomUtil.appendTextNode(doc, elm, "backgroundImageId", interf.getBackgroundImage().getId());
        }
        if (interf.getBackgroundColor()!=null) {
        	DomUtil.appendTextNode(doc, elm, "backgroundColor", ColorFactory.toString(interf.getBackgroundColor()));
        }
		Element widgetsNode = doc.createElement("widgets");
		elm.appendChild(widgetsNode);
		Collection<IWidget> widgets = interf.getWidgets();
		for (IWidget widget : widgets)
			widgetsNode.appendChild(widgetToDom(doc, widget));
		return elm;
	}
	private static Element snuToDom(Document doc, ISnu snu) throws ExportException, DOMException, MapperException
	{
		Element elm = doc.createElement("Snu");
		resourceToDom(doc, snu, elm);
//		if (snu.getIVideo()!=null)
		if (snu.getThumbnail() != null)
			DomUtil.appendTextNode(doc, elm, "thumbnailId", snu.getThumbnail().getId());
		DomUtil.appendTextNode(doc, elm, "mainMediaId", snu.getMainMedia().getId());
		DomUtil.appendNumberNode(doc, elm, "rating", snu.getRating());
		DomUtil.appendNumberNode(doc, elm, "lives", snu.getLives(), "NaN");
		DomUtil.appendNumberNode(doc, elm, "maxLinks", snu.getMaxLinks(), "NaN");
		DomUtil.appendBooleanNode(doc, elm, "looping", snu.getLooping());
		DomUtil.appendBooleanNode(doc, elm, "starter", snu.getStarter());
		DomUtil.appendBooleanNode(doc, elm, "ender", snu.getEnder());
        if (snu.getBackgroundSound()!=null) {
        	DomUtil.appendTextNode(doc, elm, "backgroundSoundId", snu.getBackgroundSound().getId());
        	DomUtil.appendNumberNode(doc, elm, "backgroundSoundVolume", snu.getBackgroundSoundVolume());
        }
    	DomUtil.appendTextNode(doc, elm, "backgroundSoundMode", snu.getBackgroundSoundMode().getId());
    	DomUtil.appendBooleanNode(doc, elm, "backgroundSoundLooping", snu.getBackgroundSoundLooping());
		Element eventsNode = doc.createElement("events");
		elm.appendChild(eventsNode);
		for (IEvent event : snu.getEvents()) {
			eventsNode.appendChild(eventToDom(doc, event.getId(), event.getTrigger(), event.getPredicate(), event.getRule()));
		}
		for (final IRule rule : snu.getRules()) {
			Element eventElm = doc.createElement("Event");
			DomUtil.appendTextNode(doc, eventElm, "id", DataRegistry.getMaxId());
			eventElm.appendChild(triggerToDom(doc, DataRegistry.getMaxId(), TriggerType.SnuTime.getId(), new MyDynamicProperties("time", rule.getTriggerTime())));
			eventElm.appendChild(predicateToDom(doc, DataRegistry.getMaxId(), PredicateType.True.getId(), null, Util.list(IPredicate.class)));
			eventElm.appendChild(ruleToDom(doc, rule.getId(), rule.getRuleType(), rule.getKeywords(), rule, rule.getRules()));
			eventsNode.appendChild(eventElm);
		}
		if (snu.getInterface()==null) {
			//TODO: Stu: this should throw some other form of exception which is caught and wrapped above
			throw new ExportException("SNU " + snu.getName() + "; " + LanguageBundle.getString("export.errors.snuhasnointerface"), null);
		}
		DomUtil.appendTextNode(doc, elm, "interfaceId", snu.getInterface().getId());
		if (snu.getPreviewMedia()!=null)
			DomUtil.appendTextNode(doc, elm, "previewMediaId", snu.getPreviewMedia().getId());
		DomUtil.appendTextNode(doc, elm, "previewText", snu.getPreviewText());
		DomUtil.appendTextNode(doc, elm, "insertText", snu.getInsertText());
		return elm;
	}
	private Document projectToDOM(IProject project,
			Collection<ISnu> snusToExport,
			Collection<IText> textsToExport,
			Collection<IImage> imagesToExport,
			Collection<ISound> soundsToExport,
			Collection<IVideo> videosToExport,
			Collection<IInterface> interfacesToExport) throws ExportException, IOException, DOMException, MapperException
	{
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
		try {
			docBuilder = dbfac.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new ExportException(e, rootDir);
		}
        Document doc = docBuilder.newDocument();
        Element root = doc.createElement("korsakow");
        doc.appendChild(root);
        
        root.setAttribute("versionMajor", Build.getVersion());
        root.setAttribute("versionMinor", ""+Build.getRelease());
        
        Element projNode = doc.createElement("Project");
        root.appendChild(projNode);
        resourceToDom(doc, project, projNode);
        
        DomUtil.appendTextNode(doc, projNode, "uuid", project.getUUID());
//        if (project.getMovieWidth() != null)
        	DomUtil.appendNumberNode(doc, projNode, "movieWidth", project.getMovieWidth());
//        if (project.getMovieHeight() != null)
        	DomUtil.appendNumberNode(doc, projNode, "movieHeight", project.getMovieHeight());
        if (project.getBackgroundSound()!=null) {
        	DomUtil.appendTextNode(doc, projNode, "backgroundSoundId", project.getBackgroundSound().getId());
        	DomUtil.appendNumberNode(doc, projNode, "backgroundSoundVolume", project.getBackgroundSoundVolume());
        	DomUtil.appendBooleanNode(doc, projNode, "backgroundSoundLooping", project.getBackgroundSoundLooping());
        }
        if (project.getClickSound()!=null) {
        	DomUtil.appendTextNode(doc, projNode, "clickSoundId", project.getClickSound().getId());
        	DomUtil.appendNumberNode(doc, projNode, "clickSoundVolume", project.getClickSoundVolume());
        }
        if (project.getSplashScreenMedia()!=null) {
        	DomUtil.appendTextNode(doc, projNode, "splashScreenMediaId", project.getSplashScreenMedia().getId());
        }
        DomUtil.appendBooleanNode(doc, projNode, "randomLinkMode", project.getRandomLinkMode());
        DomUtil.appendBooleanNode(doc, projNode, "keepLinksOnEmptySearch", project.getKeepLinksOnEmptySearch());
        DomUtil.appendNumberNode(doc, projNode, "maxLinks", project.getMaxLinks(), "NaN");
        
		List<IEvent> events = new ArrayList<IEvent>();
		if (project.getBackgroundImage() != null) {
        	DomUtil.appendTextNode(doc, projNode, "backgroundImageId", project.getBackgroundImage().getId());
        	
			ITrigger trigger = TriggerFactory.createClean(TriggerType.Initialized.getId());
			IPredicate pred = PredicateFactory.createClean(PredicateType.True.getId());
			IRule rule = RuleFactory.createClean(RuleType.SetBackgroundImage.getId());
			rule.setDynamicProperty("imageId", project.getBackgroundImage().getId());
			
			IEvent event = EventFactory.createClean(trigger, pred, rule);
			events.add(event);
		}
        if (project.getBackgroundColor()!=null) {
        	DomUtil.appendTextNode(doc, projNode, "backgroundColor", ColorFactory.toString(project.getBackgroundColor()));
        }
		Element eventsNode = doc.createElement("events");
		projNode.appendChild(eventsNode);
		for (IEvent event : events) {
			eventsNode.appendChild(eventToDom(doc, event.getId(), event.getTrigger(), event.getPredicate(), event.getRule()));
		}
		
        Element snusNode = doc.createElement("snus");
        root.appendChild(snusNode);
        for (ISnu snu : snusToExport) {
        	try {
        		validate(snu);
        		snusNode.appendChild(snuToDom(doc, snu));
	    	} catch (ExportException e) {
	    		//Hack because the export Exception is thrown in a static method that
	    		//has no reason to know of the root dir.
	    		//TODO: Stu: this should throw some other form of Exception which is caught and converted.
	    		e.setProjectFile(rootDir);
	    		throw e;
	    	}
        }
        Element interfsNode = doc.createElement("interfaces");
        root.appendChild(interfsNode);
        for (IInterface interf : interfacesToExport) {
        	try {
        		interfsNode.appendChild(interfaceToDom(doc, interf));
        	} catch (ExportException e) {
        		//Hack because the export Exception is thrown in a static method that
        		//has no reason to know of the root dir.
        		//TODO: Stu: this should throw some other form of Exception which is caught and converted.
        		e.setProjectFile(rootDir);
        		throw e;
        	}
        }
        Element textsNode = doc.createElement("texts");
        root.appendChild(textsNode);
        for (IText text : textsToExport) {
        	try {
        		validate(text);
        	} catch (ExportException e) {
        		//Hack because the export Exception is thrown in a static method that
        		//has no reason to know of the root dir.
        		//TODO: Stu: this should throw some other form of Exception which is caught and converted.
        		e.setProjectFile(rootDir);
        		throw e;
        	}
        	textsNode.appendChild(textToDom(doc, text));
        }
        Element videosNode = doc.createElement("videos");
        root.appendChild(videosNode);
        for (IVideo video : videosToExport) {
        	validate(video);
        	videosNode.appendChild(videoToDom(doc, video));
        }
        Element soundsNode = doc.createElement("sounds");
        root.appendChild(soundsNode);
        for (ISound sound : soundsToExport) {
        	validate(sound);
        	soundsNode.appendChild(soundToDom(doc, sound));
        }
        Element imagesNode = doc.createElement("images");
        root.appendChild(imagesNode);
        for (IImage image : imagesToExport) {
        	validate(image);
        	imagesNode.appendChild(imageToDom(doc, image));
        }
        Element fontsNode = doc.createElement("fonts");
        root.appendChild(fontsNode);
        
		if (!fontsToExport.isEmpty()) {
			String fontfilename = getExportFilename(new File(Exporter.FONT_DIR, "font.swf").getPath());
			DomUtil.appendTextNode(doc, fontsNode, "Font", formatExportUrl(fontfilename));
        }
        
        return doc;
	}
	private static void validate(IMedia media) throws ExportException
	{
		switch (media.getSource())
		{
		case FILE:
			if (media.getFilename() == null)
				throw new ExportException("media has null filename: " + media.getName(), null);
			if (media.getFilename().length()==0)
				throw new ExportException("media has empty filename:" + media.getName(), null);
			File file = null;
			try {
				file = new File(media.getAbsoluteFilename());
			} catch (FileNotFoundException e) {
				throw new ExportException(e, null);
			}
			if (!file.exists() || !file.canRead())
				throw new ExportException("file not found or cannot read: " + file.getAbsolutePath(), null);
			break;
		case INLINE:
			break;
		default:
			throw new ExportException("invalid source: " + media.getSource(), null);
		}
	}
	private static void validate(ISnu snu) throws ExportException
	{
		IMedia media = snu.getMainMedia();
		if (media == null)
			throw new ExportException("SNU has no video", null);
		validate(media);
	}

	/**
	 * URL encodes the elements of the path as Per URLEncoder, however we ensure this is done
	 * for all entities, e.g. space encoded as %20, not +
	 * Path separators are not encoded.
	 * 
	 * This is sometimes necessary for being able to view files on the hard drive in the browser
	 * @throws UnsupportedEncodingException 
	 */
	public static String formatExportUrl(String filename) throws UnsupportedEncodingException
	{
		String[] pathparts = filename.split("[/\\\\]");
		StringBuilder sb = new StringBuilder();
		for (String part : pathparts)
		{
			String encoded = URLEncoder.encode(part, "UTF-8");
			encoded = encoded.replace("+", "%20");
			sb.append(encoded)
				.append('/');
		}
		sb.deleteCharAt(sb.length()-1); // final '/'
		
		filename = sb.toString();
		return filename;
	}
	private String getExportFilename(String mediafilename) throws IOException
	{
		if (filenamemap.containsKey(mediafilename))
			return filenamemap.get(mediafilename);
		throw new IOException("Filename not in map: " + mediafilename);
	}
}
