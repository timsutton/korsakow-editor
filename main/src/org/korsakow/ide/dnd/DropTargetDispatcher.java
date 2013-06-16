/**
 * 
 */
package org.korsakow.ide.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.util.Hashtable;
import java.util.List;
import java.util.TooManyListenersException;

import org.apache.log4j.Logger;

public class DropTargetDispatcher extends DropTarget
{
	private final Hashtable<DataFlavor, DropTargetListener> dispatchMap = new Hashtable<DataFlavor, DropTargetListener>();
	private DropTargetListener current = null;
	public void addDropTargetListenerNoThrow(DropTargetListener listener)
	{
		try {
			addDropTargetListener(listener);
		} catch (TooManyListenersException e) {
			Logger.getLogger(DropTargetDispatcher.class).error(e);
//			e.printStackTrace();
		}
	}
	public void addFlavorHandler(DataFlavor flavor, DropTargetListener handler)
	{
		dispatchMap.put(flavor, handler);
	}
	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		super.dragEnter(dtde);
		if (current != null)
			throw new IllegalStateException();
		List<DataFlavor> flavors = dtde.getCurrentDataFlavorsAsList();
		for (DataFlavor flavor : flavors)
			if (dispatchMap.get(flavor) != null) {
				current = dispatchMap.get(flavor);
				break;
			}
		if (current == null) {
			dtde.rejectDrag();
			return;
		}
		current.dragEnter(dtde);
	}

	@Override
	public void dragExit(DropTargetEvent dte) {
		super.dragExit(dte);
		if (current != null)
			current.dragExit(dte);
		current = null;
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		super.dragOver(dtde);
		if (current != null)
			current.dragOver(dtde);
	}

	@Override
	public void drop(DropTargetDropEvent dtde) {
		super.drop(dtde);
		if (current != null)
			current.drop(dtde);
		current = null;
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		super.dropActionChanged(dtde);
		if (current != null)
			current.dropActionChanged(dtde);
	}
	
}