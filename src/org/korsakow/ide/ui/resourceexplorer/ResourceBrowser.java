package org.korsakow.ide.ui.resourceexplorer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;
import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.resources.ResourceProperty;
import org.korsakow.ide.resources.SnuProperty;
import org.korsakow.ide.ui.components.BackgroundImagePanel;
import org.korsakow.ide.ui.components.tree.FolderNode;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.controller.dnd.ResourceTreeTransferHandler;
import org.korsakow.ide.util.Platform;
import org.korsakow.ide.util.UIResourceManager;
import org.korsakow.ide.util.UIUtil;

import com.sun.swingx.treetable.TreeTableModel;

public class ResourceBrowser extends JPanel
{
	public static enum Menu
	{
		OpenAsSnu,
		Properties,
		MediaProperties,
		Rename,
		Delete,
		Duplicate,
		Copy,
		Paste,
		CreateSnu,
		NewInterface,
		ExportInterface,
		NewFolder,
		ProjectSettings,
		RevealInPlatformFilesystemBrowser, // AKA "Reveal In Finder"
	}
	public static enum Action
	{
		AddSnu,
		AddInterface,
		AddFolder
	}
	public static enum Column
	{
		PREVIEW,
		BACKGROUNDSOUND,
		CLICKSOUND,
		ISSNU,
	}
	
	protected ResourceTreeTable resourceTreeTable;
	protected JScrollPane resourceTreeTableScroll;
	protected JLabel statusLabel;
	protected JButton addSnuButton;
	protected JButton addInterfaceButton;
	protected JButton addFolderButton;
	protected JButton editFolderButton;
	protected JButton deleteFolderButton;
	protected JPopupMenu contextMenu = new JPopupMenu();
	protected HashMap<Object, JMenuItem> contextMenuItems = new HashMap<Object, JMenuItem>();
	protected BackgroundImagePanel treePanel;
	private JPanel buttonPanel;
	
	public ResourceBrowser()
	{
		initUI();
		initListeners();
	}
	public FolderNode getDefaultVideoFolder()
	{
		return resourceTreeTable.getRootNode();
	}
	public ResourceTreeTable getResourceTreeTable()
	{
		return resourceTreeTable;
	}
	
	public void deleteSelectedResource ()
	{
		onDeleteFolderAction();
	}
	
	protected void initUI()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		buttonPanel.setBorder( BorderFactory.createEmptyBorder(3, 0, 3, 0) );
		buttonPanel.add(addSnuButton = new JButton(UIResourceManager.getIcon(UIResourceManager.ICON_SNU_ADD)));
		buttonPanel.add(addInterfaceButton = new JButton(UIResourceManager.getIcon(UIResourceManager.ICON_INTERFACE_ADD)));
		buttonPanel.add(addFolderButton = new JButton(UIResourceManager.getIcon(UIResourceManager.ICON_FOLDER_ADD)));
		buttonPanel.add(editFolderButton = new JButton(UIResourceManager.getIcon(UIResourceManager.ICON_FOLDER_EDIT)));
		buttonPanel.add(deleteFolderButton = new JButton(UIResourceManager.getIcon(UIResourceManager.ICON_DELETE)));
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
		bottomPanel.add(statusLabel = new JLabel(" "));
		bottomPanel.add(buttonPanel);
		statusLabel.setPreferredSize(new Dimension(Short.MAX_VALUE, statusLabel.getHeight()));
		statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
		
		addSnuButton.setToolTipText(LanguageBundle.getString("resourcebrowser.addsnubutton.tooltip"));
		addInterfaceButton.setToolTipText(LanguageBundle.getString("resourcebrowser.addinterfacebutton.tooltip"));
		addFolderButton.setToolTipText(LanguageBundle.getString("resourcebrowser.addfolderbutton.tooltip"));
		editFolderButton.setToolTipText(LanguageBundle.getString("resourcebrowser.editfolderbutton.tooltip"));
		deleteFolderButton.setToolTipText(LanguageBundle.getString("resourcebrowser.deletefolderbutton.tooltip"));
		
		addSnuButton.setBorderPainted(false);
		addInterfaceButton.setBorderPainted(false);
		addFolderButton.setBorderPainted(false);
		editFolderButton.setBorderPainted(false);
		deleteFolderButton.setBorderPainted(false);
		
		addSnuButton.setBorder(null);
		addInterfaceButton.setBorder(null);
		addFolderButton.setBorder(null);
		editFolderButton.setBorder(null);
		deleteFolderButton.setBorder(null);
		
