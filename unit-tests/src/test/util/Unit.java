/**
 * 
 */
package test.util;

public enum Unit
{
	Bytes("b", 1),
	KiloBytes("KB", 1024),
	MegaBytes("MB", 1024*1024),
	GigaBytes("GB", 1024*1024*1024);
	String postfix;
	double multiplier;
	private Unit(String postfix, double multiplier)
	{
		this.postfix = postfix;
		this.multiplier = multiplier;
	}
	public String getPostfix()
	{
		return postfix;
	}
	public double convertBytes(long bytes)
	{
		return bytes/multiplier;
	}
}