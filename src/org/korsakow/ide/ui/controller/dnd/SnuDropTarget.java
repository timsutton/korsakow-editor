/**
 * 
 */
package org.korsakow.ide.ui.controller.dnd;

import java.awt.Dimension;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDragEvent;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.interf.ISnu;
import org.korsakow.domain.mapper.input.SnuInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.resources.media.MediaFactory;
import org.korsakow.ide.resources.media.Playable;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.ui.dnd.DataFlavors;
import org.korsakow.ide.ui.interfacebuilder.WidgetCanvas;
import org.korsakow.ide.ui.interfacebuilder.widget.SnuFixedLink;

public class SnuDropTarget extends AbstractWidgetDropTarget
{
	public SnuDropTarget(WidgetCanvas canvas)
	{
		super(canvas);
	}
	@Override
	protected WidgetModel getTransferWidget(DropTargetDragEvent dtde)
	{
		try {
			ISnu snu = SnuInputMapper.map( (Long)dtde.getTransferable().getTransferData(DataFlavors.SnuFlavor) );
			SnuFixedLink widget = new SnuFixedLink();

			Playable playable = MediaFactory.getMediaNoThrow( snu.getMainMedia() );
			Dimension preferredSize = playable.getComponent().getPreferredSize();
			widget.setWidth(preferredSize.width);
			widget.setHeight(preferredSize.height);
			widget.setSnu(snu.getId());
			
			return widget;
		} catch (UnsupportedFlavorException e) {
        	Logger.getLogger(WidgetCanvas.class).error(e);
			return null;
		} catch (IOException e) {
        	Logger.getLogger(WidgetCanvas.class).error(e);
			return null;
		} catch (MapperException e) {
        	Application.getInstance().showUnhandledErrorDialog( e );
        	return null;
		}
	}
}