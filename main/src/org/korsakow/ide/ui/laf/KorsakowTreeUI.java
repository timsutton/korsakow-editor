package org.korsakow.ide.ui.laf;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.TreePath;

public class KorsakowTreeUI extends BasicTreeUI
{
    public static ComponentUI createUI(JComponent x) {
    	return new KorsakowTreeUI();
    }


    protected void paintRow(Graphics g, Rectangle clipBounds,
		    Insets insets, Rectangle bounds, TreePath path,
		    int row, boolean isExpanded,
		    boolean hasBeenExpanded, boolean isLeaf)
    {
    	super.paintRow(g, clipBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf);
    	Graphics2D g2 = (Graphics2D)g;
    	g2.setColor(UIManager.getColor("Tree.gridColor"));

    	Rectangle clip = g2.getClipBounds();
    	g2.drawLine(bounds.x, bounds.y+bounds.height-1, clipBounds.x+clipBounds.width, bounds.y+bounds.height-1);
    }
    
	    protected MouseListener createMouseListener() {
	        return new KorsakowTableUI.MouseHandler(super.createMouseListener());
	    }
}
