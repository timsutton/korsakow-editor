/**
 * 
 */
package org.korsakow.ide.resources.widget;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JPanel;

public abstract class WidgetComponent extends JPanel
{
	private final WidgetModel widget;
	public WidgetComponent(WidgetModel widget)
	{
		this.widget = widget;
		initUI();
		initListeners();
	}
	public WidgetModel getWidget()
	{
		return widget;
	}
    @Override
	public void reshape(int x, int y, int w, int h)
    {
    	if (x != getX()) firePropertyChange("x", getX(), x);
    	if (y != getY()) firePropertyChange("y", getY(), y);
    	if (w != getWidth()) firePropertyChange("width", getWidth(), w);
    	if (h != getHeight()) firePropertyChange("height", getHeight(), h);
    	super.reshape(x, y, w, h);
    }
    public JPanel getContentPane()
    {
    	return this;
    }
    public boolean isResizable()
    {
    	return true;
    }
    public boolean getMaintainsAspectByDefaultWhenResized()
    {
    	return true;
    }
	protected void initUI()
	{
		setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		setOpaque(true);
		setBackground(new Color(0.8f, 0.8f, 1.0f));
	}
	protected void initListeners() {}
    @Override
	protected void paintChildren(Graphics g) {
    	super.paintChildren(g);
    	super.paintBorder(g);
    }
}