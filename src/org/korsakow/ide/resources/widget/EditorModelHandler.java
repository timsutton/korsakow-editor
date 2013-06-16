/**
 * 
 */
package org.korsakow.ide.resources.widget;

import java.awt.Component;
import java.util.Collection;

import javax.swing.JComboBox;
import javax.swing.event.CellEditorListener;

/**
 * A pluggable handler (controller) for the model side process of editing widget properties.
 * @author d
 *
 */
public interface EditorModelHandler
{
	void initializeEditor(Collection<? extends WidgetModel> widgets, JComboBox editor, String propertyName);
	void commitProperty(Collection<? extends WidgetModel> widgets, String propertyName, Object value);
	Component getPropertyRenderer(String propertyName, Object propertyValue);
	void addEditorListener(CellEditorListener listener);
	void removeEditorListener(CellEditorListener listener);
}