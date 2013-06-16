package org.korsakow.ide.ui.controller.action;

import java.io.File;

import org.dsrg.soenea.service.Registry;
import org.korsakow.ide.Application;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.media.JSound;
import org.korsakow.ide.resources.media.Playable;
import org.korsakow.ide.ui.controller.action.SaveProjectAction.PlayableCloser;
import org.korsakow.ide.util.FileUtil;
import org.korsakow.ide.util.UIResourceManager;

public class SaveProjectAsAction extends AbstractAction
{
	public SaveProjectAsAction()
	{
	}
	@Override
	public void performAction()
	{
		File defaultFile = Application.getInstance().getSaveFile();
		try{
			if (defaultFile == null)
				defaultFile = new File(Registry.getProperty("defaultProjectFilename"));
			File file = Application.getInstance().showFileSaveDialog(Application.getInstance().getProjectExplorer(), defaultFile);
			if (file == null)
				return;
			if (FileUtil.getFileExtension(file.getName()).length() == 0)
				file = new File(file.getPath() + '.' + OpenProjectFileAction.getDefaultExtension());
			
			if (!SaveProjectAction.saveProject(file))
				return;
			String filename = DataRegistry.getFile().getAbsolutePath();
			
			SaveProjectAction.addRecent(filename);

			Playable sound =  new JSound(UIResourceManager.getSoundResourceStream(UIResourceManager.SOUND_SAVE)); 
			sound.start();
			new PlayableCloser(sound);
		} catch (Exception e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.cantsave.title"), e);
		}
	}
}
