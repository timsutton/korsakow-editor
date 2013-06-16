package test.bugs;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.ResourceType;

import test.util.BaseTestCase;

/**
 */
public class Test997 extends BaseTestCase {

	@Override
	@Before
	public void setUp() throws Exception
	{
	}
	
	@Override
	@After
	public void tearDown() throws Exception
	{
	}
	
	@Test public void testReproduceReported() throws Throwable
	{
		/*
		 * Poor volapuk, we assume it won't be supported. volapuk is a constructed language though, so hopefully no one is offended (other than mr. Schleyer) =P
		 */
		Locale unsupported = new Locale("vo");
		Locale.setDefault(unsupported);
		LanguageBundle.setCurrentLocale(unsupported);
		
		ResourceType.SNU.getDisplayString();
	}
}
