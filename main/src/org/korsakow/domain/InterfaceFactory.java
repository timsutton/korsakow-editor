package org.korsakow.domain;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.interf.IImage;
import org.korsakow.domain.interf.IInterface;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.ISound;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.ide.DataRegistry;

public class InterfaceFactory {
	public static Interface createNew(String name, Collection<IKeyword> keywords, Collection<IWidget> widgets, int gridWidth, int gridHeight, Integer viewWidth, Integer viewHeight, ISound clickSound, float clickSoundVolume, IImage backgroundImage, Color backgroundColor)
	{
		Interface object = new Interface(DataRegistry.getMaxId(), 0, name, keywords, widgets, gridWidth, gridHeight, viewWidth, viewHeight, clickSound, clickSoundVolume, backgroundImage, backgroundColor);
		UoW.getCurrent().registerNew(object);
		return object;
	}
	public static Interface createNew()
	{
		Interface object = new Interface(DataRegistry.getMaxId(), 0, "", new ArrayList<IKeyword>(), new ArrayList<IWidget>(), 0, 0, null, null, null, 1.0F, null, null);
		UoW.getCurrent().registerNew(object);
		return object;
	}
	public static Interface createClean(long id, long version, String name, Collection<IKeyword> keywords, Collection<IWidget> widgets, int gridWidth, int gridHeight, Integer viewWidth, Integer viewHeight, ISound clickSound, float clickSoundVolume, IImage backgroundImage, Color backgroundColor)
	{
		Interface object = new Interface(id, version, name, keywords, widgets, gridWidth, gridHeight, viewWidth, viewHeight, clickSound, clickSoundVolume, backgroundImage, backgroundColor);
		UoW.getCurrent().registerClean(object);
		return object;
	}
	public static Interface createClean(String name, Collection<IKeyword> keywords, Collection<IWidget> widgets, int gridWidth, int gridHeight)
	{
		return createClean(DataRegistry.getMaxId(), 0, name, keywords, widgets, gridWidth, gridHeight, null, null, null, 1, null, null);
	}
	public static Interface copy(IInterface src)
	{
		Interface copy = createNew(
				src.getName(),
				KeywordFactory.copy(src.getKeywords()),
				WidgetFactory.copy(src.getWidgets()),
				src.getGridWidth(),
				src.getGridHeight(),
				src.getViewWidth(),
				src.getViewHeight(),
				src.getClickSound(),
				src.getClickSoundVolume(),
				src.getBackgroundImage(),
				src.getBackgroundColor()
		);
		return copy;
	}
}
