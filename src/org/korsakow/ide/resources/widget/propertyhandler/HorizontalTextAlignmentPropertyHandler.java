package org.korsakow.ide.resources.widget.propertyhandler;

import java.util.Collection;

import javax.swing.JComboBox;

import org.korsakow.ide.resources.widget.DefaultPropertyHandler;
import org.korsakow.ide.resources.widget.HorizontalTextAlignment;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.ui.components.model.KComboboxModel;

public class HorizontalTextAlignmentPropertyHandler extends DefaultPropertyHandler
{
	@Override
	public void initializeEditor(Collection<? extends WidgetModel> widgets, JComboBox editor, String propertyName) {
		super.initializeEditor(widgets, editor, propertyName);
		editor.setEditable(false);
 		editor.setRenderer(this);
 		Object value = getCommonValue(widgets, propertyName);
		editor.setModel(new KComboboxModel(new Object[] {
				HorizontalTextAlignment.Left,
				HorizontalTextAlignment.Center,
				HorizontalTextAlignment.Right,
		}, value));
	}
	@Override
	protected String formatProperty(String propertyName, Object propertyValue) {
		if (propertyValue == null)
			return null;
		if (propertyValue instanceof HorizontalTextAlignment == false)
			propertyValue = HorizontalTextAlignment.forId(propertyValue.toString());
		return ((HorizontalTextAlignment)propertyValue).getDisplay();
	}
}