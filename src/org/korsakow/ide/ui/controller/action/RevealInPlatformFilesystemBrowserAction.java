package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.mapper.input.MediaInputMapper;
import org.korsakow.domain.mapper.input.SnuInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTable;
import org.korsakow.ide.util.ShellExec;
import org.korsakow.ide.util.ShellExec.ShellException;

public class RevealInPlatformFilesystemBrowserAction implements ActionListener
{
	private final ResourceTreeTable resourceTreeTable;
	public RevealInPlatformFilesystemBrowserAction(ResourceTreeTable resourceBrowser)
	{
		resourceTreeTable = resourceBrowser;
	}
	public void actionPerformed(ActionEvent event)
	{
		Application app = Application.getInstance();
		Collection<IMedia> media = new HashSet<IMedia>();
		try {
			List<? extends KNode> selectedNodes = resourceTreeTable.getSelectedNodes();
			for (KNode node : selectedNodes)
			{
				if (node instanceof ResourceNode == false)
					continue;
				ResourceNode resourceNode = (ResourceNode)node;
				
				if (resourceNode.getResourceType() == ResourceType.SNU) {
					media.add(SnuInputMapper.map(resourceNode.getResourceId()).getMainMedia());
				} else
				if (resourceNode.getResourceType().isMedia()) {
					media.add(MediaInputMapper.map(resourceNode.getResourceId()));
				}
			}
		} catch (MapperException e) {
			app.showUnhandledErrorDialog(e);
		}
		try {
			for (IMedia medium : media)
			{
				String filename;
				try {
					filename = medium.getAbsoluteFilename();
				} catch (FileNotFoundException e) {
					filename = medium.getFilename();
				}
				ShellExec.revealInPlatformFilesystemBrowser(filename);
			}
		} catch (ShellException e) {
			app.showUnhandledErrorDialog(LanguageBundle.getString("general.errors.cantrunscript.title"), "general.errors.cantrunscript.message", e);
		}
	}
}
