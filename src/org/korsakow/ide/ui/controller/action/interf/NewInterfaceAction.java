package org.korsakow.ide.ui.controller.action.interf;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.ui.ResourceEditor;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.controller.helper.InterfaceHelper;
import org.korsakow.ide.ui.interfacebuilder.InterfaceBuilderMainPanel;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTable;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTableModel;

public class NewInterfaceAction implements ActionListener
{
	private final ResourceTreeTable resourceTreeTable;
	public NewInterfaceAction(ResourceTreeTable resourceBrowser)
	{
		resourceTreeTable = resourceBrowser;
	}
	public void actionPerformed(ActionEvent event)
	{
		Application app = Application.getInstance();
		ResourceTreeTableModel model = resourceTreeTable.getTreeTableModel();
		
		KNode selectedNode = resourceTreeTable.getSelectedNode();
		if (selectedNode == null)
			selectedNode = model.getRoot();
		
		try {
			ResourceEditor editor = app.editNew(ResourceType.INTERFACE);
			InterfaceBuilderMainPanel view = (InterfaceBuilderMainPanel)editor.getResourceView();
			InterfaceHelper.initView(view, LanguageBundle.getString("general.newinterfacename"));
		} catch (Exception e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
		}
	}
}
