/**
 * 
 */
package org.korsakow.ide.ui.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.korsakow.domain.interf.IMedia;
import org.korsakow.domain.mapper.input.MediaInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.ui.controller.action.snu.CreateSnuAction;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTable;
import org.korsakow.ide.util.UIUtil;

public class PossiblePoolMouseListener extends MouseAdapter
{
	@Override
	public void mouseClicked(MouseEvent event)
	{
		ResourceTreeTable resourceTreeTable = (ResourceTreeTable)event.getComponent();
		if (!UIUtil.isRegularDoubleClick(event))
			return;
		KNode node = resourceTreeTable.getSelectedNode();
		if (node instanceof ResourceNode == false)
			return;
		ResourceNode resourceNode = (ResourceNode)node;
		Long id = resourceNode.getResourceId();
		try {
			IMedia media = MediaInputMapper.map(id);
			CreateSnuAction.doCreateSnuFromMedia(media);
		} catch (Exception e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
		}
	}
}