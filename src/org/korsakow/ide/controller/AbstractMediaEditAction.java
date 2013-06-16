/**
 * 
 */
package org.korsakow.ide.controller;

import java.awt.Dimension;

import org.korsakow.domain.interf.IResource;
import org.korsakow.ide.Application;
import org.korsakow.ide.ui.ResourceEditor;
import org.korsakow.ide.util.UIUtil;

public abstract class AbstractMediaEditAction extends AbstractResourceEditAction
{
	@Override
	protected void adjustEditorAfterPack(ResourceEditor editor, IResource resource)
	{
		Application app = Application.getInstance();
		Dimension d = editor.getSize();
		d.width = 400;
		editor.setSize(d);
		UIUtil.centerOnFrame(editor, app.getProjectExplorer());
	}
}