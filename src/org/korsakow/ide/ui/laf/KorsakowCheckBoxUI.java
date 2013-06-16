package org.korsakow.ide.ui.laf;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

import org.korsakow.ide.util.UIResourceManager;

import sun.swing.SwingUtilities2;

import com.sun.java.swing.plaf.motif.MotifCheckBoxUI;



public class KorsakowCheckBoxUI extends MotifCheckBoxUI
{
	private static final int CHECK_HEIGHT = 12;
	private static final int CHECK_WIDTH = 12;
	private static final int TEXT_GAP = 5;
	private static Icon checkIcon = null;

	@Override
	public void installUI(JComponent c) {
        super.installUI(c);
        if (checkIcon == null) {
        	checkIcon = UIResourceManager.getIcon("laf/checkbox_check.png");
        }
//        arrowIcon = new MotifComboBoxArrowIcon(UIManager.getColor("controlHighlight"),
    }
	
    public static ComponentUI createUI(JComponent c){
    	c.setOpaque(false);
    	return new KorsakowCheckBoxUI();
    }
    
    @Override
	public Dimension getPreferredSize(JComponent c) {
    	JCheckBox check = (JCheckBox) c;
    	final Dimension d = new Dimension(CHECK_WIDTH, CHECK_HEIGHT);
    	if (check.getIcon() != null) {
    		d.width += check.getIcon().getIconWidth();
    		d.height = Math.max(d.height, check.getIcon().getIconWidth());
    	}
    	if (!check.getText().isEmpty()) {
    		d.width += check.getIconTextGap();
	    	d.width += SwingUtilities2.stringWidth(check, check.getFontMetrics(check.getFont()), check.getText());
	    	d.height = Math.max(d.height, check.getFontMetrics(check.getFont()).getHeight());
	    	d.width += TEXT_GAP;
    	}
		return d;
    }
    
    // TODO: why is the super synchronized??!?!
    @Override
	public synchronized void paint(Graphics g, JComponent c) {
    	JCheckBox check = (JCheckBox) c;
    	ButtonModel model = check.getModel();

    	float strokeSize = UIManager.getInt("CheckBox.borderSize"); //KorsakowLookAndFeel.BORDER_STROKE_SIZE;
    	
    	Graphics2D g2 = (Graphics2D)g;
    	Rectangle clipRect = g.getClipBounds();
    	
    	
    	int checkOffsetX = 0;
    	int textOffsetX = CHECK_WIDTH+2;
    	final int textWidth = clipRect.width-CHECK_WIDTH-2;
    	
    	if (check.getHorizontalTextPosition() == JCheckBox.LEFT) {
    		checkOffsetX = textWidth;
    		textOffsetX = 0;
    	} else {
    	}
    	
    	
    	int boxX = checkOffsetX;
    	int boxY = (clipRect.height-CHECK_HEIGHT)/2-0*CHECK_HEIGHT/2;
    	Rectangle2D.Float borderRect = new Rectangle2D.Float(boxX+strokeSize/2, boxY+strokeSize/2, CHECK_WIDTH-strokeSize-strokeSize/2, CHECK_HEIGHT-strokeSize-strokeSize/2);
    	
    	// draw background
    	Paint paint = null;
		paint = new GradientPaint(boxX, boxY, UIManager.getColor("CheckBox.background"), boxX, boxY+CHECK_HEIGHT, UIManager.getColor("CheckBox.background2"), false);
    	g2.setPaint(paint);
    	g2.fill(borderRect);
    	

    	// draw border
    	g2.setStroke(new BasicStroke(strokeSize));
    	g2.setColor(UIManager.getColor("CheckBox.borderColor"));
    	g2.draw(borderRect);
    	if (model.isSelected()) {
        	checkIcon.paintIcon(c, g, checkOffsetX + (CHECK_WIDTH-checkIcon.getIconWidth())/2+1, (CHECK_HEIGHT-checkIcon.getIconHeight())/2);
    	}
    	
    	if (check.getIcon() != null)
    		check.getIcon().paintIcon(check, g2, CHECK_WIDTH, (clipRect.height-check.getIcon().getIconHeight())/2);

        String text = check.getText();
        if (text != null && !text.equals("")) {
        	
			Rectangle textRect = new Rectangle(textOffsetX, 0, textWidth, clipRect.height);
        	if (check.getIcon() != null)
        		textRect.x += check.getIcon().getIconWidth() + check.getIconTextGap();
	    	paintText(g, check, textRect, text);
        }
    }
}








