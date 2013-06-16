package org.korsakow.ide.resources.widget;

import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;

import org.korsakow.ide.ui.components.KLayoutPanel;
import org.korsakow.ide.ui.interfacebuilder.WidgetCanvasModel;

/**
 * Provides a basic abstract class for widget editors used with a WidgetCanvas
 * @author d
 *
 */
public class AbstractWidgetPropertiesEditor extends KLayoutPanel implements WidgetPropertiesEditor
{
	protected EventListenerList listenerlist = new EventListenerList();
	protected Collection<? extends WidgetModel> widgets;
	protected WidgetCanvasModel canvasModel;
	public AbstractWidgetPropertiesEditor(Collection<WidgetModel> widgets)
	{
		this.widgets = widgets;
	}
	public JComponent getWidgetPropertiesEditorComponent(WidgetCanvasModel canvasModel)
	{
		this.canvasModel = canvasModel;
		return this;
	}
	public WidgetCanvasModel getCanvasModel()
	{
		return canvasModel;
	}
	public void addWidgetPropertiesEditorListener(WidgetPropertiesEditorListener listener) {
		listenerlist.add(WidgetPropertiesEditorListener.class, listener);
	}
	public void removeWidgetPropertiesEditorListener(WidgetPropertiesEditorListener listener) {
		listenerlist.remove(WidgetPropertiesEditorListener.class, listener);
	}
	public void fireEditingStopped()
	{
		ChangeEvent event = new ChangeEvent(this);
		for (WidgetPropertiesEditorListener listener : listenerlist.getListeners(WidgetPropertiesEditorListener.class))
			listener.editingStopped(event);
	}
	public void fireEditingCanceled()
	{
		ChangeEvent event = new ChangeEvent(this);
		for (WidgetPropertiesEditorListener listener : listenerlist.getListeners(WidgetPropertiesEditorListener.class))
			listener.editingCanceled(event);
	}
	public void firePropertyEditingStopped()
	{
		ChangeEvent event = new ChangeEvent(this);
		for (WidgetPropertiesEditorListener listener : listenerlist.getListeners(WidgetPropertiesEditorListener.class))
			listener.propertyEditingStopped(event);
	}
	public void firePropertyEditingCanceled()
	{
		ChangeEvent event = new ChangeEvent(this);
		for (WidgetPropertiesEditorListener listener : listenerlist.getListeners(WidgetPropertiesEditorListener.class))
			listener.propertyEditingCanceled(event);
	}
	public void stopEditing()
	{
		fireEditingStopped();
	}
	public void cancelEditing()
	{
		fireEditingCanceled();
	}
}
