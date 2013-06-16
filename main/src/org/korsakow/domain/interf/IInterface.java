package org.korsakow.domain.interf;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Collection;


public interface IInterface extends IResource
{
	void setWidgets(Collection<IWidget> widgets);
	Collection<IWidget> getWidgets();
	void setGridWidth(int gridWidth);
	int getGridWidth();
	void setGridHeight(int gridHeight);
	int getGridHeight();
	void setViewWidth(Integer width);
	Integer getViewWidth();
	void setViewHeight(Integer height);
	Integer getViewHeight();
	ISound getClickSound();
	void setClickSound(ISound sound);
	float getClickSoundVolume();
	void setClickSoundVolume(float clickSoundVolume);

	void setBackgroundImage(IImage image);
	IImage getBackgroundImage();
	public Color getBackgroundColor();
	
	Rectangle getBounds();
}
