package org.korsakow.ide.resources.widget.editors;

/**
 * 
 */

import org.korsakow.ide.resources.widget.DefaultTableWidgetPropertiesEditor;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.resources.widget.propertyhandler.ColorPropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.FontSizePropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.IntegerPropertyHandler;

public class CommentsWidgetEditor extends DefaultTableWidgetPropertiesEditor
{
	public CommentsWidgetEditor(WidgetModel widget)
	{
		super(widget);
		addPropertyHandler("fontColor", new ColorPropertyHandler());
		addPropertyHandler("backgroundColor", new ColorPropertyHandler());
		addPropertyHandler("fontSize", new FontSizePropertyHandler());
		addPropertyHandler("perPage", new IntegerPropertyHandler());
	}
}