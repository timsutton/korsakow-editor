package org.korsakow.ide.resources.widget.editors;

/**
 * 
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.korsakow.ide.resources.widget.DefaultTableWidgetPropertiesEditor;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.resources.widget.propertyhandler.BooleanPropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.ColorPropertyHandler;
import org.korsakow.ide.resources.widget.propertyhandler.IntegerPropertyHandler;

public class ScrubberWidgetEditor extends DefaultTableWidgetPropertiesEditor
{
	private static List<Integer> BAR_SIZES = Collections.unmodifiableList(new ArrayList<Integer>(Arrays.asList(
		5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100
	)));
	public ScrubberWidgetEditor(WidgetModel widget)
	{
		super(widget);
		addPropertyHandler("foregroundColor", new ColorPropertyHandler());
		addPropertyHandler("backgroundColor", new ColorPropertyHandler());
		addPropertyHandler("loadingColor", new ColorPropertyHandler());
		addPropertyHandler("barWidth", new IntegerPropertyHandler(new ArrayList<Integer>(BAR_SIZES)));
		addPropertyHandler("barHeight", new IntegerPropertyHandler(new ArrayList<Integer>(BAR_SIZES)));
		addPropertyHandler("interactive", new BooleanPropertyHandler());
		addPropertyHandler("loading", new BooleanPropertyHandler());
	}
}