package org.korsakow.ide.ui.controller.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.ui.interfacebuilder.InterfaceBuilderMainPanel;
import org.korsakow.ide.ui.interfacebuilder.WidgetCanvasModel;

public class MoveBackwardsAction extends AbstractAction
{
	public final static class DepthSort implements Comparator<WidgetModel>
	{
		private final List<WidgetModel> widgets;

		public DepthSort(List<WidgetModel> widgets)
		{
			this.widgets = widgets;
		}

		public int compare(WidgetModel o1, WidgetModel o2)
		{
			return widgets.indexOf(o1) - widgets.indexOf(o2);
		}
	}

	private final InterfaceBuilderMainPanel interfBuilder;
	public MoveBackwardsAction(InterfaceBuilderMainPanel interfBuilder)
	{
		this.interfBuilder = interfBuilder;
	}

	@Override
	public void performAction()
	{
		WidgetCanvasModel model = interfBuilder.getCanvas().getModel();
		final List<WidgetModel> widgets = model.getWidgets();
		List<WidgetModel> selected = new ArrayList<WidgetModel>(model.getSelectedWidgets());
		Collections.sort(selected, new DepthSort(widgets));
		for (WidgetModel widget : selected) {
			int index = model.indexOfWidget(widget);
			if (index < 1)
				continue;
			model.addWidget(index-1, widget);
		}
	}

}
