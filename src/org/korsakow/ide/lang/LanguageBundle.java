package org.korsakow.ide.lang;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.dsrg.soenea.service.Registry;
import org.korsakow.ide.util.PreferencesManager;
import org.korsakow.ide.util.ResourceManager;

public class LanguageBundle
{
	private static class MyLoader extends ClassLoader {

		@Override
		protected URL findResource(String name)
		{
			try {
				return ResourceManager.getResourceSource().getResourceFile( name ).toURL();
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
		}
		
	}
	private static ClassLoader source = new MyLoader();
	private static ResourceBundle defaultBundle;
	private static ResourceBundle bundle;
	private static synchronized ResourceBundle getBundle()
	{
		if (defaultBundle == null) {
			defaultBundle = ResourceBundle.getBundle("resources/lang/strings", new Locale("EN"), source);
		}
		if (bundle == null) {
			try {
				bundle = ResourceBundle.getBundle("resources/lang/strings", getCurrentLocale(), source);
			} catch (MissingResourceException e) {
				Logger.getLogger(LanguageBundle.class).error("", e);
				bundle = defaultBundle;
			}
		}
		return bundle;
	}
	private static String get(String key, ResourceBundle aBundle)
	{
		String bundleString = null;
		try {
			bundleString = aBundle.getString(key);
		} catch (MissingResourceException firstException) {
			try {
// 2009-11-03: disabling this logging due to spam. anyway we mostly care about missing from the default EN bundle which will still be logged
//	stuff missing from other langs is managed via the strings.properties files
//				Logger.getLogger(LanguageBundle.class).warn("Locale=" + getCurrentLocale() + ", key= " + key, firstException);
				bundleString = '['+defaultBundle.getString(key)+']';
			} catch (MissingResourceException secondException) { // usually the default bundle will have all strings
				throw firstException;
			}
		}
		return bundleString;
	}
	public static String getStringFromDefaultBundle(String key, Object... args)
	{
		getBundle();
		
		String bundleString = get(key, defaultBundle);
		try {
			// hack to get the bundle in UTF-8 with JDK prior to 1.6
			bundleString = new String(bundleString.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Logger.getLogger(LanguageBundle.class).error("", e);
		}
		bundleString = bundleString.replace("'", "''"); // messageformat hack... we don't currently use any of the fancy stuff in there at the moment
		return MessageFormat.format(bundleString, args);
	}
	public static String getString(String key, Object... args)
	{
		String bundleString = get(key, getBundle());
		try {
			// hack to get the bundle in UTF-8 with JDK prior to 1.6
			bundleString = new String(bundleString.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Logger.getLogger(LanguageBundle.class).error("", e);
		}
		bundleString = escapeMessageFormat(bundleString);
		return MessageFormat.format(bundleString, args);
	}
	/**
	 * Returns whether the given key exists. This method checks against the default bundle, not the current
	 * language bundle.
	 */
	public static boolean hasString(String key)
	{
		try {
			// we check against the default bundle
			defaultBundle.getString(key);
			return true;
		} catch (MissingResourceException e) {
			return false;
		}
	}
	public static int getInt(String key)
	{
		return Integer.parseInt(getString(key));
	}
	/**
	 * Gets the list of locales for which we actually have a bundle.
	 * @return
	 */
	public static Collection<Locale> getAvailableLocales()	
	{
		Collection<Locale> locales = new ArrayList<Locale>();
		try {
			String localesstr = Registry.getProperty("locales");
			String[] localesarray = localesstr.split(",");
			for (String localename : localesarray)
				locales.add(new Locale(localename));
		} catch (Exception e1) {
			locales = Arrays.asList(new Locale("EN"));
		}
		return locales;
	}
	public static void setCurrentLocale(Locale locale)
	{
		Logger.getLogger(LanguageBundle.class).info("set locale = " + locale.getDisplayName());
		PreferencesManager.getPreferences(LanguageBundle.class).put("locale_language", locale.getLanguage());
		PreferencesManager.getPreferences(LanguageBundle.class).put("locale_country", locale.getCountry());
		PreferencesManager.getPreferences(LanguageBundle.class).put("locale_variant", locale.getVariant());
	}
	public static Locale getCurrentLocale()
	{
		Locale systemDefaultLocale = Locale.getDefault();
		String language = PreferencesManager.getPreferences(LanguageBundle.class).get("locale_language", systemDefaultLocale.getLanguage());
		String country = PreferencesManager.getPreferences(LanguageBundle.class).get("locale_country", systemDefaultLocale.getCountry());
		String variant = PreferencesManager.getPreferences(LanguageBundle.class).get("locale_variant", systemDefaultLocale.getVariant());
		Logger.getLogger(LanguageBundle.class).info("get locale = " + new Locale(language, country, variant).getDisplayName());
		return new Locale(language, country, variant);
	}
	/**
	 * Takes a string that was not properly escaped for MessageFormat and escapes special chars.
	 * E.g. apostrophes are doubled.
	 */
	public static String escapeMessageFormat(String naive)
	{
		return naive.replace("'", "''"); // messageformat hack... we don't currently use any of the fancy stuff in there at the moment	}
	}
}
