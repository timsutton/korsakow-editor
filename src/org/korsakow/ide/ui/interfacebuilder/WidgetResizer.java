/**
 * 
 */
package org.korsakow.ide.ui.interfacebuilder;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;

import org.korsakow.ide.resources.widget.WidgetComponent;
import org.korsakow.ide.ui.components.KTooltip;

import com.jroller.santosh.border.ResizableBorder;

public class WidgetResizer extends MouseInputAdapter
{
	public static class Bounds
	{
		public int x1;
		public int y1;
		public int x2;
		public int y2;
		public Bounds() {
			
		}
		public Bounds(Bounds b) {
			x1 = b.x1;
			y1 = b.y1;
			x2 = b.x2;
			y2 = b.y2;
		}
	}
	private static Point getMousePosition(MouseEvent mouseEvent)
	{
		Point point = mouseEvent.getPoint();
		Component comp = mouseEvent.getComponent();
		point.x += comp.getX();
		point.y += comp.getY();
		return point;
	}
	
	private final WidgetCanvas widgetCanvas;
	private KTooltip resizeTip;
	
	private final Set<WidgetComponent> activeComponents = new HashSet<WidgetComponent>();
	private Rectangle startBounds = null;
	private Point startPoint = null;
	protected int cursor;

	public WidgetResizer(WidgetCanvas widgetCanvas)
	{
		this.widgetCanvas = widgetCanvas;
	}
	
	
	@Override
	public void mousePressed(MouseEvent mouseEvent) {
		WidgetComponent widgetComp = (WidgetComponent)mouseEvent.getComponent();
		if (widgetComp.getBorder() instanceof ResizableBorder == false)
			return;
		ResizableBorder border = (ResizableBorder)widgetComp.getBorder();
		Collection<WidgetComponent> widgets = widgetCanvas.getSelectedWidgetComponents();
		int newCursor = border.getResizeCursor(mouseEvent);
		beginResizeOrMove(getMousePosition(mouseEvent), newCursor, widgets);
	}
	@Override
	public void mouseReleased(MouseEvent mouseEvent) {
		endResizeOrMove();
	}
	@Override
	public void mouseMoved(MouseEvent mouseEvent) {
		JComponent widgetComp = (JComponent)mouseEvent.getComponent();
		if (widgetComp.getBorder() instanceof ResizableBorder) {
			ResizableBorder border = (ResizableBorder)widgetComp.getBorder();
			widgetComp.setCursor(Cursor.getPredefinedCursor(border.getResizeCursor(mouseEvent)));
		}
	}
	@Override
	public void mouseExited(MouseEvent mouseEvent) {
		JComponent widgetComp = (JComponent)mouseEvent.getComponent();
		widgetComp.setCursor(Cursor.getDefaultCursor());
	}


