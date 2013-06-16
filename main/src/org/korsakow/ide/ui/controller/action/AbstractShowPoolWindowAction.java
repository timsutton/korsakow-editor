package org.korsakow.ide.ui.controller.action;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.korsakow.ide.Application;
import org.korsakow.ide.util.UIResourceManager;
import org.korsakow.ide.util.UIUtil;

public abstract class AbstractShowPoolWindowAction implements ActionListener {

	protected JFrame createPoolDialog(String title, JComponent content)
	{
		Application app = Application.getInstance();
		JFrame dialog = new JFrame(title);
		dialog.setIconImage(UIResourceManager.getImage(UIResourceManager.ICON_WINDOW_ICON));
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//		dialog.setTitle(title);
		dialog.getContentPane().add(content);

		dialog.pack();
		
		Dimension size = dialog.getSize();
		size.width = Math.max(size.width, 300);
		size.height = Math.max(size.height, 600);
		dialog.setSize(size);
		
		UIUtil.constrainSizeToScreen(dialog);
		dialog.setResizable(true);
		
		return dialog;
	}
}
