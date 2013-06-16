package org.korsakow.ide.ui.controller.action.media;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

import org.korsakow.domain.interf.IMedia;
import org.korsakow.ide.Application;
import org.korsakow.ide.controller.ApplicationAdapter;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.ui.controller.dnd.AbstractMediaFileTransferHandler;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTable;
import org.korsakow.ide.util.FileUtil;

public class ImportMediaAction extends ApplicationAdapter implements ActionListener
{
	ResourceTreeTable tree;

	public ImportMediaAction(ResourceTreeTable tree)
	{
		this.tree = tree;
	}
	
	public void actionPerformed(ActionEvent event) 
	{
		File defaultDir = new File(".");
		File file = Application.getInstance().showFileOpenDialog(Application.getInstance().getProjectExplorer(), defaultDir, null, new MediaFilenameFilter());
		if (file == null)
			return;
		List<File> files = Arrays.asList(file);

		try {
			List<? extends IMedia> media = AbstractMediaFileTransferHandler.convertToMedia(files);
			for ( IMedia medium : media ) {
				KNode node = ResourceNode.create( medium );
				tree.getTreeTableModel().appendNode( node, tree.getTreeTableModel().getRoot() );
				Application.getInstance().notifyResourceAdded(medium);
			}
		} catch (Exception e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.cantimport.title"), e);
		}
	}
	private static class MediaFilenameFilter implements FilenameFilter
	{
		public boolean accept(File dir, String name) {
			return FileUtil.isMediaFile(name);
		}
		
	}
}
