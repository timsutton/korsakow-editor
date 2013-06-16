package org.korsakow.domain.interf;

import org.korsakow.ide.resources.widget.WidgetPersistAction;
import org.korsakow.ide.resources.widget.WidgetPersistCondition;

public interface IWidget extends IResource, IDynamicProperties
{
	void setWidgetId(String widgetId);
	String getWidgetId();
	void setPersistCondition(WidgetPersistCondition condition);
	WidgetPersistCondition getPersistCondition();
	void setPersistAction(WidgetPersistAction action);
	WidgetPersistAction getPersistAction();
	void setX(int x);
	int getX();
	void setY(int y);
	int getY();
	void setWidth(int width);
	int getWidth();
	void setHeight(int height);
	int getHeight();
}
