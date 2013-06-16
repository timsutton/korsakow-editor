package org.korsakow.ide.resources.widget;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import org.korsakow.domain.interf.IDynamicProperties;
import org.korsakow.domain.interf.IKeyword;
import org.korsakow.ide.resources.WidgetType;
import org.korsakow.ide.resources.property.AbstractProperty;
import org.korsakow.ide.util.EventDispatcher;
import org.korsakow.ide.util.Util;

public abstract class WidgetModel implements IDynamicProperties
{
	private static class PropertyChangeForwarder implements PropertyChangeListener
	{
		public void propertyChange(PropertyChangeEvent event) {
			WidgetComponent comp = (WidgetComponent)event.getSource();
			comp.getWidget().firePropertyChange(event.getPropertyName(), event.getOldValue(), event.getNewValue());
		}
	};
	private static PropertyChangeForwarder propertyChangeForwarder = new PropertyChangeForwarder();
	protected Hashtable<String, AbstractProperty> abstractProperties = new Hashtable<String, AbstractProperty>();
	protected void addProperty(AbstractProperty p) { abstractProperties.put(p.getId(), p); }
	protected void removeProperty(String id) { abstractProperties.remove(id); }
	
	protected EventDispatcher<PropertyChangeListener> dispatcher = new EventDispatcher<PropertyChangeListener>(PropertyChangeListener.class);
	protected HashSet<String> propertiesBeingUpdated = new HashSet<String>();
	private WidgetType widgetType;
	protected WidgetPersistCondition persistCondition = WidgetPersistCondition.Never;
	protected WidgetPersistAction persistAction = WidgetPersistAction.Replace;
	private WidgetComponent component;
	private WidgetPropertiesEditor widgetEditor;
	private Long id;
	private String name;
	private Collection<IKeyword> keywords = new ArrayList<IKeyword>();
	public WidgetModel(WidgetType widgetType)
	{
		this.widgetType = widgetType;
	}
	public void setWidgetType(WidgetType type)
	{
		widgetType = type;
	}
	public WidgetType getWidgetType()
	{
		return widgetType;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public Long getId()
	{
		return id;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getName()
	{
		return name;
	}
	public void setKeywords(Collection<IKeyword> keywords)
	{
		this.keywords = keywords;
	}
	public Collection<IKeyword> getKeywords()
	{
		return keywords;
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
	public Collection<String> getDynamicPropertyIds()
	{
		return abstractProperties.keySet();
	}
	public Collection<Object> getPropertyValues()
	{
		List<Object> values = new ArrayList<Object>();
		for (AbstractProperty value : abstractProperties.values())
			values.add(value.getValue());
		return values;
	}
	public void setDynamicProperty(String id, Object value)
	{
		if (!abstractProperties.containsKey(id)) {
			try {
				// bit of a hack for now... until merger of app/domain objects
				if (id.equals("version"))
					return;
				String m = "get" + id.toUpperCase().charAt(0) + id.substring(1);
				getClass().getMethod(m);
				return;
			} catch (SecurityException e) {
				throw new IllegalArgumentException(e);
			} catch (NoSuchMethodException e) {
				throw new IllegalArgumentException("no such property: " + id + " for " + getClass().getCanonicalName());
			}
		}
		abstractProperties.get(id).setValue(value);
	}
	public Object getDynamicProperty(String id)
	{
		return abstractProperties.get(id).getValue();
	}
	public Class getPropertyType(String id)
	{
		return Object.class;
	}
	protected WidgetPropertiesEditor createWidgetEditor()
	{
		return new AbstractWidgetPropertiesEditor(Util.list(WidgetModel.class, this));
	}
	public WidgetPropertiesEditor getWidgetEditor()
	{
		if (widgetEditor == null) {
			widgetEditor = createWidgetEditor();
		}
		return widgetEditor;
	}
	public void disposeWidgetEditor()
	{
		widgetEditor = null;
	}
	
	protected abstract WidgetComponent createComponent();
	public WidgetComponent getComponent()
	{
		if (component == null) {
			component = createComponent();
			component.addPropertyChangeListener("x", propertyChangeForwarder);
			component.addPropertyChangeListener("y", propertyChangeForwarder);
			component.addPropertyChangeListener("width", propertyChangeForwarder);
			component.addPropertyChangeListener("height", propertyChangeForwarder);
		}
		return component;
	}
	public void setX(int x)
	{
		getComponent().setLocation(x, getComponent().getY());
	}
	public void setY(int y)
	{
		getComponent().setLocation(getComponent().getX(), y);
	}
	public int getX()
	{
		return getComponent().getX();
	}
	public int getY()
	{
		return getComponent().getY();
	}
	public int getWidth()
	{
		return getComponent().getWidth();
	}
	public int getHeight()
	{
		return getComponent().getHeight();
	}
	public void setWidth(int width)
	{
		getComponent().setSize(width, getComponent().getHeight());
	}
	public void setHeight(int height)
	{
		getComponent().setSize(getComponent().getWidth(), height);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		dispatcher.add(listener);
	}
	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		dispatcher.remove(listener);
	}
	/**
	 * Set to true to suspend firing of property change events.
	 * @param propertyName
	 * @param updating
	 */
	public void setPropertyUpdating(String propertyName, boolean updating)
	{
		if (updating)
			propertiesBeingUpdated.add(propertyName);
		else
			propertiesBeingUpdated.remove(propertyName);
	}
	/**
	 * Also fires a change event. Even if oldValue==newValue or oldValue.equals(newValue).
	 * @param propertyName
	 * @param updating
	 * @param oldValue
	 * @param newValue
	 */
	public void setPropertyUpdating(String propertyName, boolean updating, Object oldValue, Object newValue)
	{
		setPropertyUpdating(propertyName, updating);
		PropertyChangeEvent event = new PropertyChangeEvent(this, propertyName, oldValue, newValue);
		dispatcher.notify("propertyChange", event);
	}
	/**
	 * Does nothing if oldValue==newValue or oldValue.equals(newValue).
	 * @param propertyName
	 * @param oldValue
	 * @param newValue
	 */
	public void firePropertyChange(String propertyName, Object oldValue, Object newValue)
	{
		if (oldValue == newValue)
			return;
		if (oldValue != null && newValue != null && oldValue.equals(newValue))
			return;
		if (propertiesBeingUpdated.contains(propertyName))
			return;
		PropertyChangeEvent event = new PropertyChangeEvent(this, propertyName, oldValue, newValue);
		dispatcher.notify("propertyChange", event);
	}
	/**
	 * Beans say this is how to go. Ignores any properties being updates.
	 */
	public void fireBatchPropertyChange()
	{
		PropertyChangeEvent event = new PropertyChangeEvent(this, null, null, null);
		dispatcher.notify("propertyChange", event);
	}
}
