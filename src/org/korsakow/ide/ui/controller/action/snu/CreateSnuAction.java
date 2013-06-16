package org.korsakow.ide.ui.controller.action.snu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.interf.IResource;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.mapper.input.ResourceInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.ui.ResourceEditor;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTable;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTableModel;
import org.korsakow.ide.ui.resources.SnuResourceView;
import org.korsakow.ide.util.FileUtil;

public class CreateSnuAction implements ActionListener
{
	private final ResourceTreeTable resourceTreeTable;
	public CreateSnuAction(ResourceTreeTable resourceBrowser)
	{
		resourceTreeTable = resourceBrowser;
	}
	public void actionPerformed(ActionEvent event)
	{
		Application app = Application.getInstance();
		ResourceTreeTableModel model = resourceTreeTable.getTreeTableModel();
		
		Collection<? extends KNode> selectedNodes = resourceTreeTable.getSelectedNodes();
		for (KNode selectedNode : selectedNodes)
		{
			if (selectedNode == null)
				continue;
			if (selectedNode instanceof ResourceNode == false)
				continue;
			ResourceNode resourceNode = (ResourceNode)selectedNode;
			Long resourceId = resourceNode.getResourceId();
			if (resourceId == null)
				continue;
			try {
				IResource resource = ResourceInputMapper.map(resourceId);
				if (resource instanceof IMedia == false)
					continue;
				IMedia media = (IMedia)resource;
				
				doCreateSnuFromMedia(media);
			} catch (Exception e) {
				Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
			}
		}
	}
	public static void doCreateSnuFromMedia(IMedia media) throws Exception
	{
		Application app = Application.getInstance();
		
		// try and find an open editor for a Snu with this media.
		// if we find one, focus it and abort
		Collection<ResourceEditor> openEditors = app.getOpenEditors();
		for (ResourceEditor editor : openEditors)
		{
			IResource editingResource = app.getResourceForEditor(editor);
			if (editingResource == null) {
				// i suppose its reasonable that some other code might create an unattached resource editor
				// but in that case it shouldnt be in the openEditors list
				Logger.getLogger(CreateSnuAction.class).info("no resource for editor: " + editor.getResourceView().getResourceId(), new Exception("just-for-stacktrace"));
				continue;
			}
			if (editingResource instanceof ISnu) {
				IMedia editingMedia = ((ISnu)editingResource).getMainMedia();
				if (editingMedia != null && editingMedia.getId().equals(media.getId())) {
					editor.toFront();
					return;
				}
			}
			if (editingResource instanceof IMedia) {
				if (editor.getResourceView() instanceof SnuResourceView) {
					editor.toFront();
					return;
				}
			}
		}
		
		ResourceEditor editor = app.editNew(ResourceType.SNU, media.getId());
		SnuResourceView view = (SnuResourceView)editor.getResourceView();
		SnuHelper.initView(view, FileUtil.getFilenameWithoutExtension(media.getName()), media);
	}
}
