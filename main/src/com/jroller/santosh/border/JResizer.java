package com.jroller.santosh.border;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

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
* 
*/
public class JResizer extends JPanel
{

   public JResizer(){
	   this(null, null);
   }

   public JResizer(Component comp){
	   this(comp, new DefaultResizableBorder(6, true));
   }
   
   public JResizer(Component comp, ResizableBorder border){
	   if (comp != null) {
		   setLayout(new BorderLayout());
		   add(comp);
	   }
	   setBorder(border);
   }

   @Override
public void setBorder(Border border){
	   removeMouseListener(resizeListener);
	   removeMouseMotionListener(resizeListener);
	   if(border instanceof ResizableBorder){
		   getMouseTarget().addMouseListener(resizeListener);
		   getMouseTarget().addMouseMotionListener(resizeListener);
	   }
	   super.setBorder(border);
   }

   public void didResized(){
	   if(getParent()!=null){
		   getParent().repaint();
		   invalidate();
		   ((JComponent)getParent()).revalidate();
	   }
   }
   private Component getMouseTarget()
   {
	   return this;
   }
   public void setResizeListener(MouseInputListener resizeListener)
   {
	   if (this.resizeListener != null) {
		   getMouseTarget().removeMouseListener(this.resizeListener);
		   getMouseTarget().removeMouseMotionListener(this.resizeListener);
	   }
	   this.resizeListener = resizeListener;
	   if (getBorder() instanceof ResizableBorder) {
		   getMouseTarget().addMouseListener(resizeListener);
		   getMouseTarget().addMouseMotionListener(resizeListener);
	   }
   }
   MouseInputListener resizeListener = new ResizeListener();
   
   public static class ResizeListener extends MouseInputAdapter
   {
	   @Override
	public void mouseMoved(MouseEvent me){
		   JResizer resizer = (JResizer)me.getComponent();
		   ResizableBorder border = (ResizableBorder)resizer.getBorder();
		   resizer.setCursor(Cursor.getPredefinedCursor(border.getResizeCursor(me)));
	   }

	   @Override
	public void mouseExited(MouseEvent mouseEvent){
		   JResizer resizer = (JResizer)mouseEvent.getComponent();
		   resizer.setCursor(Cursor.getDefaultCursor());
	   }

	   protected int cursor;
	   protected Point startPos = null;

	   @Override
	public void mousePressed(MouseEvent me){
		   JResizer resizer = (JResizer)me.getComponent();
		   if (resizer.getBorder() instanceof ResizableBorder) {
			   ResizableBorder border = (ResizableBorder)resizer.getBorder();
			   cursor = border.getResizeCursor(me);
			   startPos = me.getPoint();
		   }
	   }

	   @Override
	public void mouseDragged(MouseEvent me){
		   JResizer resizer = (JResizer)me.getComponent();
		   if(startPos!=null){
			   int dx = me.getX()-startPos.x;
			   int dy = me.getY()-startPos.y;
			   switch(cursor){
				   case Cursor.N_RESIZE_CURSOR:
					   resizer.setBounds(resizer.getX(), resizer.getY()+dy, resizer.getWidth(), resizer.getHeight()-dy);
					   resizer.didResized();
					   break;
				   case Cursor.S_RESIZE_CURSOR:
					   resizer.setBounds(resizer.getX(), resizer.getY(), resizer.getWidth(), resizer.getHeight()+dy);
					   startPos = me.getPoint();
					   resizer.didResized();
					   break;
				   case Cursor.W_RESIZE_CURSOR:
					  resizer.setBounds(resizer.getX()+dx, resizer.getY(), resizer.getWidth()-dx, resizer.getHeight());
					  resizer.didResized();
					  break;
				  case Cursor.E_RESIZE_CURSOR:
					  resizer.setBounds(resizer.getX(), resizer.getY(), resizer.getWidth()+dx, resizer.getHeight());
					  startPos = me.getPoint();
					  resizer.didResized();
					  break;
				  case Cursor.NW_RESIZE_CURSOR:
					  resizer.setBounds(resizer.getX()+dx, resizer.getY()+dy, resizer.getWidth()-dx, resizer.getHeight()-dy);
					  resizer.didResized();
					  break;
				  case Cursor.NE_RESIZE_CURSOR:
					  resizer.setBounds(resizer.getX(), resizer.getY()+dy, resizer.getWidth()+dx, resizer.getHeight()-dy);
					  startPos = new Point(me.getX(), startPos.y);
					  resizer.didResized();
					  break;
				  case Cursor.SW_RESIZE_CURSOR:
					  resizer.setBounds(resizer.getX()+dx, resizer.getY(), resizer.getWidth()-dx, resizer.getHeight()+dy);
					  startPos = new Point(startPos.x, me.getY());
					  resizer.didResized();
					  break;
				  case Cursor.SE_RESIZE_CURSOR:
					  resizer.setBounds(resizer.getX(), resizer.getY(), resizer.getWidth()+dx, resizer.getHeight()+dy);
					  startPos = me.getPoint();
					  resizer.didResized();
					  break;
				  case Cursor.MOVE_CURSOR:
					  Rectangle bounds = resizer.getBounds();
					  bounds.translate(dx, dy);
					  resizer.setBounds(bounds);
					  resizer.didResized();
			  }

			  // cursor shouldn't change while dragging
			   resizer.setCursor(Cursor.getPredefinedCursor(cursor));
		  }
	  }

	  @Override
	public void mouseReleased(MouseEvent mouseEvent){
		  startPos = null;
	  }
  }
}
