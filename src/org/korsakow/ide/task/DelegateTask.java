package org.korsakow.ide.task;

import java.util.List;

import org.korsakow.domain.task.ITask;

public class DelegateTask extends AbstractTask
{
	private final String title;
	
	public DelegateTask(String title, List<ITask> tasks)
	{
		super(tasks);
		this.title = title;
	}
	@Override
	public String getTitleString()
	{
		return title;
	}
	@Override
	public void runTask() throws TaskException, InterruptedException {
	}
}
