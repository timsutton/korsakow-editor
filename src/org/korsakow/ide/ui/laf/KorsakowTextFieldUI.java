package org.korsakow.ide.ui.laf;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

import com.sun.java.swing.plaf.motif.MotifTextFieldUI;

public class KorsakowTextFieldUI extends MotifTextFieldUI
{
	
	private JTextField currentPaintComponent;
	
    @Override
	public void installUI(JComponent c) {
        super.installUI(c);
        c.setBackground(UIManager.getColor("TextField.background"));
//        c.putClientProperty("ui.background2", UIManager.getColor("TextField.background2"));
        ((JTextField)c).setMargin(new Insets(0, 2, 0, 2));
    }
    
    @Override
	protected Rectangle getVisibleEditorRect() {
    	Rectangle bounds = super.getVisibleEditorRect();
    	// i was hoping that the margin property would actually get used by the LookAndFeel but.... nope! gotta do it ourselves.
    	Insets insets = UIManager.getInsets(getPropertyPrefix() + ".margin");
    	if (bounds != null && insets != null) {
	    	bounds.x += insets.left;
	    	bounds.y += insets.top;
	    	bounds.width -= insets.left + insets.right;
	    	bounds.height -= insets.top + insets.bottom;
    	}
    	return bounds;
    }
    
    public static ComponentUI createUI(JComponent c) {
        return new KorsakowTextFieldUI();
    }
    
    @Override
	public void update(Graphics g, JComponent c) {
    	currentPaintComponent = (JTextField)c;
    	super.update(g, c);
    }

    @Override
	protected void paintBackground(Graphics g) {
    	int arcWidth = UIManager.getInt("TextField.roundedCornerSize"); //KorsakowLookAndFeel.ROUNDED_CORNER_SIZE;
    	int arcHeight = UIManager.getInt("TextField.roundedCornerSize"); //KorsakowLookAndFeel.ROUNDED_CORNER_SIZE;
    	float strokeSize = UIManager.getInt("TextField.borderSize"); //KorsakowLookAndFeel.BORDER_STROKE_SIZE;
    	
    	Graphics2D g2 = (Graphics2D)g;
    	Rectangle clipRect = g.getClipBounds();
    	
    	int width = clipRect.width;
    	int height = clipRect.height;
    	RoundRectangle2D.Float borderRect = new RoundRectangle2D.Float(strokeSize/2, strokeSize/2, width-strokeSize, height-strokeSize, arcWidth, arcHeight);

    	// draw background
    	Paint paint = null;
    	paint = UIManager.getColor("TextField.background");
    	if (currentPaintComponent.isEditable()) {
//	    	paint = Color.white;
    		Color background = currentPaintComponent.getBackground();
    		Color background2 = (Color)currentPaintComponent.getClientProperty("ui.background2");
    		if (background2 == null)
    			background2 = background;
    		paint = new GradientPaint(0, 0, background, 0, height, background2, false);
    	} else {
    		paint = new GradientPaint(0, 0, UIManager.getColor("TextField.inactiveBackground"), 0, height, UIManager.getColor("TextField.inactiveBackground2"), false);
    	}
//    	paint = Color.white
    	g2.setPaint(paint);
    	g2.fill(clipRect);
    	
    	// draw border
//    	g2.setStroke(new BasicStroke(strokeSize));
//    	g2.setColor(KorsakowLafUtil.createColorRGB(0x747474));
//    	g2.draw(borderRect);
   }
}
