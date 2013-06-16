package org.korsakow.ide.dnd;

import java.awt.Point;

public class DropLocation
{
	private Point point;
	public DropLocation(String name, Point point)
	{
		this.point = point;
	}
	public Point getPoint()
	{
		return point;
	}
}
