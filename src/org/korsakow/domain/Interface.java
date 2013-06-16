package org.korsakow.domain;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.Collections;

import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.ide.resources.ResourceType;


public class Interface extends Resource implements IInterface
{
	private Collection<IWidget> widgets = Collections.EMPTY_LIST;
	private int gridWidth;
	private int gridHeight;
	private Integer viewWidth;
	private Integer viewHeight;
	private ISound clickSound;
	private float clickSoundVolume;
	private IImage backgroundImage;
	private Color backgroundColor;
	
	Interface(long id, long version, 
			String name, 
			Collection<IKeyword> keywords, 
			Collection<IWidget> widgets, 
			int gridWidth, int gridHeight, 
			Integer viewWidth, Integer viewHeight, 
			ISound clickSound, float clickSoundVolume, 
			IImage backgroundImage, Color backgroundColor)
	{
		super(id, version, name, keywords);
		setWidgets(widgets);
		setGridWidth(gridWidth);
		setGridHeight(gridHeight);
		setViewWidth(viewWidth);
		setViewHeight(viewHeight);
		setClickSound(clickSound);
		setClickSoundVolume(clickSoundVolume);
		setBackgroundColor(backgroundColor);
		setBackgroundImage(backgroundImage);
	}

	public String getType()
	{
		return ResourceType.INTERFACE.getTypeId();
	}
	public void setWidgets(Collection<IWidget> widgets)
	{
		this.widgets = widgets;
	}
	public Collection<IWidget> getWidgets()
	{
		return widgets;
	}
	public void setGridWidth(int gridWidth)
	{
		this.gridWidth = gridWidth;
	}
	public int getGridWidth()
	{
		return gridWidth;
	}
	public void setGridHeight(int gridHeight)
	{
		this.gridHeight = gridHeight;
	}
	public int getGridHeight()
	{
		return gridHeight;
	}
	public ISound getClickSound()
	{
		return clickSound;
	}
	public void setViewWidth(Integer width)
	{
		viewWidth = width;
	}
	public Integer getViewWidth()
	{
		return viewWidth;
	}
	public void setViewHeight(Integer height)
	{
		viewHeight = height;
	}
	public Integer getViewHeight()
	{
		return viewHeight;
	}
	public void setClickSound(ISound sound)
	{
		clickSound = sound;
	}
	public float getClickSoundVolume()
	{
		return clickSoundVolume;
	}
	public void setClickSoundVolume(float volume)
	{
		clickSoundVolume = volume;
	}
	public Rectangle getBounds()
	{
		Rectangle bounds = new Rectangle(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
		for (IWidget widget : getWidgets()) {
			bounds.x = Math.min(bounds.x, widget.getX());
			bounds.y = Math.min(bounds.y, widget.getY());
			bounds.width = Math.max(bounds.width, widget.getX()+widget.getWidth());
			bounds.height = Math.max(bounds.height, widget.getY()+widget.getHeight());
		}
		return bounds;
	}
	public IImage getBackgroundImage()
	{
		return backgroundImage;
	}
	public void setBackgroundImage(IImage backgroundImage)
	{
		this.backgroundImage = backgroundImage;
	}
	public Color getBackgroundColor()
	{
		return backgroundColor;
	}
	public void setBackgroundColor(Color backgroundColor)
	{
		this.backgroundColor = backgroundColor;
	}
}
