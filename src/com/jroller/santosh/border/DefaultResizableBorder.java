package com.jroller.santosh.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.SwingConstants;

/**
* MySwing: Advanced Swing Utilites
* Copyright (C) 2005  Santhosh Kumar T
* <p/>
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
* <p/>
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
* Lesser General Public License for more details.
*
* @author Santhosh Kumar T
* 
* David Reisch Modified this class a little.
*/
public class DefaultResizableBorder implements ResizableBorder
{
   private final int dist;
   private final boolean isResizable;

   int locations[] = {
		   SwingConstants.NORTH,
		   SwingConstants.SOUTH,
		   SwingConstants.WEST,
		   SwingConstants.EAST,
		   SwingConstants.NORTH_WEST,
		   SwingConstants.NORTH_EAST,
		   SwingConstants.SOUTH_WEST,
		   SwingConstants.SOUTH_EAST,
		   0, // move
		   -1, // no location
	   };

   int cursors[] = {
		   Cursor.N_RESIZE_CURSOR,
		   Cursor.S_RESIZE_CURSOR,
		   Cursor.W_RESIZE_CURSOR,
		   Cursor.E_RESIZE_CURSOR,
		   Cursor.NW_RESIZE_CURSOR,
		   Cursor.NE_RESIZE_CURSOR,
		   Cursor.SW_RESIZE_CURSOR,
		   Cursor.SE_RESIZE_CURSOR,
//		   Cursor.MOVE_CURSOR,
		   Cursor.DEFAULT_CURSOR,
   };

   public DefaultResizableBorder(int dist, boolean isResizable){
	   this.dist = dist;
	   this.isResizable = isResizable;
   }

   public Insets getBorderInsets(Component component){
	   return new Insets(1, 1, 1, 1);
   }

   public boolean isBorderOpaque(){
	   return false;
   }

   public void paintBorder(Component component, Graphics g, int x, int y, int w, int h){
	   paintOutline(g, x, y, w, h);
	   if (isResizable)
		   paintCorners(g, x, y, w, h);
   }
	protected void paintOutline(Graphics g, int x, int y, int w, int h)
	{
		g.setColor(Color.red);
		g.drawRect(x, y, w-1, h-1);
	}
	protected void paintCorners(Graphics g, int x, int y, int w, int h)
	{
		for(int i=0; i<locations.length-2; i++){
			   Rectangle rect = getRectangle(x, y, w, h, locations[i]);
			   g.setColor(Color.WHITE);
			   g.fillRect(rect.x, rect.y, rect.width-1, rect.height-1);
			   g.setColor(Color.BLACK);
			   g.drawRect(rect.x, rect.y, rect.width-1, rect.height-1);
		   }
	}

   private Rectangle getRectangle(int x, int y, int w, int h, int location){
	   switch(location){
		   case SwingConstants.NORTH:
			   return new Rectangle(x+w/2-dist/2, y, dist, dist);
		   case SwingConstants.SOUTH:
			   return new Rectangle(x+w/2-dist/2, y+h-dist, dist, dist);
		   case SwingConstants.WEST:
			   return new Rectangle(x, y+h/2-dist/2, dist, dist);
		   case SwingConstants.EAST:
			   return new Rectangle(x+w-dist, y+h/2-dist/2, dist, dist);
		   case SwingConstants.NORTH_WEST:
			   return new Rectangle(x, y, dist, dist);
		   case SwingConstants.NORTH_EAST:
			  return new Rectangle(x+w-dist, y, dist, dist);
		  case SwingConstants.SOUTH_WEST:
			  return new Rectangle(x, y+h-dist, dist, dist);
		  case SwingConstants.SOUTH_EAST:
			  return new Rectangle(x+w-dist, y+h-dist, dist, dist);
	  }
	  return null;
  }

  public int getResizeCursor(MouseEvent me){
	  Component comp = me.getComponent();
	  int w = comp.getWidth();
	  int h = comp.getHeight();

	  Rectangle bounds = new Rectangle(0, 0, w, h);

	  if(!bounds.contains(me.getPoint()))
		  return Cursor.DEFAULT_CURSOR;

	  Rectangle actualBounds = new Rectangle(dist, dist, w-2*dist, h-2*dist);
	  if(actualBounds.contains(me.getPoint()))
		  return Cursor.MOVE_CURSOR;

	  if (isResizable)
		  for(int i=0; i<locations.length-2; i++){
			  Rectangle rect = getRectangle(0, 0, w, h, locations[i]);
			  if(rect.contains(me.getPoint()))
				  return cursors[i];
		  }

	  return Cursor.MOVE_CURSOR;
  }
}
