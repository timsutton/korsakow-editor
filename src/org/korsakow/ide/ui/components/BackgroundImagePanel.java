package org.korsakow.ide.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.korsakow.ide.ui.resourceexplorer.ResourceBrowser;


public class BackgroundImagePanel extends JPanel {
	
	protected boolean showBg = false;
	protected Image backgroundImage = null;
	
	public BackgroundImagePanel() { super(); }
	public BackgroundImagePanel( boolean isDoubleBuffered ) { super( isDoubleBuffered ); }
	public BackgroundImagePanel( LayoutManager layout ) { super( layout ); }
	public BackgroundImagePanel( LayoutManager layout, boolean isDoubleBuffered ) { super( layout, isDoubleBuffered ); }; 
	
	/**
	 * Sets the background image and shows it.
	 * 
	 * @param backgroundImage
	 */
	public void setImage ( Image backgroundImage )
	{
		this.backgroundImage = backgroundImage;
		showBackground(true);
	}
	
	/**
	 * 
	 * @return a direct reference to the image. Bad encapsulation.
	 */
	public Image getImage ()
	{
		return backgroundImage;
	}

	@SuppressWarnings("unused")
	public void showBackground ( boolean show )
	{
		showBg = ( show && backgroundImage != null); // can't show if background image not present
		this.repaint();
	}
	
	public boolean isBackgroundVisible ()
	{
		return showBg;
	}
	
	// example code from http://www.codeguru.com/java/articles/177.shtml
	public void paintComponent ( Graphics g )
	{
		super.paintComponent(g);
		
		if ( !showBg ) return;
		
//		Logger.getLogger(ResourceBrowser.class).info("show background");
		// First draw the background image - tiled 
		Dimension d = getSize();
		Color bgColor = getBackground();
		g.drawImage( backgroundImage, 
						(d.width - backgroundImage.getWidth(this))/2, 
						(d.height - backgroundImage.getHeight(this))/2, 
						bgColor, null);
	}
}
