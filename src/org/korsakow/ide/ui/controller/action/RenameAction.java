package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTable;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTableCellEditor;
import org.korsakow.ide.util.UIUtil;

import com.sun.swingx.treetable.TreeTableModel;

public class RenameAction implements ActionListener, CellEditorListener
{
	private final ResourceTreeTable treeTable;
	public RenameAction(ResourceTreeTable treeTable)
	{
		this.treeTable = treeTable;
	}
	public void actionPerformed(ActionEvent event)
	{
		List<? extends KNode> selectedNodes = treeTable.getSelectedNodes();
		if (selectedNodes.size() != 1)
			return;
		treeTable.editCellAt(selectedNodes.get(0), 0);
	}
	@Override
	public void editingCanceled(ChangeEvent e)
	{
	}
	@Override
	public void editingStopped(ChangeEvent e)
	{
//		// TreeTable.getEditingNode, the obvious choice, returns null at this point because swing is terribly designed
		KNode editingNode = ((ResourceTreeTableCellEditor)treeTable.getDefaultEditor(TreeTableModel.class)).getEditingNode();
		// sometimes the node has been removed already
		if (!UIUtil.isRooted(treeTable.getRootNode(), editingNode))
			return;
		String newName = treeTable.getDefaultEditor(TreeTableModel.class).getCellEditorValue().toString();
		if (editingNode instanceof ResourceNode)
			new RenameCommitAction(treeTable, ((ResourceNode)editingNode).getResourceId(), newName).actionPerformed(null);
		else
			editingNode.setName(newName);
	}
}
