package org.korsakow.ide.ui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import javax.swing.Timer;

/**
 * Schedules runnables by class. Used so that tasks that need to be performed once
 * but may be scheduled several times over a short period are only run once.
 * @author d
 *
 */
public class CommonTaskScheduler implements ActionListener
{
	private Hashtable<Class, Runnable> queue = new Hashtable<Class, Runnable>();
	Timer timer;
	private boolean paused = false;
	public CommonTaskScheduler()
	{
		timer = new Timer(100, this);
	}
	public boolean has(Class key)
	{
		return queue.containsKey(key);
	}
	public void start()
	{
		paused = false;
		if (!timer.isRunning())
			timer.start();
	}
	public void stop()
	{
		paused = true;
		if (timer.isRunning())
			timer.stop();
	}
	public void enqueue(Runnable runnable)
	{
		Class clazz = runnable.getClass();
		queue.put(clazz, runnable);
		if (!paused && !timer.isRunning())
			timer.start();
	}
	public void actionPerformed(ActionEvent event)
	{
		Collection<Runnable> runs = new ArrayList<Runnable>(queue.values());
		queue.clear();
		for (Runnable runnable : runs)
			runnable.run();
		if (queue.isEmpty())
			timer.stop();
	}
}