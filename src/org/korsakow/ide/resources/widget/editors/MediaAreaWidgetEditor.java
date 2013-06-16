package org.korsakow.ide.resources.widget.editors;

/**
 * 
 */

import org.korsakow.ide.resources.widget.DefaultTableWidgetPropertiesEditor;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.resources.widget.propertyhandler.BooleanPropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.PlayModePropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.ScalingPolicyPropertyHandler;

public class MediaAreaWidgetEditor extends DefaultTableWidgetPropertiesEditor
{
	public MediaAreaWidgetEditor(WidgetModel widget)
	{
		super(widget);
		// mediaId not editable
		addPropertyHandler("scalingPolicy", new ScalingPolicyPropertyHandler());
		addPropertyHandler("playMode", new PlayModePropertyHandler());
		addPropertyHandler("looping", new BooleanPropertyHandler());
	}
}