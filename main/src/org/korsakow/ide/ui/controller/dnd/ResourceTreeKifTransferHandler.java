/**
 * 
 */
package org.korsakow.ide.ui.controller.dnd;

import java.io.CharConversionException;
import java.io.File;
import java.util.List;

import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.controller.action.interf.ImportInterfaceAction;
import org.korsakow.ide.ui.dnd.AggregateFileTransferHandler.FileTransferHandler;
import org.korsakow.ide.util.FileUtil;
import org.korsakow.ide.util.Util;

class ResourceTreeKifTransferHandler implements FileTransferHandler
{
	public boolean importData(List<File> files)
	{
		for ( File file : files )
			if ( !ImportInterfaceAction.getDefaultExtension().equalsIgnoreCase( FileUtil.getFileExtension( file.getName() ) ) )
				return false;
		try {
			for ( File file : files ) {
				new ImportInterfaceAction().doImport( file );
			}
			return true;
		} catch (CommandException e) {
			Application app = Application.getInstance();
			Throwable cause = Util.getRootCause(e);
			if (cause instanceof CharConversionException)
				app.showHandledErrorDialog(LanguageBundle.getString("general.errors.invalidinterfacefile.title"), LanguageBundle.getString("general.errors.invalidinterfacefile.message"));
			else
				app.showUnhandledErrorDialog("Error importing interface", e);
			return false;
		}
	}
}