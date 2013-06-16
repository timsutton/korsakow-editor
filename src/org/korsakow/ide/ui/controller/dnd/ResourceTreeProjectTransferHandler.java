/**
 * 
 */
package org.korsakow.ide.ui.controller.dnd;

import java.io.File;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.controller.action.ExitAction;
import org.korsakow.ide.ui.controller.action.OpenProjectFileAction;
import org.korsakow.ide.ui.dnd.AggregateFileTransferHandler.FileTransferHandler;
import org.korsakow.ide.util.FileUtil;

class ResourceTreeProjectTransferHandler implements FileTransferHandler
{
	public boolean importData(List<File> files)
	{
		if ( files.size() != 1 )
			return false;
		if ( !OpenProjectFileAction.getDefaultExtension().equalsIgnoreCase( FileUtil.getFileExtension( files.get(0).getName() ) ) )
			return false;
		
		File projectFile = null;
		for (File file : files) {
			String ext = FileUtil.getFileExtension(file.getName());
			if (OpenProjectFileAction.getDefaultExtension().equalsIgnoreCase(ext)) {
				projectFile = file;
				break;
			}
		}
		if (projectFile == null)
			return false;
		try {
			if (!ExitAction.checkForChangesAndPrompt())
				return false;
		} catch (MapperException e) {
			Application.getInstance().showUnhandledErrorDialog(e);
			return false;
		}
		try {
			OpenProjectFileAction.performAction(projectFile);
		} catch (Throwable e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.cantopen.title"), e);
			return false;
		}
		return true;
	}
}