package test.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import javax.imageio.ImageIO;

import org.korsakow.domain.ImageFactory;
import org.korsakow.domain.InterfaceFactory;
import org.korsakow.domain.KeywordFactory;
import org.korsakow.domain.RuleFactory;
import org.korsakow.domain.SnuFactory;
import org.korsakow.domain.VideoFactory;
import org.korsakow.domain.WidgetFactory;
import org.korsakow.domain.command.NewProjectCommand;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.rules.RuleType;
import org.korsakow.ide.util.FileUtil;


public class DOFactory
{
	/**
	 * 1x1 essentially empty PNG
	 */
	private static byte[] emptyPNGBytes = {
		0x89-256, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a, 0x00, 0x00, 0x00, 0x0d, 0x49, 0x48, 0x44, 0x52,
		0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x37, 0x6e, 0xf9-256,
		0x24, 0x00, 0x00, 0x00, 0x10, 0x49, 0x44, 0x41, 0x54, 0x78, 0x9c-256, 0x62, 0x60, 0x01, 0x00, 0x00,
		0x00, 0xff-256, 0xff-256, 0x03, 0x00, 0x00, 0x06, 0x00, 0x05, 0x57, 0xbf-256, 0xab-256, 0xd4-256, 0x00, 0x00, 0x00,
		0x00, 0x49, 0x45, 0x4e, 0x44, 0xae-256, 0x42, 0x60, 0x82-256,
	};
	public static File getEmptyPNGFile(File parentDir) throws Exception
	{
		File file = File.createTempFile("emptypng", ".png", parentDir);
		return getEmptyPNGFile(parentDir, file);
	}
	public static File getEmptyPNGFile(File parentDir, File file) throws Exception
	{
		FileUtil.writeStreamFully(new ByteArrayInputStream(emptyPNGBytes), new FileOutputStream(file));
		return file;
	}
	public static File getJpegFile(File parentDir, int width, int height) throws Exception
	{
		File file = File.createTempFile("myjpeg", ".jpg", parentDir);
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		img.getGraphics().setColor(Color.red);
		img.getGraphics().fillRect(0, 0, width, height);
		if (!ImageIO.write(img, "jpeg", file))
			throw new IOException();
		return file;
	}
	public static IMedia createDummyMedia(File parentDir) throws Exception
	{
		File file = getEmptyPNGFile(parentDir);
		IMedia media = ImageFactory.createNew();
		media.setFilename(file.getAbsolutePath());
		return media;
	}
	public static IImage createDummyImage(File parentDir, File file) throws Exception
	{
		file = getEmptyPNGFile(parentDir, file);
		IImage media = ImageFactory.createNew();
		media.setName(file.getName());
		media.setFilename(file.getAbsolutePath());
		return media;
	}
	public static IImage createDummyImage(File parentDir) throws Exception
	{
		File file = getEmptyPNGFile(parentDir);
		IImage media = ImageFactory.createNew();
		media.setName(file.getName());
		media.setFilename(file.getAbsolutePath());
		return media;
	}
	public static IVideo createVideo(File file) throws Exception
	{
		IVideo video = VideoFactory.createNew();
		video.setFilename(file.getAbsolutePath());
		video.setName(file.getName());
		return video;
	}
	public static Collection<IKeyword> createKeywords(String... value)
	{
		return createKeywords(Arrays.asList(value));
	}
	public static Collection<IKeyword> createKeywords(Collection<String> values)
	{
		Collection<IKeyword> keywords = new HashSet<IKeyword>();
		for (String value : values)
		{
			keywords.add(KeywordFactory.createNew(value));
		}
		return keywords;
	}
	public static IRule createKeywordLookupRule(String... values)
	{
		IRule rule = RuleFactory.createNew(RuleType.KeywordLookup.getId());
		rule.setKeywords(createKeywords(values));
		return rule;
	}
	public static IRule createSearchRule(IRule... rules)
	{
		IRule rule = RuleFactory.createNew(RuleType.Search.getId());
		rule.setRules(Arrays.asList(rules));
		return rule;
	}
	public static IRule createHttpRequestRule(String query, String requestVariableName, String requestVariableType)
	{
		return createHttpRequestRule("127.0.0.1", 80, query, requestVariableName, requestVariableType);
	}
	public static IRule createHttpRequestRule(String hostname, int httpPort, String query, String requestVariableName, String requestVariableValue)
	{
		IRule httpRequestRule = RuleFactory.createNew(RuleType.HttpRequest.getId());
		httpRequestRule.setDynamicProperty("hostname", "http://"+hostname+":"+httpPort+"/"+query);
		httpRequestRule.setDynamicProperty("requestVariableName", requestVariableName);
		httpRequestRule.setDynamicProperty("requestVariableValue", requestVariableValue);
		return httpRequestRule;
	}
	public static IRule createSetVariableRule(String variableName, String variableValue)
	{
		IRule setVariableRule = RuleFactory.createNew(RuleType.SetVariable.getId());
		setVariableRule.setDynamicProperty("variableName", variableName);
		setVariableRule.setDynamicProperty("variableValue", variableValue);
		return setVariableRule;
	}
	private static IInterface defaultInterface = null;
	public static IInterface getDefaultInterface()
	{
		if (defaultInterface == null) {
			defaultInterface = createDefaultInterface();
		}
		return defaultInterface;
	}
	public static IInterface createDefaultInterface()
	{
		IInterface interf = InterfaceFactory.createNew();
		Collection<IWidget> widgets = new HashSet<IWidget>();
		IWidget mainMedia = WidgetFactory.createNew(WidgetType.MainMedia.getId());
		widgets.add(mainMedia);
		// TODO: add other common widgets like previews and such
		interf.setWidgets(widgets);
		return interf;
	}
	public static IProject createDefaultTestProject(File parentDir) throws Exception
	{
		DataRegistry.initialize(DataRegistry.createDefaultDocument(), DataRegistry.getFile());
		IProject project = NewProjectCommand.newProject(parentDir);
		project.setName("Default Test Project");
		Collection<ISnu> snus = new HashSet<ISnu>();
		Collection<IMedia> media = new HashSet<IMedia>();
		int S = DomainTestUtil.getRandomInt(3, 6);
		for (int i = 0; i < S; ++i) {
			ISnu snu = createSnuWithDummyMedia(parentDir);
			snu.setName("Snu#"+i+"");
			snu.setStarter(i==0);
			snu.setEnder((i>0)&&(i%2==0));
			snus.add(snu);
			media.add(snu.getMainMedia());
		}
		project.setSnus(snus);
		project.setInterfaces(Arrays.asList(getDefaultInterface()));
		project.setMedia(media);
		return project;
	}
	public static ISnu createSnuWithDummyMedia(File parentDir) throws Exception
	{
		ISnu snu = SnuFactory.createNew();
		snu.setMainMedia(createDummyMedia(parentDir));
		snu.setInterface(getDefaultInterface());
		return snu;
	}
	public static ISnu createSnuWithMedia(IMedia media) throws Exception
	{
		ISnu snu = SnuFactory.createNew();
		snu.setMainMedia(media);
		snu.setInterface(getDefaultInterface());
		snu.setName(media.getName());
		return snu;
	}
}
