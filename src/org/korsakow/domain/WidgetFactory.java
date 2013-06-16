package org.korsakow.domain;

import java.util.ArrayList;
import java.util.Collection;

import org.dsrg.soenea.uow.UoW;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.domain.interf.IWidget;
import org.korsakow.ide.DataRegistry;
import org.korsakow.ide.resources.widget.WidgetPersistAction;
import org.korsakow.ide.resources.widget.WidgetPersistCondition;

public class WidgetFactory {
	public static Widget createNew(long id, long version)
	{
		Widget object = new Widget(id, version);
		UoW.getCurrent().registerNew(object);
		return object;
	}
	public static Widget createNew(String widgetType)
	{
		Widget object = createNew();
		object.setWidgetId(widgetType);
		return object;
	}
	public static Widget createNew()
	{
		return createNew(DataRegistry.getMaxId(), 0);
	}
	public static Widget createNew(String name, Collection<IKeyword> keywords, String widgetId, WidgetPersistCondition persistCondition, WidgetPersistAction persistAction, int x, int y, int width, int height)
	{
		Widget object = new Widget(DataRegistry.getMaxId(), 0, name, keywords, widgetId, persistCondition, persistAction, x, y, width, height);
		UoW.getCurrent().registerNew(object);
		return object;
	}
	public static Widget createClean(long id, long version, String name, Collection<IKeyword> keywords, String widgetId, WidgetPersistCondition persistCondition, WidgetPersistAction persistAction, int x, int y, int width, int height)
	{
		Widget object = new Widget(id, version, name, keywords, widgetId, persistCondition, persistAction, x, y, width, height);
		UoW.getCurrent().registerClean(object);
		return object;
	}
	public static Widget copy(IWidget src)
	{
		Widget copy = createNew(
				src.getName(),
				KeywordFactory.copy(src.getKeywords()),
				src.getWidgetId(),
				src.getPersistCondition(),
				src.getPersistAction(),
				src.getX(), src.getY(),
				src.getWidth(), src.getHeight()
		);
		for (String propId : src.getDynamicPropertyIds())
			copy.setDynamicProperty(propId, src.getDynamicProperty(propId));
		return copy;
	}
	public static Collection<IWidget> copy(Collection<IWidget> src)
	{
		Collection<IWidget> copies = new ArrayList<IWidget>();
		for (IWidget widget : src)
			copies.add(copy(widget));
		return copies;
	}
}
