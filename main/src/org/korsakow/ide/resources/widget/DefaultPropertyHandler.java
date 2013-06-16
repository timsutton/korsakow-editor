/**
 * 
 */
package org.korsakow.ide.resources.widget;

import java.awt.Component;
import java.util.Collection;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;

/**
 * Basic implementation of EditorModelHandler.
 * Takes care of such things that are unlikely to change (or are more likely to be just tweaked) in subclasses
 * such as committing and reading properties, managing event listeners.
 * 
 * Commonly only initializeEditor and formatProperty need to be overridden.
 * 
 * @author d
 *
 */
public abstract class DefaultPropertyHandler extends DefaultListCellRenderer implements EditorModelHandler
{
	protected EventListenerList listenerList = new EventListenerList();
	protected String editingName;
	public void addEditorListener(CellEditorListener listener)
	{
		listenerList.add(CellEditorListener.class, listener);
	}
	public void removeEditorListener(CellEditorListener listener)
	{
		listenerList.remove(CellEditorListener.class, listener);
	}
	public void notifyEditingStopped()
	{
		ChangeEvent event = new ChangeEvent(this);
		for (CellEditorListener listener : listenerList.getListeners(CellEditorListener.class))
			listener.editingStopped(event);
	}
	public void notifyEditingCanceled()
	{
		ChangeEvent event = new ChangeEvent(this);
		for (CellEditorListener listener : listenerList.getListeners(CellEditorListener.class))
			listener.editingCanceled(event);
	}
	protected String formatProperty(String propertyName, Object propertyValue)
	{
		return propertyValue != null ? propertyValue.toString() : "";
	}
	public void commitProperty(Collection<? extends WidgetModel> widgets, String propertyName, Object value)
	{
		for (WidgetModel widget : widgets)
			widget.setDynamicProperty(propertyName, value);
	}
	protected Object getCommonValue(Collection<? extends WidgetModel> widgets, String propertyName)
	{
		Object value = widgets.isEmpty()?null:widgets.iterator().next().getDynamicProperty(propertyName);
		for (WidgetModel widget : widgets) {
			Object o = widget.getDynamicProperty(propertyName);
			if (value != null && !value.equals(o)) {
				value = null;
				break;
			}
		}
		return value;
	}
	@Override
	public void initializeEditor(Collection<? extends WidgetModel> widgets, final JComboBox editor, String propertyName) {
		editingName = propertyName;
		editor.setEditable(true);
		Object value = getCommonValue(widgets, propertyName);
		editor.getModel().setSelectedItem(value);
	}
	public Component getPropertyRenderer(String propertyName, Object propertyValue)
	{
		setIcon(null);
		setText(formatProperty(propertyName, propertyValue));
		return this;
	}
	@Override
	public Component getListCellRendererComponent(JList list,
			Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		value = formatProperty(editingName, value);
		if (value != null && value.toString().length() == 0) // wierd combo rendering bug
			value = " " + value;
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		return this;
	}
}