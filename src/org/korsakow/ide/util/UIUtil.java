package org.korsakow.ide.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.EventListenerList;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.TreeNode;

import org.apache.log4j.Logger;
import org.korsakow.ide.Application;
import org.korsakow.ide.ui.components.KTreeTable;
import org.korsakow.ide.ui.components.code.CodeTable;
import org.korsakow.ide.ui.components.code.CodeTableModel;
import org.korsakow.ide.ui.components.tree.FolderNode;
import org.korsakow.ide.ui.components.tree.KNode;
import org.korsakow.ide.ui.components.tree.KTreeTableModel;
import org.korsakow.ide.ui.laf.KorsakowLookAndFeel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sun.awt.SunToolkit;

public class UIUtil
{
	/**
	 * Sets the Korsakow Look and Feel (LAF) while conserving the required Mac look and feel if applicable
	 */
	public static void setUpLAF () 
	{
		//
		// Mac-specific behaviour removed per #840
		//
		
		
		HashMap<String, Object> platformUI = new HashMap<String, Object>();
		final Collection<String> uiNames = new ArrayList<String>();
		
		// on OSX restoring FileChooserUI causes a NullPointerException (also we use FileDialog)
		if (Platform.isWindowsOS()) {
			uiNames.add("FileChooserUI");
		}
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			for (String uiName : uiNames)
				platformUI.put(uiName, UIManager.get(uiName));
		} catch (Exception e) {
			Logger.getLogger(Application.class).error("", e);
		}
//		// properties required to ensure the Menu Bar appears properly in Mac
//		String[] macUIProperties = {"MenuBarUI"};
//		
//		Boolean isMac = Platform.getOSFamily() == Platform.OSFamily.MAC;
//		
//		//
//		// First, set the Mac LAF to grab the properties we need
//		//
//		if ( isMac ) {
//			try{
//				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//			} catch (Exception e) {
//				Logger.getLogger(Application.class).error("", e);
//			}
//			
//			for (int i = 0; i < macUIProperties.length; i++) {
//				String property = macUIProperties[i];
//				macUICollection.put(property, UIManager.get(property));
//			}
//		}
		
		//
		// Second, set the Korsakow LAF, that overrides everything.
		//
		try {
			UIManager.setLookAndFeel(new KorsakowLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			Logger.getLogger(Application.class).error("", e);
		}
		
		for (String uiName : platformUI.keySet())
			UIManager.put(uiName, platformUI.get(uiName));
		//
		// Third, restore the necessary elements of the Mac LAF
		//
//		if ( isMac ) {
//			for (int i = 0; i < macUIProperties.length; i++) {
//				String property = macUIProperties[i];
//				UIManager.put(property, macUICollection.get(property));
//			}
//		}
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
	}
	
