package org.korsakow.domain;

import java.util.Collection;
import java.util.HashMap;

import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.ide.resources.ResourceType;
import org.korsakow.ide.resources.widget.WidgetPersistAction;
import org.korsakow.ide.resources.widget.WidgetPersistCondition;

public class Widget extends Resource implements IWidget
{
	private final HashMap<String, Object> abstractProperties = new HashMap<String, Object>();
	private String widgetId;
	private WidgetPersistCondition persistCondition = WidgetPersistCondition.Never;
	private WidgetPersistAction persistAction = WidgetPersistAction.Replace;
	private int x;
	private int y;
	private int width;
	private int height;
	Widget(long id, long version)
	{
		super(id, version);
	}
	Widget(long id, long version, String name, Collection<IKeyword> keywords, String widgetId, WidgetPersistCondition persistCondition, WidgetPersistAction persistAction, int x, int y, int width, int height)
	{
		super(id, version, name, keywords);
		setWidgetId(widgetId);
		setPersistCondition(persistCondition);
		setPersistAction(persistAction);
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
	}
	public String getType()
	{
		return ResourceType.WIDGET.getTypeId();
	}
	public Collection<String> getDynamicPropertyIds()
	{
		return abstractProperties.keySet();
	}
	public Object getDynamicProperty(String id)
	{
		return abstractProperties.get(id);
	}
	public void setDynamicProperty(String id, Object value)
	{
		if (id == null)
			throw new NullPointerException();
//		if (value == null)
//			abstractProperties.remove(id);
//		else
			abstractProperties.put(id, value);
	}
	public Class getPropertyType(String id) {
		return Object.class;
	}

	public void setWidgetId(String widgetId)
	{
		this.widgetId = widgetId;
	}
	public String getWidgetId()
	{
		return widgetId;
	}
	public void setPersistCondition(WidgetPersistCondition condition)
	{
		persistCondition = condition;
	}
	public WidgetPersistCondition getPersistCondition()
	{
		return persistCondition;
	}
	public void setPersistAction(WidgetPersistAction action)
	{
		persistAction = action;
	}
	public WidgetPersistAction getPersistAction()
	{
		return persistAction;
	}
	public void setX(int x)
	{
		this.x = x;
	}
	public int getX()
	{
		return x;
	}
	public void setY(int y)
	{
		this.y = y;
	}
	public int getY()
	{
		return y;
	}
	public void setWidth(int width)
	{
		this.width = width;
	}
	public int getWidth()
	{
		return width;
	}
	public void setHeight(int height)
	{
		this.height = height;
	}
	public int getHeight()
	{
		return height;
	}
}
