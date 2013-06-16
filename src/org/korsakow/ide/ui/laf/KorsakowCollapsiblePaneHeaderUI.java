package org.korsakow.ide.ui.laf;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

import org.korsakow.ide.ui.components.KCollapsiblePane;
import org.korsakow.ide.util.UIResourceManager;

public class KorsakowCollapsiblePaneHeaderUI extends BasicButtonUI
{
	private static final Icon arrowExpandedIcon = UIResourceManager.getIcon("arrow_down.png");
	private static final Icon arrowCollapsedIcon = UIResourceManager.getIcon("arrow_left.png");

    // Shared UI object
    private final static KorsakowCollapsiblePaneHeaderUI sharedUI = new KorsakowCollapsiblePaneHeaderUI();
    
    public static ComponentUI createUI(JComponent c) {
        return sharedUI;
    }
    
	public void installUI(JComponent c) {
		super.installUI(c);
		c.setOpaque(false);
		((AbstractButton)c).setRolloverEnabled(true);
	}
//    public void
    public void installDefaults(AbstractButton b) {
    	super.installDefaults(b);
    	Insets insets = UIManager.getInsets("CollapsiblePaneHeader.margin");
    	b.setBorder(null); // aqua does this. perhaps other too. this happens early enough that it should not interfere with borders set in user code
    	b.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right)); // cant get margins to work!
    	b.setVerticalAlignment(SwingConstants.BOTTOM);
    	b.setHorizontalAlignment(SwingConstants.LEFT);
//    	b.setMargin(UIManager.getInsets("CollapsiblePaneHeader.margin"));
    }
//	contentPane.setBackground(UIManager.getColor("CollapsiblePane.background"));

    
    public void paint(Graphics g, JComponent c) {
    	if (Boolean.FALSE.equals(c.getClientProperty("roundedCorners")))
    		paintSquare(g, c);
    	else
    		paintRounded(g, c);

    	KCollapsiblePane pane = (KCollapsiblePane)c.getClientProperty(KCollapsiblePane.HEADER_UI_PROPERTY_OWNER_PANE);
    	if (pane != null)
    	{
    		Rectangle bounds = c.getBounds();
	    	Icon arrowIcon = pane.isExpanded()?arrowExpandedIcon:arrowCollapsedIcon;
	    	arrowIcon.paintIcon(c, g, bounds.width-arrowIcon.getIconWidth(), (bounds.height - arrowIcon.getIconWidth())/2);
    	}
    }
    public void paintSquare(Graphics g, JComponent c) {
    	AbstractButton button = (AbstractButton)c;
    	button.getInsets();
    	float strokeSize = UIManager.getInt("CollapsiblePaneHeader.borderSize"); //KorsakowLookAndFeel.BORDER_STROKE_SIZE;
    	int width = c.getWidth();
    	width -= 1; // rounded rect gets clipped slightly
    	int height = c.getHeight();
    	
    	Rectangle bounds = c.getBounds();
    	Graphics2D g2 = (Graphics2D)g;
    	
    	// background
    	// background comes after to cover the crossign lines of the two border rects
		GradientPaint paint = new GradientPaint(0, 0, UIManager.getColor("CollapsiblePaneHeader.background"), 0, height, UIManager.getColor("CollapsiblePaneHeader.background2"), false);
		boolean isOver = button.getMousePosition(true) != null;
		if (isOver)
			paint = new GradientPaint(0, 0, UIManager.getColor("CollapsiblePaneHeader.activeBackground"), 0, height, UIManager.getColor("CollapsiblePaneHeader.activeBackground2"), false);;
    	g2.setPaint(paint);
    	g2.fill(bounds);
    	    	
    	// border
    	g2.setStroke(new BasicStroke(strokeSize));
    	g2.setColor(UIManager.getColor("CollapsiblePaneHeader.border"));
    	g2.draw(bounds);
    	
    	super.paint(g2, c);
    }

    public void paintRounded(Graphics g, JComponent c) {
    	AbstractButton button = (AbstractButton)c;
    	button.getInsets();
    	int arcWidth = UIManager.getInt("CollapsiblePaneHeader.roundedCornerSize"); //KorsakowLookAndFeel.ROUNDED_CORNER_SIZE;
    	int arcHeight = UIManager.getInt("CollapsiblePaneHeader.roundedCornerSize"); //KorsakowLookAndFeel.ROUNDED_CORNER_SIZE;
    	float strokeSize = UIManager.getInt("CollapsiblePaneHeader.borderSize"); //KorsakowLookAndFeel.BORDER_STROKE_SIZE;
    	int width = c.getWidth();
    	width -= 1; // rounded rect gets clipped slightly
    	int height = c.getHeight();
    	
    	Rectangle bounds = c.getBounds();
    	Graphics2D g2 = (Graphics2D)g;
    	
    	RoundRectangle2D.Float borderTopRect = new RoundRectangle2D.Float(strokeSize/2, strokeSize/2, width-strokeSize, height-strokeSize, arcWidth, arcHeight);
    	Rectangle2D.Float borderBottomRect = new Rectangle2D.Float(strokeSize/2, height/2, width, height/2);

    	// background
    	// background comes after to cover the crossign lines of the two border rects
		GradientPaint paint = new GradientPaint(0, 0, UIManager.getColor("CollapsiblePaneHeader.background"), 0, height, UIManager.getColor("CollapsiblePaneHeader.background2"), false);
		boolean isOver = button.getMousePosition(true) != null;
		if (isOver)
			paint = new GradientPaint(0, 0, UIManager.getColor("CollapsiblePaneHeader.activeBackground"), 0, height, UIManager.getColor("CollapsiblePaneHeader.activeBackground2"), false);;
    	g2.setPaint(paint);
    	g2.fill(borderTopRect);
    	g2.fill(borderBottomRect);
    	    	
    	// border
    	g2.setStroke(new BasicStroke(strokeSize));
    	g2.setColor(UIManager.getColor("CollapsiblePaneHeader.borderColor"));
//    	g2.setColor(Color.red);
    	KorsakowLafUtil.drawTopRoundedRect(g2, 0, 0, width, height, arcWidth, arcHeight);
//    	g2.drawArc((int)(borderTopRect.x), (int)(borderTopRect.y), arcWidth, arcHeight*2, 90, 90); // top left
//    	g2.drawArc((int)(borderTopRect.x+borderTopRect.width-arcWidth), (int)(borderTopRect.y), arcWidth, arcHeight, 0, 90); // top right
//    	g2.drawLine((int)(borderTopRect.x+arcWidth/2), (int)(borderTopRect.y), (int)(borderTopRect.width-arcWidth/2), (int)borderTopRect.y); // top
//    	g2.drawLine((int)(borderTopRect.x), (int)(borderTopRect.y+arcHeight/2), (int)(borderTopRect.x), (int)(borderBottomRect.y+borderBottomRect.height)); // left
//    	g2.drawLine((int)(borderTopRect.x+borderTopRect.width), (int)(borderTopRect.y+arcHeight/2), (int)(borderTopRect.x+borderTopRect.width), (int)(borderBottomRect.y+borderBottomRect.height)); // right
//    	g2.drawLine((int)(borderTopRect.x), (int)(borderBottomRect.y+borderBottomRect.height-1), (int)(borderTopRect.x + borderTopRect.width), (int)(borderBottomRect.y+borderBottomRect.height-1)); // bottom
//    	g2.draw(borderTopRect);
//    	g2.draw(borderBottomRect);
    	
    	super.paint(g2, c);
    }
}
