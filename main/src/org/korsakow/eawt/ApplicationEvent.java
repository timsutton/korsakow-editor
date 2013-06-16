package org.korsakow.eawt;

import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

public class ApplicationEvent extends EventObject
{
	protected Map<String, Object> properties = new HashMap<String, Object>();
	protected boolean canceled = false;
	public ApplicationEvent(Object source)
	{
		super(source);
	}
	public void setProperty(String key, Object value)
	{
		properties.put(key, value);
	}
	public Object getProperty(String key)
	{
		return properties.get(key);
	}
	public void cancel()
	{
		this.canceled = true;
	}
	public boolean isCanceled()
	{
		return canceled;
	}
}
