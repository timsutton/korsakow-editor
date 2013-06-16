package test.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Assert;

import org.korsakow.ide.util.FileUtil;

public class WordFactory
{
	private static List<String> dictionaryWords = null;
	private static List<String> getDictionaryWords() throws IOException {
		if (dictionaryWords == null) {
			dictionaryWords = FileUtil.readFileLines(new File("resources/dict"));
		}
		return new ArrayList<String>(dictionaryWords);
	}
	public static Set<String> getRandomWords(int maxWords) throws IOException
	{
		return getRandomWords(maxWords, Collections.EMPTY_LIST);
	}
	public static Set<String> getRandomWords(int maxWords, Collection<String> notContaining) throws IOException
	{
		List<String> words = getDictionaryWords();
		words.removeAll(notContaining);
		int N = 1;
		if (maxWords > 1)
			N += new Random().nextInt(maxWords-1);
		Set<String> results = new HashSet<String>();
		for (int i = 0; i < N; ++i)
			results.add(words.get(new Random().nextInt(words.size())));
		Assert.assertNotSame(0, results.size()); // possible but unlikely given size of dictionary
		return results;
	}
	public static String createRandomWordString(int numWords) throws IOException
	{
		return createRandomWordString(numWords, Collections.EMPTY_LIST);
	}
	public static String createRandomWordString(int numWords, Collection<String> notContaining) throws IOException
	{
		Set<String> words = getRandomWords(numWords, notContaining);
		StringBuilder sb = new StringBuilder();
		for (String word : words)
			sb.append(word).append(" ");
		return sb.toString();
	}
}
