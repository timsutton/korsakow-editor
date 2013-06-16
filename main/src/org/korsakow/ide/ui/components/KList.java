/**
 * 
 */
package org.korsakow.ide.ui.components;

import java.applet.Applet;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventObject;

import javax.swing.CellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;


/**
 * Supports:
 * -editing
 * -ToolTipRenderer
 * -rollover states
 * @author d
 *
 */
public class KList extends JList implements CellEditorListener
{
	public static final String ROLLOVER_PROPERTY = "KList.ROLLOVER_PROPERTY";
	
    /** If editing, the <code>Component</code> that is handling the editing. */
    transient protected Component       editorComp;

    /**
     * The active cell editor object, that overwrites the screen real estate
     * occupied by the current cell and allows the user to change its contents.
     * {@code null} if the table isn't currently editing.
     */
    transient protected ListCellEditor cellEditor;

    /** Identifies the row of the cell being edited. */
    transient protected int             editingRow;
	private CellEditorRemover editorRemover;
	private ToolTipRenderer tooltipRenderer = null;
	private MouseInputListener rolloverListener;

	private boolean editable = true;
	public KList()
	{
		cellEditor = new DefaultListCellEditor(new JTextField());
	}
	public void setToolTipRenderer(ToolTipRenderer renderer)
	{
		this.tooltipRenderer = renderer;
	}
	public ToolTipRenderer getToolTipRenderer()
	{
		return tooltipRenderer;
	}
	public String getToolTipText(MouseEvent event)
	{
		if (tooltipRenderer != null)
			return tooltipRenderer.getToolTipText(event);
		else
			return super.getToolTipText(event);
	}
	public ListCellEditor getCellEditor()
	{
		return cellEditor;
	}
	public void setRolloverEnabled(boolean b)
	{
		if (b) {
			if (rolloverListener == null) {
				rolloverListener = new RolloverListener(this);
				addMouseListener(rolloverListener);
				addMouseMotionListener(rolloverListener);
			}
		} else {
			if (rolloverListener != null) {
				removeMouseListener(rolloverListener);
				removeMouseMotionListener(rolloverListener);
			}
			rolloverListener = null;
		}
	}
	/**
	 * Returns the index maintained by the rollover mechanism. Some UI implementations (such as BasicListUI) will
	 * recurse if you attempt to get the cell bounds from within a CellRenderer call.
	 * 
	 * This method circumvents that limitation.
	 * 
	 * @return -1 if setRolloverEnabled(true) has not been called
	 */
	public int getRolloverIndex()
	{
		Integer index = (Integer)getClientProperty(ROLLOVER_PROPERTY);
		return index!=null?index:-1;
	}
	
	/**
     * Sets the active cell editor.
     *
     * @param anEditor the active cell editor
     * @see #cellEditor
     * @beaninfo
     *  bound: true
     *  description: The table's active cell editor.
     */
    public void setCellEditor(ListCellEditor anEditor) {
	ListCellEditor oldEditor = cellEditor;
        cellEditor = anEditor;
	firePropertyChange("listCellEditor", oldEditor, anEditor);
    }		public boolean isEditable()
	{
		return editable;
	}
	protected void setEditingRow(int row)
	{
		editingRow = row;
	}
    /**
     * Returns true if a cell is being edited.
     *
     * @return  true if the table is editing a cell
     * @see     #editingColumn
     * @see     #editingRow
     */
    public boolean isEditing() {
        return (cellEditor == null)? false : true;
    }
    /**
     * Discards the editor object and frees the real estate it used for
     * cell rendering.
     */
    public void removeEditor() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().
            removePropertyChangeListener("permanentFocusOwner", editorRemover);
	editorRemover = null;

