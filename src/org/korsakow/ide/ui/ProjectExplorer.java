package org.korsakow.ide.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.ui.resourceexplorer.ResourceBrowser;
import org.korsakow.ide.util.Platform;
import org.korsakow.ide.util.UIResourceManager;
import org.korsakow.ide.util.UIUtil;


public class ProjectExplorer extends JFrame
{
	public static enum Action
	{
		MenuFile,
		MenuFileImport,
		MenuFileImportK3Project,
		MenuFileImportInterface,
		MenuFileImportMedia,
		MenuFileExport,
		MenuFileExportWeb,
		MenuFileExportDraftWeb,
		MenuFileExportInterface,
		MenuFileSave,
		MenuFileSaveAs,
		MenuFileOpen,
		MenuFileNew,
		MenuFileRecent,
		MenuFileProjectSettings,
		MenuFileExit,
		
		MenuEdit,
		MenuEditDuplicate,
		MenuEditCopy,
		MenuEditPaste,
		MenuEditDelete,
		MenuEditUndo,
		MenuEditRedo,
		
		MenuTools,
		MenuToolsKeywordPool,
		MenuToolsSnuPool,
		MenuToolsLinkPool,
		MenuToolsPossiblePool,
		
		MenuLanguage,
		
		MenuWindow,
		
		MenuHelp,
		MenuHelpAbout,
		MenuHelpExample,
		MenuHelpLog,
		MenuHelpKorsakowWebSite,
		
		MenuInterfacesShowBuilder,
	}
	
	private final EventListenerList listenerList = new EventListenerList();
	private ResourceBrowser resourceBrowser;
	private JMenuBar mainMenu;
	private final HashMap<Action, JMenuItem> menus = new HashMap<Action, JMenuItem>();
	
