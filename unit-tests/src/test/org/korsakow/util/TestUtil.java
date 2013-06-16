package test.org.korsakow.util;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.korsakow.ide.util.Util;
import org.korsakow.ide.util.Util.Predicate;

public class TestUtil
{
	@Test
	public void testFilter() {
		List<String> src = Arrays.asList("Keep","it","secret","keep","it","safe");
		List<String> expected = Arrays.asList("it","secret","it");
		List<String> actual = Util.filterList(expected, new Predicate<String>() {
			public boolean execute(String s) {
				return s.contains("t");
			}
		});
		Assert.assertEquals(expected, actual);
	}
}
