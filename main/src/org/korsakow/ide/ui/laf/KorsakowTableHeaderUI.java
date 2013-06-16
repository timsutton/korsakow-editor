package org.korsakow.ide.ui.laf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

import org.korsakow.ide.util.UIResourceManager;

public class KorsakowTableHeaderUI extends MyBasicTableHeaderUI
{
	private static final Icon arrowDownIcon = UIResourceManager.getIcon("arrow_down.png");
	private static final Icon arrowUpIcon = UIResourceManager.getIcon("arrow_up.png");
	
    public static ComponentUI createUI(JComponent h) {
        return new KorsakowTableHeaderUI();
    }
    
    public void installUI(JComponent c)
    {
    	super.installUI(c);
    }
    
    public void paint(Graphics g, JComponent c)
    {
    	super.paint(g, c);
    }
    /**
     * Note that although the parameter is called viewColumnIndex, MyBasicTableHeaderUI, which is a copy/paste of
     * BasicTableHeaderUI actually appears to completely ignore the view/model distinction of column indices,
     * so in some sense its a bit unclear what's getting passed in (or at least what was intended to be passed in!).
     * We should go and fix that perhaps.
     */
    protected void paintCell(Graphics g, Rectangle cellRect, int viewColumnIndex)
    {
    	Graphics2D g2 = (Graphics2D)g;
    	Integer sortedColumnDir = (Integer)header.getTable().getClientProperty("ui.sortedColumnDirection");
    	Integer sortedModelColumnIndex = (Integer)header.getTable().getClientProperty("ui.sortedColumn");
    	
    	int modelColumnIndex = header.getTable().convertColumnIndexToModel(viewColumnIndex);
    	boolean isSortedColumn = sortedModelColumnIndex != null && sortedModelColumnIndex == modelColumnIndex;
    	
    	if (isSortedColumn) {
    		Paint paint = new GradientPaint(cellRect.x, cellRect.y, UIManager.getColor("TableHeader.sortedBackground"), cellRect.x, cellRect.y+cellRect.height, UIManager.getColor("TableHeader.sortedBackground2"));
    		g2.setPaint(paint);
    		g2.fill(cellRect);
    	}
    	
    	super.paintCell(g, cellRect, viewColumnIndex); // expects view indices... apparently!


    	if (isSortedColumn) {
    		Icon arrowIcon = sortedColumnDir==null||sortedColumnDir==1?arrowDownIcon:arrowUpIcon;
	    	Component c = getHeaderRenderer(modelColumnIndex);
	    	int x = cellRect.x + cellRect.width - arrowIcon.getIconWidth();
	    	int y = cellRect.y + (cellRect.height - arrowIcon.getIconHeight())/2;
	    	arrowIcon.paintIcon(c, g, x, y);
    	}
//    	DEBUG OUTPUT
//		String tmp=""+modelColumnIndex+";"+viewColumnIndex;
//		g2.setFont(new Font("Arial", 0, 12));
//		g2.setBackground(Color.white);
//		g2.setColor(Color.red);
//		g2.setPaint(Color.red);
//		g2.drawString(tmp, cellRect.x, cellRect.y+15);
    }
}