	public ProjectExplorer()
	{
		initUI();
		initListeners();
//		resourceExplorer.setSelectedTab(Tab.SNU);
//		showResourceList(ResourceType.ALL);
	}
	@Override
	public void finalize() throws Throwable
	{
		super.finalize();
		System.err.println("ProjectExplorer.finalize");
	}
	private void initUI()
	{
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		//setLayout(new FlowLayout(FlowLayout.LEFT));
		setTitle(LanguageBundle.getString("projectexplorer.window.title"));
		setIconImage(UIResourceManager.getImage(UIResourceManager.ICON_WINDOW_ICON));

		mainMenu = new JMenuBar();
		setJMenuBar(mainMenu);
		JMenu menu;
		JMenuItem menuItem;
		
		// file menu
		mainMenu.add(menu = createMenu(LanguageBundle.getString("projectexplorer.menu.file.label"), Action.MenuFile, KeyEvent.VK_F));
		menu.add(createMenuItem(LanguageBundle.getString("projectexplorer.menu.file.new.label"), Action.MenuFileNew, KeyEvent.VK_N, 'N'));
		menu.add(createMenuItem(LanguageBundle.getString("projectexplorer.menu.file.open.label"), Action.MenuFileOpen, KeyEvent.VK_O, 'O'));
		menu.add(createMenu(LanguageBundle.getString("projectexplorer.menu.file.recent.label"), Action.MenuFileRecent, KeyEvent.VK_R));
		menu.add(new JSeparator());
		menu.add(createMenuItem(LanguageBundle.getString("projectexplorer.menu.file.save.label"), Action.MenuFileSave, KeyEvent.VK_S, 'S'));
		menu.add(createMenuItem(LanguageBundle.getString("projectexplorer.menu.file.saveas.label"), Action.MenuFileSaveAs, KeyEvent.VK_A, KeyStroke.getKeyStroke(KeyEvent.VK_S, UIUtil.getPlatformCommandKeyMask() | InputEvent.SHIFT_MASK)));
		menu.add(new JSeparator());
		JMenu importMenu;
		menu.add(importMenu = createMenu(LanguageBundle.getString("projectexplorer.menu.file.import.label"), Action.MenuFileImport, KeyEvent.VK_I));
		importMenu.add(createMenuItem(LanguageBundle.getString("projectexplorer.menu.file.import.media.label"), Action.MenuFileImportMedia, KeyEvent.VK_M, KeyStroke.getKeyStroke(KeyEvent.VK_M, UIUtil.getPlatformCommandKeyMask() | InputEvent.SHIFT_MASK)));
		menu.add(new JSeparator());
		importMenu.add(createMenuItem(LanguageBundle.getString("projectexplorer.menu.file.import.k3project.label"), Action.MenuFileImportK3Project, KeyEvent.VK_3));
		importMenu.add(createMenuItem(LanguageBundle.getString("projectexplorer.menu.file.import.interface.label"), Action.MenuFileImportInterface, KeyEvent.VK_I));
		JMenu exportMenu;
		menu.add(exportMenu = createMenu(LanguageBundle.getString("projectexplorer.menu.file.export.label"), Action.MenuFileExport, KeyEvent.VK_E));
		
		exportMenu.add(createMenuItem(LanguageBundle.getString("projectexplorer.menu.file.export.web.label"),
				Action.MenuFileExportWeb, 
				KeyEvent.VK_E, 
				KeyStroke.getKeyStroke('E', UIUtil.getPlatformCommandKeyMask())));
		addAccelerator(menus.get(Action.MenuFileExportWeb), 
				KeyStroke.getKeyStroke('E', UIUtil.getPlatformCommandKeyMask() | InputEvent.SHIFT_MASK));

		exportMenu.add(createMenuItem(LanguageBundle.getString("projectexplorer.menu.file.export.draftweb.label"), 
				Action.MenuFileExportDraftWeb, 
				KeyEvent.VK_E));

		exportMenu.add(createMenuItem(LanguageBundle.getString("projectexplorer.menu.file.export.interface.label"), Action.MenuFileExportInterface, KeyEvent.VK_E));
		menu.add(new JSeparator());
		menu.add(createMenuItem(LanguageBundle.getString("projectexplorer.menu.file.projectsettings.label"), Action.MenuFileProjectSettings, KeyEvent.VK_COMMA, ','));
		JSeparator exitSeparator = new JSeparator(); // keep reference so we can hide on Mac
		menu.add(exitSeparator);
		menu.add(createMenuItem(LanguageBundle.getString("projectexplorer.menu.file.exit.label"), Action.MenuFileExit, KeyEvent.VK_X));
		
		// edit menu
		mainMenu.add(menu = createMenu(LanguageBundle.getString("projectexplorer.menu.edit.label"), Action.MenuEdit, KeyEvent.VK_E));
		menu.add(createMenuItem(LanguageBundle.getString("projectexplorer.menu.edit.copy.label"), Action.MenuEditCopy, KeyEvent.VK_C, 'C'));
		menu.add(createMenuItem(LanguageBundle.getString("projectexplorer.menu.edit.paste.label"), Action.MenuEditPaste, KeyEvent.VK_V, 'V'));
		menu.add(createMenuItem(LanguageBundle.getString("projectexplorer.menu.edit.duplicate.label"), Action.MenuEditDuplicate, KeyEvent.VK_D, 'D'));
		menu.add(createMenuItem(LanguageBundle.getString("projectexplorer.menu.edit.delete.label"), Action.MenuEditDelete, KeyEvent.VK_K, 'K'));
		menu.add(createMenuItem(LanguageBundle.getString("projectexplorer.menu.edit.undo.label"), Action.MenuEditUndo, KeyEvent.VK_Z, KeyStroke.getKeyStroke(KeyEvent.VK_Z, UIUtil.getPlatformCommandKeyMask())));
		menu.add(createMenuItem(LanguageBundle.getString("projectexplorer.menu.edit.redo.label"), Action.MenuEditRedo, KeyEvent.VK_Y, KeyStroke.getKeyStroke(KeyEvent.VK_Y, UIUtil.getPlatformCommandKeyMask())));

		// tools menu
		mainMenu.add(menu = createMenu(LanguageBundle.getString("projectexplorer.menu.tools.label"), Action.MenuTools, KeyEvent.VK_T));
		menu.add(createMenuItem(LanguageBundle.getString("projectexplorer.menu.tools.keywordpool.label"), Action.MenuToolsKeywordPool, KeyEvent.VK_1, '1'));
		menu.add(createMenuItem(LanguageBundle.getString("projectexplorer.menu.tools.snupool.label"), Action.MenuToolsSnuPool, KeyEvent.VK_2, '2'));
		menu.add(createMenuItem(LanguageBundle.getString("projectexplorer.menu.tools.linkpool.label"), Action.MenuToolsLinkPool, KeyEvent.VK_3, '3'));
		menu.add(createMenuItem(LanguageBundle.getString("projectexplorer.menu.tools.possiblepool.label"), Action.MenuToolsPossiblePool, KeyEvent.VK_4, '4'));

		// language menu
		mainMenu.add(menu = createMenu(LanguageBundle.getString("projectexplorer.menu.language.label"), Action.MenuLanguage, KeyEvent.VK_L));
		
		// window menu
		mainMenu.add(menu = createMenu(LanguageBundle.getString("projectexplorer.menu.window.label"), Action.MenuWindow, KeyEvent.VK_W));
		menu.setVisible(false); // disabled by client request
		
		// help menu
		mainMenu.add(menu = createMenu(LanguageBundle.getString("projectexplorer.menu.help.label"), Action.MenuHelp, KeyEvent.VK_H));
		menu.add(createMenuItem(LanguageBundle.getString("projectexplorer.menu.help.about.label"), Action.MenuHelpAbout, KeyEvent.VK_A));
		menu.add(createMenuItem(LanguageBundle.getString("projectexplorer.menu.help.example.label"), Action.MenuHelpExample, KeyEvent.VK_E));
		menu.add(createMenuItem(LanguageBundle.getString("projectexplorer.menu.help.log.label"), Action.MenuHelpLog, KeyEvent.VK_L));
		menu.add(createMenuItem(LanguageBundle.getString("projectexplorer.menu.help.korsakowwebsite.label"), Action.MenuHelpKorsakowWebSite, KeyEvent.VK_K));

		add(resourceBrowser = new ResourceBrowser());

		if (Platform.isMacOS()) {
			getMenu(Action.MenuHelpAbout).setVisible(false);
			exitSeparator.setVisible(false);
			getMenu(Action.MenuFileExit).setVisible(false);
		}
		// Copy/Paste replaced with Duplicate see issue #741
		getMenu(Action.MenuEditCopy).setVisible(false);
		getMenu(Action.MenuEditPaste).setVisible(false);
	}
	private void initListeners()
	{
	}
	public ResourceBrowser getResourceBrowser()
	{
		return resourceBrowser;
	}
	public void addActionListener(ActionListener listener)
	{
		listenerList.add(ActionListener.class, listener);
	}
	public void addMenuAction(final Action key, final ActionListener action)
	{
		JMenuItem menuItem = getMenu(key);
		if (menuItem instanceof JMenu)
			((JMenu)menuItem).addMenuListener(new MenuListener() {
				public void menuCanceled(MenuEvent e) {}
				public void menuDeselected(MenuEvent e) {}
				public void menuSelected(MenuEvent e) {
					action.actionPerformed(new ActionEvent(ProjectExplorer.this, ActionEvent.ACTION_PERFORMED, key.name()));
				}
				
			});
		else
			menuItem.addActionListener(action);
	}
	public JMenu createMenu(String label, final Action action, int mnemonic)
	{
		JMenu menu = new JMenu(label);
		menu.setMnemonic(mnemonic);
		menu.addMenuListener(new MenuListener() {
			public void menuCanceled(MenuEvent e) {
			}
			public void menuDeselected(MenuEvent e) {
			}
			public void menuSelected(MenuEvent e) {
				UIUtil.dispatchEvent(listenerList, new ActionEvent(ProjectExplorer.this, ActionEvent.ACTION_PERFORMED, action.name()));
			}
			
		});
		menus.put(action, menu);
		return menu;
	}
	public JMenuItem createMenuItem(String label, final Action action, int mnemonic)
	{
		return createMenuItem(label, action, mnemonic, null);
	}
	public JMenuItem createMenuItem(String label, final Action action, int mnemonic, char acceleratorKeyCode)
	{
		return createMenuItem(label, action, mnemonic, KeyStroke.getKeyStroke(acceleratorKeyCode, UIUtil.getPlatformShortcutKeyMask()));
	}
	public JMenuItem createMenuItem(String label, final Action action, int mnemonic, final KeyStroke stroke)
	{
		final JMenuItem menu = new JMenuItem(label, mnemonic);
		if (stroke != null) {
			menu.setAccelerator(stroke);
		}
		menu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				UIUtil.dispatchEvent(listenerList, new ActionEvent(ProjectExplorer.this, ActionEvent.ACTION_PERFORMED, action.name()));
			}
		});
		menus.put(action, menu);
		return menu;
	}
	public void addAccelerator(final JMenuItem item, KeyStroke stroke) {
		SwingUtilities.getUIInputMap(item, JComponent.WHEN_IN_FOCUSED_WINDOW).put(stroke, "doClick");
	}
	public void setTitleExtra(String projectName, String fileName)
	{
		if (projectName == null)
			projectName = "";
		if (fileName == null)
			fileName = "";
		else
			fileName = "(" + fileName + ")";
		setTitle(LanguageBundle.getString("projectexplorer.window.title2", projectName + " " + fileName));
	}
	public JMenuItem getMenu(Action action)
	{
		return menus.get(action);
	}
}
