package org.korsakow.ide.ui.controller.action.snu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.ui.ResourceEditor;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTable;

public class OpenAsSnuAction implements ActionListener
{
	private final ResourceTreeTable resourceTreeTable;
	public OpenAsSnuAction(ResourceTreeTable resourceBrowser)
	{
		resourceTreeTable = resourceBrowser;
	}
	public void actionPerformed(ActionEvent event)
	{
		Exception firstException = null;
		
		Application app = Application.getInstance();
		List<? extends KNode> selectedNodes = resourceTreeTable.getSelectedNodes();
		int x = 0;
		int y = 0;
		for (KNode node : selectedNodes) {
			if (node instanceof ResourceNode) {
				ResourceNode resourceNode = (ResourceNode)node;
				Long resourceId = resourceNode.getResourceId();
				if (resourceNode.getResourceType() != ResourceType.SNU)
					continue;
				ResourceEditor editor;
				try {
					editor = app.edit(resourceNode.getType(), resourceId);
				} catch (Exception e) {
					if (firstException == null)
						firstException = e;
					continue;
				}
				// stagger windows
				editor.setLocation(editor.getX()+x, editor.getY()+y);
				x += 20;
				y += 20;
			}
		}
		if (firstException != null)
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), firstException);
	}
}
