/**
 * 
 */
package org.korsakow.ide.task;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.korsakow.domain.task.ITask;
import org.korsakow.services.export.ExportOptions;

public abstract class AbstractTask implements Task
{
	private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
	protected List<ITask> subTasks = new ArrayList<ITask>();
	/**
	 * Must synchronize on access.
	 */
	protected ExportOptions exportOptions;
	public AbstractTask(ExportOptions exportOptions)
	{
		this.exportOptions = exportOptions;
	}
	public AbstractTask()
	{
		exportOptions = null;
	}
	public AbstractTask(List<ITask> subTasks)
	{
		this();
		this.subTasks = subTasks;
	}
	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		changeSupport.addPropertyChangeListener(listener);
	}
	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		changeSupport.addPropertyChangeListener(listener);
	}
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
	{
		changeSupport.addPropertyChangeListener(propertyName, listener);
	}
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
	{
		changeSupport.removePropertyChangeListener(propertyName, listener);
	}
	public void firePropertyChanged(String propertyName, Object oldValue, Object newValue)
	{
		changeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}
	public void fireProgressChanged(int oldValue, int newValue)
	{
		firePropertyChanged(PROPERTY_PROGRESS, oldValue, newValue);
	}
	public void fireDisplayStringChanged(String oldValue, String newValue)
	{
		firePropertyChanged(PROPERTY_DISPLAY_STRING, oldValue, newValue);
	}
	public String getTitleString()
	{
		return "";
	}
	public void run() throws TaskException, InterruptedException
	{
		runTask();
		runSubTasks();
	}
	protected void runSubTasks() throws TaskException, InterruptedException
	{
		int i = 0;
		int size = subTasks.size();
		for (ITask subTask : subTasks) {
			fireProgressChanged(0, ++i*100/size);
			fireDisplayStringChanged("", subTask.getTitleString());
			subTask.run();
		}
	}
	public abstract void runTask() throws TaskException, InterruptedException;
}