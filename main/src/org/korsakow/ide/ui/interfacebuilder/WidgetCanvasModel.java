package org.korsakow.ide.ui.interfacebuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.util.EventDispatcher;

public class WidgetCanvasModel
{
	private long _idSeed = 0;
	private final EventDispatcher<WidgetCanvasModelListener> eventDispatcher = new EventDispatcher<WidgetCanvasModelListener>(WidgetCanvasModelListener.class);
	private final List<WidgetModel> widgets = new ArrayList<WidgetModel>();
	private final Set<WidgetModel> selectedWidgets = new HashSet<WidgetModel>();
	private int gridWidth = 0;
	private int gridHeight = 0;
	private int movieWidth = 640;
	private int movieHeight = 480;
	private int stageWidth = movieWidth*2;
	private int stageHeight = movieHeight*2;
	private boolean snapToGrid = true;
	
	public WidgetCanvasModel()
	{
	}
	
	public long getNextId()
	{
		return ++_idSeed;
	}
	public void addListener(WidgetCanvasModelListener listener)
	{
		eventDispatcher.add(listener);
	}
	public List<WidgetModel> getWidgets()
	{
		return widgets;
	}
	/**
	 * Notified the undo model that the current state is the initial stage
	 */
	public void notifyInitialState()
	{
		eventDispatcher.notify(WidgetCanvasModelListener.INITIAL_STATE);
	}
	public void addWidget(WidgetModel widget)
	{
		if (widget.getId() == null)
			widget.setId(getNextId());
		int oldIndex = widgets.indexOf(widget);
		widgets.remove(widget);
		widgets.add(widget);
		int index = widgets.indexOf(widget);
		if (oldIndex == -1)
			eventDispatcher.notify(WidgetCanvasModelListener.WIDGET_ADDED, widget);
		else
		if (oldIndex != index)
			eventDispatcher.notify(WidgetCanvasModelListener.WIDGET_ADDED, widget);
	}
	/**
	 * Adds at the specified position, shifting existing entries to the right (end of the list, front of the display).
	 */
	public void addWidget(int index, WidgetModel widget)
	{
		if (widget.getId() == null)
			widget.setId(getNextId());
		int oldIndex = widgets.indexOf(widget);
		widgets.add(index, widget);
		if (oldIndex != -1) {
			if (oldIndex > index)
				++oldIndex;
			widgets.remove(oldIndex);
		}
		if (oldIndex == -1)
			eventDispatcher.notify(WidgetCanvasModelListener.WIDGET_ADDED, widget);
		else
		if (oldIndex != index) {
			eventDispatcher.notify(WidgetCanvasModelListener.WIDGET_MOVED);
		}
	}
	public void removeWidget(WidgetModel widget)
	{
		removeSelected(widget);
		widgets.remove(widget);
		eventDispatcher.notify(WidgetCanvasModelListener.WIDGET_REMOVED, widget);
	}
	public int indexOfWidget(WidgetModel widget)
	{
		return widgets.indexOf(widget);
	}
	public WidgetModel getWidgetAt(int index)
	{
		return widgets.get(index);
	}
	
	public void clearWidgets()
	{
		List<WidgetModel> oldWidgets = new ArrayList<WidgetModel>(widgets);
		for (WidgetModel widget : oldWidgets)
			removeWidget(widget);
	}
	public void removeWidgets(Collection<WidgetModel> toDelete)
	{
		toDelete = new ArrayList<WidgetModel>(toDelete);
		for (WidgetModel widget : toDelete)
			removeWidget(widget);
	}
	public void addSelected(WidgetModel widget)
	{
		Collection<WidgetModel> oldSelection = new HashSet<WidgetModel>(selectedWidgets);
		selectedWidgets.add(widget);
		eventDispatcher.notify(WidgetCanvasModelListener.SELECTION_CHANGED, oldSelection, selectedWidgets);
	}
	public void removeSelected(WidgetModel widget)
	{
		Collection<WidgetModel> oldSelection = new HashSet<WidgetModel>(selectedWidgets);
		selectedWidgets.remove(widget);
		eventDispatcher.notify(WidgetCanvasModelListener.SELECTION_CHANGED, oldSelection, selectedWidgets);
	}
	public void selectAll()
	{
		Collection<WidgetModel> oldSelection = new HashSet<WidgetModel>(selectedWidgets);
		for (WidgetModel widget : widgets)
			selectedWidgets.add(widget);
		eventDispatcher.notify(WidgetCanvasModelListener.SELECTION_CHANGED, oldSelection, selectedWidgets);
	}
	public void clearSelection()
	{
		Collection<WidgetModel> oldSelection = new HashSet<WidgetModel>(selectedWidgets);
		selectedWidgets.clear();
		eventDispatcher.notify(WidgetCanvasModelListener.SELECTION_CHANGED, oldSelection, selectedWidgets);
	}
	public boolean isSelected(WidgetModel widget)
	{
		return selectedWidgets.contains(widget);
	}
	public Collection<WidgetModel> getSelectedWidgets()
	{
		return selectedWidgets;
	}
	public int getGridWidth()
	{
		return gridWidth;
	}
	public int getGridHeight()
	{
		return gridHeight;
	}
	public void setGridSize(int width, int height)
	{
		int oldWidth = gridWidth;
		int oldHeight = gridHeight;
		gridWidth = width;
		gridHeight = height;
		eventDispatcher.notify(WidgetCanvasModelListener.GRIDSIZE_CHANGED, oldWidth, oldHeight, width, height);
	}
	public void setMovieSize(int width, int height)
	{
		int oldWidth = movieWidth;
		int oldHeight = movieHeight;
		movieWidth = width;
		movieHeight = height;
		eventDispatcher.notify(WidgetCanvasModelListener.MOVIESIZE_CHANGED, oldWidth, oldHeight, movieWidth, movieHeight);
	}
	public void setStageSize(int width, int height)
	{
		int oldWidth = stageWidth;
		int oldHeight = stageHeight;
		stageWidth = width;
		stageHeight = height;
		eventDispatcher.notify(WidgetCanvasModelListener.MOVIESIZE_CHANGED, oldWidth, oldHeight, stageWidth, stageHeight);
	}

	public int getMovieWidth()
	{
		return movieWidth;
	}

	public int getMovieHeight()
	{
		return movieHeight;
	}

	public int getMovieOffsetX() {
		return getMovieCenterX();
	}
	public int getMovieOffsetY() {
		return getMovieCenterY();
	}
	public int getMovieCenterX()
	{
		return (getStageWidth() - getMovieWidth())/2;
	}

	public int getMovieCenterY()
	{
		return (getStageHeight() - getMovieHeight())/2;
	}
	
	public int getStageWidth()
	{
		return stageWidth;
	}

	public int getStageHeight()
	{
		return stageHeight;
	}
	
	public boolean isSnapToGrid(){
		return snapToGrid;
	}
	
	public void toggleSnapToGrid(){
	snapToGrid = !snapToGrid;	
	}
	
	
}
