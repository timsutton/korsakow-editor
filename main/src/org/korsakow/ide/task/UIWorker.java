/**
 * 
 */
package org.korsakow.ide.task;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.korsakow.domain.task.ITask;
import org.korsakow.domain.task.IWorker;
import org.korsakow.ide.util.Util;

import com.sun.swingx.SwingWorker;

public class UIWorker extends SwingWorker<Void, String> implements IWorker, PropertyChangeListener
{
	private volatile Throwable exception = null;
	
	private final List<ITask> tasks;
	public UIWorker()
	{
		tasks = new ArrayList<ITask>();
	}
	public UIWorker(ITask task)
	{
		this(Util.list(ITask.class, task));
	}
	public UIWorker(List<ITask> tasks)
	{
		this.tasks = new ArrayList<ITask>(tasks); // private copy
	}
	public void addTask(ITask newTask)
	{
		synchronized (tasks) {
			tasks.add(newTask);
		}
	}
	public void addTasks(List<ITask> newTasks)
	{
		synchronized (tasks) {
			tasks.addAll(newTasks);
		}
	}
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
	{
		getPropertyChangeSupport().addPropertyChangeListener(propertyName, listener);
	}
	protected synchronized void setException(Throwable t)
	{
		if (exception == null)
			exception = t;
	}
	public synchronized Throwable getException()
	{
		return exception;
	}
	@Override
	public void process(List<String> displayStrings)
	{
		System.out.println("Processing task: " + displayStrings.get(0));
	}
	@Override
	public Void doInBackground() throws Exception
	{
try {
		int total = 1;
		int index = 0;
		int count = 0;
		boolean done = false;
		setProgress(0);
		while (!done)
		{
			ITask task = null;
			synchronized (tasks) {
				total = tasks.size();
				if (index < total) {
					task = tasks.get(index);
					++index;
				}
			}
			if (task == null) {
				done = true;
				continue;
			}
			task.addPropertyChangeListener(ITask.PROPERTY_PROGRESS, this);
			task.addPropertyChangeListener(ITask.PROPERTY_DISPLAY_STRING, this);
			
			System.out.println("Task: " + task.getTitleString());
			firePropertyChange("displayString", null, task.getTitleString());
			Thread.sleep(70); // this is a completely unreliable way of giving the publish a chance to run

			try {
				task.run();
			} catch (InterruptedException e) {
				done = true;
			} catch (TaskException e) {
				done = true;
				exception = e.getCause();
			}
			
			task.removePropertyChangeListener(ITask.PROPERTY_DISPLAY_STRING, this);
			task.removePropertyChangeListener(ITask.PROPERTY_PROGRESS, this);
			
			synchronized (tasks) {
				// lame hack to account for some cases where one task adds more
				total = tasks.size();
			}
			setProgress(100*++count/total);
			System.gc();
		}
} catch (Throwable t) {
	if (exception == null)
		exception = t;
}
		return null;
	}
	@Override
	public void done()
	{
	}
	public void waitFor() throws ExecutionException, InterruptedException
	{
		get();
	}
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getPropertyName().equals(ITask.PROPERTY_PROGRESS)) {
			firePropertyChange(PROPERTY_SUB_PROGRESS, event.getOldValue(), event.getNewValue());
		} else
		if (event.getPropertyName().equals(ITask.PROPERTY_DISPLAY_STRING)) {
			firePropertyChange(PROPERTY_SUB_DISPLAY_STRING, event.getOldValue(), event.getNewValue());
		}
	}
}