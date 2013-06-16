/**
 * 
 */
package org.korsakow.ide.resources.widget.propertyhandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JComboBox;

import org.korsakow.ide.resources.widget.DefaultPropertyHandler;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.ui.components.model.KComboboxModel;

public class IntegerPropertyHandler extends DefaultPropertyHandler
{
	private final List<Integer> defaultChoices;
	public IntegerPropertyHandler()
	{
		this(new ArrayList<Integer>());
	}
	public IntegerPropertyHandler(List<Integer> defaultChoices)
	{
		this.defaultChoices = defaultChoices;
	}
	@Override
	public void initializeEditor(Collection<? extends WidgetModel> widgets, JComboBox editor, String propertyName) {
		super.initializeEditor(widgets, editor, propertyName);
		editor.setEditable(true);
		List<Integer> model = defaultChoices;
		model.add(0, null);
 		Object value = getCommonValue(widgets, propertyName);
		editor.setModel(new KComboboxModel(model.toArray(), value));
	}
}