/**
 * 
 */
package org.korsakow.ide.ui.controller.dnd;

import java.awt.Dimension;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDragEvent;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.resources.widget.WidgetComponent;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.ui.dnd.DataFlavors;
import org.korsakow.ide.ui.interfacebuilder.InterfaceBuilderMainPanel;
import org.korsakow.ide.ui.interfacebuilder.WidgetCanvas;

public class WidgetTypeDropTarget extends AbstractWidgetDropTarget
{
	InterfaceBuilderMainPanel interfaceBuilder;
	public WidgetTypeDropTarget(InterfaceBuilderMainPanel interfaceBuilder)
	{
		super(interfaceBuilder.getCanvas());
		this.interfaceBuilder = interfaceBuilder;
	}
	@Override
	protected WidgetModel getTransferWidget(DropTargetDragEvent dtde)
	{
		Float aspect = null;
		
		if (interfaceBuilder.getGridInfoPanel().isGridAspectLocked()) {
			if (canvas.getModel().getGridHeight() > 0)
				aspect = canvas.getModel().getGridWidth() / (float)canvas.getModel().getGridHeight();
		}

		if (aspect!=null && (aspect < 0.2 || aspect > 5))
			aspect = 1.0F;
		
		try {
			WidgetType known = (WidgetType)dtde.getTransferable().getTransferData(DataFlavors.WidgetType);
			WidgetModel widget = known.newInstance();
			switch (known)
			{
			case MainMedia:
			case MediaArea:
//				widget.setHeight((int)(widget.getWidth()/aspect));
				// we adjust the width because the default is 16:9, and we want to reduce, if anything the size of the widget
				if (aspect != null) {
					final int newWidth = (int)(widget.getHeight()*aspect);
					if (newWidth > 0)
						widget.setWidth(newWidth);
				}
				break;
			case SnuAutoLink:
			case SnuFixedLink:
				Dimension d = null;
				List<WidgetComponent> comps = canvas.getWidgetComponents();
				WIDGETS: /* woot labels! */
				for (int i = comps.size()-1; i >= 0; --i) {
					WidgetComponent comp = comps.get(i);
					switch (comp.getWidget().getWidgetType())
					{
					case SnuAutoLink:
					case SnuFixedLink:
						d = comp.getSize();
						break WIDGETS;
					}
				}
				if (d != null && d.width>0 && d.height>0) {
					widget.setWidth(d.width);
					widget.setHeight(d.height);
				} else
//					widget.setHeight((int)(widget.getWidth()/aspect));
					if (aspect != null) {
						final int newWidth = (int)(widget.getHeight()*aspect);
						if (newWidth > 0)
							widget.setWidth(newWidth);
					}
				break;
			}
			return widget;
		} catch (UnsupportedFlavorException e) {
        	Logger.getLogger(WidgetCanvas.class).error(e);
			return null;
		} catch (IOException e) {
        	Logger.getLogger(WidgetCanvas.class).error(e);
			return null;
		}
	}
}