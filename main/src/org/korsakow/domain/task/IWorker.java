package org.korsakow.domain.task;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface IWorker
{
	public static final String PROPERTY_STATE = "state";
	public static final String PROPERTY_DISPLAY_STRING = "displayString";
	public static final String PROPERTY_PROGRESS = "progress";
	public static final String PROPERTY_SUB_DISPLAY_STRING = "subDisplayString";
	public static final String PROPERTY_SUB_PROGRESS = "subProgress";
	void addTask(ITask task);
	void addTasks(List<ITask> tasks);
	void waitFor() throws ExecutionException, InterruptedException;
	void execute();
	Throwable getException();
	void addPropertyChangeListener(PropertyChangeListener listener);
	void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);
	
	boolean cancel(boolean interrupt);
	boolean isCancelled();
	boolean isDone();
}
