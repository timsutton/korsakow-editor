package org.korsakow.ide.resources.widget.editors;

/**
 * 
 */

import org.korsakow.ide.resources.widget.DefaultTableWidgetPropertiesEditor;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.resources.widget.propertyhandler.ColorPropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.FontFamilyPropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.FontSizePropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.FontStylePropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.FontWeightPropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.TextDecorationPropertyHandler;

public class SubtitlesWidgetEditor extends DefaultTableWidgetPropertiesEditor
{
	public SubtitlesWidgetEditor(WidgetModel widget)
	{
		super(widget);
		addPropertyHandler("fontColor", new ColorPropertyHandler());
		addPropertyHandler("fontSize", new FontSizePropertyHandler());
		addPropertyHandler("fontFamily", new FontFamilyPropertyHandler());
		addPropertyHandler("fontWeight", new FontWeightPropertyHandler());
		addPropertyHandler("fontStyle", new FontStylePropertyHandler());
		addPropertyHandler("textDecoration", new TextDecorationPropertyHandler());
	}
}