package org.korsakow.ide.ui.laf;

import java.awt.BasicStroke;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

public class KorsakowTabbedPaneUI extends BasicTabbedPaneUI
{
    public static ComponentUI createUI(JComponent c) {
        return new KorsakowTabbedPaneUI();
    }
    protected void paintTabBackground(Graphics g, int tabPlacement,
            int tabIndex,
            int x, int y, int w, int h, 
            boolean isSelected )
    {
    	Paint paint = null;
    	switch (tabPlacement)
    	{
    	case LEFT:
    	case RIGHT:
        	if (isSelected) {
        		paint = new GradientPaint(0, 0, UIManager.getColor("TabbedPane.selectedTabBackground"), w, 0, UIManager.getColor("TabbedPane.selectedTabBackground2"), false);
        	} else {
        		paint = new GradientPaint(0, 0, UIManager.getColor("TabbedPane.tabBackground"), w, 0, UIManager.getColor("TabbedPane.tabBackground2"), false);
        	}
    		break;
    	case TOP:
    	case BOTTOM:
        	if (isSelected) {
        		paint = new GradientPaint(0, 0, UIManager.getColor("TabbedPane.selectedTabBackground"), 0, h, UIManager.getColor("TabbedPane.selectedTabBackground2"), false);
        	} else {
        		paint = new GradientPaint(0, 0, UIManager.getColor("TabbedPane.tabBackground"), 0, h, UIManager.getColor("TabbedPane.tabBackground2"), false);
        	}
    		break;
    	}
    	
		Graphics2D g2 = (Graphics2D)g;
		float arcWidth = UIManager.getInt("TabbedPane.roundedCornerSize");
		g2.setStroke(new BasicStroke(1));
    	g2.setPaint(paint);
    	switch (tabPlacement)
    	{
    	case LEFT:
    		KorsakowLafUtil.fillLeftRoundedRect(g2, x, y, w, h, arcWidth, arcWidth);
    		break;
    	case TOP:
    		KorsakowLafUtil.fillTopRoundedRect(g2, x, y, w, h, arcWidth, arcWidth);
    		break;
    	case RIGHT:
    	case BOTTOM:
    		throw new IllegalStateException("Placement not yet implemented!");
    	}
	}

    /**
     * this function draws the border around each tab
     * note that this function does now draw the background of the tab.
     * that is done elsewhere
     */
   protected void paintTabBorder(Graphics g, int tabPlacement,
                                 int tabIndex,
                                 int x, int y, int w, int h, 
                                 boolean isSelected )
   {
		Graphics2D g2 = (Graphics2D)g;
		float arcWidth = UIManager.getInt("TabbedPane.roundedCornerSize");
		g2.setPaint(UIManager.getColor("TabbedPane.shadow"));
		g2.setStroke(new BasicStroke(1));
    	switch (tabPlacement)
    	{
    	case LEFT:
    		KorsakowLafUtil.drawLeftRoundedRect(g2, x, y, w, h, arcWidth, arcWidth);
    		break;
    	case TOP:
    		KorsakowLafUtil.drawTopRoundedRect(g2, x, y, w, h, arcWidth, arcWidth);
    		break;
    	case RIGHT:
    	case BOTTOM:
    		throw new IllegalStateException("Placement not yet implemented!");
    	}
   }
    
   protected void paintFocusIndicator(Graphics g, int tabPlacement,
           Rectangle[] rects, int tabIndex, 
           Rectangle iconRect, Rectangle textRect,
           boolean isSelected)
   {
	   // do nothing
   }
   protected int getTabLabelShiftX(int tabPlacement, int tabIndex, boolean isSelected) {
	   return 0;
   }
   protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected) {
	   return 0;
   }
   protected Insets getSelectedTabPadInsets(int tabPlacement) {
	   return selectedTabPadInsets;
   }
   protected static void copyInsets(Insets srcInsets, Insets targetInsets) {
	   targetInsets.left = srcInsets.left;
	   targetInsets.top = srcInsets.top;
	   targetInsets.right = srcInsets.right;
	   targetInsets.bottom = srcInsets.bottom;;
   }
}
