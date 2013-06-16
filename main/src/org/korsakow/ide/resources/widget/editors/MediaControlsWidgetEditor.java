package org.korsakow.ide.resources.widget.editors;

/**
 * 
 */

import org.korsakow.ide.resources.widget.DefaultTableWidgetPropertiesEditor;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.resources.widget.propertyhandler.BooleanPropertyHandler;

public class MediaControlsWidgetEditor extends DefaultTableWidgetPropertiesEditor
{
	public MediaControlsWidgetEditor(WidgetModel widget)
	{
		super(widget);
		addPropertyHandler("showPlayPause", new BooleanPropertyHandler());
		addPropertyHandler("showPlayTime", new BooleanPropertyHandler());
		addPropertyHandler("showScrubber", new BooleanPropertyHandler());
		addPropertyHandler("scrubberInteractive", new BooleanPropertyHandler());
		addPropertyHandler("showLoading", new BooleanPropertyHandler());
		addPropertyHandler("showTotalTime", new BooleanPropertyHandler());
		addPropertyHandler("showVolume", new BooleanPropertyHandler());
	}
}