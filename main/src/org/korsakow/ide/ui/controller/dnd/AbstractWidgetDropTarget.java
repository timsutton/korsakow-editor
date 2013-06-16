/**
 * 
 */
package org.korsakow.ide.ui.controller.dnd;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;

import org.korsakow.ide.resources.widget.WidgetComponent;
import org.korsakow.ide.resources.widget.WidgetModel;
import org.korsakow.ide.ui.dnd.WidgetTypeTransferable;
import org.korsakow.ide.ui.interfacebuilder.WidgetCanvas;
import org.korsakow.ide.ui.interfacebuilder.WidgetResizer;

public abstract class AbstractWidgetDropTarget implements DropTargetListener
{
	protected WidgetCanvas canvas;
	private WidgetModel widget = null;
	private WidgetComponent comp = null;
	protected abstract WidgetModel getTransferWidget(DropTargetDragEvent dtde);
	protected boolean isAltDown(DropTargetDropEvent dtde) {
		try {
			return (Boolean)dtde.getTransferable().getTransferData(WidgetTypeTransferable.ALT_STATUS_FLAVOR);
		} catch (UnsupportedFlavorException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}
	protected boolean isAltDown(DropTargetDragEvent dtde) {
		try {
			return (Boolean)dtde.getTransferable().getTransferData(WidgetTypeTransferable.ALT_STATUS_FLAVOR);
		} catch (UnsupportedFlavorException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}
	public AbstractWidgetDropTarget(WidgetCanvas canvas)
	{
		this.canvas = canvas;
	}
	public void dragEnter(DropTargetDragEvent dtde) {
		canvas.getDragLayer().setVisible(true);
		if (widget != null)
			throw new IllegalStateException();
		widget = getTransferWidget(dtde);
		comp = widget.getComponent();
		comp.setEnabled(false); // avoids DnD conflicts with drop-target-widgets (ex MediaArea)
		canvas.getDragLayer().add(comp);
	}
	private void end()
	{
		widget = null;
		canvas.getDragLayer().setVisible(false);
		canvas.getDragLayer().removeAll();
	}
	public void dragExit(DropTargetEvent dte) {
		end();
	}
	public void dragOver(DropTargetDragEvent dtde) {
		canvas.getDragLayer().setVisible(true);
		Rectangle startBounds = new Rectangle((int)dtde.getLocation().getX(), (int)dtde.getLocation().getY(), comp.getWidth(), comp.getHeight());
		Point startPoint = new Point(startBounds.x, startBounds.y);
		Point movePoint = new Point(startBounds.x, startBounds.y);
		boolean snapToGrid = !isAltDown(dtde);
    	WidgetResizer.Bounds newBounds = WidgetResizer.doResizeOrMove(canvas.getModel(), Cursor.MOVE_CURSOR, startPoint, startBounds, movePoint, canvas.getModel().getGridWidth(), canvas.getModel().getGridHeight(), snapToGrid, true);
		comp.setBounds(newBounds.x1, newBounds.y1, newBounds.x2-newBounds.x1, newBounds.y2-newBounds.y1);
		canvas.getDragLayer().repaint();
	}
	public void drop(DropTargetDropEvent dtde) {
		
		Rectangle startBounds = new Rectangle((int)dtde.getLocation().getX(), (int)dtde.getLocation().getY(), comp.getWidth(), comp.getHeight());
		Point startPoint = new Point(startBounds.x, startBounds.y);
		Point movePoint = new Point(startBounds.x, startBounds.y);
		boolean snapToGrid = !isAltDown(dtde);
    	WidgetResizer.Bounds newBounds = WidgetResizer.doResizeOrMove(canvas.getModel(), Cursor.MOVE_CURSOR, startPoint, startBounds, movePoint, canvas.getModel().getGridWidth(), canvas.getModel().getGridHeight(), snapToGrid, true);
		comp.setBounds(newBounds.x1, newBounds.y1, newBounds.x2-newBounds.x1, newBounds.y2-newBounds.y1);
		canvas.getModel().addWidget(widget);
		comp.setEnabled(true);
		end();
	}
	public void dropActionChanged(DropTargetDragEvent dtde) {
	}
}
