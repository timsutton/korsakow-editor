/**
 * 
 */
package org.korsakow.ide.ui.controller;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import org.apache.log4j.Logger;
import org.dsrg.soenea.domain.MapperException;
import org.dsrg.soenea.domain.command.CommandException;
import org.korsakow.domain.mapper.input.MediaInputMapper;
import org.korsakow.domain.mapper.input.SnuInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.PropertyKey;
import org.korsakow.ide.resources.ResourceProperty;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.resources.SnuProperty;
import org.korsakow.ide.ui.components.tree.FolderNode;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.components.tree.ResourceNode;
import org.korsakow.ide.ui.controller.action.CopyAction;
import org.korsakow.ide.ui.controller.action.DeleteAction;
import org.korsakow.ide.ui.controller.action.DuplicateAction;
import org.korsakow.ide.ui.controller.action.MediaPropertiesAction;
import org.korsakow.ide.ui.controller.action.NewFolderAction;
import org.korsakow.ide.ui.controller.action.PasteAction;
import org.korsakow.ide.ui.controller.action.ProjectSettingsAction;
import org.korsakow.ide.ui.controller.action.PropertiesAction;
import org.korsakow.ide.ui.controller.action.RenameAction;
import org.korsakow.ide.ui.controller.action.RevealInPlatformFilesystemBrowserAction;
import org.korsakow.ide.ui.controller.action.interf.ExportInterfaceAction;
import org.korsakow.ide.ui.controller.action.interf.NewInterfaceAction;
import org.korsakow.ide.ui.controller.action.snu.CreateSnuAction;
import org.korsakow.ide.ui.controller.action.snu.OpenAsSnuAction;
import org.korsakow.ide.ui.resourceexplorer.ResourceBrowser;
import org.korsakow.ide.ui.resourceexplorer.ResourceBrowserListener;
import org.korsakow.ide.ui.resourceexplorer.ResourceTreeTable;
import org.korsakow.ide.util.UIUtil;

import com.sun.swingx.treetable.TreeTableModel;

