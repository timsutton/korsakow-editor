package org.korsakow.ide.ui.interfacebuilder;

import java.util.Collection;

import org.korsakow.ide.resources.widget.WidgetModel;

public class WidgetCanvasModelAdapter implements WidgetCanvasModelListener
{
	public void widgetRemoved(WidgetModel widget) {}
	public void widgetAdded(WidgetModel widget) {}
	public void widgetsDepthChanged() {}
	public void gridSizeChanged(int oldWidth, int oldHeight, int newWidth, int newHeight) {}
	public void selectionChanged(Collection<WidgetModel> oldSelection, Collection<WidgetModel> newSelection) {}
	public void initialState() {}
	public void movieSizeChanged(int oldWidth, int oldHeight, int newWidth, int newHeight){}
}
