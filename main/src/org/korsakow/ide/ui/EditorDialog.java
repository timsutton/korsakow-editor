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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import org.korsakow.ide.lang.LanguageBundle;
import org.korsakow.ide.util.StrongReference;


public class EditorDialog extends JDialog
{
	public static class EditorCancelAction extends AbstractAction
	{
		EditorDialog editor;
		public EditorCancelAction(EditorDialog editor) {
			this.editor = editor;
		}
		public void actionPerformed(ActionEvent event) {
			if (DefaultFocusManager.getCurrentManager().getActiveWindow() != editor)
				return;
			editor.cancelButton.doClick();
		}
	}
	public static final ActionListener DisposeAction = EditorFrame.DisposeAction;
	public static EditorDialog fromWindow(Window window)
	{
		if (window instanceof JFrame)
			return new EditorDialog((JFrame)window);
		else
			return new EditorDialog((JDialog)window);
	}
	
	protected JButton okButton;
	protected JButton cancelButton;
	
	public EditorDialog(JFrame parent)
	{
		super(parent);
		initUI();
	}
	public EditorDialog(JDialog parent)
	{
		super(parent);
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
		vBox.add(hBox);
		add(vBox, BorderLayout.SOUTH);
	}
	public void initKeybindings() {
		getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
		getRootPane().getActionMap().put("escape", new EditorCancelAction(this));
		getRootPane().setDefaultButton(okButton);
	}
	/**
	 * Convenience method for showing modal dialogs and getting the OK/Cancel as the return value
	 * @return
	 */
	public boolean showModal()
	{
		final StrongReference<Boolean> resultRef = new StrongReference<Boolean>();
		ActionListener okListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resultRef.set(Boolean.TRUE);
				dispose();
			}
		};
		ActionListener cancelListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resultRef.set(Boolean.FALSE);
				dispose();
			}
		};
		addOKButtonActionListener(okListener);
		addCancelButtonActionListener(cancelListener);
		setModal(true);
		setVisible(true);
		removeOKButtonActionListener(okListener);
		removeCancelButtonActionListener(cancelListener);
		return !resultRef.isNull() && resultRef.get(); // isNull in case the dialog is otherwise disposed
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
	public void removeOKButtonActionListener(ActionListener listener)
	{
		okButton.removeActionListener(listener);
	}
	public void removeCancelButtonActionListener(ActionListener listener)
	{
		cancelButton.removeActionListener(listener);
	}
}
