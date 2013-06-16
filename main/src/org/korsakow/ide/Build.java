package org.korsakow.ide;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.korsakow.ide.util.ResourceManager;

/**
 * @author d
 *
 */
public class Build
{
	private static String BUILD_RESOURCE = "build";
	private static String BUILD_KEY = "build.number";
	private static String RELEASE_KEY = "release.number";
	private static String VERSION_KEY = "version.number";
	
	private static Build instance;
	/**
	 * Used internall by test suites.
	 */
	public static void setInstance(Build build)
	{
		instance = build;
	}
	private static Build getInstance()
	{
		if (instance == null) {
			instance = new Build();
		}
		return instance;
	}
	private Properties properties;

	private Properties getProperties()
	{
		if (properties == null) {
			properties = new Properties();
			try {
				properties.load(ResourceManager.getResourceStream(BUILD_RESOURCE));
			} catch (IOException e) {
				properties.put(RELEASE_KEY, "0.0");
				properties.put(VERSION_KEY, "0.0");
				Logger.getLogger(Build.class).error("", e);
			}
		}
		return properties;
	}
	private String getString(String key)
	{
		Object value = getProperties().get(key);
		return value!=null?value.toString():"";
	}
	private double getDouble(String key)
	{
		return Double.parseDouble(getString(key));
	}
	protected String _getBuild()
	{
		return getString(BUILD_KEY);
	}
	protected double _getRelease()
	{
		return getDouble(RELEASE_KEY);
	}
	protected BigDecimal _getRelease2()
	{
		return new BigDecimal(getString(RELEASE_KEY));
	}
	protected String _getVersion()
	{
		return getString(VERSION_KEY);
	}
	protected String _getAboutString()
	{
		return String.format("Version %s, Release %s", getVersion(), getRelease());
	}
	public static String getBuild()
	{
		return getInstance()._getBuild();
	}
	public static double getRelease()
	{
		return getInstance()._getRelease();
	}
	public static BigDecimal getRelease2()
	{
		return getInstance()._getRelease2();
	}
	public static String getVersion()
	{
		return getInstance()._getVersion();
	}
	public static String getAboutString()
	{
		return getInstance()._getAboutString();
	}
}
