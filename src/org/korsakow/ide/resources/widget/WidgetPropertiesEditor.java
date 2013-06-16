package org.korsakow.ide.resources.widget;

import javax.swing.JComponent;

import org.korsakow.ide.ui.interfacebuilder.WidgetCanvasModel;

public interface WidgetPropertiesEditor
{
	void addWidgetPropertiesEditorListener(WidgetPropertiesEditorListener listener);
	void removeWidgetPropertiesEditorListener(WidgetPropertiesEditorListener listener);
	JComponent getWidgetPropertiesEditorComponent(WidgetCanvasModel canvasModel);
	void stopEditing();
	void cancelEditing();
}
