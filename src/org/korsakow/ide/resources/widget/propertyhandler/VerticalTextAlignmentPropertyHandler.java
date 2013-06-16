package org.korsakow.ide.resources.widget.propertyhandler;

import java.util.Collection;

import javax.swing.JComboBox;

import org.korsakow.ide.resources.widget.DefaultPropertyHandler;
import org.korsakow.ide.resources.widget.VerticalTextAlignment;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.ui.components.model.KComboboxModel;

public class VerticalTextAlignmentPropertyHandler extends DefaultPropertyHandler
{
	@Override
	public void initializeEditor(Collection<? extends WidgetModel> widgets, JComboBox editor, String propertyName) {
		super.initializeEditor(widgets, editor, propertyName);
		editor.setEditable(false);
 		editor.setRenderer(this);
 		Object value = getCommonValue(widgets, propertyName);
		editor.setModel(new KComboboxModel(new Object[] {
				VerticalTextAlignment.Top,
				VerticalTextAlignment.Middle,
				VerticalTextAlignment.Bottom,
		}, value));
	}
	@Override
	protected String formatProperty(String propertyName, Object propertyValue) {
		if (propertyValue == null)
			return null;
		if (propertyValue instanceof VerticalTextAlignment == false)
			propertyValue = VerticalTextAlignment.forId(propertyValue.toString());
		return ((VerticalTextAlignment)propertyValue).getDisplay();
	}
}