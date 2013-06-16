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
import org.korsakow.domain.interf.IText;
import org.korsakow.domain.mapper.input.TextInputMapper;
import org.korsakow.ide.Application;
import org.korsakow.ide.resources.media.MediaFactory;
import org.korsakow.ide.resources.media.Playable;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.ui.dnd.DataFlavors;
import org.korsakow.ide.ui.interfacebuilder.WidgetCanvas;
import org.korsakow.ide.ui.interfacebuilder.widget.MediaArea;

public class TextDropTarget extends AbstractWidgetDropTarget
{
	public TextDropTarget(WidgetCanvas canvas)
	{
		super(canvas);
	}
	@Override
	protected WidgetModel getTransferWidget(DropTargetDragEvent dtde)
	{
		try {
			IText text = TextInputMapper.map( (Long)dtde.getTransferable().getTransferData(DataFlavors.TextFlavor) );
			MediaArea widget = new MediaArea();
			Playable playable = MediaFactory.getMediaNoThrow( text );
			Dimension preferredSize = playable.getComponent().getPreferredSize();
			widget.setWidth(preferredSize.width);
			widget.setHeight(preferredSize.height);
			widget.setMedia(TextInputMapper.map(text.getId()));
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