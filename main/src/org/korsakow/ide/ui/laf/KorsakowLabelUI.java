package org.korsakow.ide.ui.laf;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

import com.sun.java.swing.plaf.motif.MotifLabelUI;

public class KorsakowLabelUI extends MotifLabelUI
{
	private static KorsakowLabelUI ui = new KorsakowLabelUI();
    public static ComponentUI createUI(JComponent c) {
    	return ui;
    }
    @Override
	public void installUI(JComponent c) {
        super.installUI(c);
        c.setBackground(UIManager.getColor("Label.background"));
        c.putClientProperty("ui.background2", null);
    }
    
    @Override
	public void update(Graphics g, JComponent c) {
    	JLabel label = (JLabel)c;
    	if (c.isOpaque()) {
    		Color background = label.getBackground();
    		Color background2 = (Color)label.getClientProperty("ui.background2");
    		final Rectangle clip = g.getClipBounds();
        	Graphics2D g2 = (Graphics2D)g;
    		if (background2 != null) {
            	g2.setPaint(new GradientPaint(0, 0, background, 0, clip.height, background2, false));
    		} else {
            	g2.setPaint(background);
    		}
			g2.fill(clip);
    	}
    	paint(g, c);
    }
}
