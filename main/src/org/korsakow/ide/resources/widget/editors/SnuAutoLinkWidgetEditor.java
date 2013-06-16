package org.korsakow.ide.resources.widget.editors;

/**
 * 
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.resources.widget.DefaultPropertyHandler;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.ui.interfacebuilder.widget.SnuAutoLink;

public class SnuAutoLinkWidgetEditor extends AbstractLinkWidgetEditor
{
	public SnuAutoLinkWidgetEditor(SnuAutoLink widget)
	{
		super(widget);
		addPropertyHandler("index", new IndexPropertyHandler());
	}
	public class IndexPropertyHandler extends DefaultPropertyHandler
	{
		@Override
		public void initializeEditor(Collection<? extends WidgetModel> widgets, JComboBox editor, String propertyName) {
			propertyEditor.setEditable(false);
			int maxIndex = 0;
			for (WidgetModel widget : canvasModel.getWidgets()) {
				if (widget.getWidgetType() == WidgetType.SnuAutoLink)
					if (((SnuAutoLink)widget).getIndex() > maxIndex)
						maxIndex = ((SnuAutoLink)widget).getIndex();
			}
			List<Integer> indices = new ArrayList<Integer>();
			for (int i = 0; i <= maxIndex+1; ++i)
				indices.add(i);
			editor.setModel(new DefaultComboBoxModel(indices.toArray()));
	 		Object value = getCommonValue(widgets, propertyName);
			editor.getModel().setSelectedItem(value);
		}
	}
}