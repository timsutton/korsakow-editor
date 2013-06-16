package org.korsakow.ide.resources.widget.propertyhandler;

import java.util.Collection;

import javax.swing.JComboBox;

import org.korsakow.ide.resources.widget.DefaultPropertyHandler;
import org.korsakow.ide.resources.widget.PreviewTextMode;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.ui.components.model.KComboboxModel;

public class PreviewTextModePropertyHandler extends DefaultPropertyHandler
{
	@Override
	public void initializeEditor(Collection<? extends WidgetModel> widgets, JComboBox editor, String propertyName) {
		super.initializeEditor(widgets, editor, propertyName);
		editor.setEditable(false);
 		editor.setRenderer(this);
 		Object value = getCommonValue(widgets, propertyName);
		editor.setModel(new KComboboxModel(new Object[] {
				PreviewTextMode.ALWAYS,
				PreviewTextMode.MOUSEOVER,
		}, value));
	}
	@Override
	protected String formatProperty(String propertyName, Object propertyValue) {
		if (propertyValue == null)
			return null;
		if (propertyValue instanceof PreviewTextMode == false)
			propertyValue = PreviewTextMode.forId(propertyValue.toString());
		return ((PreviewTextMode)propertyValue).getDisplay();
	}
}