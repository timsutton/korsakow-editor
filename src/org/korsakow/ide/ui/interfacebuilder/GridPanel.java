/**
 * 
 */
package org.korsakow.ide.ui.interfacebuilder;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class GridPanel extends JPanel
{
	private int offsetX = 0;
	private int offsetY = 0;
	private int gridWidth = 100;
	private int gridHeight = 100;
	public GridPanel()
	{
		setForeground(Color.gray);
	}
	public void setGridSize(int gridWidth, int gridHeight)
	{
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
	}
	public int getGridWidth()
	{
		return gridWidth;
	}
	public int getGridHeight()
	{
		return gridHeight;
	}
	public int getOffsetX()
	{
		return offsetX;
	}
	public int getOffsetY()
	{
		return offsetY;
	}
	public void setOffset(int offsetX, int offsetY)
	{
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}
	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(getForeground());
		if (gridHeight > 0) {
			int rows = (int)Math.ceil(getHeight()/(float)gridHeight);
			for (int j = 0; j < rows+1; ++j)
			{
				g.drawLine(2, offsetY+j*gridHeight, getWidth()-2, offsetY+j*gridHeight);
			}
		}
		if (gridWidth > 0) {
			int cols = (int)Math.ceil(getWidth()/(float)gridWidth);
			for (int i = 0; i < cols+1; ++i)
			{
				g.drawLine(offsetX+i*gridWidth, 2, offsetX+i*gridWidth, getHeight()-2);
			}
		}
	}
}