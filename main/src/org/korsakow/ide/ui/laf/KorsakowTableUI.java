package org.korsakow.ide.ui.laf;

import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.TooManyListenersException;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTableUI;

import org.korsakow.ide.util.Platform;
import org.korsakow.ide.util.UIUtil;


public class KorsakowTableUI extends BasicTableUI implements PropertyChangeListener
{
	protected static DropTargetListener defaultDropTargetListener = null;
    public static ComponentUI createUI(JComponent c) {
        return new KorsakowTableUI();
    }

    public void installUI(JComponent c)
    {
    	super.installUI(c);
    	c.addPropertyChangeListener("transferHandler", this);
    	c.putClientProperty("JTable.autoStartsEdit", false);
    }
	public void propertyChange(PropertyChangeEvent event) {
        DropTarget dropTarget = table.getDropTarget();
        // this compensates for BasicTableUI only doing it for UIResource instances
        if (dropTarget instanceof UIResource == false) {
            if (defaultDropTargetListener == null) {
                defaultDropTargetListener =
                    new MyTableDropTargetListener();
            }
            try {
                dropTarget.addDropTargetListener(
                        defaultDropTargetListener);
            } catch (TooManyListenersException tmle) {
                // should not happen... swing drop target is multicast
            }
        }
	}
    protected MouseInputListener createMouseInputListener() {
        return new MouseHandler(super.createMouseInputListener());
    }
    
    public static class MouseHandler implements MouseInputListener
    {
    	private boolean isFix = false;
    	private MouseListener delegate;
    	private MouseEvent fixEventObject(MouseEvent event)
    	{
    		int modifiers = event.getModifiers();
    		// BasicUI really sucks and doesn't even try to be platform friendly.
    		if (Platform.isMacOS()) {
        		if ((modifiers & MouseEvent.META_MASK) == MouseEvent.META_MASK) {
        			modifiers = (modifiers ^ MouseEvent.META_MASK) | MouseEvent.CTRL_MASK;
            		event = new MouseEvent(event.getComponent(), event.getID(), event.getWhen(), modifiers, event.getX(), event.getY(), event.getClickCount(), UIUtil.isPopupTrigger(event));
        		}
    		}
    		return event;
    	}
    	public MouseHandler(MouseInputListener delegate)
    	{
    		this.delegate = delegate;
    	}
    	public MouseHandler(MouseListener delegate)
    	{
    		this.delegate = delegate;
    	}
		public void mouseClicked(MouseEvent e) {
			delegate.mouseClicked(fixEventObject(e));
		}
		public void mouseEntered(MouseEvent e) {
			delegate.mouseEntered(e);
		}
		public void mouseExited(MouseEvent e) {
			delegate.mouseExited(e);
		}
		public void mousePressed(MouseEvent e) {
			if (UIUtil.isPopupTrigger(e)) // osx ctrl-click fix
				isFix = true;
			else
				delegate.mousePressed(fixEventObject(e));
		}
		public void mouseReleased(MouseEvent e) {
			if (!isFix) // osx ctrl-click fix
				delegate.mouseReleased(fixEventObject(e));
			isFix = false;
		}
		public void mouseDragged(MouseEvent e) {
			if (delegate instanceof MouseMotionListener)
				((MouseMotionListener)delegate).mouseDragged(e);
		}
		public void mouseMoved(MouseEvent e) {
			if (delegate instanceof MouseMotionListener)
				((MouseMotionListener)delegate).mouseMoved(e);
		}
    }
    /**
     * A DropTargetListener to extend the default Swing handling of drop operations
     * by moving the tree selection to the nearest location to the mouse pointer.
     * Also adds autoscroll capability.
     */
    protected static class MyTableDropTargetListener extends MyBasicDropTargetListener {

	/**
	 * called to save the state of a component in case it needs to
	 * be restored because a drop is not performed.
	 */
        protected void saveComponentState(JComponent comp) {
	    JTable table = (JTable) comp;
	    rows = table.getSelectedRows();
	    cols = table.getSelectedColumns();
	}

	/**
	 * called to restore the state of a component
	 * because a drop was not performed.
	 */
        protected void restoreComponentState(JComponent comp) {
	    JTable table = (JTable) comp;
	    table.clearSelection();
	    for (int i = 0; i < rows.length; i++) {
		table.addRowSelectionInterval(rows[i], rows[i]);
	    }
	    for (int i = 0; i < cols.length; i++) {
		table.addColumnSelectionInterval(cols[i], cols[i]);
	    }
	}

	/**
	 * called to set the insertion location to match the current
	 * mouse pointer coordinates.
	 */
        protected void updateInsertionLocation(JComponent comp, Point p) {
	    JTable table = (JTable) comp;
            int row = table.rowAtPoint(p);
            int col = table.columnAtPoint(p);
	    if (row != -1) {
		table.setRowSelectionInterval(row, row);
	    }
	    if (col != -1) {
		table.setColumnSelectionInterval(col, col);
	    }
	}

	private int[] rows;
	private int[] cols;
    }
}
