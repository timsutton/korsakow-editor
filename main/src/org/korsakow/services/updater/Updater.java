package org.korsakow.services.updater;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.dsrg.soenea.service.Registry;
import org.korsakow.ide.Application;
import org.korsakow.ide.Build;
import org.korsakow.ide.XPathHelper;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.util.DomUtil;
import org.korsakow.ide.util.Platform;
import org.korsakow.ide.util.PreferencesManager;
import org.korsakow.ide.util.ShellExec;
import org.korsakow.ide.util.UIUtil;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Updater extends Thread
{
	private static void setIncludeUsageStats(boolean set) {
		PreferencesManager.getVersionIndependentPreferences().putBoolean("updater.include.usageData", set);
	}
	public static void main(String[] args) {
		setIncludeUsageStats(true);
	}
	
	public static void checkAsynch()
	{
		Updater updater = new Updater();
		updater.start();
	}
	private double availableRelease;
	private String availableVersion = "";
	private String message = "";
	public Updater()
	{
		super(Updater.class.getCanonicalName());
		super.setPriority(Thread.MIN_PRIORITY); // don't be an annoying background hog
	}
	public void startAsynchronousCheck()
	{
		start();
	}
	public void synchronousCheck() throws XPathExpressionException, IOException, SAXException, ParserConfigurationException
	{
		checkUpdate();
		run();
	}
	private void checkUpdate() throws IOException, SAXException, ParserConfigurationException, XPathExpressionException
	{
		String url;
		try {
			url = Registry.getProperty("updater.url");
		} catch (Exception e) {
			throw new IOException(e.getMessage()); // i hate leaking "exception"
		}
		HttpClient client = new HttpClient();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("version", Build.getVersion());
		params.put("release", ""+Build.getRelease());
		if (PreferencesManager.getVersionIndependentPreferences().getBoolean("updater.include.usageData", true)) {
			params.put("uuid", Application.getUUID());
			params.put("platform.arch", Platform.getArch().getCanonicalName());
			params.put("platform.os", Platform.getOS().getCanonicalName());
			params.put("platform.os.version", Platform.getOS().getVersion());
			params.put("locale.country", LanguageBundle.getCurrentLocale().getCountry());
			params.put("locale.language", LanguageBundle.getCurrentLocale().getLanguage());
		}
		
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		for (String key : params.keySet())
			nvp.add(new NameValuePair(key, ""+params.get(key)));
		
		GetMethod request = new GetMethod(url);
		request.setQueryString(nvp.toArray(new NameValuePair[nvp.size()]));
		LogFactory.getLog(getClass()).info(String.format("Checking for updates: %s", request.getURI()));
		client.executeMethod(request);
		String response = request.getResponseBodyAsString();
		Document doc = DomUtil.parseXMLString(response);
		availableRelease = XPathHelper.xpathAsDouble(doc, "/korsakow/release");
		availableVersion = XPathHelper.xpathAsString(doc, "/korsakow/version");
		message = XPathHelper.xpathAsString(doc, "/korsakow/message");
	}
	@Override
	public void run()
	{
		try {
			checkUpdate();
		} catch (IOException e) {
			// we don't notify on IO errors because a common case is when you're using the software without a connection or the site is
			// down or something, don't bug the user about it.
			// other errors like in the format of the XML are exception circumstance.
			Logger.getLogger(Updater.class).error(e);
		} catch (Exception e) {
			UIUtil.runUITaskNow(new ErrorAction(LanguageBundle.getString("updater.error.title"), e));
		}
		if (availableRelease > Build.getRelease()) {
			UIUtil.runUITaskNow(new AlertAction(LanguageBundle.getString("updater.newversion.title"), LanguageBundle.getString("updater.newversion.message", availableVersion, message)));
		}
	}
	private static class AlertAction implements Runnable
	{
		private final String title;
		private final String message;
		public AlertAction(String title, String message)
		{
			this.title = title;
			this.message = message;
		}
		public void run()
		{
			if (Application.getInstance().showOKCancelDialog(title, message)) {
				try {
					ShellExec.openUrl(Registry.getProperty("downloadpage"));
				} catch (Exception e) {
					Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
				}
			}
		}
	}
	private static class ErrorAction implements Runnable
	{
		private final String title;
		private final Throwable details;
		public ErrorAction(String title, Throwable details)
		{
			this.title = title;
			this.details = details;
		}
		public void run()
		{
			Application.getInstance().showUnhandledErrorDialog(title, details);
		}
	}
}
