package org.korsakow.domain.proxy;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Collection;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.Interface;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.domain.mapper.input.InterfaceInputMapper;

public class InterfaceProxy extends ResourceProxy<Interface> implements IInterface {

	public InterfaceProxy(long id)
	{
		super(id);
	}

	@Override
	public Class<Interface> getInnerClass()
	{
		return Interface.class;
	}
	
	@Override
	protected Interface getFromMapper(Long id) throws MapperException {
		return InterfaceInputMapper.map(id);
	}

	public void setWidgets(Collection<IWidget> widgets)
	{
		getInnerObject().setWidgets(widgets);
	}
	public Collection<IWidget> getWidgets()
	{
		return getInnerObject().getWidgets();
	}
	public void setGridWidth(int gridWidth)
	{
		getInnerObject().setGridWidth(gridWidth);
	}
	public int getGridWidth()
	{
		return getInnerObject().getGridWidth();
	}
	public void setGridHeight(int gridHeight)
	{
		getInnerObject().setGridHeight(gridHeight);
	}
	public int getGridHeight()
	{
		return getInnerObject().getGridHeight();
	}
	public void setViewWidth(Integer viewWidth)
	{
		getInnerObject().setViewWidth(viewWidth);
	}
	public Integer getViewWidth()
	{
		return getInnerObject().getViewWidth();
	}
	public void setViewHeight(Integer gridHeight)
	{
		getInnerObject().setViewHeight(gridHeight);
	}
	public Integer getViewHeight()
	{
		return getInnerObject().getViewHeight();
	}
	public ISound getClickSound()
	{
		return getInnerObject().getClickSound();
	}
	public void setClickSound(ISound sound)
	{
		getInnerObject().setClickSound(sound);
	}
	public float getClickSoundVolume()
	{
		return getInnerObject().getClickSoundVolume();
	}
	public void setClickSoundVolume(float clickSoundVolume)
	{
		getInnerObject().setClickSoundVolume(clickSoundVolume);
	}
	public Rectangle getBounds()
	{
		return getInnerObject().getBounds();
	}
	public IImage getBackgroundImage()
	{
		return getInnerObject().getBackgroundImage();
	}
	public void setBackgroundImage(IImage backgroundImage)
	{
		getInnerObject().setBackgroundImage(backgroundImage);
	}
	public Color getBackgroundColor()
	{
		return getInnerObject().getBackgroundColor();
	}
	public void setBackgroundColor(Color backgroundColor)
	{
		getInnerObject().setBackgroundColor(backgroundColor);
	}
}
