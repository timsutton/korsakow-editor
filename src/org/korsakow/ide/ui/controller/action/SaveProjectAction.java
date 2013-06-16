package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.prefs.Preferences;

import javax.swing.JMenuItem;
import javax.swing.Timer;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.dsrg.soenea.environment.CreationException;
import org.dsrg.soenea.environment.KeyNotFoundException;
import org.korsakow.domain.CommandExecutor;
import org.korsakow.domain.Settings;
import org.korsakow.domain.Settings.AdjustFilenames;
import org.korsakow.domain.command.AdjustToAbsolutePathsCommand;
import org.korsakow.domain.command.AdjustToRelativeOrAbsolutePathsCommand;
import org.korsakow.domain.command.AdjustToRelativePathsCommand;
import org.korsakow.domain.command.Helper;
import org.korsakow.domain.command.Request;
import org.korsakow.domain.interf.IProject;
import org.korsakow.domain.interf.ISettings;
import org.korsakow.domain.mapper.input.ProjectInputMapper;
import org.korsakow.domain.mapper.input.SettingsInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.media.JSound;
import org.korsakow.ide.resources.media.Playable;
import org.korsakow.ide.ui.ProjectExplorer.Action;
import org.korsakow.ide.ui.components.tree.FolderNode;
import org.korsakow.ide.util.PreferencesManager;
import org.korsakow.ide.util.UIResourceManager;
import org.korsakow.ide.util.UIUtil;
import org.korsakow.ide.util.Util;
import org.w3c.dom.Element;

public class SaveProjectAction extends AbstractAction
{
	/**
	 * Keeps a reference to the playable until it finishes playing (so that it won't be GC'd prematurely). Also disposes the resource when its done.
	 * Note that this relies on the playable's duration.
	 * 
	 * @author d
	 *
	 */
	public static class PlayableCloser implements ActionListener
	{
		private static Set<PlayableCloser> strongReferenceMap = new HashSet<PlayableCloser>();
		private Timer timer;
		private Playable playable;
		public PlayableCloser(Playable playable)
		{
			this.playable = playable;
			strongReferenceMap.add(this);
			timer = new Timer((int)(playable.getDuration() + 100), this); // the 100 is just to make sure it finishes
			timer.start();
		}
		public void actionPerformed(ActionEvent event)
		{
			strongReferenceMap.remove(this);
			timer.stop();
			timer.removeActionListener(this);
			playable.dispose();
 			playable = null;
			timer = null;
		}
	}
	
	public SaveProjectAction()
	{
	}
	@Override
	public void performAction()
	{
		if (Application.getInstance().getSaveFile() == null) {
			new SaveProjectAsAction().performAction();
			return;
		}
		try{
			if (!saveProject(Application.getInstance().getSaveFile()))
				return;
			String filename = DataRegistry.getFile().getAbsolutePath();
			addRecent(filename);
			
			Playable sound =  new JSound(UIResourceManager.getSoundResourceStream(UIResourceManager.SOUND_SAVE));  
			sound.start();
			new PlayableCloser(sound);
		} catch (Exception e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.cantsave.title"), e);
		}
	}
	public static boolean saveProject(File file) throws SQLException, KeyNotFoundException, CreationException, MapperException, XPathExpressionException, IOException, TransformerException
	{
		System.gc();

		try {
			if (!adjustFilenames(file))
				return false;
		} catch (CommandException e) {
			if (e.getCause() instanceof FileNotFoundException) {
				Logger.getLogger(SaveProjectAction.class).error("", e);
				Application.getInstance().showAlertDialog(LanguageBundle.getString("general.errors.cantadjustpath.title"), LanguageBundle.getString("general.errors.cantadjustpath.message"));
			} else {
				Logger.getLogger(SaveProjectAction.class).error("", e);
				Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.cantadjustpath.title"), e);
			}
		} catch (Exception e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.cantadjustpath.title"), e);
		}
		
		FolderNode resourceRoot = Application.getInstance().getProjectExplorer().getResourceBrowser().getResourceTreeTable().getRootNode();
		Element resourcesNode = UIUtil.resourceTreeToDom(DataRegistry.getDocument(), resourceRoot);
		DataRegistry.getHelper().removeNodes("/korsakow/resources");
		DataRegistry.getHelper().xpathAsElement("/korsakow").appendChild(resourcesNode);
		DataRegistry.setFile(file);
		DataRegistry.flush();
		Application.getInstance().setSaveFile(file, DataRegistry.getHeadVersion());
		System.gc();
		
		return true;
	}
	private static boolean adjustFilenames(File file) throws CommandException, MapperException, InterruptedException
	{
		IProject project = ProjectInputMapper.find();
		ISettings settings = SettingsInputMapper.find();
		
		AdjustFilenames adjustFilenames = AdjustFilenames.Disabled;
		try {
			adjustFilenames = AdjustFilenames.fromId(settings.getString(Settings.AdjustFilenamesOnSave));
		} catch (Exception e) {
			Logger.getLogger(SaveProjectAction.class).error("", e);
		}
		
		Request request = new Request();
		Helper response = null;
		switch (adjustFilenames)
		{
		case Disabled:
			break;
		case Absolute:
			request.set("id", project.getId());
			response = CommandExecutor.executeCommand(AdjustToAbsolutePathsCommand.class, request);
			break;
		case Relative:
			request.set("id", project.getId());
			request.set("basePath", file.getParentFile().getAbsolutePath());
			response = CommandExecutor.executeCommand(AdjustToRelativePathsCommand.class, request);
			if (response.get("status") == Boolean.TRUE) {
			} else {
				Application.getInstance().showAlertDialog(LanguageBundle.getString("general.errors.cantadjustrelativepath.title"), LanguageBundle.getString("general.errors.cantadjustrelativepath.message"));
				return false;
			}
			break;
		case Smart:
			request.set("id", project.getId());
			request.set("basePath", file.getParentFile().getAbsolutePath());
			response = CommandExecutor.executeCommand(AdjustToRelativeOrAbsolutePathsCommand.class, request);
			break;
		}
		return true;
	}
	public static void addRecent(String value)
	{
		List<String> recent = getRecent();
		if (recent.contains(value)) // bump
			recent.remove(value);
		recent.add(0, value);
		setRecent(recent);
	}
	
	public static List<String> getRecent()
	{
		Preferences recentPrefs = PreferencesManager.getPreferences(SaveProjectAction.class).node("recent");
		String recentStr = recentPrefs.get("recent", "");
		List<String> recent = recentStr.length()>0?Util.asList(recentStr.split(File.pathSeparator)):new ArrayList<String>();
		return recent;
	}
	public static void setRecent(List<String> recent)
	{
		while (recent.size() > 5) {
			recent.remove(recent.get(recent.size()-1));
		}
		recent = new ArrayList<String>(recent);
		
		Preferences recentPrefs = PreferencesManager.getPreferences(SaveProjectAction.class).node("recent");
		String recentStr = Util.join(recent, File.pathSeparator);
		recentPrefs.put("recent", recentStr);

		Application app = Application.getInstance();
		app.getProjectExplorer().getMenu(Action.MenuFileRecent).removeAll();
		for (String item : recent) {
			File file = new File(item);
			if (!file.canRead())
				continue;
			JMenuItem menuItem = new JMenuItem(item);
			menuItem.addActionListener(new LoadRecentProjectAction());
			app.getProjectExplorer().getMenu(Action.MenuFileRecent).add(menuItem);
		}
	}
}