        CellEditor editor = getCellEditor();
        if(editor != null) {
            editor.removeCellEditorListener(this);

            if (editorComp != null) {
		remove(editorComp);
	    }

            Rectangle cellRect = getCellBounds(editingRow, editingRow);

            setCellEditor(null);
            setEditingRow(-1);
            editorComp = null;

            repaint(cellRect);
        }
    }
    public boolean editCellAt(int row, EventObject e){
        if (cellEditor != null && !cellEditor.stopCellEditing()) {
            return false;
        }

		if (row < 0 || row >= getModel().getSize()) {
		    return false;
		}

		if (!isEditable())
			return false;

        if (editorRemover == null) {
            KeyboardFocusManager fm =
                KeyboardFocusManager.getCurrentKeyboardFocusManager();
            editorRemover = new CellEditorRemover(fm);
            fm.addPropertyChangeListener("permanentFocusOwner", editorRemover);
        }

        ListCellEditor editor = getCellEditor();
        if (editor != null && editor.isCellEditable(e)) {
	    editorComp = prepareEditor(editor, row);
	    if (editorComp == null) {
		removeEditor();
		return false;
	    }
	    editorComp.setBounds(getCellBounds(row, row));
	    add(editorComp);
	    editorComp.validate();

	    setCellEditor(editor);
	    setEditingRow(row);
	    editor.addCellEditorListener(this);

	    return true;
        }
        return false;
    }
    /**
     * Prepares the editor by querying the data model for the value and
     * selection state of the cell at <code>row</code>, <code>column</code>.
     * <p>
     * <b>Note:</b>
     * Throughout the table package, the internal implementations always
     * use this method to prepare editors so that this default behavior
     * can be safely overridden by a subclass.
     *
     * @param editor  the <code>TableCellEditor</code> to set up
     * @param row     the row of the cell to edit,
     *		      where 0 is the first row
     * @param column  the column of the cell to edit,
     *		      where 0 is the first column
     * @return the <code>Component</code> being edited
     */
    public Component prepareEditor(ListCellEditor editor, int row) {
        Object value = getModel().getElementAt(row);
        boolean isSelected = isSelectedIndex(row);
        Component comp = editor.getListCellEditorComponent(this, value, isSelected,
                                                  row);
        if (comp instanceof JComponent) {
	    JComponent jComp = (JComponent)comp;
	    if (jComp.getNextFocusableComponent() == null) {
		jComp.setNextFocusableComponent(this);
	    }
	}
	return comp;
    }
    protected class CellEditorRemover implements PropertyChangeListener {
        KeyboardFocusManager focusManager;

        public CellEditorRemover(KeyboardFocusManager fm) {
            this.focusManager = fm;
        }

        public void propertyChange(PropertyChangeEvent ev) {
            if (!isEditing() || getClientProperty("terminateEditOnFocusLost") != Boolean.TRUE) {
                return;
            }

            Component c = focusManager.getPermanentFocusOwner();
            while (c != null) {
                if (c == KList.this) {
                    // focus remains inside the table
                    return;
                } else if ((c instanceof Window) ||
                           (c instanceof Applet && c.getParent() == null)) {
                    if (c == SwingUtilities.getRoot(KList.this)) {
                        if (!getCellEditor().stopCellEditing()) {
                            getCellEditor().cancelCellEditing();
                        }
                    }
                    break;
                }
                c = c.getParent();
            }
        }
    }
    /**
     * Invoked when editing is finished. The changes are saved and the
     * editor is discarded.
     * <p>
     * Application code will not use these methods explicitly, they
     * are used internally by JTable.
     *
     * @param  e  the event received
     * @see CellEditorListener
     */
    public void editingStopped(ChangeEvent e) {
        // Take in the new value
        ListCellEditor editor = getCellEditor();
        if (editor != null) {
            Object value = editor.getCellEditorValue();
            if (getModel() instanceof DefaultListModel)
            	((DefaultListModel)getModel()).setElementAt(value, editingRow);
            else
            if (getModel() instanceof MutableListModel)
            	((MutableListModel)getModel()).setElementAt(value, editingRow);
            removeEditor();
        }
    }

    /**
     * Invoked when editing is canceled. The editor object is discarded
     * and the cell is rendered once again.
     * <p>
     * Application code will not use these methods explicitly, they
     * are used internally by JTable.
     *
     * @param  e  the event received
     * @see CellEditorListener
     */
    public void editingCanceled(ChangeEvent e) {
        removeEditor();
    }
    protected static class RolloverListener extends MouseInputAdapter
    {
    	private KList list;
    	public RolloverListener(KList list)
    	{
    		this.list = list;
    	}
		private void update(MouseEvent e) {
			Integer oldIndex = (Integer)list.getClientProperty(ROLLOVER_PROPERTY);
			Integer index = null;
			if (e != null) {
				index = list.locationToIndex(e.getPoint());
				list.putClientProperty(ROLLOVER_PROPERTY, index);
				list.repaint(list.getCellBounds(index, index));
			} else {
				list.putClientProperty(ROLLOVER_PROPERTY, -1);
			}
			if (oldIndex!=null && oldIndex!=index) {
				Rectangle rect = list.getCellBounds(oldIndex, oldIndex);
				if (rect != null)
					list.repaint(rect);
			}
		}
		public void mouseExited(MouseEvent e) {
			update(null);
		}
		public void mouseDragged(MouseEvent e) {
			update(e);
		}
		public void mouseMoved(MouseEvent e) {
			update(e);
		}
    }
}