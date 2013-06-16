package org.korsakow.ide.ui.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.IResource;
import org.korsakow.ide.Application;
import org.korsakow.ide.ui.ResourceEditor;
import org.korsakow.ide.ui.ProjectExplorer.Action;


public class MenuWindowAction implements ActionListener
{

	public void actionPerformed(ActionEvent e) {
		Application app = Application.getInstance();
		JMenu menu = (JMenu)app.getProjectExplorer().getMenu(Action.MenuWindow);
		menu.removeAll();
		ActionListener itemActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JMenuItem comp = (JMenuItem)event.getSource();
				WeakReference<JFrame> ref = (WeakReference<JFrame>)comp.getClientProperty("frame");
				JFrame frame = ref.get();
				if (frame != null) {
					try {
						frame.toFront();
					} catch (Throwable t) {
						// ignored; caught in case frame had already been disposed
					}
				}
			}
		};
		JMenuItem item;
		int count = 0;
		if (app.getKeywordPoolDialog() != null) {
			item = new JMenuItem(app.getKeywordPoolDialog().getTitle(), ++count);
			item.putClientProperty("frame", new WeakReference<JFrame>(app.getKeywordPoolDialog()));
			item.addActionListener(itemActionListener);
			menu.add(item);
		}
		if (app.getSnuPoolDialog() != null) {
			item = new JMenuItem(app.getSnuPoolDialog().getTitle(), ++count);
			item.putClientProperty("frame", new WeakReference<JFrame>(app.getSnuPoolDialog()));
			item.addActionListener(itemActionListener);
			menu.add(item);
		}
		if (app.getLinkPoolDialog() != null) {
			item = new JMenuItem(app.getLinkPoolDialog().getTitle(), ++count);
			item.putClientProperty("frame", new WeakReference<JFrame>(app.getSnuPoolDialog()));
			item.addActionListener(itemActionListener);
			menu.add(item);
		}
		if (app.getPossiblePoolDialog() != null) {
			item = new JMenuItem(app.getPossiblePoolDialog().getTitle(), ++count);
			item.putClientProperty("frame", new WeakReference<JFrame>(app.getSnuPoolDialog()));
			item.addActionListener(itemActionListener);
			menu.add(item);
		}
		if (menu.getMenuComponentCount() > 0)
			menu.add(new JSeparator());
		Collection<ResourceEditor> editors = app.getOpenEditors();
		for (ResourceEditor editor : editors) {
			IResource resource = null;
			try {
				resource = app.getResourceForEditor(editor);
			} catch (MapperException e1) {
				Application.getInstance().showUnhandledErrorDialog(e1);
			}
			if (resource != null) {
				item = new JMenuItem(editor.getTitle() + ":" + resource.getName(), ++count);
				item.putClientProperty("frame", new WeakReference<JFrame>(editor));
				item.addActionListener(itemActionListener);
				menu.add(item);
			}
		}
	}

}