	/**
	 * TODO: determine if this is actually different from PlatformCommandKeyMask
	 * @return
	 */
	public static int getPlatformShortcutKeyMask()
	{
		return Toolkit.getDefaultToolkit(  ).getMenuShortcutKeyMask();
	}
	public static int getPlatformCommandKeyMask()
	{
		switch (Platform.getOS())
		{
		case WIN:
		case NIX:
			return InputEvent.CTRL_DOWN_MASK;
		case MAC:
			return InputEvent.META_DOWN_MASK;
		}
		return 0;
	}
	public static boolean isPlatformMultipleSectionKeyDown(InputEvent event)
	{
		switch (Platform.getOS())
		{
		case WIN:
		case NIX:
			return event.isControlDown();
		case MAC:
			return event.isMetaDown();
		}
		return false;
	}
	public static boolean isPlatformCommandKeyDown(InputEvent event)
	{
		switch (Platform.getOS())
		{
		case WIN:
		case NIX:
			return event.isControlDown();
		case MAC:
			return event.isMetaDown();
		}
		return false;
	}
	/**
	 * At least once difference between this and MouseEvent.isPopupTrigger is that this function also
	 * accounts for the fact that on Mac, ctl+left-click = popuptrigger, notably on Macbooks.
	 * 
	 * OTOH this method is useful, OTOH its TOO useful in that it will introduce itself(UIUtil) as a dependency(read: coupling) in too many places, but such is life. If it ever comes to it we can introduce more localized(up to some reasonable package-level) versions of this method.
	 * 
	 * @param event
	 * @return
	 */
	public static boolean isPopupTrigger(MouseEvent event)
	{
		switch (Platform.getOS())
		{
		case WIN:
		case NIX:
			return event.isPopupTrigger();
		case MAC:
			return event.isPopupTrigger();// || (event.getButton()==MouseEvent.BUTTON1&&event.isControlDown());
		}
		return false;
	}
	public static void setChildrenEnabled(Component comp, boolean enabled)
	{
		if (comp instanceof Container) {
			Container parent = (Container)comp;
			for (Component child: parent.getComponents())
				if (child instanceof Component)
					setChildrenEnabled(child, enabled);
		}
	}
	public static int getComponentIndex(Container parent, Component child)
	{
		Component[] comps = parent.getComponents();
		for (int i = 0; i < comps.length; ++i)
			if (comps[i] == child)
				return i;
		return -1;
	}
	public static Timer runUITaskLater(final Runnable runnable, final long delay)
	{
		Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				runUITaskLater(runnable);
			}
		}, delay);
		return timer;
	}
	public static void runUITaskLater(Runnable runnable)
	{
		SwingUtilities.invokeLater(runnable);
	}
	public static void runUITaskNow(Runnable runnable)
	{
		if (SwingUtilities.isEventDispatchThread())
			runnable.run();
		else
			try {
				SwingUtilities.invokeAndWait(runnable);
			} catch (InterruptedException e) {
				Logger.getLogger(UIUtil.class).error("", e);
			} catch (InvocationTargetException e) {
				Logger.getLogger(UIUtil.class).error("", e);
			}
	}
	public static interface RunnableThrow
	{
		public void run() throws Throwable;
	}
	public static void runUITaskNowThrow(final RunnableThrow runnable) throws Exception
	{
		final StrongReference<Exception> ref = new StrongReference<Exception>();
		UIUtil.runUITaskNow(new Runnable() {
			public void run() {
				try {
					runnable.run();
				} catch (Exception e) {
					ref.set(e);
				} catch (Throwable e) {
					ref.set(new Exception(e));
				}
			}
		});
		if (!ref.isNull())
			throw ref.get();
	}
	public static void dispatchEvent(EventListenerList listenerList, ActionEvent event)
	{
		ActionListener[] listeners = listenerList.getListeners(ActionListener.class);
		for (ActionListener listener : listeners) {
			listener.actionPerformed(event);
		}
	}
	public static ActionListener createActionEventRedispatcher(final Component redispatcher, boolean changeSource)
	{
		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				event = new ActionEvent(redispatcher, event.getID(), event.getActionCommand());
				redispatcher.dispatchEvent(event);
			}
		};
		return listener;
	}
	public static ActionListener createActionEventRedispatcher(final Component redispatcher)
	{
		return createActionEventRedispatcher(redispatcher, false);
	}
	public static void addColumn(CodeTable table, String name, String identifier)
	{
		TableColumnModel tableColumnModel = table.getColumnModel();
		CodeTableModel tableModel = table.getModel();
		TableColumn column = new TableColumn(tableColumnModel.getColumnCount());
		column.setHeaderValue(name);
		column.setIdentifier(identifier);
		tableColumnModel.addColumn(column);
		tableModel.setColumnName(tableColumnModel.getColumnCount()-1, identifier);
	}
	public static void addColumn(JTable table, String name, Object identifier)
	{
		TableColumnModel tableColumnModel = table.getColumnModel();
		DefaultTableModel tableModel = (DefaultTableModel)table.getModel();
		TableColumn column = new TableColumn(tableColumnModel.getColumnCount());
		column.setHeaderValue(name);
		column.setIdentifier(identifier);
		tableColumnModel.addColumn(column);
		tableModel.addColumn(identifier);
	}
	public static void addColumn(KTreeTable table, String name, Object identifier)
	{
		TableColumnModel tableColumnModel = table.getColumnModel();
		KTreeTableModel treeTableModel = table.getTreeTableModel();
		TableColumn column = new TableColumn(tableColumnModel.getColumnCount());
		column.setHeaderValue(name);
		column.setIdentifier(identifier);
		tableColumnModel.addColumn(column);
		treeTableModel.addColumn(identifier);
	}
	public static void setColumnFixedSize(JTable table, Object identifier, int width)
	{
		table.getColumn(identifier).setMinWidth(width);
		table.getColumn(identifier).setMaxWidth(width);
		table.getColumn(identifier).setResizable(false);
	}
	private static MouseMotionListener JListDragOnPressMotionListener = new MouseMotionAdapter() {
		@Override
		public void mouseDragged(MouseEvent e) {
			JList list = (JList)e.getSource();
			int index = list.locationToIndex(e.getPoint());
			if (index != -1) {
				// if pressed on part of current selection, maintain the selection
				// otherwise create a new selection
				boolean isSelected = false;
				for (int i : list.getSelectedIndices())
					if (i == index)
						isSelected = true;
				if (!isSelected)
					list.setSelectedIndex(index);
			}
			TransferHandler handler = list.getTransferHandler();
			handler.exportAsDrag(list, e, TransferHandler.COPY);
		}
	};
	private static MouseListener JListDragOnPressListener = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			e.getComponent().removeMouseMotionListener(JListDragOnPressMotionListener);
			e.getComponent().addMouseMotionListener(JListDragOnPressMotionListener);
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			e.getComponent().removeMouseMotionListener(JListDragOnPressMotionListener);
		}
	};
	private static MouseMotionListener JTableDragOnPressMotionListener = new MouseMotionAdapter() {
		@Override
		public void mouseDragged(MouseEvent e) {
			if (e.getButton() != MouseEvent.BUTTON1)
				return;
			JTable table = (JTable)e.getSource();
			int index = table.rowAtPoint(e.getPoint());
			if (index != -1) {
				// if pressed on part of current selection, maintain the selection
				boolean isSelected = false;
				int[] selectedRows = table.getSelectedRows();
				for (int i : selectedRows)
					if (i == index)
						isSelected = true;
				if (!isSelected)
					table.getSelectionModel().setSelectionInterval(index, index);
			}
			TransferHandler handler = table.getTransferHandler();
			handler.exportAsDrag(table, e, TransferHandler.MOVE);
		}
	};
	private static MouseListener JTableDragOnPressListener = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			e.getComponent().removeMouseMotionListener(JTableDragOnPressMotionListener);
			e.getComponent().addMouseMotionListener(JTableDragOnPressMotionListener);
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			e.getComponent().removeMouseMotionListener(JTableDragOnPressMotionListener);
		}
	};
	public static void setDragOnPress(JTable table)
	{
		table.removeMouseListener(JTableDragOnPressListener);
		table.addMouseListener(JTableDragOnPressListener);
	}
	public static void setDragOnPress(JList list)
	{
		list.removeMouseListener(JListDragOnPressListener);
		list.addMouseListener(JListDragOnPressListener);
	}
	public static MouseListener forwardMouseEvents(Component origin, final Component target)
	{
		MouseListener listener = new MouseListener() {
			private MouseEvent fixMouseEvent(MouseEvent e) {
				return new MouseEvent(target, e.getID(), e.getWhen(), e.getModifiers(), e.getX(), e.getY(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
			}
			public void mouseClicked(MouseEvent e) {
				target.dispatchEvent(fixMouseEvent(e));
			}
			public void mouseEntered(MouseEvent e) {
				target.dispatchEvent(fixMouseEvent(e));
			}
			public void mouseExited(MouseEvent e) {
				target.dispatchEvent(fixMouseEvent(e));
			}
			public void mousePressed(MouseEvent e) {
				target.dispatchEvent(fixMouseEvent(e));
			}
			public void mouseReleased(MouseEvent e) {
				target.dispatchEvent(fixMouseEvent(e));
			}
		};
		origin.addMouseListener(listener);
		return listener;
	}
	public static String createUniqueName(KNode parent, String base, String prefix)
	{
		if (prefix.length() == 0) //otherwise we'd loop indefinite
			throw new IllegalArgumentException("empty prefix");
		String name = base;
		while (true)
		{
			int length = parent.getChildCount();
			for (int i = 0; i < length; ++i)
			{
				KNode child = parent.getChildAt(i);
				if (child.getName().equals(name)) {
					name = prefix + name;
					i = -1; // -1 cause of subsequent ++, java has no unsigned =P must start over since name has changed
					continue;
				}
			}
			break;
		}
		return name;
	}
	public static String createUniqueName(KNode parent, String base)
	{
		String name = base;
		List<Integer> numbers = new ArrayList<Integer>();
		for (int i = 0; i < parent.getChildCount(); ++i)
			numbers.add(i);
		List<Integer> toRemove = new ArrayList<Integer>();
		for (int i = 0; i < parent.getChildCount(); ++i) {
			KNode child = parent.getChildAt(i);
			if (child.getName() == null)
				continue;
			if (child.getName().equals(name)) {
				toRemove.add(0);
				continue;
			}
			if (!child.getName().startsWith(name + " "))
				continue;
			try {
				int num = Integer.parseInt(child.getName().substring(name.length() + 1));
				toRemove.add(num);
			} catch (NumberFormatException e) {
				// nothing
			}
		}
		numbers.removeAll(toRemove);
		int num = 0;
		if (numbers.isEmpty()) {
			num = parent.getChildCount();
		} else {
			num = numbers.get(0);
		}
		if (num > 0)
			name = name + " "  + num;
		return name;
	}
	public static Element resourceTreeToDom(Document doc, FolderNode treeRoot)
	{
        Element docRoot = doc.createElement("resources");
        docRoot.appendChild(treeRoot.toDomElement(doc));
        return docRoot;
	}
	public static boolean isRegularDoubleClick(MouseEvent event)
	{
		return event.getButton() == 1 && event.getClickCount() == 2;
	}
	public static boolean isRegularSingleClick(MouseEvent event)
	{
		return !event.isPopupTrigger() && event.getButton() == 1 && event.getClickCount() == 1;
	}
	public static boolean isRooted(TreeNode root, TreeNode node)
	{
		while (node != null) {
			if (node == root)
				return true;
			node = node.getParent();
		}
		return false;
	}
	public static JMenuItem createMenuItem(final EventListenerList listenerList, String label, Integer mnemonic, final String actionCommand)
	{
		final JMenuItem menuItem = new JMenuItem(label);
		if (mnemonic != null)
			menuItem.setMnemonic(mnemonic);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				UIUtil.dispatchEvent(listenerList, new ActionEvent(menuItem, ActionEvent.ACTION_PERFORMED, actionCommand));
			}
		});
		return menuItem;
	}
	public static JCheckBoxMenuItem createCheckboxMenuItem(final EventListenerList listenerList, String label, final String actionCommand)
	{
		final JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(label);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				UIUtil.dispatchEvent(listenerList, new ActionEvent(menuItem, ActionEvent.ACTION_PERFORMED, actionCommand));
			}
		});
		return menuItem;
	}
	private static JFrame _graphicsConfigurator = new JFrame();
	private static GraphicsConfiguration getGraphicsConfiguration()
	{
		if (_graphicsConfigurator == null)
			_graphicsConfigurator = new JFrame();
		return _graphicsConfigurator.getGraphicsConfiguration();
	}
	public static Dimension getAvailableScreenSize()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
		screenSize.width -= screenInsets.left + screenInsets.right;
		screenSize.height -= screenInsets.top + screenInsets.bottom;
		return screenSize;
	}
	public static Dimension constrainSizeToScreen(Component comp)
	{
		Dimension screenSize = getAvailableScreenSize();
		Dimension size = comp.getSize();
		size.width = Math.min(size.width, screenSize.width);
		size.height = Math.min(size.height, screenSize.height);
		comp.setSize(size);
		return size;
	}
	public static void centerOnScreen(Window dialog)
	{
		Dimension screenSize = getAvailableScreenSize();
		Dimension size = dialog.getSize();
		dialog.setLocation((screenSize.width-size.width)/2, (screenSize.height-size.height)/2);
	}
	public static void centerOnScreen(JDialog dialog)
	{
		Dimension screenSize = getAvailableScreenSize();
		Dimension size = dialog.getSize();
		dialog.setLocation((screenSize.width-size.width)/2, (screenSize.height-size.height)/2);
	}
	public static void centerOnFrame(JDialog dialog, Window frame)
	{
		dialog.setLocation(frame.getX() + (frame.getWidth() - dialog.getWidth())/2, frame.getY() + (frame.getHeight() - dialog.getHeight())/2);
	}
	public static void centerOnFrame(JDialog dialog, JFrame frame)
	{
		dialog.setLocation(frame.getX() + (frame.getWidth() - dialog.getWidth())/2, frame.getY() + (frame.getHeight() - dialog.getHeight())/2);
	}
	public static void centerOnFrame(JFrame dialog, JFrame frame)
	{
		dialog.setLocation(frame.getX() + (frame.getWidth() - dialog.getWidth())/2, frame.getY() + (frame.getHeight() - dialog.getHeight())/2);
	}
	/**
	 * Simulates the WINDOW_CLOSING event.
	 * This is pertinant since it allows JFrame/JDialog defaultCloseOperation and the event hooks to operate as usual.
	 * @author d
	 *
	 */
	public static void closeWindow(Window window) {
		WindowEvent event = new WindowEvent(window, WindowEvent.WINDOW_CLOSING);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(event);
		event = new WindowEvent(window, WindowEvent.WINDOW_CLOSED);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(event);
		SunToolkit.flushPendingEvents(); // force them to happen synchronously
		window.dispose(); // for some reason the above doesn't always work
	}
}
