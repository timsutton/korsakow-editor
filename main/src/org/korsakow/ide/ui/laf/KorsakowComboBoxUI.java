package org.korsakow.ide.ui.laf;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

import org.korsakow.ide.util.UIResourceManager;

import com.sun.java.swing.plaf.motif.MotifComboBoxUI;

public class KorsakowComboBoxUI extends MotifComboBoxUI
{
	private static Icon arrowIcon;
	
    public static ComponentUI createUI(JComponent c) {
        return new KorsakowComboBoxUI();
    }  
    
    @Override
	protected ComboPopup createPopup() {
        return new MyBasicComboPopup(comboBox);
    }
    
    @Override
	public void installUI(JComponent c) {
        super.installUI(c);
        if (arrowIcon == null) {
        	arrowIcon = UIResourceManager.getIcon("laf/combobox_arrow.png");
        }
        c.setOpaque(false);
        c.setBackground(UIManager.getColor("ComboBox.background"));
        c.putClientProperty("ui.background2", UIManager.getColor("ComboBox.background2"));
    }
    
    @Override
	public Insets getInsets() {
    	int arcWidth = UIManager.getInt("ComboBox.roundedCornerSize"); //KorsakowLookAndFeel.ROUNDED_CORNER_SIZE;
    	Insets insets = super.getInsets();
    	insets.left += arcWidth/2;
    	insets.top += 1;
    	return insets;
    }

    @Override
	public Dimension getMinimumSize(JComponent c) {
    	float strokeSize = UIManager.getInt("ComboBox.borderSize");
    	Dimension d = super.getMinimumSize(c);
    	d.height = 16;
    	d.height += strokeSize*2;
    	return d;
    }
    
    @Override
	public void paint(Graphics g, JComponent c) {
    	JComboBox comp = (JComboBox)c;
    	
        Color background2 = (Color)c.getClientProperty("ui.background2");
        
    	int arcWidth = UIManager.getInt("ComboBox.roundedCornerSize"); //KorsakowLookAndFeel.ROUNDED_CORNER_SIZE;
    	int arcHeight = UIManager.getInt("ComboBox.roundedCornerSize"); //KorsakowLookAndFeel.ROUNDED_CORNER_SIZE;
    	float strokeSize = UIManager.getInt("ComboBox.borderSize"); //KorsakowLookAndFeel.BORDER_STROKE_SIZE;
    	
    	int width = c.getWidth();
    	width -= 1; // rounded rect gets clipped slightly
    	int height = c.getHeight();
    	Rectangle bounds = c.getBounds();
    	Graphics2D g2 = (Graphics2D)g;
    	RoundRectangle2D.Float borderRect = new RoundRectangle2D.Float(strokeSize, strokeSize/2, width-strokeSize, height-strokeSize, arcWidth, arcHeight);
    	final int separatorX = width-15;
    	final int arrowLeft = width-15 + (15 - arrowIcon.getIconWidth())/2;
    	final int buttonWidth = width-separatorX;
		RoundRectangle2D.Float buttonRect1 = new RoundRectangle2D.Float(arrowLeft, strokeSize/2, buttonWidth, height-strokeSize, arcWidth, arcHeight);
    	Rectangle2D.Float buttonRect2 = new Rectangle2D.Float(separatorX+strokeSize, strokeSize/2, buttonWidth-arcWidth, height-strokeSize);
    	
    	// draw background
    	
//		if (!comp.isEditable()) {
			Paint paint = null;
//			paint = KorsakowLafUtil.createDefaultBackgroundGradient(height);
////			paint = UIManager.getColor("ComboBox.background");
//	    	g2.setPaint(paint);
//	    	g2.fill(borderRect);
//		} else {
//			Paint paint = null;
////			paint = KorsakowLafUtil.createDefaultBackgroundGradient(height);
//			paint = UIManager.getColor("TextField.background");
//	    	g2.setPaint(paint);
//	    	g2.fill(borderRect);
////			paintCurrentValue(g2, bounds, hasFocus);
//		}

    	// background
		paint = null;
		if (background2 == null)
			paint = c.getBackground();
		else
			paint = new GradientPaint(0, 0, c.getBackground(), 0, height, background2, false);
    	g2.setPaint(paint);
    	g2.fill(borderRect);

    	// button background
    	g2.setPaint(new GradientPaint(0, 0, UIManager.getColor("ComboBox.background"), 0, height, UIManager.getColor("ComboBox.background2"), false));
    			//UIManager.getColor("ComboBox.background"));
    	g2.fill(buttonRect1);
    	g2.fill(buttonRect2);
    	g2.setPaint(paint);

    	// draw separator
    	g2.setStroke(new BasicStroke(strokeSize));
    	g2.setColor(UIManager.getColor("ComboBox.borderColor"));
    	g2.fillRect(separatorX, 1, 2, height-1);

    	// draw arrow
		arrowIcon.paintIcon(c, g, arrowLeft, (15 - arrowIcon.getIconHeight())/2);

    	// draw border
    	g2.setStroke(new BasicStroke(strokeSize));
    	g2.setColor(UIManager.getColor("ComboBox.borderColor"));
    	borderRect.height-=1;
    	g2.draw(borderRect);
    	
        if ( !comboBox.isEditable() ) {
            Rectangle r = rectangleForCurrentValue();
            paintCurrentValue(g,r,hasFocus);
        }
        
        comp.getEditor().getEditorComponent().setBackground(c.getBackground());
        comp.getEditor().getEditorComponent().setForeground(c.getForeground());
		((JComponent)comp.getEditor().getEditorComponent()).putClientProperty("ui.background2", background2);
		comp.getEditor().getEditorComponent().repaint();
    }
    @Override
	public void paintCurrentValue(Graphics g,Rectangle bounds,boolean hasFocus)
    {
        ListCellRenderer renderer = comboBox.getRenderer();
        JComponent c;
        Dimension d;
        c = (JComponent)renderer.getListCellRendererComponent(listBox, comboBox.getSelectedItem(), -1, false, false);
        c.setFont(comboBox.getFont());
		if ( comboBox.isEnabled() ) {
            c.setForeground(comboBox.getForeground());
            c.setBackground(comboBox.getBackground());
            c.setOpaque(false);
		} else {
            c.setOpaque(false);
            c.setForeground(UIManager.getColor("ComboBox.disabledForeground"));
            c.setBackground(UIManager.getColor("ComboBox.disabledBackground"));
		}
        d  = c.getPreferredSize();
        currentValuePane.paintComponent(g,c,comboBox,bounds.x,(bounds.y),
                bounds.width,(d.height));
    }
    private static class MyBasicComboPopup extends BasicComboPopup
    {
		private static int scrollbarWidth = new JScrollBar().getPreferredSize().width;;
		public MyBasicComboPopup(JComboBox combo) {
			super(combo);
		}
	    @Override
		protected void configureList() {
	    	super.configureList();
			setForeground(Color.black);
	    }
	    @Override
		protected Rectangle computePopupBounds(int px,int py,int pw,int ph) {
	    	// we assume a reasonable metric is at least as big as the combobox itself
	    	// and no bigger than twice its size
	    	int w = Math.min(pw*2, getList().getPreferredSize().width);
	    	// TODO: actually determine whether the list will scroll and only add the extra width if necessary
			w += scrollbarWidth;
	    	pw = Math.max(w, pw);
	    	return super.computePopupBounds(px, py, pw, ph);
	    }
    }
}
