package org.korsakow.ide.ui.resourceexplorer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Timer;

import javax.swing.JComboBox;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreeSelectionModel;

import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.ResourceProperty;
import org.korsakow.ide.ui.components.KTreeTable;
import org.korsakow.ide.ui.components.tree.FolderNode;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.controller.action.RenameAction;
import org.korsakow.ide.util.UIUtil;

import com.sun.swingx.treetable.TreeTableModel;

public class ResourceTreeTable extends KTreeTable
{
	protected TreeModelListener treeListener;
	
	public ResourceTreeTable()
	{
		super(new DefaultResourceTreeTableModel(new FolderNode("/")));
		getTree().putClientProperty("doNotCancelPopup", new JComboBox().getClientProperty("doNotCancelPopup"));
		setTreeCellRenderer(new ResourceTreeCellRenderer());
		setRootVisible(false);
		setShowGrid(true);
		setDragEnabled(true);
		setEditable(true);
//		resourceTreeTable.getColumnModel().getColumn(resourceTreeTable.getTreeTableModel().getHierarchicalColumn()).setCellEditor(new KTreeTreeCellEditor());
//		resourceTree.setCellEditor(new KTreeTreeCellEditor(resourceTree, (ResourceTreeCellRenderer)resourceTree.getCellRenderer()));
		getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
		setDefaultEditor(TreeTableModel.class, new ResourceTreeTableCellEditor(this));
		setDefaultRenderer(Object.class, new ResourceTreeTableCellRenderer());
		getColumnModel().getColumn(0).setIdentifier(ResourceProperty.NAME);
		getColumnModel().getColumn(0).setHeaderValue(LanguageBundle.getString("resourcebrowser.columns.name.label"));
		setRowHeight(18); // this used to be not set, but the snu icon at this time (2009/07/01) was getting cut off
		initListeners();
	}
	
	@Override
	public boolean isCellEditable(int row, int column)
	{
		KNode cell = getNodeAt(row);
		if (column != 0)
			return false;
		
//		if (cell instanceof FolderNode == false)
//			return false;
		if (cell == getRootNode())
			return false;
		
		return true;
	}
	public KNode getEditingNode()
	{
		TableCellEditor editor = getCellEditor();
		if (editor instanceof ResourceTreeTableCellEditor)
			return ((ResourceTreeTableCellEditor)editor).getEditingNode();
		return null;
	}
	@Override
	public ResourceTreeTableModel getTreeTableModel()
	{
		return (ResourceTreeTableModel)super.getTreeTableModel();
	}
	
	public void setExternalTreeListener ( TreeModelListener tListener ) {
		treeListener = tListener;
		ResourceTreeTableModel tModel = this.getTreeTableModel();
		tModel.addTreeModelListener(treeListener);
	}
	
	public void setTreeTableModel(ResourceTreeTableModel model)
	{
		List<?> identifiers = ((DefaultResourceTreeTableModel)getTreeTableModel()).getColumnIdentifiers();
		model.setColumnIdentifiers(identifiers);
		if ( treeListener != null ) model.addTreeModelListener( treeListener );
		super.setTreeTableModel(model);
	}
	protected void initListeners()
	{
		final RenameAfterDelayedClickAdapter mama = new RenameAfterDelayedClickAdapter(this);
		addMouseListener(mama);
		getSelectionModel().addListSelectionListener(mama);
	}
	private static class RenameAfterDelayedClickAdapter extends MouseAdapter implements ListSelectionListener
	{
		private final ResourceTreeTable tree;
		private Timer timer;
		public RenameAfterDelayedClickAdapter(ResourceTreeTable tree)
		{
			super();
			this.tree = tree;
		}
		private boolean isSelection = false;
		@Override
		public void mousePressed(MouseEvent event) {
			cancel();
		}
		private void cancel()
		{
			if (timer != null) {
				timer.cancel();
				timer = null;
			}
		}
		@Override
		public void mouseReleased(MouseEvent event) {
			if (!UIUtil.isRegularSingleClick(event)) {
				cancel();
				return;
			}
			if (!isSelection) {
				if (tree.getSelectedNode() != null) {
					// haha, for some reason using javax.swing.Timer here didn't work. editing would start and
					// then promptly stop
					if (timer != null)
						timer.cancel();
					timer = UIUtil.runUITaskLater(new Runnable() {
						public void run() {
							new RenameAction(tree).actionPerformed(null);
						}
					}, 1000);
				}
			}
			isSelection = false;
		}
		public void valueChanged(ListSelectionEvent e) {
			isSelection = true;
		}
	}
}
