package org.korsakow.ide.ui.components;

import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.RowMapper;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.korsakow.ide.ui.components.tree.FolderNode;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.components.tree.KTreeTableModel;
import org.korsakow.ide.util.Platform;
import org.korsakow.ide.util.UIUtil;
import org.korsakow.ide.util.Util;

import com.sun.swingx.JTreeTable;
import com.sun.swingx.TreeTableCellEditor;
import com.sun.swingx.treetable.TreeTableModel;
import com.sun.swingx.treetable.TreeTableModelAdapter;

public class KTreeTable extends JTreeTable
{
	protected boolean editable;
	protected Boolean canCollpaseRoot = null;
	protected FocusListener editorFocusListener = new FocusAdapter() {
		@Override
		public void focusLost(FocusEvent event) {
    		TableCellEditor editor = getCellEditor();
    		if (editor != null) {
    			editor.stopCellEditing();
    		}
		}
	};
	/**
	 * Used to prevent the tree from altering the selection when forwarding MouseEvents to it. Having both the table and tree
	 * change the selection based on user input is problematic since their semantics differ and you end up with slightly
	 * different selection behavior depending on if the user for example clicked on the tree directly or on on table portion outside the tree.
	 * 
	 */
	protected boolean ignoreTreeSelection = false;
	
	public KTreeTable(TreeTableModel model)
	{
		super(model);
	    setColumnSelectionAllowed(false);
		setDefaultEditor(TreeTableModel.class, new KTreeTableCellEditor(this));
		getTree().setSelectionModel(new TreeSelectionModelWrapper(tree.getSelectionModel()));
		getTree().getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		getTree().setBorder(BorderFactory.createLineBorder(UIManager.getColor("Table.gridColor"))); // fixes the tree-column's right border
		addMouseListener(new StopEditingOnClickListener());
//		getTree().setOpaque(false);
	}
	public KNode getSelectedNode()
	{
		List<? extends KNode> nodes = getSelectedNodes();
		return nodes.isEmpty()?null:nodes.get(0);
	}
	public List<? extends KNode> getSelectedNodes()
	{
		int[] rows = getSelectedRows();
		List<KNode> nodes = new ArrayList<KNode>();
		for (int row : rows) {
			KNode node = getNodeAt(row);
			nodes.add(node);
		}
		return nodes;
	}
	@Override
	public DefaultTableColumnModel getColumnModel()
	{
		return (DefaultTableColumnModel)super.getColumnModel();
	}
	public FolderNode getRootNode()
	{
		return (FolderNode)tree.getModel().getRoot();
	}
	@Override
	public boolean isCellEditable(int row, int column)
	{
//		return false;
		return editable && super.isCellEditable(row, column);
	}
	public void setEditable(boolean editable)
	{
//		tree.setEditable(editable);
		this.editable = editable;
	}
	public void setRootVisible(boolean rootVisible)
	{
		tree.setRootVisible(rootVisible);
	}
	public void setTreeCellRenderer(TreeCellRenderer renderer)
	{
		tree.setCellRenderer(renderer);
	}
	public void setTreeTableModel(KTreeTableModel model)
	{
		// maintain columns
		List<?> identifiers = getTreeTableModel().getColumnIdentifiers();
		model.setColumnIdentifiers(identifiers);
		
		getTree().setModel(model);
		setModel(new TreeTableModelAdapter(model, getTree()));
		model.addTreeModelListener(new TreeModelListener() {
			public void treeNodesChanged(TreeModelEvent e) {
				expandNode(getRootNode());
			}
			public void treeNodesInserted(TreeModelEvent e) {
				expandNode(getRootNode());
			}
			public void treeNodesRemoved(TreeModelEvent e) {
				expandNode(getRootNode());
			}
			public void treeStructureChanged(TreeModelEvent e) {
				expandNode(getRootNode());
			}
		});
	}
	public KTreeTableModel getTreeTableModel()
	{
		return (KTreeTableModel)super.getTree().getModel();
	}
	public KNode getNodeForPath(List<String> namespath)
	{
		List<String> names = new ArrayList<String>(namespath);
		if (names.isEmpty())
			return null;
		KNode node = getRootNode();
		if (!names.remove(0).equals(node.getName()))
			throw new IllegalArgumentException("named path must start at root");
		while (!names.isEmpty() && node!=null) {
			String name = names.remove(0);
			node = node.getChild(name);
			if (node == null) {
				throw new IllegalArgumentException("invalid path: " + Util.join(namespath));
//				return null;
			}
		}
		return node;
	}
	public int getRow(KNode node)
	{
		// does treetable support a better way to do this?
		for (int i = 0; i < getRowCount(); ++i)
			if (getNodeAt(i) == node)
				return i;
		return -1;
	}
	public TreePath getPathForLocation(int x, int y)
	{
		return tree.getPathForLocation(x, y);
	}
	public TreePath getPathForLocation(Point p)
	{
		return getPathForLocation(p.x, p.y);
	}
	public TreePath getPathForRow(int row)
	{
		return tree.getPathForRow(row);
	}
	public KNode getNodeAt(int row)
	{
		TreePath path = tree.getPathForRow(row);
		if (path == null)
			return null;
		return (KNode)path.getLastPathComponent();
	}
	public void setRootCollapsible(boolean b)
	{
		if (!b) {
			expandNode(getRootNode()); // otherwise it'd be forever invisible
		}
		if (canCollpaseRoot == null) // lazy init
		{
			getTree().addTreeWillExpandListener(new TreeWillExpandListener() {
				public void treeWillCollapse(TreeExpansionEvent event)
						throws ExpandVetoException {
					if (!canCollpaseRoot && event.getPath().getLastPathComponent() == getRootNode())
						throw new ExpandVetoException(event);
				}
				public void treeWillExpand(TreeExpansionEvent event)
						throws ExpandVetoException {
//					if (!canExpandRoot && event.getPath().getLastPathComponent() == getRootNode())
//						throw new ExpandVetoException(event);
				}
			});
		}
		canCollpaseRoot = b;
	}
	public void expandNode(KNode node)
	{
		if (node.isLeaf() && node != getRootNode())
			node = node.getParent();
		if (node != null)
			tree.expandPath(node.getTreePath());
//		expandRow(getRow(node));
	}
	public void expandNodes(Collection<KNode> nodes)
	{
		for (KNode node : nodes)
		{
			expandNode(node);
		}
	}
	public void expandAllRecursive()
	{
		expandAllRecursive(getRootNode());
	}
	public void expandAllRecursive(KNode node)
	{
		if (node.isLeaf()) {
			expandNode(node);
		} else {
			for (KNode child : node)
				expandAllRecursive(child);
		}
	}
	/**
	 * @return the set of all nodes which are expanded
	 */
	public Set<KNode> getExpandedNodes()
	{
		Set<KNode> nodes = new HashSet<KNode>();
		getExpandedNodes(nodes, getRootNode());
		return nodes;
	}
	private void getExpandedNodes(Set<KNode> nodes, KNode node)
	{
		if (!tree.isExpanded(node.getTreePath()))
			return; // if we're not expanded neither are our children
		
		nodes.add(node);
		for (KNode child : node.getChildren()) {
			getExpandedNodes(nodes, child);
		}
	}
	public void editCellAt(KNode node, int col)
	{
		super.editCellAt(getRow(node), col);
	}
    @Override
	public boolean editCellAt(int row, int column, EventObject e)
    {
		if (!isCellEditable(row, column))
			return false;
		if (e instanceof MouseEvent) {
			MouseEvent me = (MouseEvent)e;
			if (me.getClickCount() > 2)
				return false;
		}
		boolean ret = super.editCellAt(row, column, e);
		if (editorComp != null) { // this null check is without motivation
			// this fixes the editor placement to overlap the original text properly
			// i don't know where the specific numbers come from, they're empirical.
			editorComp.removeFocusListener(editorFocusListener);
			editorComp.addFocusListener(editorFocusListener);
		}
		return ret;
    }
	@Override
	public Object getValueAt(int row, int col)
	{
		return super.getValueAt(row, col);
	}
	
