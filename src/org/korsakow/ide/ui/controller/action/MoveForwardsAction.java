package org.korsakow.ide.ui.controller.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.ui.controller.action.MoveBackwardsAction.DepthSort;
import org.korsakow.ide.ui.interfacebuilder.InterfaceBuilderMainPanel;
import org.korsakow.ide.ui.interfacebuilder.WidgetCanvasModel;

public class MoveForwardsAction extends AbstractAction
{
	private final InterfaceBuilderMainPanel interfBuilder;
	public MoveForwardsAction(InterfaceBuilderMainPanel interfBuilder)
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

		for (int i = selected.size()-1; i >= 0; --i) {
			WidgetModel widget = selected.get(i);
			int index = model.indexOfWidget(widget);
			if (index+2 > widgets.size())
				continue;
			if (index+1 < widgets.size() &&
				selected.contains(widgets.get(index+1)))
				continue; // preserve relative ordering
			model.addWidget(index+2, widget);
		}
	}

}
