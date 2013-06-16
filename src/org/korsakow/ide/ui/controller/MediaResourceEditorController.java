package org.korsakow.ide.ui.controller;

import org.korsakow.ide.ui.ResourceEditor;


public abstract class MediaResourceEditorController
{
	protected ResourceEditor editor;
	protected Long resourceId;
	public MediaResourceEditorController(ResourceEditor editor, Long resourceId)
	{
		this.editor = editor;
		this.resourceId = resourceId;
	}
}