		addSnuButton.setEnabled(false); // initially disabled, this is controlled by the controller
		
		
		treePanel = new BackgroundImagePanel(new BorderLayout());
		ImageIcon bgIcon = (ImageIcon) UIResourceManager.getLanguageIcon(UIResourceManager.DRAG_DROP_KEY);
		treePanel.setImage(bgIcon.getImage());
		add(treePanel);
		
		resourceTreeTableScroll = new JScrollPane(resourceTreeTable = new ResourceTreeTable());
		// set the scroll pane, and the viewport as transparent so the background image can show through
		resourceTreeTableScroll.setOpaque(false);
		resourceTreeTableScroll.getViewport().setOpaque(false);
		
		initResourceTable();
		treePanel.add(resourceTreeTableScroll);

		UIUtil.setDragOnPress(resourceTreeTable);
//		resourceTreeTableScroll.addMouseListener(resourceTreeTable.createStopEditingOnClickListener());
		
		add(bottomPanel);
		
		UIUtil.addColumn(resourceTreeTable, LanguageBundle.getString("resourcebrowser.columns.type.label"), ResourceProperty.TYPE);
		UIUtil.addColumn(resourceTreeTable, LanguageBundle.getString("resourcebrowser.columns.issnu.label"), ResourceBrowser.Column.ISSNU);
		
//		UIUtil.addColumn(resourceTreeTable, LanguageBundle.getString("resourcebrowser.columns.filename.label"), Media.Property.FILENAME);
		UIUtil.addColumn(resourceTreeTable, LanguageBundle.getString("resourcebrowser.columns.clicksound.label"), ResourceBrowser.Column.CLICKSOUND);
		UIUtil.addColumn(resourceTreeTable, LanguageBundle.getString("resourcebrowser.columns.backgroundsound.label"), ResourceBrowser.Column.BACKGROUNDSOUND);
		UIUtil.addColumn(resourceTreeTable, LanguageBundle.getString("resourcebrowser.columns.preview.label"), ResourceBrowser.Column.PREVIEW);

		UIUtil.addColumn(resourceTreeTable, LanguageBundle.getString("resourceexplorer.columns.starting.label"), SnuProperty.STARTER);
		UIUtil.addColumn(resourceTreeTable, LanguageBundle.getString("resourceexplorer.columns.ending.label"), SnuProperty.ENDER);

		UIUtil.addColumn(resourceTreeTable, LanguageBundle.getString("resourceexplorer.columns.lives.label"), SnuProperty.LIVES);
//		UIUtil.addColumn(resourceTreeTable, LanguageBundle.getString("resourceexplorer.columns.maxlinks.label"), Snu.Property.MAXLINKS);
//		UIUtil.addColumn(resourceTreeTable, LanguageBundle.getString("resourceexplorer.columns.mainmedia.label"), Snu.Property.MAINMEDIA);
		UIUtil.addColumn(resourceTreeTable, LanguageBundle.getString("resourceexplorer.columns.interface.label"), SnuProperty.INTERFACE);
		
		resourceTreeTable.getColumn(ResourceProperty.NAME).setPreferredWidth(200);
		resourceTreeTable.getColumn(SnuProperty.INTERFACE).setPreferredWidth(120);
//		UIUtil.setColumnFixedSize(resourceTreeTable, Resource.Property.TYPE, 40);
//		UIUtil.setColumnFixedSize(resourceTreeTable, ResourceBrowser.Column.ISSNU, 70);
		
//		UIUtil.setColumnFixedSize(resourceTreeTable, MediaResourceListColumns.Column.CLICKSOUND, 30);
//		UIUtil.setColumnFixedSize(resourceTreeTable, ResourceBrowser.Column.BACKGROUNDSOUND, 30);
//		UIUtil.setColumnFixedSize(resourceTreeTable, ResourceBrowser.Column.PREVIEW, 30);
		
//		resourceTreeTable.getColumn(Snu.Property.LIVES).setPreferredWidth(20);
//		UIUtil.setColumnFixedSize(resourceTreeTable, Snu.Property.STARTER, 30);
//		UIUtil.setColumnFixedSize(resourceTreeTable, Snu.Property.ENDER, 30);

		
		//resourceTreeTableScroll.setPreferredSize(new Dimension(600, 400));
		UIUtil.runUITaskLater(new Runnable() {
			public void run() {
				resourceTreeTable.getSelectionModel().setSelectionInterval(0, 0);
			}
		});
//		resourceTreeTable.getTreeTableModel().setColumnIdentifier("kk", 0);
		createContextMenu();
		
