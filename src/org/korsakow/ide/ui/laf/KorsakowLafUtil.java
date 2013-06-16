package org.korsakow.ide.ui.laf;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;

public class KorsakowLafUtil
{
	private static HashMap<Integer, Color> colors = new HashMap<Integer, Color>();
	public static Color createColorRGB(Integer value)
	{
		Color c = colors.get(value);
		if (c == null) {
			c = new Color(value);
			colors.put(value, c);
		}
		return c;
	}
	
	/**
	 * 
	 * @param g must be a Graphics2D, Graphics taken for your convenience
	 * @param x is cast to int, float taken for your convenience
	 * @param y is cast to int, float taken for your convenience
	 * @param width is cast to int, float taken for your convenience
	 * @param height is cast to int, float taken for your convenience
	 * @param arcWidth is cast to int, float taken for your convenience
	 * @param arcHeight is cast to int, float taken for your convenience
	 */
	public static void drawTopRoundedRect(Graphics g, float x, float y, float width, float height, float arcWidth, float arcHeight)
	{
		Graphics2D g2 = (Graphics2D)g; // for convenience we do the casting here instead of requiring it in teh parameter
    	g2.drawArc((int)(x), (int)(y), (int)arcWidth, (int)(arcHeight*2), 90, 90); // top left
    	g2.drawArc((int)(x+width-arcWidth), (int)(y), (int)arcWidth, (int)arcHeight, 0, 90); // top right
    	g2.drawLine((int)(x+arcWidth/2), (int)(y), (int)(x+width-arcWidth/2), (int)y); // top
    	g2.drawLine((int)(x), (int)(y+arcHeight/2), (int)(x), (int)(y+height)); // left
    	g2.drawLine((int)(x+width), (int)(y+arcHeight/2), (int)(x+width), (int)(y+height)); // right
    	g2.drawLine((int)(x), (int)(y+height-1), (int)(x + width), (int)(y+height-1)); // bottom
	}
	/**
	 * 
	 * @param g must be a Graphics2D, Graphics taken for your convenience
	 * @param x is cast to int, float taken for your convenience
	 * @param y is cast to int, float taken for your convenience
	 * @param width is cast to int, float taken for your convenience
	 * @param height is cast to int, float taken for your convenience
	 * @param arcWidth is cast to int, float taken for your convenience
	 * @param arcHeight is cast to int, float taken for your convenience
	 */
	public static void fillTopRoundedRect(Graphics g, float x, float y, float width, float height, float arcWidth, float arcHeight)
	{
		Graphics2D g2 = (Graphics2D)g; // for convenience we do the casting here instead of requiring it in teh parameter
		RoundRectangle2D.Float rrect = new RoundRectangle2D.Float(x, y, width, height, arcWidth, arcHeight);
		g2.fill(rrect);
		Rectangle2D.Float srect = new Rectangle2D.Float(x, y+arcHeight, width, height-arcHeight);
		g2.fill(srect);
	}
	/**
	 * 
	 * @param g must be a Graphics2D, Graphics taken for your convenience
	 * @param x is cast to int, float taken for your convenience
	 * @param y is cast to int, float taken for your convenience
	 * @param width is cast to int, float taken for your convenience
	 * @param height is cast to int, float taken for your convenience
	 * @param arcWidth is cast to int, float taken for your convenience
	 * @param arcHeight is cast to int, float taken for your convenience
	 */
	public static void drawLeftRoundedRect(Graphics g, float x, float y, float width, float height, float arcWidth, float arcHeight)
	{
		Graphics2D g2 = (Graphics2D)g; // for convenience we do the casting here instead of requiring it in teh parameter
    	g2.drawArc((int)(x), (int)(y), (int)arcWidth, (int)(arcHeight*2), 90, 90); // top left
    	g2.drawArc((int)(x), (int)(y+height-arcHeight*2), (int)arcWidth, (int)(arcHeight*2), 180, 90); // bottom left
    	g2.drawLine((int)(x+arcWidth/2), (int)(y), (int)(x+width), (int)y); // top
    	g2.drawLine((int)(x), (int)(y+arcHeight/2), (int)(x), (int)(y+height-arcHeight/2)); // left
    	g2.drawLine((int)(x+width), (int)(y), (int)(x+width), (int)(y+height)); // right
    	g2.drawLine((int)(x+arcWidth/2), (int)(y+height), (int)(x+width), (int)(y+height)); // bottom
	}
	/**
	 * 
	 * @param g must be a Graphics2D, Graphics taken for your convenience
	 * @param x is cast to int, float taken for your convenience
	 * @param y is cast to int, float taken for your convenience
	 * @param width is cast to int, float taken for your convenience
	 * @param height is cast to int, float taken for your convenience
	 * @param arcWidth is cast to int, float taken for your convenience
	 * @param arcHeight is cast to int, float taken for your convenience
	 */
	public static void fillLeftRoundedRect(Graphics g, float x, float y, float width, float height, float arcWidth, float arcHeight)
	{
		Graphics2D g2 = (Graphics2D)g; // for convenience we do the casting here instead of requiring it in teh parameter
		RoundRectangle2D.Float rrect = new RoundRectangle2D.Float(x, y, width, height, arcWidth, arcHeight);
		g2.fill(rrect);
		Rectangle2D.Float srect = new Rectangle2D.Float(x+arcWidth, y, width-arcWidth, height);
		g2.fill(srect);
	}
}
