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

public class BooleanPropertyHandler extends DefaultPropertyHandler
{
	private static final Boolean[] MODEL = {
		Boolean.TRUE, Boolean.FALSE
	};
	@Override
	public void initializeEditor(Collection<? extends WidgetModel> widgets, JComboBox editor, String propertyName) {
		super.initializeEditor(widgets, editor, propertyName);
		editor.setEditable(false);
		List<Boolean> model = Util.asList(MODEL);
		model.add(0, null);
 		Object value = getCommonValue(widgets, propertyName);
		editor.setModel(new KComboboxModel(model.toArray(), value));
	}
}