	@Override
	protected void processMouseEvent(MouseEvent event)
	{
		// L&F fix for MAC which uses CTL-left click as a popup trigger
		if (Platform.isMacOS() && event.isControlDown() && event.getButton() == MouseEvent.BUTTON1) {
			event = new MouseEvent(event.getComponent(), event.getID(), event.getWhen(), event.getModifiers(), event.getX(), event.getY(), event.getClickCount(), true);
		}
//		if (me.getID() == MouseEvent.MOUSE_CLICKED)
//		if (me.getModifiers() == 0 ||
//                me.getModifiers() == InputEvent.BUTTON1_MASK)
		super.processMouseEvent(event);
		{
		    for (int counter = getColumnCount() - 1; counter >= 0; counter--)
		    {
				if (getColumnClass(counter) == TreeTableModel.class)
				{
				    MouseEvent newME = new MouseEvent
				          (KTreeTable.this.getTree(), event.getID(),
					   event.getWhen(), event.getModifiers(),
					   event.getX() - getCellRect(0, counter, true).x,
					   event.getY(), event.getClickCount(),
	                                   UIUtil.isPopupTrigger(event));
				    // see comments for ignoreTreeSelection
				    ignoreTreeSelection = true;
				    KTreeTable.this.getTree().dispatchEvent(newME);
				    ignoreTreeSelection = false;
				    break;
				}
		    }
		}
	}
	
	/**
	 * fixes some wierd UI issues in JTreeTable.
	 * @author d
	 *
	 */
    public static class KTreeTableCellEditor extends TreeTableCellEditor
    {
    	public KTreeTableCellEditor(JTreeTable jTreeTable) {
			super(jTreeTable);
		}

