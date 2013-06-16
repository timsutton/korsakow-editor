package test.org.korsakow.k3;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.korsakow.domain.Keyword;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.domain.k3.importer.K3Importer;
import org.korsakow.domain.mapper.input.SnuInputMapper;
import org.korsakow.domain.task.ITask;
import org.korsakow.domain.task.IWorker;
import org.korsakow.ide.Application;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.rules.RuleType;
import org.korsakow.ide.task.Task;
import org.korsakow.ide.task.UIWorker;
import org.korsakow.ide.util.FileUtil;
import org.korsakow.ide.util.ResourceManager;
import org.w3c.dom.Document;

import test.util.BaseTestCase;
import test.util.TestResourceSource;

/**
 * @author d
 *
 */
public class TestImportFeatures extends BaseTestCase
{
	private File baseFile;
	private IProject project;
	
	@Override
	@Before
	public void setUp() throws Exception
	{
		baseFile = FileUtil.createTempDirectory("k3import", "korsakow");
		baseFile.deleteOnExit();
		
		Application.initializeInstance();

		project = commonImport("myfilm");
	}
	@Override
	@After
	public void tearDown()
	{
		project = null;
		Application.destroyInstance();
	}
	@Test public void testMovieSize() throws Exception
	{
		Assert.assertEquals(812, project.getMovieWidth());
		Assert.assertEquals(634, project.getMovieHeight());
	}
	@Test public void testProjectRandomLinkMode() throws Exception
	{
		Assert.assertEquals(true, project.getRandomLinkMode());
	}
	@Test public void testInsertTextFontFamily() throws Exception
	{
		IInterface interf = project.getInterfaces().iterator().next();
		boolean found = false;
		for (IWidget widget : interf.getWidgets()) {
			if (widget.getWidgetId().equals(WidgetType.InsertText.getId())) {
				found = true;
				Assert.assertEquals("Garamond", widget.getDynamicProperty("fontFamily"));
			}
		}
		Assert.assertTrue(found);
	}
	@Test public void testInsertTextFontColor() throws Exception
	{
		IInterface interf = project.getInterfaces().iterator().next();
		boolean found = false;
		for (IWidget widget : interf.getWidgets()) {
			if (widget.getWidgetId().equals(WidgetType.InsertText.getId())) {
				found = true;
				Assert.assertNull(widget.getDynamicProperty("fontColor"));
			}
		}
		Assert.assertTrue(found);
	}
	@Test public void testInsertTextFontSize() throws Exception
	{
		IInterface interf = project.getInterfaces().iterator().next();
		boolean found = false;
		for (IWidget widget : interf.getWidgets()) {
			if (widget.getWidgetId().equals(WidgetType.InsertText.getId())) {
				found = true;
				Assert.assertEquals(14, Integer.parseInt(""+widget.getDynamicProperty("fontSize")));
			}
		}
		Assert.assertTrue(found);
	}
	@Test public void testPreviewTextFontFamily() throws Exception
	{
		IInterface interf = project.getInterfaces().iterator().next();
		boolean found = false;
		for (IWidget widget : interf.getWidgets()) {
			if (widget.getWidgetId().equals(WidgetType.SnuAutoLink.getId())) {
				found = true;
				Assert.assertNull(widget.getDynamicProperty("fontFamily"));
			}
		}
		Assert.assertTrue(found);
	}
	@Test public void testPreviewTextFontColor() throws Exception
	{
		IInterface interf = project.getInterfaces().iterator().next();
		boolean found = false;
		for (IWidget widget : interf.getWidgets()) {
			if (widget.getWidgetId().equals(WidgetType.SnuAutoLink.getId())) {
				found = true;
				Assert.assertEquals(123, Integer.parseInt(""+widget.getDynamicProperty("fontColor")));
			}
		}
		Assert.assertTrue(found);
	}
	@Test public void testPreviewTextFontSize() throws Exception
	{
		IInterface interf = project.getInterfaces().iterator().next();
		boolean found = false;
		for (IWidget widget : interf.getWidgets()) {
			if (widget.getWidgetId().equals(WidgetType.SnuAutoLink.getId())) {
				found = true;
				Assert.assertEquals(9, Integer.parseInt(""+widget.getDynamicProperty("fontSize")));
			}
		}
		Assert.assertTrue(found);
	}
	@Test public void testSubtitleTextFontFamily() throws Exception
	{
		IInterface interf = project.getInterfaces().iterator().next();
		boolean found = false;
		for (IWidget widget : interf.getWidgets()) {
			if (widget.getWidgetId().equals(WidgetType.Subtitles.getId())) {
				found = true;
				Assert.assertEquals("Arial", widget.getDynamicProperty("fontFamily"));
			}
		}
		Assert.assertTrue(found);
	}
	@Test public void testSubtitleTextFontColor() throws Exception
	{
		IInterface interf = project.getInterfaces().iterator().next();
		boolean found = false;
		for (IWidget widget : interf.getWidgets()) {
			if (widget.getWidgetId().equals(WidgetType.Subtitles.getId())) {
				found = true;
				Assert.assertEquals(456, Integer.parseInt(""+widget.getDynamicProperty("fontColor")));
			}
		}
		Assert.assertTrue(found);
	}
	@Test public void testSubtitleTextFontSize() throws Exception
	{
		IInterface interf = project.getInterfaces().iterator().next();
		boolean found = false;
		for (IWidget widget : interf.getWidgets()) {
			if (widget.getWidgetId().equals(WidgetType.Subtitles.getId())) {
				found = true;
				Assert.assertEquals(20, Integer.parseInt(""+widget.getDynamicProperty("fontSize")));
			}
		}
		Assert.assertTrue(found);
	}
	@Test public void testSearchRuleMaxLinks() throws Exception
	{
		ISnu snu = SnuInputMapper.findByName("testSearchRuleMaxLinks").iterator().next();
		List<IRule> searchRules = snu.getRules();
		Assert.assertNull(searchRules.get(0).getDynamicProperty("maxLinks"));
		Assert.assertEquals(31415L, Long.parseLong(""+searchRules.get(1).getDynamicProperty("maxLinks")));
		Assert.assertEquals(27182L, Long.parseLong(""+searchRules.get(2).getDynamicProperty("maxLinks")));
	}
	@Test public void testSearchRuleTriggerTime() throws Exception
	{

		ISnu snu = SnuInputMapper.findByName("testSearchRuleTriggerTime").iterator().next();
		List<IRule> searchRules = snu.getRules();
		Assert.assertEquals(23000, searchRules.get(0).getTriggerTime());
		Assert.assertEquals(229000, searchRules.get(1).getTriggerTime());
		Assert.assertEquals(4173000, searchRules.get(2).getTriggerTime());
	}
	@Test public void testSearchRuleKeywordLookup() throws Exception
	{

		ISnu snu = SnuInputMapper.findByName("testSearchRuleKeywordLookup").iterator().next();
		List<IRule> searchRules = snu.getRules();
		Assert.assertEquals(0000, searchRules.get(0).getTriggerTime());
		for (IRule rule : searchRules.get(0).getRules()) {
			Assert.assertEquals(RuleType.KeywordLookup.getId(), rule.getRuleType());
			Collection<String> keywords = Keyword.toStrings(rule.getKeywords());
			Assert.assertEquals(2, keywords.size());
			Assert.assertTrue(keywords.contains("dog"));
			Assert.assertTrue(keywords.contains("cat"));
		}
		Assert.assertEquals(1000, searchRules.get(1).getTriggerTime());
		for (IRule rule : searchRules.get(1).getRules()) {
			Assert.assertEquals(RuleType.KeywordLookup.getId(), rule.getRuleType());
			Collection<String> keywords = Keyword.toStrings(rule.getKeywords());
			Assert.assertEquals(3, keywords.size());
			Assert.assertTrue(keywords.contains("man"));
			Assert.assertTrue(keywords.contains("woman"));
			Assert.assertTrue(keywords.contains("child"));
		}
		Assert.assertEquals(2000, searchRules.get(2).getTriggerTime());
		for (IRule rule : searchRules.get(2).getRules()) {
			Assert.assertEquals(RuleType.KeywordLookup.getId(), rule.getRuleType());
			Collection<String> keywords = Keyword.toStrings(rule.getKeywords());
			Assert.assertEquals(4, keywords.size());
			Assert.assertTrue(keywords.contains("neutral"));
			Assert.assertTrue(keywords.contains("good"));
			Assert.assertTrue(keywords.contains("bad"));
			Assert.assertTrue(keywords.contains("ugly"));
		}
	}
	@Test public void testSearchRuleClearScores() throws Exception
	{

		ISnu snu = SnuInputMapper.findByName("testSearchRuleClearScores").iterator().next();
		List<IRule> searchRules = snu.getRules();
		Assert.assertEquals(RuleType.ClearScores.getId(), searchRules.get(0).getRules().get(0).getRuleType());
		for (IRule rule : searchRules.get(1).getRules())
			Assert.assertFalse(RuleType.ClearScores.getId().equals(rule.getRuleType()));
	}
	@Test public void testSnuIsStarter() throws Exception
	{
		ISnu snu = SnuInputMapper.findByName("starter.mov").iterator().next();
		Assert.assertEquals(true, snu.getStarter());
	}
	@Test public void testSnuGeneral() throws Exception
	{

		List<ISnu> snus = SnuInputMapper.findByName("testSnuGeneral");
		Assert.assertEquals(1, snus.size());
		ISnu snu = snus.get(0);
		Assert.assertEquals("testSnuGeneral", snu.getName());
		Assert.assertEquals((Long)12L, snu.getLives());
		Assert.assertEquals(true, snu.getLooping());
		Assert.assertEquals(false, snu.getStarter());
		Assert.assertEquals(false, snu.getEnder());
		Assert.assertEquals("my preview text", snu.getPreviewText());
		Assert.assertEquals("your insert text", snu.getInsertText());
		Assert.assertEquals(6.22F, snu.getRating(), 0);
		
		IMedia mainMedia = snu.getMainMedia();
		Assert.assertNotNull(mainMedia);
		Assert.assertEquals("testSnuGeneral", mainMedia.getName());
		File mediaFile = new File(mainMedia.getFilename());
		Assert.assertEquals("myFolder", mediaFile.getParentFile().getName());
//		Assert.assertTrue(mainMedia.getFilename(), mainMedia.getFilename().endsWith("myFolder/testSnuGeneral"));
	}
	private IProject commonImport(String filename) throws Exception
	{
		Document document = DataRegistry.createDefaultEmptyDocument();

		File datafile = File.createTempFile("k3import", ".krw", baseFile);
	    DataRegistry.initialize(document, datafile);
		
		File k3file = new File(TestImport.TESTFILES_BASE + File.separatorChar + filename + File.separatorChar + "data");
		
		K3Importer k3Importer = new K3Importer(k3file);
		
		List<Task> importTasks = k3Importer.createImportTasks();

		List<ITask> uowTasks = new ArrayList<ITask>();
		for (Task task : importTasks)
			uowTasks.add(new TestImport.UoWTask(task));
		IWorker importWorker = new UIWorker(uowTasks);

		importWorker.execute();
		importWorker.waitFor();
		if (importWorker.getException() != null)
			throw (Exception)importWorker.getException();

		return k3Importer.getProject();
	}
}
