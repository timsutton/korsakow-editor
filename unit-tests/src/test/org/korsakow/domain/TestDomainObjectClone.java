package test.org.korsakow.domain;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.korsakow.domain.CloneFactory;
import org.korsakow.domain.ImageFactory;
import org.korsakow.domain.PredicateFactory;
import org.korsakow.domain.RuleFactory;
import org.korsakow.domain.SnuFactory;
import org.korsakow.domain.SoundFactory;
import org.korsakow.domain.TextFactory;
import org.korsakow.domain.TriggerFactory;
import org.korsakow.domain.VideoFactory;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IPredicate;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.interf.IText;
import org.korsakow.domain.interf.ITrigger;
import org.korsakow.domain.interf.IVideo;
import org.korsakow.ide.resources.PredicateType;
import org.korsakow.ide.resources.TriggerType;

import test.util.DOFactory;
import test.util.DomainTestUtil;

/**
 * TODO: test that the clone (or its properties, for example list properties) is not == to in certain cases
 * @author d
 *
 */
public class TestDomainObjectClone extends AbstractDomainObjectTestCase
{
	private Random random;
	
	@Override
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
		random = new Random();
	}
	/**
	 * Testing the test framework, as it were. I like to sprinkle these here and there.
	 */
	@Test public void testVideoFalsePositive() throws Exception
	{
		IVideo original = VideoFactory.createNew(random.nextLong(), random.nextLong());
		DomainTestUtil.initializeRandom(original);
		IVideo clone = CloneFactory.clone(original);
		DomainTestUtil.initializeRandom(clone); // mixing things up a little. in theory this test could fail if the random values lined up just right.
		try {
			DomainTestUtil.assertEqual(original, clone.getName(), clone.getKeywords(), clone.getSource(), clone.getFilename());
			throw new Exception("Assert Failed!"); // have to throw something that wont get caught 
		} catch (AssertionError expected) {
			// good!
		}
	}
	/**
	 * @throws Exception
	 */
	@Test public void testInterfaceFalsePositive() throws Exception
	{
		IInterface original = DOFactory.createDefaultInterface();
		DomainTestUtil.initializeRandom(original, true);
		IInterface clone = CloneFactory.clone(original);
		clone.getWidgets().iterator().next().setX(clone.getWidgets().iterator().next().getX()+1); // will this get detected?
		try {
			DomainTestUtil.assertEqual(original, clone.getName(), clone.getKeywords(), clone.getClickSoundVolume(), clone.getGridHeight(), clone.getGridWidth(), clone.getViewHeight(), clone.getViewWidth(), clone.getWidgets());
			throw new Exception("Assert Failed!"); // have to throw something that wont get caught 
		} catch (AssertionError expected) {
			// good!
		}
	}
	@Test public void testCloneVideo() throws Exception
	{
		IVideo original = VideoFactory.createNew(random.nextLong(), random.nextLong());
		DomainTestUtil.initializeRandom(original);
		IVideo clone = CloneFactory.clone(original);
		Assert.assertTrue(original.getId() != clone.getId());
		DomainTestUtil.assertEqual(original, clone.getName(), clone.getKeywords(), clone.getSource(), clone.getFilename());
	}
	@Test public void testCloneImage() throws Exception
	{
		IImage original = ImageFactory.createNew(random.nextLong(), random.nextLong());
		DomainTestUtil.initializeRandom(original);
		IImage clone = CloneFactory.clone(original);
		Assert.assertTrue(original.getId() != clone.getId());
		DomainTestUtil.assertEqual(original, clone.getName(), clone.getKeywords(), clone.getSource(), clone.getFilename());
	}
	@Test public void testCloneText() throws Exception
	{
		IText original = TextFactory.createNew(random.nextLong(), random.nextLong());
		DomainTestUtil.initializeRandom(original);
		IText clone = CloneFactory.clone(original);
		Assert.assertTrue(original.getId() != clone.getId());
		DomainTestUtil.assertEqual(original, clone.getName(), clone.getKeywords(), clone.getSource(), clone.getFilename());
	}
	@Test public void testCloneSound() throws Exception
	{
		ISound original = SoundFactory.createNew(random.nextLong(), random.nextLong());
		DomainTestUtil.initializeRandom(original);
		ISound clone = CloneFactory.clone(original);
		Assert.assertTrue(original.getId() != clone.getId());
		DomainTestUtil.assertEqual(original, clone.getName(), clone.getKeywords(), clone.getSource(), clone.getFilename());
	}
	@Test public void testCloneSnu() throws Exception
	{
		ISnu original = SnuFactory.createNew(random.nextLong(), random.nextLong());
		DomainTestUtil.initializeRandom(original);
		ISnu clone = CloneFactory.clone(original);
		Assert.assertTrue(original.getId() != clone.getId());
		DomainTestUtil.assertEqual(original, clone);
	}
	@Test public void testCloneSnuAlsoClonesMainMedia() throws Exception
	{
		ISnu original = SnuFactory.createNew(random.nextLong(), random.nextLong());
		DomainTestUtil.initializeRandom(original);
		IVideo originalMainMedia = VideoFactory.createNew(random.nextLong(), random.nextLong());
		DomainTestUtil.initializeRandom(originalMainMedia);
		original.setMainMedia(originalMainMedia);
		original.setPreviewMedia(originalMainMedia);
		ISnu clone = CloneFactory.clone(original);
		
		Assert.assertTrue(original.getId() != clone.getId());
		DomainTestUtil.assertEqual(original, clone);
		
		Assert.assertTrue(originalMainMedia.getId() != clone.getMainMedia().getId());
		DomainTestUtil.assertEqual(originalMainMedia, (IVideo)clone.getMainMedia());
		DomainTestUtil.assertEqual((IVideo)original.getPreviewMedia(), (IVideo)clone.getPreviewMedia());
	}
	@Test public void testCloneInterface() throws Exception
	{
		IInterface original = DOFactory.createDefaultInterface();
		DomainTestUtil.initializeRandom(original, false);
		IInterface clone = CloneFactory.clone(original);
		Assert.assertTrue(original.getId() != clone.getId());
		DomainTestUtil.assertEqual(original, clone.getName(), clone.getKeywords(), clone.getClickSoundVolume(), clone.getGridHeight(), clone.getGridWidth(), clone.getViewHeight(), clone.getViewWidth(), clone.getWidgets());
	}
	@Test public void testCloneRule() throws Exception
	{
		IRule original = RuleFactory.createNew(random.nextLong(), random.nextLong());
		DomainTestUtil.initializeRandom(original);
		IRule clone = CloneFactory.clone(original);
		Assert.assertTrue(original.getId() != clone.getId());
		DomainTestUtil.assertEqual(original, clone.getName(), clone.getKeywords(), clone, clone.getTriggerTime(), clone.getRuleType());
	}
	@Test public void testClonePredicate() throws Exception
	{
		IPredicate original = PredicateFactory.createClean(random.nextLong(), random.nextLong(), PredicateType.values()[0].getId(), new ArrayList<IPredicate>());
		DomainTestUtil.initializeRandom(original);
		IPredicate clone = CloneFactory.clone(original);
		Assert.assertTrue(original.getId() != clone.getId());
		DomainTestUtil.assertEqual(original, clone, clone.getPredicateType());
	}
	@Test public void testCloneTrigger() throws Exception
	{
		ITrigger original = TriggerFactory.createClean(random.nextLong(), random.nextLong(), TriggerType.values()[0].getId());
		DomainTestUtil.initializeRandom(original);
		ITrigger clone = CloneFactory.clone(original);
		Assert.assertTrue(original.getId() != clone.getId());
		DomainTestUtil.assertEqual(original, clone, clone.getTriggerType());
	}
}