		/**
    	 * JTreeTable tries to use this opportunity to forward mouse events. We do so in processMouseEvents instead.
    	 * Doing it here leads to wierd behaviors for example trying to expand/collapse nodes.
    	 * 
    	 * (Strangely ?,) returning false here only prevents the mouse-driven double click event. Or at least
    	 * we can (and still do) initiate editing programatically. This however is more or less what we want so its ok.
    	 */
    	@Override
		public boolean isCellEditable(EventObject e) {
    		return e == null;
        }
    	
    	@Override
		public KTreeTable getTreeTable() {
    		return (KTreeTable)super.getTreeTable();
    	}
    }
    
    /**
     * A wrapper that optionally ignored attemps at selection @see ignoreTreeSelection
     */
    public class TreeSelectionModelWrapper implements TreeSelectionModel
    {
    	private final TreeSelectionModel delegate;
    	public TreeSelectionModelWrapper(TreeSelectionModel delegate)
    	{
    		this.delegate = delegate;
    	}
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			delegate.addPropertyChangeListener(listener);
		}

		public void addSelectionPath(TreePath path) {
			if (ignoreTreeSelection) return;
			delegate.addSelectionPath(path);
		}

		public void addSelectionPaths(TreePath[] paths) {
			if (ignoreTreeSelection) return;
			delegate.addSelectionPaths(paths);
		}

		public void addTreeSelectionListener(TreeSelectionListener x) {
			delegate.addTreeSelectionListener(x);
		}

		public void clearSelection() {
			if (ignoreTreeSelection) return;
			delegate.clearSelection();
		}

		public TreePath getLeadSelectionPath() {
			return delegate.getLeadSelectionPath();
		}

		public int getLeadSelectionRow() {
			return delegate.getLeadSelectionRow();
		}

		public int getMaxSelectionRow() {
			return delegate.getMaxSelectionRow();
		}

		public int getMinSelectionRow() {
			return delegate.getMinSelectionRow();
		}

		public RowMapper getRowMapper() {
			return delegate.getRowMapper();
		}

		public int getSelectionCount() {
			return delegate.getSelectionCount();
		}

		public int getSelectionMode() {
			return delegate.getSelectionMode();
		}

		public TreePath getSelectionPath() {
			return delegate.getSelectionPath();
		}

		public TreePath[] getSelectionPaths() {
			return delegate.getSelectionPaths();
		}

		public int[] getSelectionRows() {
			return delegate.getSelectionRows();
		}

		public boolean isPathSelected(TreePath path) {
			return delegate.isPathSelected(path);
		}

		public boolean isRowSelected(int row) {
			return delegate.isRowSelected(row);
		}

		public boolean isSelectionEmpty() {
			return delegate.isSelectionEmpty();
		}

		public void removePropertyChangeListener(PropertyChangeListener listener) {
			delegate.removePropertyChangeListener(listener);
		}

		public void removeSelectionPath(TreePath path) {
			if (ignoreTreeSelection) return;
			delegate.removeSelectionPath(path);
		}

		public void removeSelectionPaths(TreePath[] paths) {
			if (ignoreTreeSelection) return;
			delegate.removeSelectionPaths(paths);
		}

		public void removeTreeSelectionListener(TreeSelectionListener x) {
			delegate.removeTreeSelectionListener(x);
		}

		public void resetRowSelection() {
			if (ignoreTreeSelection) return;
			delegate.resetRowSelection();
		}

		public void setRowMapper(RowMapper newMapper) {
			delegate.setRowMapper(newMapper);
		}

		public void setSelectionMode(int mode) {
			if (ignoreTreeSelection) return;
			delegate.setSelectionMode(mode);
		}

		public void setSelectionPath(TreePath path) {
			if (ignoreTreeSelection) return;
			delegate.setSelectionPath(path);
		}

		public void setSelectionPaths(TreePath[] paths) {
			if (ignoreTreeSelection) return;
			delegate.setSelectionPaths(paths);
		}
    	
    }
    
    /**
     * Um... the default implementation will ignore our DropTarget if it implements UIResource
     * yet BasicTableUI won't do the selection changing while dragging UNLESS it implements UIResource. arg.
     * @param handler
     * @param dropTarget
     */
    public void setTransferHandler(TransferHandler handler, DropTarget dropTarget)
    {
    	
    }
    public class StopEditingOnClickListener extends MouseAdapter
    {
    	@Override
		public void mousePressed(MouseEvent mouseEvent)
    	{
//    		TableCellEditor editor = getCellEditor();
//    		if (editor != null) {
//    			editor.stopCellEditing();
//    		}
    	}
    }
    public MouseListener createStopEditingOnClickListener()
    {
    	return new StopEditingOnClickListener();
    }
    
    @Override
    public void editingStopped(ChangeEvent e) {
    	// prevents a host of NPE
    	if (getEditingRow() != -1)
    		super.editingStopped(e);
    	else {
            TableCellEditor editor = getCellEditor();
            if (editor != null) {
                removeEditor();
            }
    	}
    }
}