	private void beginResizeOrMove(Point mousePoint, int cursor,
			Collection<WidgetComponent> widgetComps) {
		this.cursor = cursor;

		startBounds = new Rectangle(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
		for (WidgetComponent widgetComp: widgetComps) {
			
			widgetComp.getWidget().setPropertyUpdating("x", true);
			widgetComp.getWidget().setPropertyUpdating("y", true);
			widgetComp.getWidget().setPropertyUpdating("width", true);
			widgetComp.getWidget().setPropertyUpdating("height", true);
	
//			startPoints.put(widgetComp, mousePoint);
			Rectangle bounds = widgetComp.getBounds();
			startBounds.x = Math.min(startBounds.x, bounds.x);
			startBounds.y = Math.min(startBounds.y, bounds.y);
			startBounds.width = Math.max(startBounds.width, bounds.width);
			startBounds.height = Math.max(startBounds.height, bounds.height);
			activeComponents.add(widgetComp);
		}
		startPoint = mousePoint;
	}
	private void endResizeOrMove() {
		for (JComponent comp : activeComponents) {
			WidgetComponent widgetComp = (WidgetComponent)comp;
			widgetComp.getWidget().setPropertyUpdating("x", false);
			widgetComp.getWidget().setPropertyUpdating("y", false);
			widgetComp.getWidget().setPropertyUpdating("width", false);
			widgetComp.getWidget().setPropertyUpdating("height", false);
			widgetComp.getWidget().fireBatchPropertyChange();
		}
		activeComponents.clear();
//		startPoints.clear();
		startPoint = null;
		startBounds = null;
		if (resizeTip != null)
			resizeTip.setVisible(false);
	}
	

	
	@Override
	public void mouseDragged(MouseEvent mouseEvent)
	{
		Point mousePoint = getMousePosition(mouseEvent);
		final boolean isAltDown = isSnapToGrid(mouseEvent);
		final boolean isShiftDown = mouseEvent.isShiftDown();
		
		doDragged(activeComponents, mousePoint, isAltDown, isShiftDown);
	}
	
	private boolean isSnapToGrid(MouseEvent mouseEvent){	
		if(!(widgetCanvas.getModel().isSnapToGrid())){
			return !mouseEvent.isAltDown();
		}else{
			return mouseEvent.isAltDown();
		}
	}
	public void doDragged(final Collection<WidgetComponent> widgets, final Point mousePoint, final boolean isAltDown, final boolean isShiftDown)
	{
		if (widgets.isEmpty())
			return;
		if (mousePoint == null)
			return;
		if (startPoint == null)
			return;
		
		final boolean snapToGrid = !isAltDown;
		final int gridWidth = widgetCanvas.getModel().getGridWidth();
		final int gridHeight = widgetCanvas.getModel().getGridHeight();

		Bounds tooltipBounds = null;

		if (widgets.size() == 1) {
			WidgetComponent widgetComp = widgets.iterator().next();
			
			boolean maintainAspect = widgetComp.getMaintainsAspectByDefaultWhenResized();
			if (isShiftDown)
				maintainAspect = !maintainAspect;
			final Bounds newBounds = doResizeOrMove(widgetCanvas.getModel(), cursor, startPoint, startBounds, mousePoint, gridWidth, gridHeight, snapToGrid, maintainAspect);
			
			widgetComp.setBounds(newBounds.x1, newBounds.y1, newBounds.x2-newBounds.x1, newBounds.y2-newBounds.y1);
			
			tooltipBounds = new Bounds(newBounds);
		} else {
			boolean maintainAspect = false;
			for (WidgetComponent wc : widgets)
				maintainAspect = maintainAspect || wc.getMaintainsAspectByDefaultWhenResized();
			if (isShiftDown)
				maintainAspect = !maintainAspect;
			
			final Bounds newBounds = doResizeOrMove(widgetCanvas.getModel(), cursor, startPoint, startBounds, mousePoint, gridWidth, gridHeight, snapToGrid, maintainAspect);
			
			int dx = newBounds.x1 - startBounds.x;
			int dy = newBounds.y1 - startBounds.y;
			
			startBounds.x = newBounds.x1;
			startBounds.y = newBounds.y1;
			startPoint.x += dx;
			startPoint.y += dy;
			
			tooltipBounds = new Bounds(newBounds);
			
			for (WidgetComponent wc : widgets) {
				wc.setLocation(wc.getX()+dx, wc.getY()+dy);
			}
		}
		for (JComponent widgetComp : widgets)
		{
			if (widgetComp.getParent() != null) {
				widgetComp.getParent().repaint();
				((JComponent)widgetComp.getParent()).revalidate();
			}
			widgetComp.invalidate();
		}
		
		if (resizeTip == null) {
			resizeTip = new KTooltip((Frame)widgetCanvas.getTopLevelAncestor());
		}
		
		// tooltip
		Point pos = MouseInfo.getPointerInfo().getLocation();
		if (pos != null) {
			tooltipBounds.x1 -= widgetCanvas.getModel().getMovieOffsetX();
			tooltipBounds.y1 -= widgetCanvas.getModel().getMovieOffsetY();
			tooltipBounds.x2 -= widgetCanvas.getModel().getMovieOffsetX();
			tooltipBounds.y2 -= widgetCanvas.getModel().getMovieOffsetY();
			
			pos.y -= resizeTip.getHeight();
			String tip = cursor == Cursor.MOVE_CURSOR?
					String.format("%d,%d", tooltipBounds.x1, tooltipBounds.y1):
					String.format("%d,%d", tooltipBounds.x2-tooltipBounds.x1, tooltipBounds.y2-tooltipBounds.y1);
			resizeTip.show(pos, tip);
		}
		// cursor shouldn't change while dragging
		widgetCanvas.setCursor(Cursor.getPredefinedCursor(cursor));
	}
	
	public static Bounds doResizeOrMove(
			WidgetCanvasModel canvasModel,
			final int cursor,
			final Point startPoint,
			final Rectangle startBounds,
			final Point mousePoint, 
			final int gridWidth,
			final int gridHeight,
			final boolean snapToGrid, final boolean maintainAspect)
	{
		final double aspect = startBounds.width / (double)startBounds.height;
		final Bounds newBounds = new Bounds();
		newBounds.x1 = startBounds.x;
		newBounds.y1 = startBounds.y;
		newBounds.x2 = startBounds.x+startBounds.width;
		newBounds.y2 = startBounds.y+startBounds.height;
		final int mX = mousePoint.x;
		final int mY = mousePoint.y;
		final int movieOffsetX = canvasModel.getMovieOffsetX();
		final int movieOffsetY = canvasModel.getMovieOffsetY();
		switch(cursor)
		{
		case Cursor.N_RESIZE_CURSOR:
			resizeN(movieOffsetX, movieOffsetY, startPoint, startBounds, gridWidth, gridHeight, maintainAspect, snapToGrid, aspect, mX, mY, newBounds);
			break;
		case Cursor.S_RESIZE_CURSOR:
			resizeS(movieOffsetX, movieOffsetY, startPoint, startBounds, gridWidth, gridHeight, maintainAspect, snapToGrid, aspect, mX, mY, newBounds);
			break;
		case Cursor.W_RESIZE_CURSOR:
			resizeW(movieOffsetX, movieOffsetY, startPoint, startBounds, gridWidth, gridHeight, maintainAspect, snapToGrid, aspect, mX, mY, newBounds);
			break;
		case Cursor.E_RESIZE_CURSOR:
			resizeE(movieOffsetX, movieOffsetY, startPoint, startBounds, gridWidth, gridHeight, maintainAspect, snapToGrid, aspect, mX, mY, newBounds);
			break;
		case Cursor.NE_RESIZE_CURSOR:
			resizeNE(movieOffsetX, movieOffsetY, startPoint, startBounds, gridWidth, gridHeight, maintainAspect, snapToGrid, aspect, mX, mY, newBounds);
			break;
		case Cursor.SE_RESIZE_CURSOR:
			resizeSE(movieOffsetX, movieOffsetY, startPoint, startBounds, gridWidth, gridHeight, maintainAspect, snapToGrid, aspect, mX, mY, newBounds);
			break;
		case Cursor.SW_RESIZE_CURSOR:
			resizeSW(movieOffsetX, movieOffsetY, startPoint, startBounds, gridWidth, gridHeight, maintainAspect, snapToGrid, aspect, mX, mY, newBounds);
			break;
		case Cursor.NW_RESIZE_CURSOR:
			resizeNW(movieOffsetX, movieOffsetY, startPoint, startBounds, gridWidth, gridHeight, maintainAspect, snapToGrid, aspect, mX, mY, newBounds);
			break;
		case Cursor.MOVE_CURSOR:
			move(movieOffsetX, movieOffsetY, startPoint, startBounds, gridWidth, gridHeight, maintainAspect, snapToGrid, aspect, mX, mY, newBounds);
			break;
		}
		return newBounds;
	}

	private static void move(
			int movieOffsetX, int movieOffsetY,
			final Point startPoint, final Rectangle startBounds,
			final int gridWidth, final int gridHeight,
			final boolean maintainAspect, final boolean snapToGrid,
			final double aspect, final int mX, final int mY,
			final Bounds nb
			) {
		nb.x1 = mX + (startBounds.x - startPoint.x);
		nb.y1 = mY + (startBounds.y - startPoint.y);
		nb.x2 = nb.x1 + startBounds.width;
		nb.y2 = nb.y1 + startBounds.height;

		if (snapToGrid) {
			if (gridWidth != 0) {
				int dX = (nb.x1-movieOffsetX)%gridWidth;
				nb.x1 -= dX;
				nb.x2 -= dX;
			}
			if (gridHeight != 0) {
				int dY = (nb.y1-movieOffsetY)%gridHeight;
				nb.y1 -= dY;
				nb.y2 -= dY;
			}
		}
	}

	private static void resizeNW(
			int movieOffsetX, int movieOffsetY,
			final Point startPoint, final Rectangle startBounds,
			final int gridWidth, final int gridHeight,
			final boolean maintainAspect, final boolean snapToGrid,
			final double aspect, final int mX, final int mY,
			final Bounds nb
			) {
		if (maintainAspect) {
			double dX = (mX - (startBounds.x + startBounds.width));
			double dY = (mY - (startBounds.y + startBounds.height));
			if (snapToGrid) {
				dX -= (dX-movieOffsetX)%gridWidth;
				dY -= (dY-movieOffsetY)%gridHeight;
			}
			double distance = Math.sqrt(dX*dX + dY*dY);
			if (startBounds.width > startBounds.height) {
				nb.x1 = (int)(nb.x2 - distance);
				if (snapToGrid)
					nb.x1 -= (nb.x1-movieOffsetX)%gridWidth;
				int height = (int)((nb.x2-nb.x1)/aspect);
				nb.y1 = nb.y2 - height;
			} else {
				nb.y1 = (int)(nb.y2 - distance);
				if (snapToGrid)
					nb.y1 -= (nb.y1-movieOffsetY)%gridHeight;
				int width = (int)((nb.y2-nb.y2)*aspect);
				nb.x1 = nb.x2 - width;
			}
		} else {
			nb.x1 = mX;
			nb.y1 = mY;

			if (snapToGrid) {
				nb.x1 -= (nb.x1-movieOffsetX)%gridWidth;
			}
			if (snapToGrid)
				nb.y1 -= (nb.y1-movieOffsetY)%gridHeight;

			if (nb.x1 >= nb.x2)
				nb.x1 = nb.x2 - gridWidth;
			if (nb.y1 >= nb.y2)
				nb.y1 = nb.y2 - gridHeight;
		}
	}

	private static void resizeSW(
			int movieOffsetX, int movieOffsetY,
			final Point startPoint, final Rectangle startBounds,
			final int gridWidth, final int gridHeight,
			final boolean maintainAspect, final boolean snapToGrid,
			final double aspect, final int mX, final int mY,
			final Bounds nb
			) {
		if (maintainAspect) {
			double dX = (mX - (startBounds.x + startBounds.width));
			double dY = (mY - (startBounds.y));
			if (snapToGrid) {
				dX -= (dX-movieOffsetX)%gridWidth;
				dY -= (dY-movieOffsetY)%gridHeight;
			}
			double distance = Math.sqrt(dX*dX + dY*dY);
			if (startBounds.width > startBounds.height) {
				nb.x1 = (int)(nb.x2 - distance);
				if (snapToGrid)
					nb.x1 -= (nb.x1-movieOffsetX)%gridWidth;
				int height = (int)((nb.x2-nb.x1)/aspect);
				nb.y2 = nb.y1 + height;
			} else {
				nb.y2 = (int)(nb.y1 + distance);
				if (snapToGrid)
					nb.y2 -= (nb.y2-movieOffsetY)%gridHeight;
				int width = (int)((nb.y2-nb.y1)*aspect);
				nb.x1 = nb.x2 - width;
			}
		} else {
			nb.x1 = mX;
			nb.y2 = mY;

			if (snapToGrid) {
				nb.x1 -= (nb.x1-movieOffsetX)%gridWidth;
			}
			if (snapToGrid)
				nb.y2 -= (nb.y2-movieOffsetY)%gridHeight;

			if (nb.x1 >= nb.x2)
				nb.x1 = nb.x2 - gridWidth;
			if (nb.y2 <= nb.y1)
				nb.y2 = nb.y1 + gridHeight;
		}
	}

	private static void resizeSE(
			int movieOffsetX, int movieOffsetY,
			final Point startPoint, final Rectangle startBounds,
			final int gridWidth, final int gridHeight,
			final boolean maintainAspect, final boolean snapToGrid,
			final double aspect, final int mX, final int mY,
			final Bounds nb
			) {
		if (maintainAspect) {
			double dX = (mX - (startBounds.x));
			double dY = (mY - (startBounds.y));
			if (snapToGrid) {
				dX -= (dX-movieOffsetX)%gridWidth;
				dY -= (dY-movieOffsetY)%gridHeight;
			}
			double distance = Math.sqrt(dX*dX + dY*dY);
			if (startBounds.width > startBounds.height) {
				nb.x2 = (int)(nb.x1 + distance);
				if (snapToGrid)
					nb.x2 -= (nb.x2-movieOffsetX)%gridWidth;
				int height = (int)((nb.x2-nb.x1)/aspect);
				nb.y2 = nb.y1 + height;
			} else {
				nb.y2 = (int)(nb.y1 + distance);
				if (snapToGrid)
					nb.y2 -= (nb.y2-movieOffsetY)%gridHeight;
				int width = (int)((nb.y2-nb.y1)*aspect);
				nb.x2 = nb.x1 + width;
			}
		} else {
			nb.x2 = mX;
			nb.y2 = mY;

			if (snapToGrid) {
				nb.x2 -= (nb.x2-movieOffsetX)%gridWidth;
			}
			if (snapToGrid)
				nb.y2 -= (nb.y2-movieOffsetY)%gridHeight;

			if (nb.x2 <= nb.x1)
				nb.x2 = nb.x1 + gridWidth;
			if (nb.y2 <= nb.y1)
				nb.y2 = nb.y1 + gridHeight;
		}
	}

	private static void resizeNE(
			int movieOffsetX, int movieOffsetY,
			final Point startPoint, final Rectangle startBounds,
			final int gridWidth, final int gridHeight,
			final boolean maintainAspect, final boolean snapToGrid,
			final double aspect, final int mX, final int mY,
			final Bounds nb
			) {
		if (maintainAspect) {
			double dX = (mX - (startBounds.x));
			double dY = (mY - (startBounds.y + startBounds.height));
			if (snapToGrid) {
				dX -= (dX+movieOffsetX)%gridWidth;
				dY -= (dY+movieOffsetY)%gridHeight;
			}
			double distance = Math.sqrt(dX*dX + dY*dY);
			if (startBounds.width > startBounds.height) {
				nb.x2 = (int)(nb.x1 + distance);
				if (snapToGrid)
					nb.x2 -= (nb.x2+movieOffsetX)%gridWidth;
				int height = (int)((nb.x2-nb.x1)/aspect);
				nb.y1 = nb.y2 - height;
			} else {
				nb.y1 = (int)(nb.y2 - distance);
				if (snapToGrid)
					nb.y1 -= (nb.y1+movieOffsetY)%gridHeight;
				int width = (int)((nb.y2-nb.y1)*aspect);
				nb.x2 = nb.x1 + width;
			}
		} else {
			nb.x2 = mX;
			nb.y1 = mY;

			if (snapToGrid) {
				nb.x2 -= (nb.x2-movieOffsetX)%gridWidth;
			}
			if (snapToGrid)
				nb.y1 -= (nb.y1-movieOffsetY)%gridHeight;

			if (nb.x2 <= nb.x1)
				nb.x2 = nb.x1 + gridWidth;
			if (nb.y1 >= nb.y2)
				nb.y1 = nb.y2 - gridHeight;
		}
	}

	private static void resizeE(
			int movieOffsetX, int movieOffsetY,
			final Point startPoint, final Rectangle startBounds,
			final int gridWidth, final int gridHeight,
			final boolean maintainAspect, final boolean snapToGrid,
			final double aspect, final int mX, final int mY,
			final Bounds nb
			) {
		if (mX <= nb.x1)
			return;
		nb.x2 = mX;
		if (snapToGrid)
			nb.x2 -= (nb.x2-movieOffsetX)%gridWidth;
		if (maintainAspect) {
			int height = (int)((nb.x2-nb.x1)/aspect);
			nb.y1 = startBounds.y + (startBounds.height - height)/2;//(startBounds.height/2 - height/2) introduces rounding errors
			nb.y2 = startBounds.y + (startBounds.height + height)/2;
		}
	}

	private static void resizeW(
			int movieOffsetX, int movieOffsetY,
			final Point startPoint, final Rectangle startBounds,
			final int gridWidth, final int gridHeight,
			final boolean maintainAspect, final boolean snapToGrid,
			final double aspect, final int mX, final int mY,
			final Bounds nb
			) {
		if (mX >= nb.x2)
			return;
		nb.x1 = mX;
		if (snapToGrid)
			nb.x1 -= (nb.x1-movieOffsetX)%gridWidth;
		if (maintainAspect) {
			int height = (int)((nb.x2-nb.x1)/aspect);
			nb.y1 = startBounds.y + (startBounds.height - height)/2;
			nb.y2 = startBounds.y + (startBounds.height + height)/2;
		}
	}

	private static void resizeS(
			int movieOffsetX, int movieOffsetY,
			final Point startPoint, final Rectangle startBounds,
			final int gridWidth, final int gridHeight,
			final boolean maintainAspect, final boolean snapToGrid,
			final double aspect, final int mX, final int mY,
			final Bounds nb
			) {
		if (mY <= nb.y1)
			return;
		nb.y2 = mY;
		if (snapToGrid)
			nb.y2 -= (nb.y2-movieOffsetY)%gridHeight;
		if (maintainAspect) {
			int width = (int)((nb.y2-nb.y1)*aspect);
			nb.x1 = startBounds.x + (startBounds.width - width)/2;
			nb.x2 = startBounds.x + (startBounds.width + width)/2;
		}
	}

	private static void resizeN(
			int movieOffsetX, int movieOffsetY,
			final Point startPoint, final Rectangle startBounds,
			final int gridWidth, final int gridHeight,
			final boolean maintainAspect, final boolean snapToGrid,
			final double aspect, final int mX, final int mY,
			final Bounds nb
			) {
		if (mY >= nb.y2)
			return;
		nb.y1 = mY;
		if (snapToGrid)
			nb.y1 -= (nb.y1-movieOffsetY)%gridHeight;
		if (maintainAspect) {
			int width = (int)((nb.y2-nb.y1)*aspect);
			nb.x1 = startBounds.x + (startBounds.width - width)/2;
			nb.x2 = startBounds.x + (startBounds.width + width)/2;
		}
	}
}
