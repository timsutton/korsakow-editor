package org.korsakow.ide.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.DefaultFocusManager;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import org.korsakow.ide.lang.LanguageBundle;


public class EditorFrame extends JFrame
{
	public static class EditorCancelAction extends AbstractAction
	{
		EditorFrame editor;
		public EditorCancelAction(EditorFrame editor) {
			this.editor = editor;
		}
		public void actionPerformed(ActionEvent event) {
			if (DefaultFocusManager.getCurrentManager().getActiveWindow() != editor)
				return;
			editor.cancelButton.doClick();
		}
	}
	public static final ActionListener DisposeAction = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JComponent comp = (JComponent)e.getSource();
			Window win = (Window)comp.getTopLevelAncestor();
			win.dispose();
		}
	};
	protected JButton okButton;
	protected JButton cancelButton;
	
	public EditorFrame()
	{
		initUI();
		initKeybindings();
	}
	private void initUI()
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setLayout(new BorderLayout());

		Box hBox = Box.createHorizontalBox();
		hBox.add(Box.createHorizontalGlue());
		hBox.add(okButton = new JButton(LanguageBundle.getString("resourceeditor.okbutton.label")));
		hBox.add(Box.createHorizontalStrut(10));
		hBox.add(cancelButton = new JButton(LanguageBundle.getString("resourceeditor.cancelbutton.label")));
		Box vBox = Box.createVerticalBox();
		vBox.add(Box.createVerticalStrut(10));
		add(vBox, BorderLayout.SOUTH);
	}
	public void initKeybindings() {
		getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
		getRootPane().getActionMap().put("escape", new EditorCancelAction(this));
		getRootPane().setDefaultButton(okButton);
	}
	
	public void setContent(Component content)
	{
		add(content, BorderLayout.CENTER);
	}
	public void addOKButtonActionListener(ActionListener listener)
	{
		okButton.addActionListener(listener);
	}
	public void addCancelButtonActionListener(ActionListener listener)
	{
		cancelButton.addActionListener(listener);
	}
}
