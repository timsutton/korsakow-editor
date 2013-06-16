package org.korsakow.ide.resources.widget.propertyhandler;

import java.util.Collection;

import javax.swing.JComboBox;

import org.korsakow.ide.resources.widget.DefaultPropertyHandler;
import org.korsakow.ide.resources.widget.ScalingPolicy;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.ui.components.model.KComboboxModel;

public class ScalingPolicyPropertyHandler extends DefaultPropertyHandler
{
	@Override
	public void initializeEditor(Collection<? extends WidgetModel> widgets, JComboBox editor, String propertyName) {
		super.initializeEditor(widgets, editor, propertyName);
		editor.setEditable(false);
 		editor.setRenderer(this);
 		Object value = getCommonValue(widgets, propertyName);
		editor.setModel(new KComboboxModel(new Object[] {
				ScalingPolicy.None,
				ScalingPolicy.ExactFit,
				ScalingPolicy.MaintainAspectRatio,
				ScalingPolicy.ScaleDownMaintainAspectRatio,
		}, value));
	}
	@Override
	protected String formatProperty(String propertyName, Object propertyValue) {
		if (propertyValue == null)
			return null;
		if (propertyValue instanceof ScalingPolicy == false)
			propertyValue = ScalingPolicy.forId(propertyValue.toString());
		return ((ScalingPolicy)propertyValue).getDisplay();
	}
}