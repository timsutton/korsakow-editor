package test.org.korsakow.command;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Assert;

import org.dsrg.soenea.uow.UoW;
import org.junit.Test;
import org.korsakow.domain.KeywordFactory;
import org.korsakow.domain.SnuFactory;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.command.Response;
import org.korsakow.domain.command.SimulatedSearchCommand;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IRule;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.ide.DataRegistry;

import test.org.korsakow.domain.AbstractDomainObjectTestCase;
import test.util.DOFactory;
import test.util.DomainTestUtil;

public class TestSimulatedSearchCommand extends AbstractDomainObjectTestCase
{
	private static final int MIN_KEYWORD_LENGTH = 10; // should be not-too-small to avoid possibility of collisions
	private static final int MAX_KEYWORD_LENGTH = 32;
	
	@Test public void testSearchFindsSingleSnuBySingleKeyword() throws Exception
	{
		final String keyword = DomainTestUtil.getRandomString(MIN_KEYWORD_LENGTH, MAX_KEYWORD_LENGTH);
		
		ISnu searchSnu = createSnuWithKeywordLookup(parentDir, keyword);
		
		// expected search result
		ISnu toFind1 = SnuFactory.createNew(DataRegistry.getMaxId(), 0);
		DomainTestUtil.initializeRandom(toFind1);
		toFind1.setName(""); // avoid search hits by name
		toFind1.setKeywords(Arrays.asList((IKeyword)KeywordFactory.createNew(keyword)));
		
		// generate some potential false positives
		for (int i = 0; i < 10; ++i) {
			final String nonExistant = DomainTestUtil.getRandomString(MIN_KEYWORD_LENGTH, MAX_KEYWORD_LENGTH);
			Assert.assertFalse(keyword.equals(nonExistant));
			
			ISnu toNotFind = SnuFactory.createNew(DataRegistry.getMaxId(), 0);
			DomainTestUtil.initializeRandom(toNotFind);
			toNotFind.setName(""); // avoid search hits by name
			toNotFind.setKeywords(Arrays.asList((IKeyword)KeywordFactory.createNew(nonExistant)));
		}
		
		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		Request request = new Request();
		request.set("id", searchSnu.getId());
		Response response = new Response();
		new SimulatedSearchCommand(request, response).execute();
		
		List<ISnu> results = (List<ISnu>)response.get("results");
		
		Assert.assertEquals(1, results.size());
		DomainTestUtil.assertEqual(toFind1, results.get(0));
	}
	@Test public void testSearchNonExistantKeywordFindsNothing() throws Exception
	{
		final String nonExistant = DomainTestUtil.getRandomString(MIN_KEYWORD_LENGTH, MAX_KEYWORD_LENGTH);
		
		ISnu searchSnu = createSnuWithKeywordLookup(parentDir, nonExistant);
		
		// generate some potential false positives
		for (int i = 0; i < 10; ++i) {
			final String keyword = DomainTestUtil.getRandomString(MIN_KEYWORD_LENGTH, MAX_KEYWORD_LENGTH);
			Assert.assertFalse(keyword.equals(nonExistant));
			
			ISnu toNotFind = SnuFactory.createNew();
			DomainTestUtil.initializeRandom(toNotFind);
			toNotFind.setName(""); // avoid search hits by name
			toNotFind.setKeywords(Arrays.asList((IKeyword)KeywordFactory.createNew(keyword)));
		}
		
		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		Request request = new Request();
		request.set("id", searchSnu.getId());
		Response response = new Response();
		new SimulatedSearchCommand(request, response).execute();
		
		List<ISnu> results = (List<ISnu>)response.get("results");
		
		Assert.assertEquals(0, results.size());
	}
	@Test public void testSearchFindsSeveralSnusByKeywords() throws Exception
	{
		List<String> keywordsToFind = Arrays.asList(
				DomainTestUtil.getRandomString(MIN_KEYWORD_LENGTH, MAX_KEYWORD_LENGTH),
				DomainTestUtil.getRandomString(MIN_KEYWORD_LENGTH, MAX_KEYWORD_LENGTH),
				DomainTestUtil.getRandomString(MIN_KEYWORD_LENGTH, MAX_KEYWORD_LENGTH),
				DomainTestUtil.getRandomString(MIN_KEYWORD_LENGTH, MAX_KEYWORD_LENGTH),
				DomainTestUtil.getRandomString(MIN_KEYWORD_LENGTH, MAX_KEYWORD_LENGTH)
		);
		
		// expected search result
		Collection<ISnu> snusToFind = new ArrayList<ISnu>();
		for (int i = 0; i < 5; ++i)
		{
			ISnu toFind = SnuFactory.createNew();
			DomainTestUtil.initializeRandom(toFind);
			toFind.setName(""); // avoid search hits by name
			List<String> keywords = getRandomNonEmptySublist(keywordsToFind);
			toFind.setKeywords(DOFactory.createKeywords(keywords));
			snusToFind.add(toFind);
		}
		
		// generate some potential false positives
		for (int i = 0; i < 10; ++i) {
			final String keyword = DomainTestUtil.getRandomString(MIN_KEYWORD_LENGTH, MAX_KEYWORD_LENGTH);
			Assert.assertFalse(keywordsToFind.contains(keyword)); // basically these kinds of things are unlikely enough that i assert instead of handling the possibility
			
			ISnu toNotFind = SnuFactory.createNew();
			DomainTestUtil.initializeRandom(toNotFind);
			toNotFind.setName(""); // avoid search hits by name
			toNotFind.setKeywords(Arrays.asList((IKeyword)KeywordFactory.createNew(keyword)));
		}
		
		ISnu searchSnu = createSnuWithKeywordLookup(parentDir, keywordsToFind);
		
		
		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		Request request = new Request();
		request.set("id", searchSnu.getId());
		Response response = new Response();
		new SimulatedSearchCommand(request, response).execute();
		
		List<ISnu> results = (List<ISnu>)response.get("results");
		
		Assert.assertEquals(snusToFind.size(), results.size());
		for (ISnu expected : snusToFind)
			Assert.assertTrue(results.contains(expected)); // DO equality by id
	}
	@Test public void testSearchResultRanking() throws Exception
	{
		List<String> keywordsToFind = Arrays.asList(
				DomainTestUtil.getRandomString(MIN_KEYWORD_LENGTH, MAX_KEYWORD_LENGTH),
				DomainTestUtil.getRandomString(MIN_KEYWORD_LENGTH, MAX_KEYWORD_LENGTH),
				DomainTestUtil.getRandomString(MIN_KEYWORD_LENGTH, MAX_KEYWORD_LENGTH),
				DomainTestUtil.getRandomString(MIN_KEYWORD_LENGTH, MAX_KEYWORD_LENGTH),
				DomainTestUtil.getRandomString(MIN_KEYWORD_LENGTH, MAX_KEYWORD_LENGTH)
		);
		
//		System.out.println(Util.join(keywordsToFind));
		
		// the expected results each have a different number of matching keywords
		// with the first having one and the last having all
		// except we want to eliminate order of creation/setup as a possible source
		// of ordering so we generate and shuffle the indices in advance
		
		List<Integer> keywordCount = new ArrayList<Integer>();
		for (int i = 0; i < keywordsToFind.size(); ++i)
			keywordCount.add(i+1);
		Collections.shuffle(keywordCount);
		
		final Map<ISnu, Float> expectedOrdering = new HashMap<ISnu, Float>();
		
		// expected search result
		List<ISnu> snusToFind = new ArrayList<ISnu>();
		for (int i = 0; i < keywordsToFind.size(); ++i)
		{
			ISnu toFind = SnuFactory.createNew();
			DomainTestUtil.initializeRandom(toFind); // rating is randomized.
			toFind.setName(""); // avoid search hits by name
			List<String> keywords = new ArrayList<String>();
			int count = keywordCount.get(i);
			for (int j = 0; j < count; ++j)
				keywords.add(keywordsToFind.get(j));
			toFind.setKeywords(DOFactory.createKeywords(keywords));
			snusToFind.add(toFind);
//			System.out.println("E"+toFind.getId()+"\t"+count);
			expectedOrdering.put(toFind, count * toFind.getRating());
		}
		
		// put them in the order we expect to find them
		Collections.sort(snusToFind, new Comparator<ISnu>() {
			public int compare(ISnu o1, ISnu o2) {
				return expectedOrdering.get(o2).compareTo(expectedOrdering.get(o1));
			}
		});
		
		// generate some potential false positives
		for (int i = 0; i < 10; ++i) {
			final String keyword = DomainTestUtil.getRandomString(MIN_KEYWORD_LENGTH, MAX_KEYWORD_LENGTH);
			if (keywordsToFind.contains(keyword)) {
				// this is unlikely if the length & seed of keywords is large enough, so just try again
				--i;
				continue;
			}
			Assert.assertFalse(""+keyword.length(), keywordsToFind.contains(keyword)); // basically these kinds of things are unlikely enough that i assert instead of handling the possibility
			
			ISnu toNotFind = SnuFactory.createNew();
			DomainTestUtil.initializeRandom(toNotFind);
			toNotFind.setName(""); // avoid search hits by name
			toNotFind.setKeywords(Arrays.asList((IKeyword)KeywordFactory.createNew(keyword)));
		}
		
		ISnu searchSnu = createSnuWithKeywordLookup(parentDir, keywordsToFind);
		
		
		UoW.getCurrent().commit();
		UoW.newCurrent();
		
		Request request = new Request();
		request.set("id", searchSnu.getId());
		Response response = new Response();
		new SimulatedSearchCommand(request, response).execute();
		
		List<ISnu> results = (List<ISnu>)response.get("results");
		
//		for (ISnu result : snusToFind)
//			System.out.println("Expected=" + result.getId() + Util.join(result.getKeywords()));
//		for (ISnu result : results)
//			System.out.println("Actual=" + result.getId() + Util.join(result.getKeywords()));
		
		Assert.assertEquals(snusToFind.size(), results.size());
		
		// we found them, and in the expected order
		for (int i = 0; i < snusToFind.size(); ++i)
		{
			long expected = snusToFind.get(i).getId();
			long actual = results.get(i).getId();
//			System.out.println("#"+i+"; Expected=" + expected + " \tActual=" + actual);
			Assert.assertEquals(expected, actual); // DO.equals is currently fuxed
		}
	}
	private static <T> List<T> getRandomNonEmptySublist(List<T> list)
	{
		List<T> shuffled = new ArrayList<T>(list);
		if (list.isEmpty())
			return shuffled;
		
		Collections.shuffle(shuffled);
		
		Random random = new Random();
		shuffled = shuffled.subList(1, 1+random.nextInt(list.size()-1)+1); // 1 guarantees non-empty
		
		Assert.assertTrue(!shuffled.isEmpty());
		return shuffled;
	}
	private static ISnu createSnuWithKeywordLookup(File parentDir, Collection<String> keywords) throws Exception
	{
		return createSnuWithKeywordLookup(parentDir, keywords.toArray(new String[0]));
	}
	private static ISnu createSnuWithKeywordLookup(File parentDir, String... keywords) throws Exception
	{
		ISnu snu = DOFactory.createSnuWithDummyMedia(parentDir);
		
		List<IRule> searchRules = new ArrayList<IRule>();
		searchRules.add(DOFactory.createSearchRule(DOFactory.createKeywordLookupRule(keywords)));
		snu.setRules(searchRules);
		return snu;
	}
}
