/**
 * 
 */
package test.mock;

import org.korsakow.ide.Build;

public class MockBuild extends Build
{
	private final String build;
	private final float release;
	private final String version;
	public MockBuild(String build, float release, String version)
	{
		this.build = build;
		this.release = release;
		this.version = version;
	}
	@Override
	public String _getBuild()
	{
		return build;
	}
	@Override
	public double _getRelease()
	{
		return release;
	}
	@Override
	public String _getVersion()
	{
		return version;
	}
}