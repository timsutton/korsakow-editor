package test;

public class Warning
{
	private static void printWarning(String message)
	{
		System.err.println("WARNING: " + message);
	}
	public static void warnTrue(String message, boolean tautology)
	{
		if (!tautology)
			printWarning(message);
	}
}
