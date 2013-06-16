package org.korsakow.domain.proxy;

import java.util.Collection;

import org.dsrg.soenea.domain.MapperException;
import org.korsakow.domain.Widget;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.domain.mapper.input.WidgetInputMapper;
import org.korsakow.ide.resources.widget.WidgetPersistAction;
import org.korsakow.ide.resources.widget.WidgetPersistCondition;

public class WidgetProxy extends ResourceProxy<Widget> implements IWidget {

	public WidgetProxy(long id)
	{
		super(id);
	}


	@Override
	public Class<Widget> getInnerClass()
	{
		return Widget.class;
	}
	
	@Override
	protected Widget getFromMapper(Long id) throws MapperException {
		return WidgetInputMapper.map(id);
	}
	public void setWidgetId(String widgetId)
	{
		getInnerObject().setWidgetId(widgetId);
	}
	public String getWidgetId()
	{
		return getInnerObject().getWidgetId();
	}
	public void setX(int x)
	{
		getInnerObject().setX(x);
	}
	public int getX()
	{
		return getInnerObject().getX();
	}
	public void setY(int y)
	{
		getInnerObject().setY(y);
	}
	public int getY()
	{
		return getInnerObject().getY();
	}
	public void setWidth(int width)
	{
		getInnerObject().setWidth(width);
	}
	public int getWidth()
	{
		return getInnerObject().getWidth();
	}
	public void setHeight(int height)
	{
		getInnerObject().setHeight(height);
	}
	public int getHeight()
	{
		return getInnerObject().getHeight();
	}

	public Object getDynamicProperty(String id) {
		return getInnerObject().getDynamicProperty(id);
	}

	public Collection<String> getDynamicPropertyIds() {
		return getInnerObject().getDynamicPropertyIds();
	}

	public void setDynamicProperty(String id, Object value) {
		getInnerObject().setDynamicProperty(id, value);
	}
	public Class getPropertyType(String id) {
		return getInnerObject().getPropertyType(id);
	}

	public WidgetPersistAction getPersistAction() {
		return getInnerObject().getPersistAction();
	}

	public WidgetPersistCondition getPersistCondition() {
		return getInnerObject().getPersistCondition();
	}

	public void setPersistAction(WidgetPersistAction action) {
		getInnerObject().setPersistAction(action);
	}

	public void setPersistCondition(WidgetPersistCondition condition) {
		getInnerObject().setPersistCondition(condition);
	}
}
