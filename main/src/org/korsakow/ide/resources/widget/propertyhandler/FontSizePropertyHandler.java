/**
 * 
 */
package org.korsakow.ide.resources.widget.propertyhandler;

import java.util.Collection;
import java.util.List;

import javax.swing.JComboBox;

import org.korsakow.ide.resources.widget.DefaultPropertyHandler;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.ui.components.model.KComboboxModel;
import org.korsakow.ide.util.Util;

public class FontSizePropertyHandler extends DefaultPropertyHandler
{
	private static final Integer[] MODEL = {
		6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 28, 30, 36, 40, 48, 60
	};
	@Override
	public void initializeEditor(Collection<? extends WidgetModel> widgets, JComboBox editor, String propertyName) {
		super.initializeEditor(widgets, editor, propertyName);
		editor.setEditable(false);
		List<Integer> model = Util.asList(MODEL);
		model.add(0, null);
 		Object value = getCommonValue(widgets, propertyName);
		editor.setModel(new KComboboxModel(model.toArray(), value));
	}
	@Override
	protected String formatProperty(String propertyName, Object propertyValue) {
		return super.formatProperty(propertyName, propertyValue);
	}
}
