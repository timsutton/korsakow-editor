package org.korsakow.ide.resources.widget;

import java.awt.Component;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.apache.log4j.Logger;
import org.korsakow.ide.util.UIUtil;
import org.korsakow.ide.util.Util;

/**
 * An implementation of TableWidgetPropertiesEditor that uses pluggable PropertyHandlers (EditorModelHandler) to Render
 * and Edit its cells.
 * 
 * Most of the work of this class involves managing and delegating the handlers.
 * 
 * @author d
 *
 */
public class DefaultTableWidgetPropertiesEditor extends AbstractTableWidgetPropertiesEditor implements TableCellRenderer
{
	protected HashMap<String, EditorModelHandler> propertyHandlerMap = new HashMap<String, EditorModelHandler>();
	protected JLabel defaultEditor = new JLabel();
	protected EditorModelHandler currentEditorHandler;
	public DefaultTableWidgetPropertiesEditor(WidgetModel widget) {
		this(Util.list(WidgetModel.class, widget));
	}
	public DefaultTableWidgetPropertiesEditor(Collection<WidgetModel> widgets) {
		super(widgets);
		table.getColumn(Column.NAME).setCellRenderer(new NameRenderer());
		table.getColumn(Column.VALUE).setCellRenderer(this);
		UIUtil.runUITaskLater(new Runnable() {
			public void run() {
				configureTableModel((DefaultTableModel)table.getModel());
			}
		});
	}
	protected void configureTableModel(DefaultTableModel model)
	{
		Set<String> ids = new TreeSet<String>();
		
		// build list of common properties
		for (WidgetModel widget : widgets)
			ids.addAll(widget.getDynamicPropertyIds());
		for (WidgetModel widget : widgets)
			ids.retainAll(widget.getDynamicPropertyIds());
		
		IDS:
		for (String id : ids) {
			Object value = widgets.isEmpty()?null:widgets.iterator().next().getDynamicProperty(id);
			for (WidgetModel widget : widgets) {
				if (widget.getWidgetEditor() instanceof DefaultTableWidgetPropertiesEditor) {
					if (!((DefaultTableWidgetPropertiesEditor)widget.getWidgetEditor()).propertyHandlerMap.containsKey(id))
						continue IDS;
				}
				Object o = widget.getDynamicProperty(id);
				if (value != null && !value.equals(o)) {
					value = null;
					break;
				}
			}
			model.addRow(new Object[] {id, value });
		}
	}
	protected void addPropertyHandler(String propertyName, EditorModelHandler handler)
	{
		propertyHandlerMap.put(propertyName, handler);
	}
	protected EditorModelHandler getHandler(String propertyName)
	{
		EditorModelHandler handler = propertyHandlerMap.get(propertyName);
		if (handler == null) {
			try {
				for (WidgetModel widget : widgets) {
					if (widget.getWidgetEditor() instanceof DefaultTableWidgetPropertiesEditor) {
						handler = ((DefaultTableWidgetPropertiesEditor)widget.getWidgetEditor()).propertyHandlerMap.get(propertyName);
						if (handler != null)
							break;
					}
				}
			} catch (Exception e) {
				// the below comment isn't clear. I wish I knew when to expect this.
				Logger.getLogger(DefaultTableWidgetPropertiesEditor.class).error("couldn't get a handler for: " + propertyName, e);
				// no registered editor, or unknown property
			}
			if (handler == null) {
				Logger.getLogger(DefaultTableWidgetPropertiesEditor.class).error("", new Exception("No handler for: " + propertyName));
			}
		}
		return handler;
	}
	@Override
	protected void prepareEditor(int row, int column)
	{
		super.prepareEditor(row, column);
		EditorModelHandler handler = getHandler(editingName);
		if (handler != null) {
			cellEditor.removeCellEditorListener(this); // otherwise we might get notifications during init that could stop editing prematurely
			handler.initializeEditor(widgets, propertyEditor, editingName);
			// the thing is... we still have our own listener on the table... it seems like we should give the handler
			// a chance to be the one to notify and if it doesn't then we use our regular one.
			// now we do both.
			handler.removeEditorListener(this);
			handler.addEditorListener(this); // when would be a good time to remove this listener?
			cellEditor.addCellEditorListener(this);
		}
		currentEditorHandler = handler;
	}
	@Override
	protected boolean commitPropertyChanges(String name, Object value)
	{
		if (currentEditorHandler != null) {
			currentEditorHandler.commitProperty(widgets, editingName, value);
			currentEditorHandler = null;
			return true;
		}
		currentEditorHandler = null;
		return false;
	}
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{
		String name = (String)table.getValueAt(row, 0);
		EditorModelHandler handler = getHandler(name);
		if (handler != null)
			return handler.getPropertyRenderer(name, value);
		else {
			defaultEditor.setText(value!=null?value.toString():null);
			return defaultEditor;
		}
	}
}
