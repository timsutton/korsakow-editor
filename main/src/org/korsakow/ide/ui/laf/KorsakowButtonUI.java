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
import javax.swing.plaf.basic.BasicButtonUI;

public class KorsakowButtonUI extends BasicButtonUI
{
    public static ComponentUI createUI(JComponent c) 
    {
    	return new KorsakowButtonUI();
    }
    @Override
	public void installUI(JComponent c)
    {
    	super.installUI(c);
//    	AbstractButton b = (AbstractButton)c;
//		b.setBorderPainted(false);
//		b.setBorder(null);
    	c.putClientProperty("Button.background", UIManager.getColor("Button.background"));
    	c.putClientProperty("Button.background2", UIManager.getColor("Button.background2"));
    	c.putClientProperty("Button.activeBackground", UIManager.getColor("Button.activeBackground"));
    	c.putClientProperty("Button.activeBackground2", UIManager.getColor("Button.activeBackground2"));
    	c.putClientProperty("Button.inactiveBackground", UIManager.getColor("Button.inactiveBackground"));
    	c.putClientProperty("Button.inactiveBackground2", UIManager.getColor("Button.inactiveBackground2"));
    	c.putClientProperty("Button.selectBackground", UIManager.getColor("Button.selectBackground"));
    	c.putClientProperty("Button.selectBackground2", UIManager.getColor("Button.selectBackground2"));
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
    	if (c.isEnabled()) {
    		if (c.getMousePosition(true) != null) {
        		color1 = (Color)c.getClientProperty("Button.activeBackground");
        		color2 = (Color)c.getClientProperty("Button.activeBackground2");
	    	} else {
	    		color1 = (Color)c.getClientProperty("Button.background");
	    		color2 = (Color)c.getClientProperty("Button.background2");
	    	}
		} else {
    		color1 = (Color)c.getClientProperty("Button.inactiveBackground");
    		color2 = (Color)c.getClientProperty("Button.inactiveBackground2");
		}
    	paintBackground(g, c, color1, color2);
    	super.paint(g, c);
    }
    @Override
	protected void paintButtonPressed(Graphics g, AbstractButton b)
    {
    	paintBackground(g, b, (Color)b.getClientProperty("Button.selectBackground"), (Color)b.getClientProperty("Button.selectBackground2"));
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
