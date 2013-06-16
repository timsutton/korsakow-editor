package org.korsakow.ide.ui.factory;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.korsakow.ide.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DomLayout implements LayoutManager
{
	private final Document document;
	private final DomHelper helper;

	/**
	 * An absolute layout based on the dom.
	 * @param document
	 */
	public DomLayout(Document document)
	{
		this.document = document;
		helper = new DomHelper(document);
	}
	

	/**
	 * Purely for debugging purposes
	 * @param comp
	 */
	private void defaultLayoutComponent(Component comp)
	{
//		comp.setLocation((int)(Math.random()*800), (int)(Math.random()*600));
//		comp.setSize(comp.getPreferredSize());
	}
	private Rectangle getComponentBounds(String name, Rectangle rect) throws XPathExpressionException
	{
		Element element = helper.xpathAsElement("/layout/component[id=?]", name);
		if (element == null) {
			Logger.getLogger(DomLayout.class).debug("Layout not found for '" + name + "'");
//			System.out.println(name);
			return rect;
		}
		Integer x = getInt(element, "x", rect.x);
		Integer y = getInt(element, "y", rect.y);
		Integer width = getInt(element, "width", rect.width);
		Integer height = getInt(element, "height", rect.height);
		rect.x = x;
		rect.y = y;
		rect.width = width;
		rect.height = height;
		return rect;
	}
	private void layoutComponent(Component comp) throws XPathExpressionException
	{
		String name = comp.getName();
		if (name != null) {
			Rectangle bounds = comp.getBounds();
			Dimension d = comp.getPreferredSize();
			bounds.width = d.width;
			bounds.height = d.height;
			if (name.equals("livesComboBox"))
				name=name+"";
			bounds = getComponentBounds(name, bounds);
			comp.setBounds(bounds);
		}
	}
	private Integer getInt(Element element, String name, Integer defaultValue)
	{
		Integer value = helper.getInt(element, name);
		return value==null?defaultValue:value;
	}
	
	public void addLayoutComponent(String name, Component comp) {
	}
	public void layoutContainer(Container parent) {
		for (int i = 0; i < parent.getComponentCount(); ++i)
		{
			Component comp = parent.getComponent(i);
			try {
				layoutComponent(comp);
			} catch (XPathExpressionException e) {
				Logger.getLogger(DomLayout.class).error("", e);
			} catch (NumberFormatException e) {
				Logger.getLogger(DomLayout.class).error("", e);
			}
		}
	}
	public Dimension minimumLayoutSize(Container parent) {
		Dimension d = preferredLayoutSize(parent);
		return d;
	}
	public Dimension preferredLayoutSize(Container parent) {
		try {
			int width = helper.xpathAsInt("/layout/attribute::width");
			int height = helper.xpathAsInt("/layout/attribute::height");
			return new Dimension(width, height);
		} catch (Exception e) {
			// just dont return
		}
		
		int paddingLeft = 0;
		int paddingRight = 0;
		int paddingBottom = 0;
		int paddingTop = 0;
		try { paddingLeft   = helper.xpathAsInt("/layout/@paddingLeft"); } catch (Exception e) {} 
		try { paddingRight  = helper.xpathAsInt("/layout/@paddingRight"); } catch (Exception e) {} 
		try { paddingTop    = helper.xpathAsInt("/layout/@paddingTop"); } catch (Exception e) {} 
		try { paddingBottom = helper.xpathAsInt("/layout/@paddingBottom"); } catch (Exception e) {} 
		
		int left = Integer.MAX_VALUE;
		int right = Integer.MIN_VALUE;
		int top = Integer.MAX_VALUE;
		int bottom = Integer.MIN_VALUE;
		Rectangle bounds = new Rectangle();
		for (int i = 0; i < parent.getComponentCount(); ++i)
		{
			Component comp = parent.getComponent(i);
			String name = comp.getName();
			if (name == null)
				continue;
			try {
				bounds = getComponentBounds(name, comp.getBounds(bounds));
			} catch (XPathExpressionException e) {
				continue;
			}
			if (bounds.x < left)
				left = bounds.x;
			if (bounds.y < top)
				top = bounds.y;
			if (bounds.x + bounds.width > right)
				right = bounds.x + bounds.width;
			if (bounds.y + bounds.height > bottom)
				bottom = bounds.y + bounds.height;
		}
		return new Dimension(right + paddingLeft+paddingRight, bottom + paddingTop+paddingBottom);
	}
	public void removeLayoutComponent(Component comp) {
	}
}


/*

<layout>
	<component>
		<id />
		<x />
		<y />
		<width />
		<height />
	</component>
</layout>

*/
