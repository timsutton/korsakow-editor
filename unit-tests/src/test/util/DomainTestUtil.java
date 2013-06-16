/**
 * 
 */
package test.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Assert;
import org.korsakow.domain.KeywordFactory;
import org.korsakow.domain.MediaSource;
import org.korsakow.domain.RuleFactory;
import org.korsakow.domain.WidgetFactory;
import org.korsakow.domain.interf.IDynamicProperties;
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
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.DomHelper;
import org.korsakow.ide.Main;
import org.korsakow.ide.resources.PredicateType;
import org.korsakow.ide.resources.TriggerType;
import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.rules.RuleType;
import org.korsakow.ide.util.DomUtil;
import org.korsakow.ide.util.Util;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class DomainTestUtil
{
	private static final Random random = new Random();
	public static void setupDataRegistry(File dataFile) throws Exception
	{
		Main.setupLogging();
		
	    Document document = null;
	    document = DomUtil.createDocument();
	    document.appendChild(document.createElement("korsakow"));

		setupDataRegistry(dataFile, document);
	}
	public static void setupDataRegistry(File dataFile, Document document) throws Exception
	{
	    DataRegistry.initialize(document, dataFile);
	}
	public static DomHelper createHelper(File file) throws Exception
	{
		return new DomHelper(DomUtil.parseXML(file));
	}
	
	public DomainTestUtil()
	{
	}
	public static String getRandomString() throws IOException
	{
		return getRandomString(0, 32);
	}
	/**
	 * Generates a string with characters in the range 0x20 (space) to 0x7E (tilde).
	 * TODO: include random unicode from various languages.
	 */
	public static String getRandomString(int minLength, int maxLength) throws IOException
	{
//		you could use the word factory if you wanted human readable words
//		return WordFactory.createRandomWordString((minLength+maxLength)/2);
		StringBuffer buffer = new StringBuffer();
		int n = minLength+random.nextInt(maxLength-minLength);
		for (int i = 0; i < n; ++i)
			buffer.append((char)(0x20+random.nextInt(0x7E-0x20)));
		return buffer.toString();
	}
	public static long getRandomLong()
	{
		return random.nextLong();
	}
	public static int getRandomInt(int low, int high)
	{
		return low + random.nextInt(high-low);
	}
	public static int getRandomInt()
	{
		return random.nextInt();
	}
	public static double getRandomDouble()
	{
		return random.nextDouble();
	}
	public static float getRandomFloat()
	{
		return random.nextFloat();
	}
	public static boolean getRandomBoolean()
	{
		return random.nextBoolean();
	}
	public static Set<IKeyword> createRandomKeywords() throws IOException
	{
		Set<IKeyword> keywords = new LinkedHashSet<IKeyword>();
		int n = random.nextInt(8);
		for (int i = 0; i < n; ++i)
			keywords.add(KeywordFactory.createNew(getRandomString(), getRandomFloat()));
		return keywords;
	}
	public static Collection<? extends org.korsakow.domain.interf.IWidget> createRandomWidgets() throws Exception
	{
		List<org.korsakow.domain.interf.IWidget> widgets = new ArrayList<org.korsakow.domain.interf.IWidget>();
		int n = random.nextInt(8);
		for (int i = 0; i < n; ++i) {
			WidgetType widgetType = WidgetType.values()[(int)(Math.random()*WidgetType.values().length)];
			org.korsakow.domain.interf.IWidget widget = WidgetFactory.createClean(0L, 0L, "", new ArrayList<IKeyword>(), widgetType.getId(), null, null, 0, 0, 0, 0);
			initializeRandom(widget);
			widgets.add(widget);
		}
		return widgets;
	}
	public static List<IRule> createRandomRules() throws Exception
	{
		List<IRule> rules = new ArrayList<IRule>();
		int n = random.nextInt(8);
		for (int i = 0; i < n; ++i) {
			RuleType ruleType = RuleType.values()[(int)(Math.random()*RuleType.values().length)];
			IRule rule = RuleFactory.createClean(ruleType.getId());
			initializeRandom(rule);
			rules.add(rule);
		}
		return rules;
	}
	public static List<IRule> createRandomIRules() throws Exception
	{
		int ruleCount = getRandomInt(0, 10); // some reasonable number, including 0
		List<IRule> rules = new ArrayList<IRule>(ruleCount);
		for (int i = 0; i < ruleCount; ++i)
		{
			IRule rule = RuleFactory.createNew();
			initializeRandom(rule);
			rules.add(rule);
		}
		return rules;
	}
	public static Collection<IWidget> createRandomIWIdgets(boolean atLeastOneWidget) throws Exception
	{
		int widgetCount = getRandomInt(atLeastOneWidget?1:0, 10); // arbitrary, but i think its important 0 be in there
		List<IWidget> widgets = new ArrayList<IWidget>(widgetCount);
		for (int i = 0; i < widgetCount; ++i)
		{
			IWidget widget = WidgetFactory.createNew();
			initializeRandom(widget);
			widgets.add(widget);
		}
		return widgets;
	}
	public static void initializeRandomProperties(IDynamicProperties obj)
	{
		if (true) return;
		for (String id : obj.getDynamicPropertyIds()) {
			// AbstractProperties should probably store the property type eh
			try {
				obj.setDynamicProperty(id, getRandomString());
			} catch (Exception e) {
			}
			try {
				obj.setDynamicProperty(id, getRandomInt());
			} catch (Exception e) {
			}
			try {
				obj.setDynamicProperty(id, getRandomLong());
			} catch (Exception e) {
			}
			try {
				obj.setDynamicProperty(id, getRandomBoolean());
			} catch (Exception e) {
			}
			try {
				obj.setDynamicProperty(id, getRandomFloat());
			} catch (Exception e) {
			}
		}
	}
	public static void initializeRandom(IRule rule) throws Exception
	{
		rule.setName(getRandomString());
		rule.setKeywords(createRandomKeywords());
		rule.setTriggerTime(getRandomLong());
		rule.setRuleType(RuleType.values()[(int)(Math.random()*RuleType.values().length)].getId());
	}
	public static void initializeRandom(IPredicate predicate) throws Exception
	{
		predicate.setPredicateType(PredicateType.values()[(int)(Math.random()*PredicateType.values().length)].getId());
	}
	public static void initializeRandom(ITrigger trigger) throws Exception
	{
		trigger.setTriggerType(TriggerType.values()[(int)(Math.random()*TriggerType.values().length)].getId());
	}
	public static void initializeRandom(IText text) throws Exception
	{
		text.setName(getRandomString());
		text.setFilename(getRandomString());
//		text.setText(getRandomString());
		text.setKeywords(createRandomKeywords());
	}
	public static void initializeRandom(ISound sound) throws Exception
	{
		sound.setName(getRandomString());
		sound.setFilename(getRandomString());
		sound.setKeywords(createRandomKeywords());
	}
	public static void initializeRandom(IVideo video) throws Exception
	{
		video.setName(getRandomString());
		video.setFilename(getRandomString());
		video.setKeywords(createRandomKeywords());
	}
	public static void initializeRandom(IImage image) throws Exception
	{
		image.setName(getRandomString());
		image.setFilename(getRandomString());
		image.setKeywords(createRandomKeywords());
	}
	public static void initializeRandom(ISnu snu) throws Exception
	{
		snu.setName(getRandomString());
		snu.setRating(getRandomFloat());
		snu.setLives(getRandomLong());
		snu.setMaxLinks(getRandomLong());
		snu.setEnder(getRandomBoolean());
		snu.setBackgroundSoundVolume(getRandomFloat());
		snu.setLooping(getRandomBoolean());
		snu.setKeywords(createRandomKeywords());
		snu.setPreviewText(getRandomString());
		snu.setInsertText(getRandomString());
		snu.setRules(createRandomIRules());
	}
	public static void initializeRandom(IInterface interf, boolean atLeastOneWidget) throws Exception
	{
		interf.setName(getRandomString());
		interf.setGridWidth(getRandomInt());
		interf.setGridHeight(getRandomInt());
		interf.setClickSoundVolume(getRandomFloat());
		interf.setKeywords(createRandomKeywords());
		interf.setWidgets(createRandomIWIdgets(atLeastOneWidget));
	}
	public static void initializeRandom(IWidget widget) throws Exception
	{
		widget.setName(getRandomString());
		widget.setX(getRandomInt());
		widget.setY(getRandomInt());
		widget.setWidth(getRandomInt());
		widget.setHeight(getRandomInt());
		widget.setKeywords(createRandomKeywords());
		initializeRandomProperties(widget);
	}
	public static void initializeRandom(org.korsakow.domain.interf.IProject project) throws Exception
	{
		project.setName(getRandomString());
		project.setBackgroundSoundVolume(getRandomFloat());
		project.setBackgroundSoundLooping(getRandomBoolean());
		project.setClickSoundVolume(getRandomFloat());
		project.setRandomLinkMode(getRandomBoolean());
		project.setKeepLinksOnEmptySearch(getRandomBoolean());
		project.setMaxLinks(getRandomLong());
		project.setMovieWidth(getRandomInt());
		project.setMovieHeight(getRandomInt());
	}
	/**
	 * a weak equality check: disregards underlying implementation of the collections because it disregards, for example, order.
	 * otherwise it relies on .equals for each item in the collections
	 */
	public static void assertEqualContents(Collection expected, Collection actual)
	{
		Assert.assertEquals(expected.size(), actual.size());
		// the underlying implementation behind the collections may be lists or sets or whatever so we cant rely on their .equals
		// we instead rely on the implementation behind IKeyword's equals (we should probably just manually check the members though)
		// [expected UNION actual == actual IFF (expected == actual && size(expected)==size(actual)] is the basis of this test
		HashSet all = new HashSet();
		all.addAll(expected);
		all.addAll(actual);
		Assert.assertEquals(expected.size(), actual.size());
	}
	public static void assertEqualKeywords(DomHelper helper, Collection<IKeyword> expected, String query, Object...args) throws Exception
	{
		if (expected.size() != helper.xpathAsNodeList(query + "/keywords/Keyword", args).getLength()) {
			System.out.println(Util.join(expected, ", "));
			helper = helper;
		}
		Assert.assertEquals(expected.size(), helper.xpathAsNodeList(query + "/keywords/Keyword", args).getLength());
		for (IKeyword e : expected) {
			int actual = helper.xpathAsNodeList(query + "/keywords/Keyword[value=? and weight=?]", Util.arrayAdd(args, e.getValue(), e.getWeight())).getLength();
//			System.out.println("Expected: " + e + "  " + actual);
			Assert.assertEquals(e.getValue() + '(' + e.getWeight() + ')', 1, actual);
		}
	}
	public static void assertEqualProperties(DomHelper helper, IDynamicProperties obj, long id) throws Exception
	{
		for (String propid : obj.getDynamicPropertyIds()) {
			Object value = obj.getDynamicProperty(propid);
			if (value == null)
				Assert.assertNull(helper.xpathAsElement("/korsakow/descendant::*[id=?]/"+propid, id));
			else
				Assert.assertEquals(value.toString(), helper.xpathAsString("/korsakow/descendant::*[id=?]/"+propid, id));
		}
	}
	public static void assertEqual(File dataFile, long id, org.korsakow.domain.interf.IText text) throws Exception
	{
		DomHelper helper = DomainTestUtil.createHelper(dataFile);
		Assert.assertTrue(0 != helper.xpathAsNodeList("/korsakow/texts/Text[id=?]", id).getLength());
		Assert.assertEquals(text.getName(), helper.xpathAsString("/korsakow/texts/Text[id=?]/name", id));
		Assert.assertEquals(text.getSource().getId(), helper.xpathAsString("/korsakow/texts/Text[id=?]/source", id));
		Assert.assertEquals(text.getValue(), helper.xpathAsString("/korsakow/texts/Text[id=?]/value", id));
		assertEqualKeywords(helper, text.getKeywords(), "/korsakow/texts/Text[id=?]", id);
	}
	public static void assertEqual(File dataFile, long id, org.korsakow.domain.interf.ISound sound) throws Exception
	{
		DomHelper helper = DomainTestUtil.createHelper(dataFile);
		Assert.assertEquals(sound.getName(), helper.xpathAsString("/korsakow/sounds/Sound[id=?]/name", id));
		Assert.assertEquals(sound.getFilename(), helper.xpathAsString("/korsakow/sounds/Sound[id=?]/filename", id));
		assertEqualKeywords(helper, sound.getKeywords(), "/korsakow/sounds/Sound[id=?]", id);
	}
	public static void assertEqual(File dataFile, long id, org.korsakow.domain.interf.IVideo video) throws Exception
	{
		DomHelper helper = DomainTestUtil.createHelper(dataFile);
		Assert.assertEquals(video.getName(), helper.xpathAsString("/korsakow/videos/Video[id=?]/name", id));
		Assert.assertEquals(video.getFilename(), helper.xpathAsString("/korsakow/videos/Video[id=?]/filename", id));
		assertEqualKeywords(helper, video.getKeywords(), "/korsakow/videos/Video[id=?]", id);
	}
	public static void assertEqual(File dataFile, long id, org.korsakow.domain.interf.IImage image) throws Exception
	{
		DomHelper helper = DomainTestUtil.createHelper(dataFile);
		Assert.assertEquals(image.getName(), helper.xpathAsString("/korsakow/images/Image[id=?]/name", id));
		Assert.assertEquals(image.getFilename(), helper.xpathAsString("/korsakow/images/Image[id=?]/filename", id));
		assertEqualKeywords(helper, image.getKeywords(), "/korsakow/images/Image[id=?]", id);
	}
	public static void assertEqualProperties(IDynamicProperties expected, IDynamicProperties actual)
	{
		Assert.assertEquals(expected.getDynamicPropertyIds(), actual.getDynamicPropertyIds());
	}
	public static void assertEqualKeywords(Collection<IKeyword> expected, Collection<IKeyword> actual)
	{
		assertEqualContents(expected, actual);
	}
	public static void assertEqualRules(List<IRule> expected, List<IRule> actual)
	{
		Assert.assertEquals(expected.size(), actual.size());
		// rules are simpler because we can assume the order
		for (int i = 0; i < expected.size(); ++i)
		{
			IRule expectedRule = expected.get(i);
			IRule actualRule = actual.get(i);
			assertEqual(expectedRule, actualRule.getName(), actualRule.getKeywords(), actualRule, actualRule.getTriggerTime(), actualRule.getRuleType());
		}
	}
	public static void assertEqualWidgets(Collection<IWidget> expected, Collection<IWidget> actual)
	{
		// the strategy is to sort them by id, then compare matching entries
		// an alternative would be to double-iterate the collections looking for matches... how tedious! (not so much concerned about how (un)performant it would be since is just for testing and the numbers would be small)

		Comparator<IWidget> sorter = new Comparator<IWidget>()
		{
			public int compare(IWidget o1, IWidget o2) {
				return (int)(o1.getId() - o2.getId());
			}
		};
		List<IWidget> sortedExpected = new ArrayList<IWidget>(expected);
		List<IWidget> sortedActual = new ArrayList<IWidget>(actual);
		Collections.sort(sortedExpected, sorter);
		Collections.sort(sortedActual, sorter);
		
		Assert.assertEquals(sortedExpected.size(), sortedActual.size());
		for (int i = 0; i < sortedExpected.size(); ++i)
		{
			IWidget expectedWidget = sortedExpected.get(i);
			IWidget actualWidget = sortedActual.get(i);
			assertEqual(expectedWidget, actualWidget.getName(), actualWidget.getKeywords(), actualWidget, actualWidget.getHeight(), actualWidget.getWidth(), actualWidget.getWidgetId(), actualWidget.getX(), actualWidget.getY());
		}
	}
	public static void assertEqual(IResource resource, String name, Collection<IKeyword> keywords)
	{
		Assert.assertEquals(resource.getName(), name);
		Assert.assertEquals(resource.getKeywords().size(), keywords.size());
		assertEqualKeywords(resource.getKeywords(), keywords);
	}
	public static void assertEqual(IProject expected, String name,
			Collection<IKeyword> keywords,
			int movieWidth, int movieHeight,
			float backgroundSoundVolume, boolean backgroundSoundLooping,
			float clickSoundVolume,
			boolean randomLinkMode, boolean keepLinks,
			Long maxLinks)
	{
		assertEqual(expected, name, keywords);
		Assert.assertEquals(expected.getMovieWidth(), movieWidth);
		Assert.assertEquals(expected.getMovieHeight(), movieHeight);
		Assert.assertEquals(expected.getBackgroundSoundVolume(), backgroundSoundVolume, 0);
		Assert.assertEquals(expected.getBackgroundSoundLooping(), backgroundSoundLooping);
		Assert.assertEquals(expected.getClickSoundVolume(), clickSoundVolume, 0);
		Assert.assertEquals(expected.getRandomLinkMode(), randomLinkMode);
		Assert.assertEquals(expected.getKeepLinksOnEmptySearch(), keepLinks);
		Assert.assertEquals(expected.getMaxLinks(), maxLinks);
	}
	public static void assertEqual(ISnu expected, ISnu actual)
	{
		assertEqual(expected, actual.getName(), actual.getRating(), actual.getKeywords(), actual.getStarter(), actual.getEnder(), actual.getLives(), actual.getLooping(), actual.getMaxLinks(), actual.getBackgroundSoundVolume(), actual.getBackgroundSoundLooping(),  actual.getPreviewText(), actual.getInsertText(), actual.getRules());
	}
	public static void assertEqual(IVideo expected, IVideo actual)
	{
		assertEqual(expected, actual.getName(), actual.getKeywords(), actual.getSource(), actual.getFilename(), actual.getSubtitles());
	}
	public static void assertEqual(ISound expected, ISound actual)
	{
		assertEqual(expected, actual.getName(), actual.getKeywords(), actual.getSource(), actual.getFilename());
	}
	public static void assertEqual(IImage expected, IImage actual)
	{
		assertEqual(expected, actual.getName(), actual.getKeywords(), actual.getSource(), actual.getFilename());
	}
	public static void assertEqual(IText expected, IText actual)
	{
		assertEqual(expected, actual.getName(), actual.getKeywords(), actual.getSource(), actual.getFilename());
	}
	public static void assertEqual(ISnu snu, String name, float rating, Collection<IKeyword> keywords, boolean starter, boolean ender, Long lives, boolean looping, Long maxLinks, float backgroundSoundVolume, boolean backgroundSoundLooping, String previewTooltip, String insertText, List<IRule> rules)
	{
		assertEqual(snu, name, keywords);
		Assert.assertEquals(snu.getRating(), rating, 0); // currently we use 0 tolerance here, but its not realistic since we have floats
		Assert.assertEquals(snu.getStarter(), starter);
		Assert.assertEquals(snu.getEnder(), ender);
		Assert.assertEquals(snu.getLives(), lives);
		Assert.assertEquals(snu.getLooping(), looping);
		Assert.assertEquals(snu.getMaxLinks(), maxLinks);
		Assert.assertEquals(snu.getBackgroundSoundVolume(), backgroundSoundVolume, 0);
		Assert.assertEquals(snu.getBackgroundSoundLooping(), backgroundSoundLooping);
		Assert.assertEquals(snu.getPreviewText(), previewTooltip);
		Assert.assertEquals(snu.getInsertText(), insertText);
		assertEqualRules(snu.getRules(), rules);
	}
	public static void assertEqual(IInterface interf, String name, Collection<IKeyword> keywords, float clickSoundVolume, int gridHeight, int gridWidth, Integer viewHeight, Integer viewWidth, Collection<IWidget> widgets)
	{
		assertEqual(interf, name, keywords);
		Assert.assertEquals(interf.getClickSoundVolume(), clickSoundVolume, 0);
		Assert.assertEquals(interf.getGridHeight(), gridHeight);
		Assert.assertEquals(interf.getGridWidth(), gridWidth);
		Assert.assertEquals(interf.getViewHeight(), viewHeight);
		Assert.assertEquals(interf.getViewWidth(), viewWidth);
		assertEqualWidgets(interf.getWidgets(), widgets);
	}
	public static void assertEqual(IMedia media, String name, Collection<IKeyword> keywords, MediaSource source, String filename)
	{
		assertEqual(media, name, keywords);
		Assert.assertEquals(media.getSource(), source);
		Assert.assertEquals(media.getFilename(), filename);
	}
	public static void assertEqual(IVideo media, String name, Collection<IKeyword> keywords, MediaSource source, String filename, String subtitles)
	{
		assertEqual(media, name, keywords);
		Assert.assertEquals(media.getSubtitles(), subtitles);
	}
	public static void assertEqual(IRule rule, String name, Collection<IKeyword> keywords, IDynamicProperties properties, long triggerTime, String type)
	{
		assertEqual(rule, name, keywords);
		assertEqualProperties(rule, properties);
		Assert.assertEquals(rule.getTriggerTime(), triggerTime);
		Assert.assertEquals(rule.getRuleType(), type);
	}
	public static void assertEqual(IPredicate predicate, IDynamicProperties properties, String type)
	{
		assertEqualProperties(predicate, properties);
		Assert.assertEquals(predicate.getPredicateType(), type);
	}
	public static void assertEqual(ITrigger trigger, IDynamicProperties properties, String type)
	{
		assertEqualProperties(trigger, properties);
		Assert.assertEquals(trigger.getTriggerType(), type);
	}
	public static void assertEqual(IWidget widget, String name, Collection<IKeyword> keywords, IDynamicProperties properties, int height, int width, String widgetId, int x, int y)
	{
		assertEqual(widget, name, keywords);
		Assert.assertEquals(widget.getHeight(), height);
		Assert.assertEquals(widget.getWidth(), width);
		Assert.assertEquals(widget.getWidgetId(), widgetId);
		Assert.assertEquals(widget.getX(), x);
		Assert.assertEquals(widget.getY(), y);
		Assert.assertEquals(widget.getHeight(), height);
	}
	public static void assertEqual(File dataFile, long id, org.korsakow.domain.interf.ISnu snu) throws Exception
	{
		DomHelper helper = DomainTestUtil.createHelper(dataFile);
		Assert.assertEquals(snu.getName(), helper.xpathAsString("/korsakow/snus/Snu[id=?]/name", id));
		Assert.assertEquals(snu.getRating(), helper.xpathAsFloat("/korsakow/snus/Snu[id=?]/rating", id), 0);
		Assert.assertEquals(snu.getStarter(), helper.xpathAsBoolean("/korsakow/snus/Snu[id=?]/starter", id));
		Assert.assertEquals(snu.getEnder(), helper.xpathAsBoolean("/korsakow/snus/Snu[id=?]/ender", id));
		Assert.assertEquals(snu.getLooping(), helper.xpathAsBoolean("/korsakow/snus/Snu[id=?]/looping", id));
		Assert.assertEquals((long)snu.getLives(), helper.xpathAsLong("/korsakow/snus/Snu[id=?]/lives", id));
		Assert.assertEquals((long)snu.getMaxLinks(), helper.xpathAsLong("/korsakow/snus/Snu[id=?]/maxLinks", id));
		Assert.assertEquals(snu.getBackgroundSoundVolume(), helper.xpathAsFloat("/korsakow/snus/Snu[id=?]/backgroundSoundVolume", id), 0);
		Assert.assertEquals(snu.getBackgroundSoundMode().getId(), helper.xpathAsString("/korsakow/snus/Snu[id=?]/backgroundSoundMode", id));
		Assert.assertEquals(snu.getBackgroundSoundLooping(), helper.xpathAsBoolean("/korsakow/snus/Snu[id=?]/backgroundSoundLooping", id));
		Assert.assertEquals(snu.getPreviewText(), helper.xpathAsString("/korsakow/snus/Snu[id=?]/previewText", id));
		Assert.assertEquals(snu.getInsertText(), helper.xpathAsString("/korsakow/snus/Snu[id=?]/insertText", id));
		for (org.korsakow.domain.interf.IRule rule : snu.getRules()) {
			assertEqual(dataFile, id, rule);
		}
//		for (IRule rule : snu.getRules()) {
//			Assert.assertEquals(rule.getRuleType(), helper.xpathAsString("/korsakow/snus/Snu[id=?]/rules/Rule[id=?]/type", id, rule.getId()), rule.getRuleType());
//			Assert.assertEquals(rule.getName(), helper.xpathAsString("/korsakow/snus/Snu[id=?]/rules/Rule[id=?]/name", id, rule.getId()), rule.getName());
//		}
		assertEqualKeywords(helper, snu.getKeywords(), "/korsakow/snus/Snu[id=?]", id);
	}
	public static void assertEqual(File dataFile, long id, org.korsakow.domain.interf.IInterface interf) throws Exception
	{
		DomHelper helper = DomainTestUtil.createHelper(dataFile);
		Assert.assertEquals(interf.getName(), helper.xpathAsString("/korsakow/interfaces/Interface[id=?]/name", id));
		Assert.assertEquals(interf.getGridWidth(), helper.xpathAsInt("/korsakow/interfaces/Interface[id=?]/gridWidth", id));
		Assert.assertEquals(interf.getGridHeight(), helper.xpathAsInt("/korsakow/interfaces/Interface[id=?]/gridHeight", id));
		Assert.assertEquals(interf.getClickSoundVolume(), helper.xpathAsFloat("/korsakow/interfaces/Interface[id=?]/clickSoundVolume", id), 0);
		Assert.assertEquals(interf.getWidgets().size(), helper.xpathAsInt("count(/korsakow/interfaces/Interface[id=?]/widgets/Widget)", id));
		for (org.korsakow.domain.interf.IWidget widget : interf.getWidgets()) {
			assertEqual(dataFile, id, widget);
		}
		assertEqualKeywords(helper, interf.getKeywords(), "/korsakow/interfaces/Interface[id=?]", id);
	}
	public static void assertEqual(File dataFile, long interfaceId, org.korsakow.domain.interf.IWidget widget) throws Exception
	{
		DomHelper helper = DomainTestUtil.createHelper(dataFile);
		Assert.assertEquals(1, helper.xpathAsInt("count(/korsakow/interfaces/Interface[id=?]/widgets/Widget[name=?])", interfaceId, widget.getName())); // poorly written test
		long widgetId = helper.xpathAsLong("/korsakow/interfaces/Interface[id=?]/widgets/Widget[name=?]/id", interfaceId, widget.getName());
		Assert.assertEquals(widget.getName(), helper.xpathAsString("/korsakow/interfaces/Interface[id=?]/widgets/Widget[id=?]/name", interfaceId, widgetId));
		Assert.assertEquals(widget.getX(), helper.xpathAsInt("/korsakow/interfaces/Interface[id=?]/widgets/Widget[id=?]/x", interfaceId, widgetId));
		Assert.assertEquals(widget.getY(), helper.xpathAsInt("/korsakow/interfaces/Interface[id=?]/widgets/Widget[id=?]/y", interfaceId, widgetId));
		Assert.assertEquals(widget.getWidth(), helper.xpathAsInt("/korsakow/interfaces/Interface[id=?]/widgets/Widget[id=?]/width", interfaceId, widgetId));
		Assert.assertEquals(widget.getHeight(), helper.xpathAsInt("/korsakow/interfaces/Interface[id=?]/widgets/Widget[id=?]/height", interfaceId, widgetId));
		
		assertEqualProperties(helper, widget, interfaceId);
		assertEqualKeywords(helper, widget.getKeywords(), "/korsakow/interfaces/Interface[id=?]/widgets/Widget[id=?]", interfaceId, widgetId);
	}
	public static void assertEqual(File dataFile, long resourceId, org.korsakow.domain.interf.IRule rule) throws Exception
	{
		DomHelper helper = DomainTestUtil.createHelper(dataFile);
		Assert.assertEquals(1, helper.xpathAsInt("count(/korsakow/descendant::*[id=?]/rules/Rule[name=?])", resourceId, rule.getName())); // poorly written test
		long ruleId = helper.xpathAsLong("/korsakow/descendant::*[id=?]/rules/Rule[name=?]/id", resourceId, rule.getName());
		Assert.assertEquals(rule.getName(), helper.xpathAsString("/korsakow/descendant::*[id=?]/rules/Rule[id=?]/name", resourceId, ruleId));
		Assert.assertEquals(rule.getRuleType(), helper.xpathAsString("/korsakow/descendant::*[id=?]/rules/Rule[id=?]/type", resourceId, ruleId));
		Assert.assertEquals(rule.getTriggerTime(), helper.xpathAsLong("/korsakow/descendant::*[id=?]/rules/Rule[id=?]/triggerTime", resourceId, ruleId));
		
		assertEqualProperties(helper, rule, resourceId);
		assertEqualKeywords(helper, rule.getKeywords(), "/korsakow/descendant::*[id=?]/rules/Rule[id=?]", resourceId, ruleId);
	}
	public static void assertEqual(File dataFile, long id, org.korsakow.domain.interf.IProject project) throws Exception
	{
		DomHelper helper = DomainTestUtil.createHelper(dataFile);
		Assert.assertEquals(project.getName(), helper.xpathAsString("/korsakow/projects/Project[id=?]/name", id));
		Assert.assertEquals(project.getBackgroundSoundVolume(), helper.xpathAsFloat("/korsakow/projects/Project[id=?]/backgroundSoundVolume", id), 0);
		Assert.assertEquals(project.getClickSoundVolume(), helper.xpathAsFloat("/korsakow/projects/Project[id=?]/clickSoundVolume", id), 0);
		Assert.assertEquals(project.getMovieWidth(), helper.xpathAsInt("/korsakow/projects/Project[id=?]/movieWidth", id));
		Assert.assertEquals(project.getMovieHeight(), helper.xpathAsInt("/korsakow/projects/Project[id=?]/movieHeight", id));
		Assert.assertEquals(project.getRandomLinkMode(), helper.xpathAsBoolean("/korsakow/projects/Project[id=?]/randomLinkMode", id));
		Assert.assertEquals(project.getKeepLinksOnEmptySearch(), helper.xpathAsBoolean("/korsakow/projects/Project[id=?]/keepLinksOnEmptySearch", id));
		Assert.assertEquals((long)project.getMaxLinks(), helper.xpathAsLong("/korsakow/projects/Project[id=?]/maxLinks", id));
		
		for (IRule rule : project.getRules())
			assertEqual(dataFile, rule.getId(), rule);
		assertEqualKeywords(helper, project.getKeywords(), "/korsakow/projects/Project[id=?]", id);
	}
	public static void assertTextNotExist(File dataFile, long id) throws Exception
	{
		DomHelper helper = DomainTestUtil.createHelper(dataFile);
		NodeList list = helper.xpathAsNodeList("/korsakow/texts/Text[id=?]", id);
		Assert.assertEquals(0, list.getLength());
	}
	public static void assertSoundNotExist(File dataFile, long id) throws Exception
	{
		DomHelper helper = DomainTestUtil.createHelper(dataFile);
		NodeList list = helper.xpathAsNodeList("/korsakow/sounds/Sound[id=?]", id);
		Assert.assertEquals(0, list.getLength());
	}
	public static void assertVideoExist(File dataFile, long id, boolean exist) throws Exception
	{
		DomHelper helper = DomainTestUtil.createHelper(dataFile);
		NodeList list = helper.xpathAsNodeList("/korsakow/videos/Video[id=?]", id);
		Assert.assertEquals(exist?1:0, list.getLength());
	}
	public static void assertImageNotExist(File dataFile, long id) throws Exception
	{
		DomHelper helper = DomainTestUtil.createHelper(dataFile);
		NodeList list = helper.xpathAsNodeList("/korsakow/images/Image[id=?]", id);
		Assert.assertEquals(0, list.getLength());
	}
	public static void assertSnuNotExist(File dataFile, long id) throws Exception
	{
		DomHelper helper = DomainTestUtil.createHelper(dataFile);
		NodeList list = helper.xpathAsNodeList("/korsakow/snus/Snu[id=?]", id);
		Assert.assertEquals(0, list.getLength());
	}
	public static void assertInterfaceNotExist(File dataFile, long id) throws Exception
	{
		DomHelper helper = DomainTestUtil.createHelper(dataFile);
		NodeList list = helper.xpathAsNodeList("/korsakow/interfaces/Interface[id=?]", id);
		Assert.assertEquals(0, list.getLength());
	}
}