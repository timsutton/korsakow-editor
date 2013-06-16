/**
 * 
 */
package org.korsakow.ide.controller;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.DefaultFocusManager;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import org.korsakow.domain.interf.IResource;
import org.korsakow.ide.Application;
import org.korsakow.ide.ui.ResourceEditor;
import org.korsakow.ide.util.UIUtil;

public abstract class AbstractResourceEditAction implements ResourceEditAction
{
	private static class ResourceEditorCancelAction extends AbstractAction
	{
		ResourceEditor editor;
		public ResourceEditorCancelAction(ResourceEditor editor) {
			this.editor = editor;
		}
		public void actionPerformed(ActionEvent event) {
			if (DefaultFocusManager.getCurrentManager().getActiveWindow() != editor)
				return;
			editor.dispose();
		}
	}
	public ResourceEditor run(IResource resource) throws Exception
	{
		Application app = Application.getInstance();
		ResourceEditor editor = app.getOpenEditor(resource);
		if (editor == null) {
			editor = app.createResourceEditor(resource);
			
			try {
				initViewHelper(editor, resource);
			} catch (Exception e) {
				editor.dispose();
				throw e;
			}
			editor.pack();
			
			UIUtil.constrainSizeToScreen(editor);
			
			UIUtil.centerOnScreen(editor);
			
		}
		editor.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
		editor.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
		editor.getRootPane().getActionMap().put("escape", new ResourceEditorCancelAction(editor));
		editor.addCancelActionListener( new ResourceEditorCancelAction(editor) );
		editor.getRootPane().setDefaultButton(editor.getOKButton());
		adjustEditorAfterPack(editor, resource);
		
		editor.setVisible(true);
		return editor;
	}
	protected void adjustEditorAfterPack(ResourceEditor editor, IResource resource) {}
	protected abstract void initViewHelper(ResourceEditor editor, IResource resource) throws Exception;
}