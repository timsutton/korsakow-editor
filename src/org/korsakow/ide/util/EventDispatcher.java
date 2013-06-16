/**
 * 
 */
package org.korsakow.ide.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EventListener;

import javax.swing.event.EventListenerList;

/**
 * TODO: get rid of this thing. its terrible in too many ways.
 * @deprecated
 * @author d
 *
 * @param <Listener>
 */
@Deprecated
public class EventDispatcher<Listener extends EventListener>
{
	private EventListenerList listeners;
	private Class<? extends Listener> clazz;
	public EventDispatcher(Class<? extends Listener> clazz, EventListenerList listeners)
	{
		this.clazz = clazz;
		this.listeners = listeners;
	}
	public EventDispatcher(Class<? extends Listener> clazz)
	{
		this(clazz, new EventListenerList());
	}
	public void add(Listener listener)
	{
		listeners.add((Class<Listener>)clazz, listener);
	}
	public void remove(Listener listener)
	{
		listeners.remove((Class<Listener>)clazz, listener);
	}
	public void notify(String eventName, Object... args)
	{
		try {
			Method method = null;
			// perform a relatively weak lookup on the method
			// but its hard to be more precice, since args might have been boxed or subclasses
			for (Method m : clazz.getMethods())
				if (m.getName().equals(eventName) && m.getParameterTypes().length == args.length) {
					method = m;
					break;
				}
			if (method == null)
				throw new NoSuchMethodException(String.format("Not found: %s.%s", clazz.getName(), eventName));
			for (EventListener listener : listeners.getListeners(clazz))
				method.invoke(listener, args);
		} catch (InvocationTargetException e) {
			// bypass pointless and annoyingly long traces (we dont lose any info, just cut out the middle man cause by reflection)
//			if (e.getCause() != null)
//				throw new RuntimeException(e.getCause());
//			else 
				throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}