package org.korsakow.ide.util;

import java.util.prefs.Preferences;

import org.korsakow.ide.Build;

public class PreferencesManager
{
	public static Preferences getVersionIndependentPreferences()
	{
		Preferences prefs = Preferences.userNodeForPackage(PreferencesManager.class).node("unversioned");
		return prefs;
	}
	public static Preferences getPreferences()
	{
		Preferences prefs = Preferences.userNodeForPackage(PreferencesManager.class).node(""+Build.getRelease());
		return prefs;
	}
	public static Preferences getPreferences(Class<?> clazz)
	{
		return getPreferences().node(clazz.getCanonicalName());
	}
}
