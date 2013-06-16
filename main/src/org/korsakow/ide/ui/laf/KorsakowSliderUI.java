package org.korsakow.ide.ui.laf;

import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSliderUI;

import org.korsakow.ide.util.UIResourceManager;

public class KorsakowSliderUI extends BasicSliderUI
{
	public static final String SCROLL_INCREMENT = "scrollIncrement";
	private static Icon thumbIcon;
	
	public static ComponentUI createUI(JComponent b)    {
        return new KorsakowSliderUI((JSlider)b);
    }
    
    public KorsakowSliderUI(JSlider b) {
		super(b);
		if (thumbIcon == null){
			thumbIcon = UIResourceManager.getIcon("laf/slider_thumb.png");
		}
	}
    @Override
	public void installUI(JComponent c)   {
    	super.installUI(c);
    }

    @Override
	protected Dimension getThumbSize() {
    	return new Dimension(thumbIcon.getIconWidth(), thumbIcon.getIconHeight());
    }
    


    @Override
	public Dimension getMaximumSize(JComponent c)
    {
    	Dimension d = super.getMaximumSize(c);
        if ( slider.getOrientation() == JSlider.HORIZONTAL ) {
        	d.height = 15;
        	if (slider.getPaintTicks()) {
        		d.height += getHeightOfTallestLabel();
        	}
        } else {
        	d.width = 15;
        	if (slider.getPaintTicks())
        		d.width += getWidthOfWidestLabel();
        }
        
        return d;
    }
    @Override
	public Dimension getMinimumHorizontalSize() {
    	return getPreferredHorizontalSize();
    }
    public Dimension getMinimumVersicalSize() {
    	return getPreferredVerticalSize();
    }
    @Override
	public Dimension getPreferredHorizontalSize()
    {
    	Dimension d = super.getPreferredHorizontalSize();
    	d.height = 15;
    	return d;
    }
    @Override
	public Dimension getPreferredVerticalSize()
    {
    	Dimension d = super.getPreferredVerticalSize();
    	d.width = 15;
    	return d;
    }

    @Override
	public Dimension getPreferredSize(JComponent c)    {
        recalculateIfInsetsChanged();
        Dimension d;
        if ( slider.getOrientation() == JSlider.VERTICAL ) {
            d = new Dimension(getPreferredVerticalSize());
            d.width += tickRect.width + labelRect.width;
        }
        else {
            d = new Dimension(getPreferredHorizontalSize());
            d.height += tickRect.height + labelRect.height;
        }
        
        d.width += insetCache.left + insetCache.right;
        d.height += insetCache.top + insetCache.bottom;

        return d;
    }
    
    @Override
	public void paintThumb(Graphics g)  {
    	thumbIcon.paintIcon(slider, g, thumbRect.x, thumbRect.y);
    }
    
    @Override
	public void paintTrack(Graphics g)  {
    	Graphics2D g2 = (Graphics2D)g;
    	
        int cx, cy, cw, ch;
        int pad;

        Rectangle trackBounds = trackRect;
        

        if ( slider.getOrientation() == JSlider.HORIZONTAL ) {
            pad = 0;
            cx = 0;
            ch = 5;
            cy = (trackBounds.height-ch) / 2;
            cw = trackBounds.width;

            GradientPaint paint = new GradientPaint(0, 0, KorsakowLafUtil.createColorRGB(0xffffff), 0, trackBounds.height/2, KorsakowLafUtil.createColorRGB(0xc9c9c9), true);
        	g2.setPaint(paint);
            g2.fillRect(0, 0, slider.getWidth(), slider.getHeight());

            g.translate(trackBounds.x, trackBounds.y);

        	paint = new GradientPaint(0, 0, KorsakowLafUtil.createColorRGB(0xd6d6d6), 0, ch, KorsakowLafUtil.createColorRGB(0x717171), true);
        	RoundRectangle2D rect = new RoundRectangle2D.Float(cx+pad, cy, cw-pad*2, ch, 3, 3);
        	g2.setPaint(paint);
            g2.fill(rect);

            g.translate(-trackBounds.x, -(trackBounds.y));
        }
        else {
            pad = 0;
            cw = 5;
            cx = (trackBounds.width-cw) / 2;
            cy = 0;
            ch = trackBounds.height;

            GradientPaint paint = new GradientPaint(0, 0, KorsakowLafUtil.createColorRGB(0xffffff), trackBounds.width/2, 0, KorsakowLafUtil.createColorRGB(0xc9c9c9), true);
        	g2.setPaint(paint);
            g2.fill(trackBounds);
            
            g.translate(trackBounds.x, trackBounds.y);

        	paint = new GradientPaint(0, 0, KorsakowLafUtil.createColorRGB(0xd6d6d6), cw, 0, KorsakowLafUtil.createColorRGB(0x717171), true);
        	RoundRectangle2D rect = new RoundRectangle2D.Float(cx, cy+pad, cw, ch-pad*2, 3, 3);
        	g2.setPaint(paint);
            g2.fill(rect);

            g.translate(-(trackBounds.x), -trackBounds.y);
        }
    }
    
    @Override
	public void scrollByBlock(int direction) {
        synchronized(slider)    {

            int oldValue = slider.getValue();
            Integer blockIncrement = null;
            if ( slider.getClientProperty( SCROLL_INCREMENT ) != null ) {
            	try {
            		blockIncrement = Integer.parseInt( String.valueOf( slider.getClientProperty( SCROLL_INCREMENT ) ) );
            	} catch ( NumberFormatException e ) {
            		; //
            	}
            }
            if ( blockIncrement == null ) {
                blockIncrement = (slider.getMaximum() - slider.getMinimum()) / 10;
                if (blockIncrement <= 0 &&
                    slider.getMaximum() > slider.getMinimum()) {

                    blockIncrement = 1;
                }
            }

            int delta = blockIncrement * ((direction > 0) ? POSITIVE_SCROLL : NEGATIVE_SCROLL);
            slider.setValue(oldValue + delta);          
        }
    }
    @Override
	public void scrollByUnit(int direction) {
    	scrollByBlock( direction );
    }
}
