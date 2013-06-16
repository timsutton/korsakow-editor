/**
 * 
 */
package test.util;

import java.io.File;

import org.junit.Assert;


public class MoreAsserts
{
	public static void assertInstanceOf(Class expected, Object actual)
	{
		Assert.assertTrue((actual!=null?actual.getClass().getCanonicalName():null) + " instanceof " + expected.getCanonicalName(), expected.isInstance(actual));
	}
	public static void assertEquals(float f1, float f2, float tolerance)
	{
		if (Math.abs(f1-f2) > tolerance)
			throw new AssertionError("Math.abs(" + f1 + " - " + f2 + ") > " + tolerance);
	}
	public static void assertEquals(int expected, int actual, int tolerance)
	{
		if (Math.abs(expected-actual) > tolerance)
			throw new AssertionError("Math.abs(" + expected + " - " + actual + ") > " + tolerance);
	}
	public static void assertFileNotExists(File file)
	{
		Assert.assertFalse("File Not Exists: " + file.getAbsolutePath(), file.exists() && file.canRead()); // canRead, why not
	}
	public static void assertFileExists(File file)
	{
		Assert.assertTrue("File Exists: " + file.getAbsolutePath(), file.exists() && file.canRead()); // canRead, why not
	}
	public static void assertFileExists(String file)
	{
		assertFileExists(new File(file));
	}
	public static void assertPositive(float f)
	{
		if (f <= 0)
			throw new AssertionError(f + " <= 0");
	}
	public static void assertNegative(float f)
	{
		if (f >= 0)
			throw new AssertionError(f + " >= 0");
	}
	public static void assertNonNegative(float f)
	{
		if (f < 0)
			throw new AssertionError(f + " < 0");
	}
	public static void assertNonPositive(float f)
	{
		if (f > 0)
			throw new AssertionError(f + " > 0");
	}
}