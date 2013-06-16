package org.korsakow.ide.resources.widget.editors;

/**
 * 
 */

import org.korsakow.ide.resources.widget.DefaultTableWidgetPropertiesEditor;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.resources.widget.propertyhandler.ScalingPolicyPropertyHandler;

public class MainMediaWidgetEditor extends DefaultTableWidgetPropertiesEditor
{
	public MainMediaWidgetEditor(WidgetModel widget)
	{
		super(widget);
		addPropertyHandler("scalingPolicy", new ScalingPolicyPropertyHandler());
	}
}