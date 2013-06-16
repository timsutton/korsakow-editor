/**
 * 
 */
package test.org.korsakow.export;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class StatusPrinter implements PropertyChangeListener
{
	public void propertyChange(PropertyChangeEvent event)
	{
		String propertyName = event.getPropertyName();
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();
		
		System.out.println("Progress: " + propertyName + " = " + newValue);
	}
}