public class ResourceBrowserController implements ResourceBrowserListener
{
	private final ResourceBrowser resourceBrowser;
	private final RenameAction renameAction;
	private DeleteAction deleteAction;
	private NewFolderAction newFolderAction;
	private CreateSnuAction createSnuAction;
	private NewInterfaceAction newInterfaceAction;
	public ResourceBrowserController(ResourceBrowser resourceBrowser)
	{
		this.resourceBrowser = resourceBrowser;
		renameAction = new RenameAction(resourceBrowser.getResourceTreeTable());
		resourceBrowser.getResourceTreeTable().addMouseListener(new ResourceTreeDoubleClickListener());
		resourceBrowser.getResourceTreeTable().getDefaultEditor(TreeTableModel.class).addCellEditorListener(renameAction);
		resourceBrowser.addPopupMenuListener(new PopupMenuHandler());
		resourceBrowser.setContextMenuAction(ResourceBrowser.Menu.OpenAsSnu, new OpenAsSnuAction(resourceBrowser.getResourceTreeTable()));
		resourceBrowser.setContextMenuAction(ResourceBrowser.Menu.Properties, new PropertiesAction(resourceBrowser.getResourceTreeTable()));
		resourceBrowser.setContextMenuAction(ResourceBrowser.Menu.MediaProperties, new MediaPropertiesAction(resourceBrowser.getResourceTreeTable()));
		resourceBrowser.setContextMenuAction(ResourceBrowser.Menu.RevealInPlatformFilesystemBrowser, new RevealInPlatformFilesystemBrowserAction(resourceBrowser.getResourceTreeTable()));
		resourceBrowser.setContextMenuAction(ResourceBrowser.Menu.Copy, new CopyAction(resourceBrowser.getResourceTreeTable()));
		resourceBrowser.setContextMenuAction(ResourceBrowser.Menu.Paste, new PasteAction(resourceBrowser.getResourceTreeTable()));
		resourceBrowser.setContextMenuAction(ResourceBrowser.Menu.Duplicate, new DuplicateAction(resourceBrowser.getResourceTreeTable()));
		resourceBrowser.setContextMenuAction(ResourceBrowser.Menu.Rename, renameAction);
		resourceBrowser.setContextMenuAction(ResourceBrowser.Menu.Delete, deleteAction = new DeleteAction(resourceBrowser.getResourceTreeTable()));
		resourceBrowser.getResourceTreeTable().addKeyListener(deleteAction);
		resourceBrowser.setContextMenuAction(ResourceBrowser.Menu.NewFolder, newFolderAction = new NewFolderAction(resourceBrowser.getResourceTreeTable()));
		resourceBrowser.setContextMenuAction(ResourceBrowser.Menu.CreateSnu, createSnuAction = new CreateSnuAction(resourceBrowser.getResourceTreeTable()));
		resourceBrowser.setContextMenuAction(ResourceBrowser.Menu.NewInterface, newInterfaceAction = new NewInterfaceAction(resourceBrowser.getResourceTreeTable()));
		resourceBrowser.setContextMenuAction(ResourceBrowser.Menu.ExportInterface, new ExportInterfaceAction(resourceBrowser.getResourceTreeTable()));
		resourceBrowser.setContextMenuAction(ResourceBrowser.Menu.ProjectSettings, new ProjectSettingsAction());
		resourceBrowser.setAction(ResourceBrowser.Action.AddFolder, newFolderAction);
		resourceBrowser.setAction(ResourceBrowser.Action.AddSnu, createSnuAction);
		resourceBrowser.setAction(ResourceBrowser.Action.AddInterface, newInterfaceAction);
		new SorterListener(resourceBrowser.getResourceTreeTable());
		resourceBrowser.getResourceTreeTable().getSelectionModel().addListSelectionListener(new CreateSnuActionButtonEnabler(resourceBrowser));

		resourceBrowser.getResourceTreeTable().getDefaultEditor(TreeTableModel.class).addCellEditorListener(new CellEditorListener() {
			public void editingCanceled(ChangeEvent e) {
			}
			public void editingStopped(ChangeEvent e) {
//				KNode node = ResourceBrowserController.this.resourceBrowser.getResourceTreeTable().getCellEditor();
//				if (node instanceof ResourceNode) {
//					ResourceNode resourceNode = (ResourceNode)node;
//					Resource resource = resourceNode.getResource();
//					resource.setName(resourceNode.getName());
//					resource.update();
//				}
			}
		});
		
		final StatusUpdater statusUpdater = new StatusUpdater(resourceBrowser);
		resourceBrowser.getResourceTreeTable().addMouseMotionListener(statusUpdater);
		resourceBrowser.getResourceTreeTable().addMouseListener(statusUpdater);
	}
	public void onDeleteNodes(Collection<? extends KNode> nodes) {
		try {
			DeleteAction.deleteNodes(resourceBrowser.getResourceTreeTable().getTreeTableModel(), nodes);
		} catch (CommandException e) {
			Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
		} catch (MapperException e) {
			Application.getInstance().showUnhandledErrorDialog(e);
		}
	}
	/**
	 * Recursively descends the tree collecting resource nodes
	 * @param nodes
	 * @param resourceNodes any non null, mutable list which will be populated
	 */
	public static Collection<ResourceNode> filterResourceNodes(Collection<? extends KNode> nodes)
	{
		Collection<ResourceNode> resourceNodes = new LinkedHashSet<ResourceNode>();
		filterResourceNodes(nodes, resourceNodes);
		return resourceNodes;
	}
	private static void filterResourceNodes(Collection<? extends KNode> nodes, Collection<ResourceNode> resourceNodes)
	{
		KNode rootNode = null;
		for (KNode node : nodes)
		{
			if (rootNode == null)
				rootNode = (KNode)node.getRoot();
			if (node == rootNode)
				continue;
			if (node instanceof ResourceNode)
			{
				resourceNodes.add((ResourceNode)node);
			} else
			if (node instanceof FolderNode)
			{
				FolderNode folderNode = (FolderNode)node;
				filterResourceNodes(folderNode.getChildren(), resourceNodes);
			}
		}
	}
	private class ResourceTreeDoubleClickListener extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent event)
		{
			if (!UIUtil.isRegularDoubleClick(event))
				return;
			ResourceTreeTable treeTable = (ResourceTreeTable)event.getComponent();
			if (event.getPoint() == null)
				return;
			int row = treeTable.rowAtPoint(event.getPoint());
			if (row == -1)
				return;
			KNode node = treeTable.getNodeAt(row);
			if (node instanceof ResourceNode) {
				ResourceNode rNode = (ResourceNode)node;
				if (rNode.getType().isMedia() &&
					rNode.getType() != ResourceType.SOUND) {
					createSnuAction.actionPerformed(null);
				} else {
					try {
						Application.getInstance().edit(rNode.getType(), rNode.getResourceId());
					} catch (Exception e) {
						Application.getInstance().showUnhandledErrorDialog(LanguageBundle.getString("general.errors.uncaughtexception.title"), e);
					}
				}
			}
		}
	}
	/**
	 * Determines which menu items are visible/enabled/whatever when the popup menu is about to be shown.
	 * 
	 * @author d
	 *
	 */
	private class PopupMenuHandler implements PopupMenuListener
	{
		public void popupMenuCanceled(PopupMenuEvent e) {
		}
		public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		}
		public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
			// by default, all items are invisible. that's just the current heuristic, assuming its easier to enable those that should be, as opposed to the opposite
			
			ResourceTreeTable treeTable = resourceBrowser.getResourceTreeTable();
			Point p = treeTable.getMousePosition();
			int column = -1;
			if (p != null) {
				column = treeTable.columnAtPoint(p);
				column = treeTable.convertColumnIndexToModel(column);
			}
			List<? extends KNode> selectedNodes = treeTable.getSelectedNodes();
			
			boolean isSingleFolderSelection = !selectedNodes.isEmpty() && selectedNodes.get(0) instanceof FolderNode;
			boolean isFirstColumn = column == 0;

			if (!isFirstColumn || selectedNodes.isEmpty()) {
				resourceBrowser.getContextMenuItem(ResourceBrowser.Menu.NewFolder).setVisible(true);
				resourceBrowser.getContextMenuItem(ResourceBrowser.Menu.NewInterface).setVisible(true);
				resourceBrowser.getContextMenuItem(ResourceBrowser.Menu.ProjectSettings).setVisible(true);
			} else {
				boolean isOnlySnu = !selectedNodes.isEmpty();
				boolean isOnlyMediaOrSnu = !selectedNodes.isEmpty();
				boolean isOnlySnuableMedia = !selectedNodes.isEmpty();
				boolean isOnlyInterface = !selectedNodes.isEmpty();
				for (KNode node : selectedNodes)
				{
					if (node instanceof ResourceNode)
					{
						ResourceType type = ((ResourceNode)node).getResourceType();
						resourceBrowser.getContextMenuItem(ResourceBrowser.Menu.Properties).setVisible(true);
						
						if (!type.isMedia() && type!=ResourceType.SNU)
							isOnlyMediaOrSnu = false;
						
						if (!type.isMedia() && type!=ResourceType.SOUND)
							isOnlySnuableMedia = false;
						
						if (type!=ResourceType.SNU)
							isOnlySnu = false;
						
						if (type!=ResourceType.INTERFACE)
							isOnlyInterface = false;
						
					} else {
						isOnlyMediaOrSnu = false;
						isOnlySnuableMedia = false;
						isOnlySnu = false;
						isOnlyInterface = false;
					}
				}
				if (isOnlySnu) {
					resourceBrowser.getContextMenuItem(ResourceBrowser.Menu.OpenAsSnu).setVisible(true);
					resourceBrowser.getContextMenuItem(ResourceBrowser.Menu.Properties).setVisible(false);
					resourceBrowser.getContextMenuItem(ResourceBrowser.Menu.MediaProperties).setVisible(true);
				}
				if (isOnlyMediaOrSnu)
					resourceBrowser.getContextMenuItem(ResourceBrowser.Menu.RevealInPlatformFilesystemBrowser).setVisible(true);
				if (isOnlySnuableMedia)
					resourceBrowser.getContextMenuItem(ResourceBrowser.Menu.CreateSnu).setVisible(true);
				if (isOnlyInterface)
					resourceBrowser.getContextMenuItem(ResourceBrowser.Menu.ExportInterface).setVisible(true);
				if (selectedNodes.size() == 1)
				{
					KNode selectedNode = selectedNodes.get(0);
					resourceBrowser.getContextMenuItem(ResourceBrowser.Menu.Rename).setVisible(true);
				}
				resourceBrowser.getContextMenuItem(ResourceBrowser.Menu.Delete).setVisible(true);
				// Copy/Paste replaced with Duplicate see issue #741
				resourceBrowser.getContextMenuItem(ResourceBrowser.Menu.Duplicate).setVisible(true);
//				resourceBrowser.getContextMenuItem(ResourceBrowser.Menu.Copy).setVisible(true);
//				if (Toolkit.getDefaultToolkit().getSystemClipboard().isDataFlavorAvailable(DataFlavors.TreeTableNodesFlavor)) {
//					resourceBrowser.getContextMenuItem(ResourceBrowser.Menu.Paste).setVisible(true);
//				}
			}
			
			// everything past here is just fixups & niceties
			
			final JPopupMenu menu = (JPopupMenu)e.getSource();
			JComponent prev = null;
			boolean showNextSeparator = false;
			boolean haveVisibleAfterLastSeparator = false;
			JSeparator lastSeparator = null;
			// we first set all separators visible and then hide any consecutive or trailing ones
			for (int i = 0; i < menu.getComponentCount(); ++i) {
				JComponent comp = (JComponent)menu.getComponent(i);
				if (comp instanceof JSeparator)
					comp.setVisible(true);
			}
			for (int i = 0; i < menu.getComponentCount(); ++i) {
				JComponent comp = (JComponent)menu.getComponent(i);
				
				// no double separators
				if (comp instanceof JSeparator) {
					lastSeparator = (JSeparator)comp;
					if (!showNextSeparator) {
						comp.setVisible(false);
					}
				} else {
					haveVisibleAfterLastSeparator = comp.isVisible();
				}
				
				showNextSeparator = comp instanceof JSeparator == false && (showNextSeparator || comp.isVisible());
				prev = comp;
			}
			if (!haveVisibleAfterLastSeparator && lastSeparator != null)
				lastSeparator.setVisible(false);

			boolean hasVisibleItems = false;
			for (int i = 0; i < menu.getComponentCount(); ++ i) {
				if (menu.getComponent(i).isVisible()) {
					hasVisibleItems = true;
					break;
				}
			}

			if (!hasVisibleItems) {
				// using runLater is flakey since we admit unclear timing of operations, simply that
				// we need to let this run after some "other" events have transpired
				// we're just lucky that this happens to run late enough to work
                UIUtil.runUITaskLater(new Runnable() {
                	public void run() {
        				menu.setVisible(false);
                	}
                });
			}
		}
	}
	private static class ResourceTreeSorter implements Comparator<KNode>
	{
		private Object key;
		private int sortDir = 1; // -1 to reverse order
		public ResourceTreeSorter()
		{
		}
		/**
		 * pass > 0 for forward, < 0 for reverse
		 */
		public void setSortDirection(int dir)
		{
			dir = (int)Math.signum(dir);
			if (dir == 0)
				throw new IllegalArgumentException("invalid direction: should be 1 or -1");
			sortDir = dir;
		}
		public int getSortDirection()
		{
			return sortDir;
		}
		public Object getSortKey()
		{
			return key;
		}
		public void setSortKey(Object property)
		{
			key = property;
		}
		public int compare(KNode o1, KNode o2) {
			return sortDir * _compare(o1, o2);
		}
		private int _compare(KNode o1, KNode o2) {
			if (o1 instanceof FolderNode) {
				if (o2 instanceof FolderNode) {
					return o1.getName().compareTo(o2.getName());
				} else {
					return -1;
				}
			} else {
				if (o2 instanceof FolderNode) {
					return 1;
				} else {
					// fall though (this is the only fallthrough case, i just dont want to keep all the indentation)
				}
			}
			
			// should not happen in current impl
			if (o1 instanceof ResourceNode == false && o2 instanceof ResourceNode == false)
				return 0;
			
			ResourceNode rn1 = (ResourceNode)o1;
			ResourceNode rn2 = (ResourceNode)o2;
			
			if (key instanceof PropertyKey) {
				return compareByPropertyKey(rn1, rn2, (PropertyKey)key);
			} else
			if (key instanceof ResourceBrowser.Column) {
				return compareByColumn(rn1, rn2, (ResourceBrowser.Column)key);
			} else
				return 0;
		}
		private int rankClickSound(ResourceNode r1)
		{
			return r1.getClickSound()?1:-1;
		}
		private int rankBackgroundSound(ResourceNode r1)
		{
			return r1.getBgSound()?1:-1;
		}
		private int rankPreview(ResourceNode r1)
		{
			return r1.getPreview()?1:-1;
		}
		private int rankIsSnu(ResourceNode r1)
		{
			return r1.isSnufied()?1:-1;
		}
		private int compareByColumn(ResourceNode r1, ResourceNode r2, ResourceBrowser.Column key)
		{
			switch (key)
			{
			case CLICKSOUND:
				return (int)Math.signum(rankClickSound(r1) - rankClickSound(r2));
			case BACKGROUNDSOUND:
				return (int)Math.signum(rankBackgroundSound(r1) - rankBackgroundSound(r2));
			case PREVIEW:
				return (int)Math.signum(rankPreview(r1) - rankPreview(r2));
			case ISSNU:
				return (int)Math.signum(rankIsSnu(r1) - rankIsSnu(r2));
			}
			return 0;
		}
		private int compareBoolean(boolean b1, boolean b2)
		{
			return -1*Boolean.valueOf(b1).compareTo(b2); // TODO: just reverse order instead of -1
		}
		private int compareNumber(Long n1, Long n2)
		{
			if (n1 == null) {
				if (n2 == null) {
					return 0;
				} else {
					return 1;
				}
			} else {
				if (n2 == null) {
					return -1;
				} else {
					return (int)(n1 - n2);
				}
			}
		}
		private int compareString(String n1, String n2)
		{
			if (n1 == null) {
				if (n2 == null) {
					return 0;
				} else {
					return 1;
				}
			} else {
				if (n2 == null) {
					return -1;
				} else {
					return n1.compareToIgnoreCase(n2);
				}
			}
		}
		private int compareByPropertyKey(ResourceNode r1, ResourceNode r2, PropertyKey key)
		{
			if (key == ResourceProperty.NAME)
				return r1.getName().compareToIgnoreCase(r2.getName());
			if (key == ResourceProperty.TYPE)
				return r1.getResourceType().compareTo(r2.getResourceType());
			if (key == SnuProperty.STARTER)
				return compareBoolean(r1.isStartSnu(), r2.isStartSnu());
			if (key == SnuProperty.ENDER)
				return compareBoolean(r2.isEndSnu(), r2.isEndSnu());
			if (key == SnuProperty.LIVES)
				return compareNumber(r1.getLives(), r2.getLives());
			if (key == SnuProperty.INTERFACE)
				return compareString(r1.getInterfaceName(), r2.getInterfaceName());
			return 0;
		}
	}
	private static class SorterListener extends MouseAdapter implements TreeModelListener, Runnable
	{
		private final ResourceTreeTable treeTable;
		private final ResourceTreeSorter nodeComparator;
		private boolean isUpdating = false;
		public SorterListener(ResourceTreeTable treeTable)
		{
			this.treeTable = treeTable;
			nodeComparator = new ResourceTreeSorter();
			
			treeTable.getTableHeader().addMouseListener(this);
			treeTable.getTreeTableModel().addTreeModelListener(this);
			treeTable.addPropertyChangeListener("model", new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					SorterListener.this.treeTable.getTreeTableModel().addTreeModelListener(SorterListener.this);
				}
			});
		}
		public void update()
		{
			if (isUpdating) // avoids not recursion but constant repitiion
				return;
			Application.getInstance().enqueueCommonTask(this);
		}
		public void run()
		{
			doUpdate();
		}
		public void sortNode(KNode node)
		{
			// depth first, though i don't think it matters in the least since we always traverse the entire tree and sorting each node is independent
			int childCount = node.getChildCount();
			List<KNode> list = new ArrayList<KNode>();
			for (int i = 0; i < childCount; ++i)
			{
				KNode child = node.getChildAt(i);
				list.add(child);
				sortNode(child);
			}
			
			Collections.sort(list, nodeComparator);
			for (KNode child : list)
			{
				node.setChildIndex(list.indexOf(child), child);
			}
		}
		private void doUpdate()
		{
			if (isUpdating) // dont recurse, since we fireChanged
				return;
			isUpdating = true;
			
			Application.getInstance().beginBusyOperation();
			
			try {
				Collection<KNode> expanded = treeTable.getExpandedNodes();
				
				sortNode(treeTable.getTreeTableModel().getRoot());
				treeTable.getTreeTableModel().fireChanged();
				
				treeTable.expandNodes(expanded);
				
			} finally {
				Application.getInstance().endBusyOperation();
				isUpdating = false;
			}
		}
		@Override
		public void mouseClicked(MouseEvent event)
		{
			int viewColumnIndex = treeTable.columnAtPoint(event.getPoint());
			int modelColumnIndex = treeTable.convertColumnIndexToModel(viewColumnIndex);
			if (modelColumnIndex == -1)
				return;
			Object identifier = treeTable.getColumnModel().getColumn(viewColumnIndex).getIdentifier();
			Object previousSortKey = nodeComparator.getSortKey();
			nodeComparator.setSortKey(identifier);
			
			int sortDir = nodeComparator.getSortDirection();
			if (previousSortKey == identifier) {
				sortDir *= -1;
				nodeComparator.setSortDirection(sortDir);
			} else {
				sortDir = 1;
				nodeComparator.setSortDirection(sortDir);
			}
			
			treeTable.putClientProperty("ui.sortedColumnDirection", sortDir);
			treeTable.putClientProperty("ui.sortedColumn", modelColumnIndex);
			
			doUpdate();
		}
		public void treeNodesChanged(TreeModelEvent e) {
			update();
		}
		public void treeNodesInserted(TreeModelEvent e) {
			update();
		}
		public void treeNodesRemoved(TreeModelEvent e) {
			update();
		}
		public void treeStructureChanged(TreeModelEvent e) {
			update();
		}
	}
	private static class CreateSnuActionButtonEnabler implements ListSelectionListener
	{
		private final ResourceBrowser resourceBrowser;
		public CreateSnuActionButtonEnabler(ResourceBrowser resourceBrowser)
		{
			this.resourceBrowser = resourceBrowser;
		}
		public void valueChanged(ListSelectionEvent e) {
			Collection<? extends KNode> selectedNodes = resourceBrowser.getResourceTreeTable().getSelectedNodes();
			// basically the button is enabled IFF the selection contains only snuable media
			boolean selectionIsSnuable = !selectedNodes.isEmpty();
			for (KNode node : selectedNodes) {
				if (node instanceof ResourceNode == false) {
					selectionIsSnuable = false;
					break;
				}
				ResourceNode resourceNode = (ResourceNode)node;
				ResourceType resource = resourceNode.getResourceType();
				if (resourceNode.getResourceType().isMedia()) {
					selectionIsSnuable = false;
					break;
				}
				if (resourceNode.getResourceType() == ResourceType.SOUND) {
					selectionIsSnuable = false;
					break;
				}
			}
			resourceBrowser.setActionButtonEnabled(ResourceBrowser.Action.AddSnu, selectionIsSnuable);
		}
		
	}
	private static class StatusUpdater extends MouseInputAdapter
	{
		ResourceBrowser resourceBrowser;
	    public StatusUpdater(ResourceBrowser resourceBrowser)
		{
			super();
			this.resourceBrowser = resourceBrowser;
		}
	    @Override
		public void mouseExited(MouseEvent e) {
			resourceBrowser.setStatus(" ");
	    }
		@Override
		public void mouseMoved(MouseEvent e) {
			if (e.getPoint() == null) // why/when would this happen? but it did...
				return;
	    	int row = resourceBrowser.getResourceTreeTable().rowAtPoint(e.getPoint());
	    	if (row == -1)
	    		return;
	    	KNode node = resourceBrowser.getResourceTreeTable().getNodeAt(row);
	    	if (node == null) // this actually happened to many users as around 2012/10
	    		return;
	    	if (node instanceof ResourceNode) {
	    		ResourceNode resourceNode = (ResourceNode)node;
	    		switch (resourceNode.getResourceType())
	    		{
	    		case IMAGE:
	    		case SOUND:
	    		case TEXT:
	    		case VIDEO:
	    			try {
						resourceBrowser.setStatus(MediaInputMapper.map(resourceNode.getResourceId()).getFilename());
					} catch (MapperException e1) {
						resourceBrowser.setStatus("");
						Logger.getLogger(getClass()).error("", e1);
					}
	    			break;
	    		case SNU:
	    			try {
						resourceBrowser.setStatus(SnuInputMapper.map(resourceNode.getResourceId()).getMainMedia().getFilename());
					} catch (MapperException e1) {
						resourceBrowser.setStatus("");
						Logger.getLogger(getClass()).error("", e1);
					}
	    			break;
    			default:
	    			resourceBrowser.setStatus(" ");
	    			break;
	    		}
	    	}
	    }
	}
}

