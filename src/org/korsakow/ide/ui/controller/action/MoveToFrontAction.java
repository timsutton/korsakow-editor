package org.korsakow.ide.ui.controller.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.ui.interfacebuilder.InterfaceBuilderMainPanel;
import org.korsakow.ide.ui.interfacebuilder.WidgetCanvasModel;

public class MoveToFrontAction extends AbstractAction
{
	private final InterfaceBuilderMainPanel interfBuilder;
	public MoveToFrontAction(InterfaceBuilderMainPanel interfBuilder)
	{
		this.interfBuilder = interfBuilder;
	}

	@Override
	public void performAction()
	{
		WidgetCanvasModel model = interfBuilder.getCanvas().getModel();
		final List<WidgetModel> widgets = model.getWidgets();
		List<WidgetModel> selected = new ArrayList<WidgetModel>(model.getSelectedWidgets());
		Collections.sort(selected, new MoveBackwardsAction.DepthSort(widgets));
		for (int i = 0; i < selected.size(); ++i) {
			WidgetModel widget = selected.get(i);
			model.addWidget(widgets.size(), widget);
		}
	}

}
