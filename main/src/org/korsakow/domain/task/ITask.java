package org.korsakow.domain.task;

import java.beans.PropertyChangeListener;

import org.korsakow.ide.task.TaskException;

public interface ITask
{
	public static final String PROPERTY_PROGRESS = "progress";
	public static final String PROPERTY_DISPLAY_STRING = "displayString";
	
	public void run() throws TaskException, InterruptedException;
	
	String getTitleString();
	void addPropertyChangeListener(PropertyChangeListener listener);
	void removePropertyChangeListener(PropertyChangeListener listener);
	void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);
	void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
}
