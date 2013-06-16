/**
 * 
 */
package org.korsakow.ide.ui.controller.eventeditor;

import java.util.Collection;

import javax.swing.ListCellRenderer;

import org.korsakow.ide.ui.resources.EventEditor;

public interface ArgConfig
{
	String getLabel();
	Collection<?> getChoices();
	boolean isEditable();
	ListCellRenderer getRenderer(EventEditor editor);
}