package org.korsakow.ide.ui.laf;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToggleButtonUI;

public class KorsakowToggleButtonUI extends BasicToggleButtonUI
{
    public static ComponentUI createUI(JComponent c) 
    {
    	return new KorsakowToggleButtonUI();
    }
    @Override
	public void installUI(JComponent c)
    {
    	super.installUI(c);
    	c.putClientProperty("ToggleButton.background", UIManager.getColor("ToggleButton.background"));
    	c.putClientProperty("ToggleButton.background2", UIManager.getColor("ToggleButton.background2"));
    	c.putClientProperty("ToggleButton.activeBackground", UIManager.getColor("ToggleButton.activeBackground"));
    	c.putClientProperty("ToggleButton.activeBackground2", UIManager.getColor("ToggleButton.activeBackground2"));
    	c.putClientProperty("ToggleButton.selectedBackground", UIManager.getColor("ToggleButton.selectedBackground"));
    	c.putClientProperty("ToggleButton.selectedBackground2", UIManager.getColor("ToggleButton.selectedBackground2"));
    	c.putClientProperty("ToggleButton.selectedActiveBackground", UIManager.getColor("ToggleButton.selectedActiveBackground"));
    	c.putClientProperty("ToggleButton.selectedActiveBackground2", UIManager.getColor("ToggleButton.selectedActiveBackground2"));
    	c.putClientProperty("ToggleButton.inactiveBackground", UIManager.getColor("ToggleButton.inactiveBackground"));
    	c.putClientProperty("ToggleButton.inactiveBackground2", UIManager.getColor("ToggleButton.inactiveBackground2"));
    }
    
    @Override
	protected void installListeners(AbstractButton b) {
    	super.installListeners(b);
    	b.addMouseListener(new MouseAdapter() {
    	    @Override
			public void mouseEntered(MouseEvent e) {
    	    	e.getComponent().repaint();	
    	    }
    	  
    	    @Override
			public void mouseExited(MouseEvent e) {
    	    	e.getComponent().repaint();
    	    }
    	});
    }
    @Override
	public void paint(Graphics g, JComponent c) {
    	Color color1;
    	Color color2;

    	AbstractButton b = (AbstractButton)c;
    	
    	if (c.isEnabled()) {	
    		if(b.isSelected()){
    			if (c.getMousePosition(true) != null) {
            		color1 = (Color)c.getClientProperty("ToggleButton.selectedActiveBackground");
            		color2 = (Color)c.getClientProperty("ToggleButton.selectedActiveBackground2");
    	    	} else {
    	    		color1 = (Color)c.getClientProperty("ToggleButton.selectedBackground");
    	    		color2 = (Color)c.getClientProperty("ToggleButton.selectedBackground2");
    	    	}
    		}else{
    			if (c.getMousePosition(true) != null) {
            		color1 = (Color)c.getClientProperty("ToggleButton.activeBackground");
            		color2 = (Color)c.getClientProperty("ToggleButton.activeBackground2");
    	    	} else {
    	    		color1 = (Color)c.getClientProperty("ToggleButton.background");
    	    		color2 = (Color)c.getClientProperty("ToggleButton.background2");
    	    	}	
    		}
    	} else {
    		color1 = (Color)c.getClientProperty("ToggleButton.inactiveBackground");
    		color2 = (Color)c.getClientProperty("ToggleButton.inactiveBackground2");
    	}
    		
    	/*	
    		if (c.getMousePosition(true) != null) {
        		color1 = (Color)c.getClientProperty("ToggleButton.activeBackground");
        		color2 = (Color)c.getClientProperty("ToggleButton.activeBackground2");
	    	} else {
	    		color1 = (Color)c.getClientProperty("ToggleButton.background");
	    		color2 = (Color)c.getClientProperty("ToggleButton.background2");
	    	}
    		
		} else {
    		color1 = (Color)c.getClientProperty("ToggleButton.inactiveBackground");
    		color2 = (Color)c.getClientProperty("ToggleButton.inactiveBackground2");
		}*/
    		
    		
    	paintBackground(g, c, color1, color2);
    	super.paint(g, c);
    }
    @Override
	protected void paintButtonPressed(Graphics g, AbstractButton b)
    {
    	
    	
    	paintBackground(g, b, (Color)b.getClientProperty("ToggleButton.selectBackground"), (Color)b.getClientProperty("ToggleButton.selectBackground2"));
    }
    protected void paintBackground(Graphics g, JComponent c, Color color1, Color color2)
    {
    	if (color1 != null && color2 != null && c.isOpaque())
    	{
	    	Graphics2D g2 = (Graphics2D)g;
	    	Paint paint = null;
			paint = new GradientPaint(0, 0, color1, 0, c.getHeight()/2, color2, true);
	    	g2.setPaint(paint);
	    	g2.fill(g.getClip());
    	}
    }
}
