package org.korsakow.ide.ui.laf;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicRadioButtonUI;

import org.korsakow.ide.util.UIResourceManager;

public class KorsakowRadioButtonUI extends KorsakowButtonUI
{
	private static class DummyIcon implements Icon
	{
		private int width;
		private int height;
		public DummyIcon(int width, int height)
		{
			this.width = width;
			this.height = height;
		}
		public int getIconHeight() {
			return height;
		}
		public int getIconWidth() {
			return width;
		}
		public void paintIcon(Component c, Graphics g, int x, int y) {
		}
	}
	private static Icon checkIcon = null;
	private static Icon dummyIcon = new DummyIcon(12, 12);
    public static ComponentUI createUI(JComponent c) 
    {
    	return new KorsakowRadioButtonUI();
    }
	public void installUI(JComponent c) {
        super.installUI(c);
        if (checkIcon == null) {
        	checkIcon = UIResourceManager.getIcon("laf/checkbox_check.png");
        }
    	c.putClientProperty("Button.background", null);
    	c.putClientProperty("Button.background2", null);
    	c.putClientProperty("Button.activeBackground", null);
    	c.putClientProperty("Button.activeBackground2", null);
    	c.putClientProperty("Button.inactiveBackground", null);
    	c.putClientProperty("Button.inactiveBackground2", null);
    	c.putClientProperty("Button.selectBackground", null);
    	c.putClientProperty("Button.selectBackground2", null);
    	c.setOpaque(false);
    	((JRadioButton)c).setIcon(dummyIcon);
    }
    public void paint(Graphics g, JComponent c) {
    	super.paint(g, c);
    	JRadioButton check = (JRadioButton) c;
    	ButtonModel model = check.getModel();

    	float strokeSize = UIManager.getInt("RadioButton.borderSize"); //KorsakowLookAndFeel.BORDER_STROKE_SIZE;
    	
    	Graphics2D g2 = (Graphics2D)g;
    	Rectangle clipRect = g.getClipBounds();
    	int width = clipRect.width;
    	int height = clipRect.height;
    	
    	float borderWidth = 12 - strokeSize;
    	float borderHeight = 12 - strokeSize;
    	float borderX = strokeSize/2;
    	float borderY = (height-strokeSize/2 - borderHeight)/2;
    	Shape border = new Ellipse2D.Float(borderX, borderY, borderWidth, borderHeight);
    	
    	// draw border
    	g2.setStroke(new BasicStroke(strokeSize));
    	g2.setColor(UIManager.getColor("RadioButton.borderColor"));
    	g2.draw(border);
    	
    	if (model.isSelected()) {
        	checkIcon.paintIcon(c, g, (int)(borderX+(borderWidth-checkIcon.getIconWidth())/2), (int)(borderY+(borderHeight-checkIcon.getIconHeight())/2));
    	}
   }
}