		UIUtil.runUITaskLater(new Runnable() {
			public void run() {
				TableCellRenderer renderer = resourceTreeTable.getTableHeader().getDefaultRenderer();
				for (int i= 0; i < resourceTreeTable.getColumnCount(); ++i) {
					TableColumn column = resourceTreeTable.getColumnModel().getColumn(i);
					if (column.getIdentifier() == ResourceProperty.NAME)
						continue;
					if (column.getIdentifier() == SnuProperty.INTERFACE)
						continue;
					
					Component renderComp = renderer.getTableCellRendererComponent(resourceTreeTable, column.getHeaderValue(), false, false, 0, i);
					Dimension size = renderComp.getPreferredSize();
					int margin = 10;
					column.setPreferredWidth(size.width + margin*2);
				}
				resourceTreeTable.invalidate();
			}
		});
	}
	protected void initResourceTable ()
	{
		TreeModelListener tListener = new TreeModelListener(){
			Logger logger = Logger.getLogger(ResourceBrowser.class);

			public void treeNodesInserted(TreeModelEvent e) {
				checkToHide(e);
			}

			public void treeNodesRemoved(TreeModelEvent e) {
				checkToShow(e);
			}

			public void treeNodesChanged(TreeModelEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void treeStructureChanged(TreeModelEvent e) {
				checkToHide(e);
				checkToShow(e);
			}
			
			protected void checkToHide ( TreeModelEvent e )
			{
				if ( treePanel.isBackgroundVisible() ) {
					ResourceTreeTableModel tModel = (ResourceTreeTableModel) e.getSource();
					if ( tModel.getChildCount(tModel.getRoot()) > 1 ) {
						treePanel.showBackground(false);
					}
				}
			}
			
			protected void checkToShow ( TreeModelEvent e )
			{
				if ( !treePanel.isBackgroundVisible() ) {
					ResourceTreeTableModel tModel = (ResourceTreeTableModel) e.getSource();
					if ( tModel.getChildCount(tModel.getRoot()) <= 1 ) {
						treePanel.showBackground(true);
					}
				}
			}
		};
		
		resourceTreeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		resourceTreeTable.setAutoCreateColumnsFromModel(false);
		resourceTreeTable.setRootCollapsible(false);
		resourceTreeTable.getTableHeader().setReorderingAllowed(true);
		resourceTreeTable.getTableHeader().setDefaultRenderer(new ResourceTreeTableHeaderCellRenderer());
		resourceTreeTable.setExternalTreeListener(tListener);
	}
	protected void initListeners()
	{
		ResourceTreeTransferHandler resourceTreeTransferHandler = new ResourceTreeTransferHandler(resourceTreeTable);

		resourceTreeTable.setDragEnabled(true);
//		resourceTreeTable.setDropTarget(new KorsakowDropTarget(resourceTreeTable));
		resourceTreeTable.setTransferHandler(resourceTreeTransferHandler);


		// setting the droptarget to "new KorsakowDropTarget(resourceTreeTable)" causes problems under Windows JDK6; anyway it seems unnecessary.
		resourceTreeTableScroll.setTransferHandler(resourceTreeTransferHandler);
		
		resourceTreeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				KNode selected = resourceTreeTable.getSelectedNode();
				boolean isFolder = (selected instanceof FolderNode);
				boolean isRoot = selected == resourceTreeTable.getRootNode();
				editFolderButton.setEnabled(isFolder && !isRoot);
				deleteFolderButton.setEnabled(!isRoot);
			}
		});
		editFolderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				onEditFolderAction();
			}
		});
		deleteFolderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				onDeleteFolderAction();
			}
		});
		resourceTreeTableScroll.addMouseListener(new MouseAdapter() {
		    @Override
			public void mousePressed(MouseEvent event) {
		    	//seems to be a bug with jtreetable that setting the selection on the treetable doesn'ts set the selection on the tree
				resourceTreeTable.getTree().setSelectionInterval(-1, -1);
			}
		});
		resourceTreeTable.addMouseListener(new MouseAdapter() {
		    @Override
			public void mousePressed(MouseEvent event) {
		    	// technically speaking this is flawed, since the popuptrigger might execute
		    	// before us (awt event notification order being arbitrary), before we can actually set the selection
		    	// however it seems to work so far on mac/win which is good enough for me, sorry. (probably beacase they execute on something other than pressed)
		    	int row = resourceTreeTable.rowAtPoint(event.getPoint());
	    		int selectedRows[] = resourceTreeTable.getSelectedRows();
	    		boolean isPointSelected = false;
	    		for (int s : selectedRows) {
	    			if (s == row) {
	    				isPointSelected = true;
	    				break;
	    			}
	    		}
	    		if (!isPointSelected) {
					resourceTreeTable.getTree().setSelectionInterval(row, row);
	    		}
	    	}
		});
		MouseListener popupListener = new MouseAdapter() {
			/**
			 * On OSX when CTRL-CLICK to show context menu on the JTreeTable, Pressed,Released and Clicked are ALL popup triggers
			 * which results in contextMenu.show being called a few times, which for some reason causes it to show up as blank. 
			 */
			private boolean isAlreadyGoingToShow = false;
			@Override
			public void mousePressed(MouseEvent e) {
				maybeShowPopup(e);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				maybeShowPopup(e);
				isAlreadyGoingToShow = false;
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				maybeShowPopup(e);
			}
			public void maybeShowPopup(MouseEvent e) {
				if (isAlreadyGoingToShow || !UIUtil.isPopupTrigger(e)) {
					return;
				}
				isAlreadyGoingToShow = true;
				for (JMenuItem item : contextMenuItems.values())
					item.setVisible(false);
				contextMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		};
		resourceTreeTable.addMouseListener(popupListener);
		resourceTreeTableScroll.addMouseListener(popupListener);
	}
	protected void onAddFolderAction()
	{
		KNode parent = resourceTreeTable.getSelectedNode();
		if (parent == null)
			parent = resourceTreeTable.getRootNode();
		parent = findParentFolderNode(parent);
		if (parent == null)
			return;
		String name = UIUtil.createUniqueName(parent, LanguageBundle.getString("general.newfoldername"));
		FolderNode node = new FolderNode(name);
		resourceTreeTable.getTreeTableModel().appendNode(node, parent);
	}
	protected void onEditFolderAction()
	{
		KNode selected = resourceTreeTable.getSelectedNode();
		if (selected instanceof FolderNode == false)
			return;
		if (selected == resourceTreeTable.getRootNode())
			return;
		resourceTreeTable.editCellAt(selected, 0);
	}
	protected void onDeleteFolderAction()
	{
		Collection<? extends KNode> selected = resourceTreeTable.getSelectedNodes();
		for (ResourceBrowserListener listener : listenerList.getListeners(ResourceBrowserListener.class))
			listener.onDeleteNodes(selected);
	}
	protected void onTreeCellEditingStopped()
	{
		final KNode node = resourceTreeTable.getNodeAt(resourceTreeTable.getEditingRow());
		String name = (String)resourceTreeTable.getDefaultEditor(TreeTableModel.class).getCellEditorValue();
		if (node.getParent().getChild(name) != null) {
			JOptionPane.showMessageDialog(resourceTreeTable, LanguageBundle.getString("general.errors.namealreadytaken.message"));
			UIUtil.runUITaskLater(new Runnable() {
				public void run() {
					resourceTreeTable.editCellAt(node, 0);
				}
			});
			return;
		}
		node.setName(name);
		resourceTreeTable.revalidate();
	}
	protected KNode findParentFolderNode(KNode node)
	{
		while (node!=null && node instanceof FolderNode == false)
		{
			node = node.getParent();
		}
		return node;
	}
	public void addEventListener(ResourceBrowserListener listener)
	{
		listenerList.add(ResourceBrowserListener.class, listener);
	}
	public void addContextMenuItem(Object key, JMenuItem item)
	{
		contextMenuItems.put(key, item);
	}
	public JMenuItem getContextMenuItem(Object key)
	{
		return contextMenuItems.get(key);
	}
	public void addPopupMenuListener(PopupMenuListener listener)
	{
		contextMenu.addPopupMenuListener(listener);
	}
	public void setContextMenuAction(Object key, ActionListener action)
	{
		getContextMenuItem(key).addActionListener(action);
	}
	public void setAction(Action key, ActionListener listener)
	{
		// it betrays the name "set" that we actually "add" actionlisteners, but thats largely just our lazy implementation
		// given that its unlikely to happen that we "set" a given action more than once.
		switch (key)
		{
		case AddFolder:
			addFolderButton.addActionListener(listener);
			break;
		case AddSnu:
			addSnuButton.addActionListener(listener);
			break;
		case AddInterface:
			addInterfaceButton.addActionListener(listener);
			break;
		}
	}
	public void setActionButtonEnabled(Action key, boolean enabled)
	{
		switch (key)
		{
		case AddFolder:
			addFolderButton.setEnabled(enabled);
			break;
		case AddSnu:
			addSnuButton.setEnabled(enabled);
			break;
		case AddInterface:
			addInterfaceButton.setEnabled(enabled);
			break;
		}
	}
	protected JPopupMenu createContextMenu()
	{
		contextMenu.removeAll();
		JMenuItem menuItem;
		contextMenu.add(menuItem = createMenuItem(LanguageBundle.getString("resourcebrowser.menu.createsnu.label"), LanguageBundle.getString("resourcebrowser.menu.createsnu.mnemonic"), Menu.CreateSnu));
		contextMenu.add(new JSeparator());
		contextMenu.add(menuItem = createMenuItem(LanguageBundle.getString("resourcebrowser.menu.openassnu.label"), LanguageBundle.getString("resourcebrowser.menu.openassnu.mnemonic"), Menu.OpenAsSnu));
		contextMenu.add(menuItem = createMenuItem(LanguageBundle.getString("resourcebrowser.menu.properties.label"), LanguageBundle.getString("resourcebrowser.menu.properties.mnemonic"), Menu.Properties));
		contextMenu.add(menuItem = createMenuItem(LanguageBundle.getString("resourcebrowser.menu.mediaproperties.label"), LanguageBundle.getString("resourcebrowser.menu.mediaproperties.mnemonic"), Menu.MediaProperties));
		contextMenu.add(menuItem = createMenuItem(LanguageBundle.getString("resourcebrowser.menu.duplicate.label"), LanguageBundle.getString("resourcebrowser.menu.duplicate.mnemonic"), Menu.Duplicate));
		contextMenu.add(menuItem = createMenuItem(LanguageBundle.getString("resourcebrowser.menu.copy.label"), LanguageBundle.getString("resourcebrowser.menu.copy.mnemonic"), Menu.Copy));
		contextMenu.add(menuItem = createMenuItem(LanguageBundle.getString("resourcebrowser.menu.paste.label"), LanguageBundle.getString("resourcebrowser.menu.paste.mnemonic"), Menu.Paste));
		contextMenu.add(menuItem = createMenuItem(Platform.isMacOS()?LanguageBundle.getString("resourcebrowser.menu.revealinplatformfilesystembrowser.label.osx"):LanguageBundle.getString("resourcebrowser.menu.revealinplatformfilesystembrowser.label"), LanguageBundle.getString("resourcebrowser.menu.revealinplatformfilesystembrowser.mnemonic"), Menu.RevealInPlatformFilesystemBrowser));
		contextMenu.add(new JSeparator());
		contextMenu.add(menuItem = createMenuItem(LanguageBundle.getString("resourcebrowser.menu.rename.label"), LanguageBundle.getString("resourcebrowser.menu.rename.mnemonic"), Menu.Rename));
		contextMenu.add(menuItem = createMenuItem(LanguageBundle.getString("resourcebrowser.menu.delete.label"), LanguageBundle.getString("resourcebrowser.menu.delete.mnemonic"), Menu.Delete));
		contextMenu.add(new JSeparator());
		contextMenu.add(menuItem = createMenuItem(LanguageBundle.getString("resourcebrowser.menu.newfolder.label"), LanguageBundle.getString("resourcebrowser.menu.newfolder.mnemonic"), Menu.NewFolder));
		contextMenu.add(menuItem = createMenuItem(LanguageBundle.getString("resourcebrowser.menu.newinterface.label"), LanguageBundle.getString("resourcebrowser.menu.newinterface.mnemonic"), Menu.NewInterface));
		contextMenu.add(menuItem = createMenuItem(LanguageBundle.getString("resourcebrowser.menu.exportinterface.label"), LanguageBundle.getString("resourcebrowser.menu.exportinterface.mnemonic"), Menu.ExportInterface));
		contextMenu.add(new JSeparator());
		contextMenu.add(menuItem = createMenuItem(LanguageBundle.getString("resourcebrowser.menu.projectsettings.label"), LanguageBundle.getString("resourcebrowser.menu.projectsettings.mnemonic"), Menu.ProjectSettings));
		return contextMenu;
	}
	protected JMenuItem createMenuItem(String label, String mnemonic, Object key)
	{
		return createMenuItem(label, (mnemonic!=null&&mnemonic.length()>0)?(int)mnemonic.charAt(0):null, key);
	}
	protected JMenuItem createMenuItem(String label, Integer mnemonic, Object key)
	{
		JMenuItem menuItem;
		menuItem = UIUtil.createMenuItem(listenerList, label, mnemonic, key.toString());
		addContextMenuItem(key, menuItem);
		return menuItem;
	}
	public void setStatus(String text)
	{
		statusLabel.setText(text);
	}
//	protected JCheckBoxMenuItem createCheckboxMenuItem(String label, String actionCommand)
//	{
//		JCheckBoxMenuItem menuItem;
//		menuItem = UIUtil.createCheckboxMenuItem(listenerList,label, Menu.Duplicate.name());
//		addContextMenuItem(actionCommand, menuItem);
//		return menuItem;
//	}
}

