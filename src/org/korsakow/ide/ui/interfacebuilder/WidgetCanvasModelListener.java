package org.korsakow.ide.ui.interfacebuilder;

import java.util.Collection;
import java.util.EventListener;

import org.korsakow.ide.resources.widget.WidgetModel;

public interface WidgetCanvasModelListener extends EventListener
{
	static final String WIDGET_ADDED = "widgetAdded";
	static final String WIDGET_REMOVED = "widgetRemoved";
	static final String WIDGET_MOVED = "widgetsDepthChanged";
	static final String GRIDSIZE_CHANGED = "gridSizeChanged";
	static final String MOVIESIZE_CHANGED = "movieSizeChanged";
	static final String SELECTION_CHANGED = "selectionChanged";
	static final String INITIAL_STATE = "initialState";
	void widgetAdded(WidgetModel widget);
	void widgetRemoved(WidgetModel widget);
	void widgetsDepthChanged();
	void movieSizeChanged(int oldWidth, int oldHeight, int newWidth, int newHeight);
	void gridSizeChanged(int oldWidth, int oldHeight, int newWidth, int newHeight);
	void selectionChanged(Collection<WidgetModel> oldSelection, Collection<WidgetModel> newSelection);
	void initialState();
}
