/**
 * 
 */
package org.korsakow.ide.ui.dnd;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TooManyListenersException;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import org.apache.log4j.Logger;
import org.korsakow.ide.resources.WidgetType;

public class WidgetTypeTransferHandler extends TransferHandler
{
	private static enum Type
	{
		Object,
	}
	@Override
	public int getSourceActions(JComponent component)
	{
		return TransferHandler.COPY;
	}
	

	private Point mousePoint = null;
	private final DropTargetListener mousePointListener = new DropTargetListener() {
		public void dragEnter(DropTargetDragEvent dtde) {
		}
		public void dragExit(DropTargetEvent dte) {
		}
		public void dragOver(DropTargetDragEvent dtde) {
			mousePoint = dtde.getLocation();
		}
		public void drop(DropTargetDropEvent dtde) {
			
		}
		public void dropActionChanged(DropTargetDragEvent dtde) {
		}
	};
	public Point getDropPoint()
	{
		return mousePoint;
	}
//	public boolean canImport(TransferHandler.TransferSupport support)
	@Override
	public boolean canImport(JComponent comp, DataFlavor[] flavours)
	{
		try {
			comp.getDropTarget().addDropTargetListener(mousePointListener);
		} catch (TooManyListenersException e) {
        	Logger.getLogger(WidgetTypeTransferHandler.class).error(e);
		}
		for (DataFlavor flavor : flavours)
		{
			if (flavor.match(DataFlavors.WidgetType))
				return true;
		}
		return false;
	}

//	public boolean importData(TransferHandler.TransferSupport support) 
	@Override
	public boolean importData(JComponent comp, Transferable t) 
	{
		comp.getDropTarget().removeDropTargetListener(mousePointListener);
try {
		Type type = null;
		DataFlavor[] flavors = t.getTransferDataFlavors();
		DataFlavor flavor = null;
		for (DataFlavor f : flavors)
		{
			if (f.match(DataFlavors.WidgetType))
			{
				flavor = f;
				type = Type.Object;
				break;
			}
		}
		assert flavor != null;
		Object data = null;
		try {
			data = t.getTransferData(flavor);
		} catch (UnsupportedFlavorException e) {
        	Logger.getLogger(WidgetTypeTransferHandler.class).error(e);
			return false;
		} catch (IOException e) {
        	Logger.getLogger(WidgetTypeTransferHandler.class).error(e);
			return false;
		}
		switch (type)
		{
		case Object:
			List<WidgetType> objects;
			if (data instanceof Collection) {
				objects = (List<WidgetType>) data;
			} else {
				objects = new ArrayList<WidgetType>();
				objects.add((WidgetType)data);
			}
			return importWidgetType(t, objects);
		default:
			return false;
		}
} catch (RuntimeException e) {
	Logger.getLogger(WidgetTypeTransferHandler.class).error(e);
	throw e;
}
	}
	
	/**
	 */
	protected boolean importWidgetType(Transferable t, List<WidgetType> widgets)
	{
		return true;
	}
}