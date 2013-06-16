package com.sun.swingx;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import org.korsakow.ide.util.UIUtil;

import com.sun.swingx.JTreeTable.TreeTableTextField;
import com.sun.swingx.treetable.TreeTableModel;

/**
 * An editor that can be used to edit the tree column. This extends
 * DefaultCellEditor and uses a JTextField (actually, TreeTableTextField)
 * to perform the actual editing.
 * <p>To support editing of the tree column we can not make the tree
 * editable. The reason this doesn't work is that you can not use
 * the same component for editing and renderering. The table may have
 * the need to paint cells, while a cell is being edited. If the same
 * component were used for the rendering and editing the component would
 * be moved around, and the contents would change. When editing, this
 * is undesirable, the contents of the text field must stay the same,
 * including the caret blinking, and selections persisting. For this
 * reason the editing is done via a TableCellEditor.
 * <p>Another interesting thing to be aware of is how tree positions
 * its render and editor. The render/editor is responsible for drawing the
 * icon indicating the type of node (leaf, branch...). The tree is
 * responsible for drawing any other indicators, perhaps an additional
 * +/- sign, or lines connecting the various nodes. So, the renderer
 * is positioned based on depth. On the other hand, table always makes
 * its editor fill the contents of the cell. To get the allusion
 * that the table cell editor is part of the tree, we don't want the
 * table cell editor to fill the cell bounds. We want it to be placed
 * in the same manner as tree places it editor, and have table message
 * the tree to paint any decorations the tree wants. Then, we would
 * only have to worry about the editing part. The approach taken
 * here is to determine where tree would place the editor, and to override
 * the <code>reshape</code> method in the JTextField component to
 * nudge the textfield to the location tree would place it. Since
 * JTreeTable will paint the tree behind the editor everything should
 * just work. So, that is what we are doing here. Determining of
 * the icon position will only work if the TreeCellRenderer is
 * an instance of DefaultTreeCellRenderer. If you need custom
 * TreeCellRenderers, that don't descend from DefaultTreeCellRenderer, 
 * and you want to support editing in JTreeTable, you will have
 * to do something similiar.
 */
public class TreeTableCellEditor extends DefaultCellEditor {
/**
	 * 
	 */
	private final JTreeTable jTreeTable;

public TreeTableCellEditor(JTreeTable jTreeTable) {
    super(new JTreeTable.TreeTableTextField());
	this.jTreeTable = jTreeTable;
}
	public JTreeTable getTreeTable() {
		return jTreeTable;
	}
/**
 * Overridden to determine an offset that tree would place the
 * editor at. The offset is determined from the
 * <code>getRowBounds</code> JTree method, and additionally
 * from the icon DefaultTreeCellRenderer will use.
 * <p>The offset is then set on the TreeTableTextField component
 * created in the constructor, and returned.
 */
public Component getTableCellEditorComponent(JTable table,
					     Object value,
					     boolean isSelected,
					     int r, int c) {
    Component component = super.getTableCellEditorComponent
	(table, value, isSelected, r, c);
    JTree t = jTreeTable.getTree();
    boolean rv = t.isRootVisible();
    int offsetRow = rv ? r : r - 1;
    Rectangle bounds = t.getRowBounds(offsetRow);
    int offset = bounds.x;
    TreeCellRenderer tcr = t.getCellRenderer();
    if (tcr instanceof DefaultTreeCellRenderer) {
	Object node = t.getPathForRow(offsetRow).
	                getLastPathComponent();
	Icon icon;
	if (t.getModel().isLeaf(node))
	    icon = ((DefaultTreeCellRenderer)tcr).getLeafIcon();
	else if (jTreeTable.tree.isExpanded(offsetRow))
	    icon = ((DefaultTreeCellRenderer)tcr).getOpenIcon();
	else
	    icon = ((DefaultTreeCellRenderer)tcr).getClosedIcon();
	if (icon != null) {
	    offset += ((DefaultTreeCellRenderer)tcr).getIconTextGap() +
		      icon.getIconWidth();
	}
    }
    ((TreeTableTextField)getComponent()).offset = offset;
    return component;
}

/**
 * This is overridden to forward the event to the tree. This will
 * return true if the click count >= 3, or the event is null.
 */
public boolean isCellEditable(EventObject e) {
    if (e instanceof MouseEvent) {
	MouseEvent me = (MouseEvent)e;
	// If the modifiers are not 0 (or the left mouse button),
            // tree may try and toggle the selection, and table
            // will then try and toggle, resulting in the
            // selection remaining the same. To avoid this, we
            // only dispatch when the modifiers are 0 (or the left mouse
            // button).
	if (me.getModifiers() == 0 ||
                me.getModifiers() == InputEvent.BUTTON1_MASK) {
	    for (int counter = jTreeTable.getColumnCount() - 1; counter >= 0;
		 counter--) {
		if (jTreeTable.getColumnClass(counter) == TreeTableModel.class) {
		    MouseEvent newME = new MouseEvent
		          (jTreeTable.tree, me.getID(),
			   me.getWhen(), me.getModifiers(),
			   me.getX() - jTreeTable.getCellRect(0, counter, true).x,
			   me.getY(), me.getClickCount(),
                               UIUtil.isPopupTrigger(me));
		    jTreeTable.tree.dispatchEvent(newME);
		    break;
		}
	    }
	}
	if (me.getClickCount() >= 3) {
	    return true;
	}
	return false;
    }
    if (e == null) {
	return true;
    }
    return false;
}
}